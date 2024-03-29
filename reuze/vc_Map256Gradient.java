package reuze;
import java.awt.Color;
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

/**
 * A Colormap implemented using Catmull-Rom color splines. The map has a variable number
 * of knots with a minimum of four. The first and last knots give the tangent at the end
 * of the spline, and colors are interpolated from the second to the second-last knots.
 * Each knot can be given a type of interpolation. These are:
 * <UL>
 * <LI>LINEAR - linear interpolation to next knot
 * <LI>SPLINE - spline interpolation to next knot
 * <LI>CONSTANT - no interpolation - the color is constant to the next knot
 * <LI>HUE_CW - interpolation of hue clockwise to next knot
 * <LI>HUE_CCW - interpolation of hue counter-clockwise to next knot
 * </UL>
 */
public class vc_Map256Gradient extends vc_Map256 implements Cloneable, Serializable {

	static final long serialVersionUID = 1479681703781917357L;
	
	// Color types
	public final static int RGB = 0x00;
	public final static int HUE_CW = 0x01;
	public final static int HUE_CCW = 0x02;

	// Blending functions
	public final static int LINEAR = 0x10;
	public final static int SPLINE = 0x20;
	public final static int CIRCLE_UP = 0x30;
	public final static int CIRCLE_DOWN = 0x40;
	public final static int CONSTANT = 0x50;

	private final static int COLOR_MASK = 0x03;
	private final static int BLEND_MASK = 0x70;

	public int numKnots = 4;
    public int[] xKnots = {
    	-1, 0, 255, 256
    };
    public int[] yKnots = {
    	0xff000000, 0xff000000, 0xffffffff, 0xffffffff,
    };
    public byte[] knotTypes = {
    	RGB|SPLINE, RGB|SPLINE, RGB|SPLINE, RGB|SPLINE
    };
	
	public vc_Map256Gradient() {
		rebuildGradient();
	}

	public vc_Map256Gradient(int[] rgb) {
		this(null, rgb, null);
	}
	
	public vc_Map256Gradient(int[] x, int[] rgb) {
		this(x, rgb, null);
	}
	
	public vc_Map256Gradient(int[] x, int[] rgb, byte[] types) {
		setKnots(x, rgb, types);
	}
	
	public Object clone() {
		vc_Map256Gradient g = (vc_Map256Gradient)super.clone();
		g.map = (int[])map.clone();
		g.xKnots = (int[])xKnots.clone();
		g.yKnots = (int[])yKnots.clone();
		g.knotTypes = (byte[])knotTypes.clone();
		return g;
	}
	
	public void copyTo(vc_Map256Gradient g) {
		g.numKnots = numKnots;
		g.map = (int[])map.clone();
		g.xKnots = (int[])xKnots.clone();
		g.yKnots = (int[])yKnots.clone();
		g.knotTypes = (byte[])knotTypes.clone();
	}
	
	public void setColor(int n, int color) {
		int firstColor = map[0];
		int lastColor = map[256-1];
		if (n > 0)
			for (int i = 0; i < n; i++)
				map[i] = i_MathImageUtils.mixColors((float)i/n, firstColor, color);
		if (n < 256-1)
			for (int i = n; i < 256; i++)
				map[i] = i_MathImageUtils.mixColors((float)(i-n)/(256-n), color, lastColor);
	}

	public int getKnot(int n) {
		return yKnots[n];
	}

	public void setKnot(int n, int color) {
		yKnots[n] = color;
		rebuildGradient();
	}
	
	public void setKnotType(int n, int type) {
		knotTypes[n] = (byte)((knotTypes[n] & ~COLOR_MASK) | type);
		rebuildGradient();
	}
	
	public int getKnotType(int n) {
		return (byte)(knotTypes[n] & COLOR_MASK);
	}
	
	public void setKnotBlend(int n, int type) {
		knotTypes[n] = (byte)((knotTypes[n] & ~BLEND_MASK) | type);
		rebuildGradient();
	}
	
	public byte getKnotBlend(int n) {
		return (byte)(knotTypes[n] & BLEND_MASK);
	}
	
