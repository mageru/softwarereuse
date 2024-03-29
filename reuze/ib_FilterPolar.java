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

public class ib_FilterPolar extends ib_a_FilterTransform {
	
	public final static int RECT_TO_POLAR = 0;
	public final static int POLAR_TO_RECT = 1;
	public final static int INVERT_IN_CIRCLE = 2;

	private int type;
	private float width, height;
	private float centreX, centreY;
	private float radius;

	public ib_FilterPolar() {
		this(RECT_TO_POLAR);
	}

	public ib_FilterPolar(int type) {
		this.type = type;
		setEdgeAction(CLAMP);
	}

    public BufferedImage filter( BufferedImage src, BufferedImage dst ) {
		this.width = src.getWidth();
		this.height = src.getHeight();
		centreX = width/2;
		centreY = height/2;
		radius = Math.max(centreY, centreX);
		return super.filter( src, dst );
	}
	
	public void setType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	private float sqr(float x) {
		return x*x;
	}

	protected void transformInverse(int x, int y, float[] out) {
		float theta, t;
		float m, xmax, ymax;
		float r = 0;
		
		switch (type) {
		case RECT_TO_POLAR:
			theta = 0;
			if (x >= centreX) {
				if (y > centreY) {
					theta = m_MathUtils.PI - (float)Math.atan(((float)(x - centreX))/((float)(y - centreY)));
					r = (float)Math.sqrt(sqr (x - centreX) + sqr (y - centreY));
				} else if (y < centreY) {
					theta = (float)Math.atan (((float)(x - centreX))/((float)(centreY - y)));
					r = (float)Math.sqrt (sqr (x - centreX) + sqr (centreY - y));
				} else {
					theta = m_MathUtils.HALF_PI;
					r = x - centreX;
				}
			} else if (x < centreX) {
				if (y < centreY) {
					theta = m_MathUtils.TWO_PI - (float)Math.atan (((float)(centreX -x))/((float)(centreY - y)));
					r = (float)Math.sqrt (sqr (centreX - x) + sqr (centreY - y));
				} else if (y > centreY) {
					theta = m_MathUtils.PI + (float)Math.atan (((float)(centreX - x))/((float)(y - centreY)));
					r = (float)Math.sqrt (sqr (centreX - x) + sqr (y - centreY));
				} else {
					theta = 1.5f * m_MathUtils.PI;
					r = centreX - x;
				}
			}
			if (x != centreX)
				m = Math.abs (((float)(y - centreY)) / ((float)(x - centreX)));
			else
				m = 0;
			
			if (m <= ((float)height / (float)width)) {
				if (x == centreX) {
					xmax = 0;
					ymax = centreY;
				} else {
					xmax = centreX;
					ymax = m * xmax;
				}
			} else {
				ymax = centreY;
				xmax = ymax / m;
			}
			
			out[0] = (width-1) - (width - 1)/m_MathUtils.TWO_PI * theta;
			out[1] = height * r / radius;
			break;
		case POLAR_TO_RECT:
			theta = x / width * m_MathUtils.TWO_PI;
			float theta2;

			if (theta >= 1.5f * m_MathUtils.PI)
				theta2 = m_MathUtils.TWO_PI - theta;
			else if (theta >= m_MathUtils.PI)
				theta2 = theta - m_MathUtils.PI;
			else if (theta >= 0.5f * m_MathUtils.PI)
				theta2 = m_MathUtils.PI - theta;
			else
				theta2 = theta;
	
			t = (float)Math.tan(theta2);
			if (t != 0)
				m = 1.0f / t;
			else
				m = 0;
	
			if (m <= ((float)(height) / (float)(width))) {
				if (theta2 == 0) {
					xmax = 0;
					ymax = centreY;
				} else {
					xmax = centreX;
					ymax = m * xmax;
				}
			} else {
				ymax = centreY;
				xmax = ymax / m;
			}
	
			r = radius * (float)(y / (float)(height));

			float nx = -r * (float)Math.sin(theta2);
			float ny = r * (float)Math.cos(theta2);
			
			if (theta >= 1.5 * Math.PI) {
				out[0] = (float)centreX - nx;
				out[1] = (float)centreY - ny;
			} else if (theta >= Math.PI) {
				out[0] = (float)centreX - nx;
				out[1] = (float)centreY + ny;
			} else if (theta >= 0.5 * Math.PI) {
				out[0] = (float)centreX + nx;
				out[1] = (float)centreY + ny;
			} else {
				out[0] = (float)centreX + nx;
				out[1] = (float)centreY - ny;
			}
			break;
		case INVERT_IN_CIRCLE:
			float dx = x-centreX;
			float dy = y-centreY;
			float distance2 = dx*dx+dy*dy;
			out[0] = centreX + centreX*centreX * dx/distance2;
			out[1] = centreY + centreY*centreY * dy/distance2;
			break;
		}
	}

	public String toString() {
		return "Distort/Polar Coordinates...";
	}

}
