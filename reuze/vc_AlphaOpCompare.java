package reuze;
import reuze.vc_e_Colors;

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



/**
 * Compares 2 colors by their alpha value.
 */
public class vc_AlphaOpCompare extends vc_a_CriteriaAccess {

    public int compare(vc_e_Colors a, vc_e_Colors b) {
        float aa = a.alpha();
        float ba = b.alpha();
        return aa < ba ? -1 : aa > ba ? 1 : 0;
    }

    public float getComponentValueFor(vc_e_Colors col) {
        return col.alpha();
    }

    public void setComponentValueFor(vc_e_Colors col, float value) {
        //TODOcol.setAlpha(value);
    }

}
