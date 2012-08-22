package reuze;
import java.awt.image.BufferedImage;
import java.awt.image.Kernel;
import java.io.Serializable;


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

public class ib_FilterShade extends ib_a_FilterWholeImage implements Serializable {
	
	public final static int COLORS_FROM_IMAGE = 0;
	public final static int COLORS_CONSTANT = 1;

	public final static int BUMPS_FROM_IMAGE = 0;
	public final static int BUMPS_FROM_IMAGE_ALPHA = 1;
	public final static int BUMPS_FROM_MAP = 2;
	public final static int BUMPS_FROM_BEVEL = 3;

	private float bumpHeight;
	private float bumpSoftness;
	private float viewDistance = 10000.0f;
	private int colorSource = COLORS_FROM_IMAGE;
	private int bumpSource = BUMPS_FROM_IMAGE;
	private m_i_Function2D bumpFunction;
	private BufferedImage environmentMap;
	private int[] envPixels;
	private int envWidth = 1, envHeight = 1;
	private gb_Vector3 l;
	private gb_Vector3 v;
	private gb_Vector3 n;
	private vc_Color4f shadedColor;
	private vc_Color4f diffuse_color;
	private vc_Color4f specular_color;
	private gb_Vector3 tmpv, tmpv2;

	public ib_FilterShade() {
		bumpHeight = 1.0f;
		bumpSoftness = 5.0f;
		l = new gb_Vector3();
		v = new gb_Vector3();
		n = new gb_Vector3();
		shadedColor = new vc_Color4f();
		diffuse_color = new vc_Color4f();
		specular_color = new vc_Color4f();
		tmpv = new gb_Vector3();
		tmpv2 = new gb_Vector3();
	}

	public void setBumpFunction(m_i_Function2D bumpFunction) {
		this.bumpFunction = bumpFunction;
	}

	public m_i_Function2D getBumpFunction() {
		return bumpFunction;
	}

	public void setBumpHeight(float bumpHeight) {
		this.bumpHeight = bumpHeight;
	}

	public float getBumpHeight() {
		return bumpHeight;
	}

	public void setBumpSoftness(float bumpSoftness) {
		this.bumpSoftness = bumpSoftness;
	}

	public float getBumpSoftness() {
		return bumpSoftness;
	}

	public void setEnvironmentMap(BufferedImage environmentMap) {
		this.environmentMap = environmentMap;
		if (environmentMap != null) {
			envWidth = environmentMap.getWidth();
			envHeight = environmentMap.getHeight();
			envPixels = getRGB( environmentMap, 0, 0, envWidth, envHeight, null );
		} else {
			envWidth = envHeight = 1;
			envPixels = null;
		}
	}

	public BufferedImage getEnvironmentMap() {
		return environmentMap;
	}

	public void setBumpSource(int bumpSource) {
		this.bumpSource = bumpSource;
	}

	public int getBumpSource() {
		return bumpSource;
	}

	protected final static float r255 = 1.0f/255.0f;

	protected void setFromRGB( vc_Color4f c, int argb ) {
		c.set( ((argb >> 16) & 0xff) * r255, ((argb >> 8) & 0xff) * r255, (argb & 0xff) * r255, ((argb >> 24) & 0xff) * r255 );
	}
	
