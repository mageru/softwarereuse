package reuze;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
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

/**
 * A filter which produces motion blur the slow, but higher-quality way.
 */
public class ib_FilterMotionBlur extends ib_a_Ops {

	public final static int LINEAR = 0;
	public final static int RADIAL = 1;
	public final static int ZOOM = 2;
	
	private float angle = 0.0f;
	private float falloff = 1.0f;
	private float distance = 1.0f;
	private float zoom = 0.0f;
	private float rotation = 0.0f;
	private boolean wrapEdges = false;

	public ib_FilterMotionBlur() {
	}

	public void setAngle( float angle ) {
		this.angle = angle;
	}

	public float getAngle() {
		return angle;
	}
	
	public void setDistance( float distance ) {
		this.distance = distance;
	}

	public float getDistance() {
		return distance;
	}
	
	public void setRotation( float rotation ) {
		this.rotation = rotation;
	}

	public float getRotation() {
		return rotation;
	}
	
	public void setZoom( float zoom ) {
		this.zoom = zoom;
	}

	public float getZoom() {
		return zoom;
	}
	
	public void setWrapEdges(boolean wrapEdges) {
		this.wrapEdges = wrapEdges;
	}

	public boolean getWrapEdges() {
		return wrapEdges;
	}

    public BufferedImage filter( BufferedImage src, BufferedImage dst ) {
        int width = src.getWidth();
        int height = src.getHeight();

        if ( dst == null )
            dst = createCompatibleDestImage( src, null );

        int[] inPixels = new int[width*height];
        int[] outPixels = new int[width*height];
        getRGB( src, 0, 0, width, height, inPixels );

		float sinAngle = (float)Math.sin(angle);
		float cosAngle = (float)Math.cos(angle);

		float total;
		int cx = width/2;
		int cy = height/2;
		int index = 0;

        float imageRadius = (float)Math.sqrt( cx*cx + cy*cy );
        float translateX = (float)(distance * Math.cos( angle ));
        float translateY = (float)(distance * -Math.sin( angle ));
		float maxDistance = distance + Math.abs(rotation*imageRadius) + zoom*imageRadius;
		int repetitions = (int)maxDistance;
		AffineTransform t = new AffineTransform();
		Point2D.Float p = new Point2D.Float();

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int a = 0, r = 0, g = 0, b = 0;
				int count = 0;
				for (int i = 0; i < repetitions; i++) {
					int newX = x, newY = y;
					float f = (float)i/repetitions;

					p.x = x;
					p.y = y;
					t.setToIdentity();
					t.translate( cx+f*translateX, cy+f*translateY );
					float s = 1-zoom*f;
					t.scale( s, s );
					if ( rotation != 0 )
						t.rotate( -rotation*f );
					t.translate( -cx, -cy );
					t.transform( p, p );
					newX = (int)p.x;
					newY = (int)p.y;

					if (newX < 0 || newX >= width) {
						if ( wrapEdges )
							newX = m_MathUtils.mod( newX, width );
						else
							break;
					}
					if (newY < 0 || newY >= height) {
						if ( wrapEdges )
							newY = m_MathUtils.mod( newY, height );
						else
							break;
					}

					count++;
					int rgb = inPixels[newY*width+newX];
					a += (rgb >> 24) & 0xff;
					r += (rgb >> 16) & 0xff;
					g += (rgb >> 8) & 0xff;
					b += rgb & 0xff;
				}
				if (count == 0) {
					outPixels[index] = inPixels[index];
				} else {
					a = m_MathUtils.clampToByte((int)(a/count));
					r = m_MathUtils.clampToByte((int)(r/count));
					g = m_MathUtils.clampToByte((int)(g/count));
					b = m_MathUtils.clampToByte((int)(b/count));
					outPixels[index] = (a << 24) | (r << 16) | (g << 8) | b;
				}
				index++;
			}
		}

        setRGB( dst, 0, 0, width, height, outPixels );
        return dst;
    }

	public String toString() {
		return "Blur/Motion Blur...";
	}
}

