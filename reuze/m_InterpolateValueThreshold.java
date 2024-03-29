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
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
 */



/**
 * Defines a single step/threshold function which returns the minimum value for all
 * factors &lt; threshold and the maximum value for all others.
 */
public class m_InterpolateValueThreshold implements m_i_InterpolateValue {

    public float threshold;

    public m_InterpolateValueThreshold(float threshold) {
        this.threshold = threshold;
    }

    public float interpolate(float a, float b, float f) {
        return f < threshold ? a : b;
    }

	public float setParameter(String name, float value) {
        float old=threshold;
        threshold=value;
		return old;
	}

	public float getParameterFloat(String name) {
		return threshold;
	}

}
