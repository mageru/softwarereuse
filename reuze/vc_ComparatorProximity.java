package reuze;
/*
 *   __               .__       .__  ._____.           
 * _/  |_  _______  __|__| ____ |  | |__\_ |__   ______
 * \   __\/  _ \  \/  /  |/ ___\|  | |  || __ \ /  ___/
 *  |  | (  <_> >    <|  \  \___|  |_|  || \_\ \\___ \ 
 *  |__|  \____/__/\_ \__|\___  >____/__||___  /____  >
 *                   \/       \/             \/     \/ 
 *
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

import reuze.vc_e_Colors;

import java.util.Comparator;

/**
 * Compares two colors by their distance to the given target color.
 */
public class vc_ComparatorProximity implements Comparator<vc_e_Colors> {

    protected vc_e_Colors col;
    protected vc_i_Distance proxy;

    public vc_ComparatorProximity(vc_e_Colors col, vc_i_Distance proxy) {
        this.col = col;
        this.proxy = proxy;
    }

    public int compare(vc_e_Colors a, vc_e_Colors b) {
        float da = proxy.distanceBetween(col, a);
        float db = proxy.distanceBetween(col, b);
        return da < db ? -1 : da > db ? 1 : 0;
    }

}