	protected int[] filterPixels( int width, int height, int[] inPixels, ga_Rectangle transformedSpace ) {
		int index = 0;
		int[] outPixels = new int[width * height];
		float width45 = Math.abs(6.0f * bumpHeight);
		boolean invertBumps = bumpHeight < 0;
		gb_Vector3 position = new gb_Vector3(0.0f, 0.0f, 0.0f);
		gb_Vector3 viewpoint = new gb_Vector3((float)width / 2.0f, (float)height / 2.0f, viewDistance);
		gb_Vector3 normal = new gb_Vector3();
		vc_Color4f c = new vc_Color4f();
		m_i_Function2D bump = bumpFunction;

		if (bumpSource == BUMPS_FROM_IMAGE || bumpSource == BUMPS_FROM_IMAGE_ALPHA || bumpSource == BUMPS_FROM_MAP || bump == null) {
			if ( bumpSoftness != 0 ) {
				int bumpWidth = width;
				int bumpHeight = height;
				int[] bumpPixels = inPixels;
				if ( bumpSource == BUMPS_FROM_MAP && bumpFunction instanceof ib_Function2D ) {
					ib_Function2D if2d = (ib_Function2D)bumpFunction;
					bumpWidth = if2d.getWidth();
					bumpHeight = if2d.getHeight();
					bumpPixels = if2d.getPixels();
				}
				Kernel kernel = ib_FilterConvolveGaussian.makeKernel( bumpSoftness );
				int [] tmpPixels = new int[bumpWidth * bumpHeight];
				int [] softPixels = new int[bumpWidth * bumpHeight];
				ib_FilterConvolveGaussian.convolveAndTranspose( kernel, bumpPixels, tmpPixels, bumpWidth, bumpHeight, true, ib_FilterConvolve.CLAMP_EDGES);
				ib_FilterConvolveGaussian.convolveAndTranspose( kernel, tmpPixels, softPixels, bumpHeight, bumpWidth, true, ib_FilterConvolve.CLAMP_EDGES);
				bump = new ib_Function2D(softPixels, bumpWidth, bumpHeight, ib_Function2D.CLAMP, bumpSource == BUMPS_FROM_IMAGE_ALPHA);
			} else
				bump = new ib_Function2D(inPixels, width, height, ib_Function2D.CLAMP, bumpSource == BUMPS_FROM_IMAGE_ALPHA);
		}

		gb_Vector3 v1 = new gb_Vector3();
		gb_Vector3 v2 = new gb_Vector3();
		gb_Vector3 n = new gb_Vector3();

		// Loop through each source pixel
		for (int y = 0; y < height; y++) {
			float ny = y;
			position.y = y;
			for (int x = 0; x < width; x++) {
				float nx = x;
				
				// Calculate the normal at this point
				if (bumpSource != BUMPS_FROM_BEVEL) {
					// Complicated and slower method
					// Calculate four normals using the gradients in +/- X/Y directions
					int count = 0;
					normal.x = normal.y = normal.z = 0;
					float m0 = width45*bump.evaluate(nx, ny);
					float m1 = x > 0 ? width45*bump.evaluate(nx - 1.0f, ny)-m0 : -2;
					float m2 = y > 0 ? width45*bump.evaluate(nx, ny - 1.0f)-m0 : -2;
					float m3 = x < width-1 ? width45*bump.evaluate(nx + 1.0f, ny)-m0 : -2;
					float m4 = y < height-1 ? width45*bump.evaluate(nx, ny + 1.0f)-m0 : -2;
					
					if (m1 != -2 && m4 != -2) {
						v1.x = -1.0f; v1.y = 0.0f; v1.z = m1;
						v2.x = 0.0f; v2.y = 1.0f; v2.z = m4;
						n.crs(v1, v2);
						n.nor();
						if (n.z < 0.0)
							n.z = -n.z;
						normal.add(n);
						count++;
					}

					if (m1 != -2 && m2 != -2) {
						v1.x = -1.0f; v1.y = 0.0f; v1.z = m1;
						v2.x = 0.0f; v2.y = -1.0f; v2.z = m2;
						n.crs(v1, v2);
						n.nor();
						if (n.z < 0.0)
							n.z = -n.z;
						normal.add(n);
						count++;
					}

					if (m2 != -2 && m3 != -2) {
						v1.x = 0.0f; v1.y = -1.0f; v1.z = m2;
						v2.x = 1.0f; v2.y = 0.0f; v2.z = m3;
						n.crs(v1, v2);
						n.nor();
						if (n.z < 0.0)
							n.z = -n.z;
						normal.add(n);
						count++;
					}

					if (m3 != -2 && m4 != -2) {
						v1.x = 1.0f; v1.y = 0.0f; v1.z = m3;
						v2.x = 0.0f; v2.y = 1.0f; v2.z = m4;
						n.crs(v1, v2);
						n.nor();
						if (n.z < 0.0)
							n.z = -n.z;
						normal.add(n);
						count++;
					}

					// Average the four normals
					normal.x /= count;
					normal.y /= count;
					normal.z /= count;
				}

/* For testing - generate a sphere bump map
				double dx = x-120;
				double dy = y-80;
				double r2 = dx*dx+dy*dy;
//				double r = Math.sqrt( r2 );
//				double t = Math.atan2( dy, dx );
				if ( r2 < 80*80 ) {
					double z = Math.sqrt( 80*80 - r2 );
					normal.x = (float)dx;
					normal.y = (float)dy;
					normal.z = (float)z;
					normal.normalize();
				} else {
					normal.x = 0;
					normal.y = 0;
					normal.z = 1;
				}
*/

				if (invertBumps) {
					normal.x = -normal.x;
					normal.y = -normal.y;
				}
				position.x = x;

				if (normal.z >= 0) {
					// Get the material colour at this point
					if (environmentMap != null) {
						//FIXME-too much normalizing going on here
						tmpv2.set(viewpoint);
						tmpv2.sub(position);
						tmpv2.nor();
						tmpv.set(normal);
						tmpv.nor();

						// Reflect
						tmpv.mul( 2.0f*tmpv.dot(tmpv2) );
						tmpv.sub(v);
						
						tmpv.nor();
						setFromRGB(c, getEnvironmentMapP(normal, inPixels, width, height));//FIXME-interpolate()
						int alpha = inPixels[index] & 0xff000000;
						int rgb = ((int)(c.x * 255) << 16) | ((int)(c.y * 255) << 8) | (int)(c.z * 255);
						outPixels[index++] = alpha | rgb;
					} else
						outPixels[index++] = 0;
				} else
					outPixels[index++] = 0;
			}
		}
		return outPixels;
	}

	private int getEnvironmentMapP(gb_Vector3 normal, int[] inPixels, int width, int height) {
		if (environmentMap != null) {
			float x = 0.5f * (1 + normal.x);
			float y = 0.5f * (1 + normal.y);
			x = m_MathUtils.clamp(x * envWidth, 0, envWidth-1);
			y = m_MathUtils.clamp(y * envHeight, 0, envHeight-1);
			int ix = (int)x;
			int iy = (int)y;

			float xWeight = x-ix;
			float yWeight = y-iy;
			int i = envWidth*iy + ix;
			int dx = ix == envWidth-1 ? 0 : 1;
			int dy = iy == envHeight-1 ? 0 : envWidth;
			return i_MathImageUtils.bilinearInterpolate( xWeight, yWeight, envPixels[i], envPixels[i+dx], envPixels[i+dy], envPixels[i+dx+dy] );
		}
		return 0;
	}
	
	public String toString() {
		return "Stylize/Shade...";
	}

}
