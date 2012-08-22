package reuze;
/*
 * Copyright (c) 2006-2011 Karsten Schmidt
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * http://creativecommons.org/licenses/LGPL/2.1/
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */



/**
 * Helper class for the spline3d classes in this package. Used to compute
 * subdivision points of the curve.
 */
public class m_PolynomialBernsteins {

    public float[] b0, b1, b2, b3;
    public int resolution;

    /**
     * @param res
     *            number of subdivision steps between each control point of the
     *            spline3d
     */
    public m_PolynomialBernsteins(int res) {
        resolution = res;
        b0 = new float[res];
        b1 = new float[res];
        b2 = new float[res];
        b3 = new float[res];
        float t = 0;
        float dt = 1.0f / (resolution - 1);
        for (int i = 0; i < resolution; i++) {
            float t1 = 1 - t;
            float t12 = t1 * t1;
            float t2 = t * t;
            b0[i] = t1 * t12;
            b1[i] = 3 * t * t12;
            b2[i] = 3 * t2 * t1;
            b3[i] = t * t2;
            t += dt;
        }
    }
}