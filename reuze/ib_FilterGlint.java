package reuze;
import java.awt.image.BufferedImage;

/*
Copyright 2006 Jerry Huxtable

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

public class ib_FilterGlint extends ib_a_Ops {

    private float threshold = 1.0f;
    private int length = 5;
    private float blur = 0.0f;
    private float amount = 0.1f;
	private boolean glintOnly = false;
	private vc_i_ColorMap colormap = new vc_ColormapLinear( 0xffffffff, 0xff000000 );

    public ib_FilterGlint() {
	}
	
	public void setThreshold( float threshold ) {
		this.threshold = threshold;
	}
	
	public float getThreshold() {
		return threshold;
	}
	
	public void setAmount( float amount ) {
		this.amount = amount;
	}
	
	public float getAmount() {
		return amount;
	}
	
	public void setLength( int length ) {
		this.length = length;
	}
	
	public int getLength() {
		return length;
	}
	
	public void setBlur(float blur) {
		this.blur = blur;
	}

	public float getBlur() {
		return blur;
	}
	
	public void setGlintOnly(boolean glintOnly) {
		this.glintOnly = glintOnly;
	}

	public boolean getGlintOnly() {
		return glintOnly;
	}
	
	public void setColormap(vc_i_ColorMap colormap) {
		this.colormap = colormap;
	}

	public vc_i_ColorMap getColormap() {
		return colormap;
	}
	
    public BufferedImage filter( BufferedImage src, BufferedImage dst ) {
        int width = src.getWidth();
        int height = src.getHeight();
		int[] pixels = new int[width];
		int length2 = (int)(length / 1.414f);
		int[] colors = new int[length+1];
		int[] colors2 = new int[length2+1];

		if ( colormap != null ) {
			for (int i = 0; i <= length; i++) {
				int argb = colormap.getColor( (float)i/length );
				int r = (argb >> 16) & 0xff;
				int g = (argb >> 8) & 0xff;
				int b = argb & 0xff;
				argb = (argb & 0xff000000) | ((int)(amount*r) << 16) | ((int)(amount*g) << 8) | (int)(amount*b);
				colors[i] = argb;
			}
			for (int i = 0; i <= length2; i++) {
				int argb = colormap.getColor( (float)i/length2 );
				int r = (argb >> 16) & 0xff;
				int g = (argb >> 8) & 0xff;
				int b = argb & 0xff;
				argb = (argb & 0xff000000) | ((int)(amount*r) << 16) | ((int)(amount*g) << 8) | (int)(amount*b);
				colors2[i] = argb;
			}
		}

        BufferedImage mask = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		int threshold3 = (int)(threshold*3*255);
		for ( int y = 0; y < height; y++ ) {
			getRGB( src, 0, y, width, 1, pixels );
			for ( int x = 0; x < width; x++ ) {
				int rgb = pixels[x];
				int a = rgb & 0xff000000;
				int r = (rgb >> 16) & 0xff;
				int g = (rgb >> 8) & 0xff;
				int b = rgb & 0xff;
				int l = r + g + b;
				if (l < threshold3)
					pixels[x] = 0xff000000;
				else {
					l /= 3;
					pixels[x] = a | (l << 16) | (l << 8) | l;
				}
			}
			setRGB( mask, 0, y, width, 1, pixels );
		}

		if ( blur != 0 )
			mask = new ib_FilterConvolveGaussian(blur).filter( mask, null );

        if ( dst == null )
            dst = createCompatibleDestImage( src, null );
		int[] dstPixels;
		if ( glintOnly )
			dstPixels = new int[width*height];
		else
			dstPixels = getRGB( src, 0, 0, width, height, null );//FIXME - only need 2*length

		for ( int y = 0; y < height; y++ ) {
			int index = y*width;
			getRGB( mask, 0, y, width, 1, pixels );
			int ymin = Math.max( y-length, 0 )-y;
			int ymax = Math.min( y+length, height-1 )-y;
			int ymin2 = Math.max( y-length2, 0 )-y;
			int ymax2 = Math.min( y+length2, height-1 )-y;
			for ( int x = 0; x < width; x++ ) {
				if ( (pixels[x] & 0xff) > threshold*255 ) {
					int xmin = Math.max( x-length, 0 )-x;
					int xmax = Math.min( x+length, width-1 )-x;
					int xmin2 = Math.max( x-length2, 0 )-x;
					int xmax2 = Math.min( x+length2, width-1 )-x;

					// Horizontal
					for ( int i = 0, k = 0; i <= xmax; i++, k++ )
						dstPixels[index+i] = vc_ColorOps.combinePixels( dstPixels[index+i], colors[k], vc_ColorOps.ADD );
					for ( int i = -1, k = 1; i >= xmin; i--, k++ )
						dstPixels[index+i] = vc_ColorOps.combinePixels( dstPixels[index+i], colors[k], vc_ColorOps.ADD );
					// Vertical
					for ( int i = 1, j = index+width, k = 0; i <= ymax; i++, j += width, k++ )
						dstPixels[j] = vc_ColorOps.combinePixels( dstPixels[j], colors[k], vc_ColorOps.ADD );
					for ( int i = -1, j = index-width, k = 0; i >= ymin; i--, j -= width, k++ )
						dstPixels[j] = vc_ColorOps.combinePixels( dstPixels[j], colors[k], vc_ColorOps.ADD );

					// Diagonals
					int xymin = Math.max( xmin2, ymin2 );
					int xymax = Math.min( xmax2, ymax2 );
					// SE
					int count = Math.min( xmax2, ymax2 );
					for ( int i = 1, j = index+width+1, k = 0; i <= count; i++, j += width+1, k++ )
						dstPixels[j] = vc_ColorOps.combinePixels( dstPixels[j], colors2[k], vc_ColorOps.ADD );
					// NW
					count = Math.min( -xmin2, -ymin2 );
					for ( int i = 1, j = index-width-1, k = 0; i <= count; i++, j -= width+1, k++ )
						dstPixels[j] = vc_ColorOps.combinePixels( dstPixels[j], colors2[k], vc_ColorOps.ADD );
					// NE
					count = Math.min( xmax2, -ymin2 );
					for ( int i = 1, j = index-width+1, k = 0; i <= count; i++, j += -width+1, k++ )
						dstPixels[j] = vc_ColorOps.combinePixels( dstPixels[j], colors2[k], vc_ColorOps.ADD );
					// SW
					count = Math.min( -xmin2, ymax2 );
					for ( int i = 1, j = index+width-1, k = 0; i <= count; i++, j += width-1, k++ )
						dstPixels[j] = vc_ColorOps.combinePixels( dstPixels[j], colors2[k], vc_ColorOps.ADD );
				}
				index++;
			}
		}
		setRGB( dst, 0, 0, width, height, dstPixels );

        return dst;
    }
    
	public String toString() {
		return "Effects/Glint...";
	}
}
