package reuze;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


/*
** Copyright 2005 Huxtable.com. All rights reserved.
*/

public class ib_FilterRays extends ib_FilterMotionBlurOp {

    private float opacity = 1.0f;
    private float threshold = 0.0f;
    private float strength = 0.5f;
	private boolean raysOnly = false;
	private vc_i_ColorMap colormap;

    public ib_FilterRays() {
	}
	
	public void setOpacity(float opacity) {
		this.opacity = opacity;
	}

	public float getOpacity() {
		return opacity;
	}

	public void setThreshold( float threshold ) {
		this.threshold = threshold;
	}
	
	public float getThreshold() {
		return threshold;
	}
	
	public void setStrength( float strength ) {
		this.strength = strength;
	}
	
	public float getStrength() {
		return strength;
	}
	
	public void setraysOnly(boolean raysOnly) {
		this.raysOnly = raysOnly;
	}

	public boolean getraysOnly() {
		return raysOnly;
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
		int[] srcPixels = new int[width];

        BufferedImage rays = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

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
			setRGB( rays, 0, y, width, 1, pixels );
		}

		rays = super.filter( rays, null );

		for ( int y = 0; y < height; y++ ) {
			getRGB( rays, 0, y, width, 1, pixels );
			getRGB( src, 0, y, width, 1, srcPixels );
			for ( int x = 0; x < width; x++ ) {
				int rgb = pixels[x];
				int a = rgb & 0xff000000;
				int r = (rgb >> 16) & 0xff;
				int g = (rgb >> 8) & 0xff;
				int b = rgb & 0xff;
				
				if ( colormap != null ) {
					int l = r + g + b;
					rgb = colormap.getColor( l * strength * (1/3f) );
				} else {
					r = m_MathUtils.clampToByte((int)(r * strength));
					g = m_MathUtils.clampToByte((int)(g * strength));
					b = m_MathUtils.clampToByte((int)(b * strength));
					rgb = a | (r << 16) | (g << 8) | b;
				}

				pixels[x] = rgb;
			}
			setRGB( rays, 0, y, width, 1, pixels );
		}

        if ( dst == null )
            dst = createCompatibleDestImage( src, null );

		Graphics2D g = dst.createGraphics();
		if ( !raysOnly ) {
			g.setComposite( AlphaComposite.SrcOver );
			g.drawRenderedImage( src, null );
		}
		g.setComposite( vc_MiscComposite.getInstance( vc_MiscComposite.ADD, opacity ) );
		g.drawRenderedImage( rays, null );
		g.dispose();

        return dst;
    }
    
	public String toString() {
		return "Stylize/Rays...";
	}
}