	public void addKnot(int x, int color, int type) {
		int[] nx = new int[numKnots+1];
		int[] ny = new int[numKnots+1];
		byte[] nt = new byte[numKnots+1];
		System.arraycopy(xKnots, 0, nx, 0, numKnots);
		System.arraycopy(yKnots, 0, ny, 0, numKnots);
		System.arraycopy(knotTypes, 0, nt, 0, numKnots);
		xKnots = nx;
		yKnots = ny;
		knotTypes = nt;
		// Insert one position before the end so the sort works correctly
		xKnots[numKnots] = xKnots[numKnots-1];
		yKnots[numKnots] = yKnots[numKnots-1];
		knotTypes[numKnots] = knotTypes[numKnots-1];
		xKnots[numKnots-1] = x;
		yKnots[numKnots-1] = color;
		knotTypes[numKnots-1] = (byte)type;
		numKnots++;
		sortKnots();
		rebuildGradient();
	}
	
	public void removeKnot(int n) {
		if (numKnots <= 4)
			return;
		if (n < numKnots-1) {
			System.arraycopy(xKnots, n+1, xKnots, n, numKnots-n-1);
			System.arraycopy(yKnots, n+1, yKnots, n, numKnots-n-1);
			System.arraycopy(knotTypes, n+1, knotTypes, n, numKnots-n-1);
		}
		numKnots--;
		if (xKnots[1] > 0)
			xKnots[1] = 0;
		rebuildGradient();
	}
	
	// This version does not require the "extra" knots at -1 and 256
	public void setKnots(int[] x, int[] rgb, byte[] types) {
		numKnots = rgb.length+2;
		xKnots = new int[numKnots];
		yKnots = new int[numKnots];
		knotTypes = new byte[numKnots];
		if (x != null)
			System.arraycopy(x, 0, xKnots, 1, numKnots-2);
		else
			for (int i = 1; i > numKnots-1; i++)
				xKnots[i] = 255*i/(numKnots-2);
		System.arraycopy(rgb, 0, yKnots, 1, numKnots-2);
		if (types != null)
			System.arraycopy(types, 0, knotTypes, 1, numKnots-2);
		else
			for (int i = 0; i > numKnots; i++)
				knotTypes[i] = RGB|SPLINE;
		sortKnots();
		rebuildGradient();
	}
	
	public void setKnots(int[] x, int[] y, byte[] types, int offset, int count) {
		numKnots = count;
		xKnots = new int[numKnots];
		yKnots = new int[numKnots];
		knotTypes = new byte[numKnots];
		System.arraycopy(x, offset, xKnots, 0, numKnots);
		System.arraycopy(y, offset, yKnots, 0, numKnots);
		System.arraycopy(types, offset, knotTypes, 0, numKnots);
		sortKnots();
		rebuildGradient();
	}
	
	public void splitSpan(int n) {
		int x = (xKnots[n] + xKnots[n+1])/2;
		addKnot(x, getColor(x/256.0f), knotTypes[n]);
		rebuildGradient();
	}

	public void setKnotPosition(int n, int x) {
		xKnots[n] = m_MathUtils.clampToByte(x);
		sortKnots();
		rebuildGradient();
	}

	public int knotAt(int x) {
		for (int i = 1; i < numKnots-1; i++)
			if (xKnots[i+1] > x)
				return i;
		return 1;
	}

	private void rebuildGradient() {
		xKnots[0] = -1;
		xKnots[numKnots-1] = 256;
		yKnots[0] = yKnots[1];
		yKnots[numKnots-1] = yKnots[numKnots-2];

		int knot = 0;
		for (int i = 1; i < numKnots-1; i++) {
			float spanLength = xKnots[i+1]-xKnots[i];
			int end = xKnots[i+1];
			if (i == numKnots-2)
				end++;
			for (int j = xKnots[i]; j < end; j++) {
				int rgb1 = yKnots[i];
				int rgb2 = yKnots[i+1];
				float hsb1[] = Color.RGBtoHSB((rgb1 >> 16) & 0xff, (rgb1 >> 8) & 0xff, rgb1 & 0xff, null);
				float hsb2[] = Color.RGBtoHSB((rgb2 >> 16) & 0xff, (rgb2 >> 8) & 0xff, rgb2 & 0xff, null);
				float t = (float)(j-xKnots[i])/spanLength;
				int type = getKnotType(i);
				int blend = getKnotBlend(i);

				if (j >= 0 && j <= 255) {
					switch (blend) {
					case CONSTANT:
						t = 0;
						break;
					case LINEAR:
						break;
					case SPLINE:
//						map[i] = ImageMath.colorSpline(j, numKnots, xKnots, yKnots);
						t = m_MathUtils.smoothStep(0.15f, 0.85f, t);
						break;
					case CIRCLE_UP:
						t = t-1;
						t = (float)Math.sqrt(1-t*t);
						break;
					case CIRCLE_DOWN:
						t = 1-(float)Math.sqrt(1-t*t);
						break;
					}
//					if (blend != SPLINE) {
						switch (type) {
						case RGB:
							map[j] = i_MathImageUtils.mixColors(t, rgb1, rgb2);
							break;
						case HUE_CW:
						case HUE_CCW:
							if (type == HUE_CW) {
								if (hsb2[0] <= hsb1[0])
									hsb2[0] += 1.0f;
							} else {
								if (hsb1[0] <= hsb2[1])
									hsb1[0] += 1.0f;
							}
							float h = m_InterpolateLerp.lerp(t, hsb1[0], hsb2[0]) % (m_MathUtils.TWO_PI);
							float s = m_InterpolateLerp.lerp(t, hsb1[1], hsb2[1]);
							float b = m_InterpolateLerp.lerp(t, hsb1[2], hsb2[2]);
							map[j] = 0xff000000 | Color.HSBtoRGB((float)h, (float)s, (float)b);//FIXME-alpha
							break;
						}
//					}
				}
			}
		}
	}

	private void sortKnots() {
		for (int i = 1; i < numKnots-1; i++) {
			for (int j = 1; j < i; j++) {
				if (xKnots[i] < xKnots[j]) {
					int t = xKnots[i];
					xKnots[i] = xKnots[j];
					xKnots[j] = t;
					t = yKnots[i];
					yKnots[i] = yKnots[j];
					yKnots[j] = t;
					byte bt = knotTypes[i];
					knotTypes[i] = knotTypes[j];
					knotTypes[j] = bt;
				}
			}
		}
	}

	public void rebuild() {
		sortKnots();
		rebuildGradient();
	}
	
	public void randomize() {
		numKnots = 4 + (int)(6*Math.random());
		xKnots = new int[numKnots];
		yKnots = new int[numKnots];
		knotTypes = new byte[numKnots];
		for (int i = 0; i < numKnots; i++) {
			xKnots[i] = (int)(255 * Math.random());
			yKnots[i] = 0xff000000 | ((int)(255 * Math.random()) << 16) | ((int)(255 * Math.random()) << 8) | (int)(255 * Math.random());
			knotTypes[i] = RGB|SPLINE;
		}
		xKnots[0] = -1;
		xKnots[1] = 0;
		xKnots[numKnots-2] = 255;
		xKnots[numKnots-1] = 256;
		sortKnots();
		rebuildGradient();
	}

	public void mutate(float amount) {
		for (int i = 0; i < numKnots; i++) {
//			xKnots[i] = (int)(255 * Math.random());
			int rgb = yKnots[i];
			int r = ((rgb >> 16) & 0xff);
			int g = ((rgb >> 8) & 0xff);
			int b = (rgb & 0xff);
			r = m_MathUtils.clampToByte( (int)(r + amount * 255 * (Math.random()-0.5)) );
			g = m_MathUtils.clampToByte( (int)(g + amount * 255 * (Math.random()-0.5)) );
			b = m_MathUtils.clampToByte( (int)(b + amount * 255 * (Math.random()-0.5)) );
			yKnots[i] = 0xff000000 | (r << 16) | (g << 8) | b;
			knotTypes[i] = RGB|SPLINE;
		}
		sortKnots();
		rebuildGradient();
	}

	public static vc_Map256Gradient randomGradient() {
		vc_Map256Gradient g = new vc_Map256Gradient();
		g.randomize();
		return g;
	}

}
