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



/**
 * An immutable origin + axis in 3D-Space.
 */
public class gb_Axis {

    /**
     * Creates a new x-Axis3D object from the world origin.
     */
    public static final gb_Axis xAxis() {
        return new gb_Axis(gb_Vector3.X);
    }

    /**
     * Creates a new y-Axis3D object from the world origin.
     */
    public static final gb_Axis yAxis() {
        return new gb_Axis(gb_Vector3.Y);
    }

    /**
     * Creates a new z-Axis3D object from the world origin.
     */
    public static final gb_Axis zAxis() {
        return new gb_Axis(gb_Vector3.Z);
    }

    public final gb_Vector3 origin;
    public final gb_Vector3 dir;

    /**
     * Creates a new z-Axis3D object from the world origin.
     */
    public gb_Axis() {
        this(gb_Vector3.Z);
    }

    public gb_Axis(float x, float y, float z) {
        this(new gb_Vector3(x, y, z));
    }

    public gb_Axis(gb_Ray ray) {
        this(ray.origin, ray.getDirection());
    }

    /**
     * Creates a new Axis3D from the world origin in the given direction.
     * 
     * @param dir
     *            direction vector
     */
    public gb_Axis(gb_Vector3 dir) {
        this(new gb_Vector3(), dir);
    }

    /**
     * Creates a new Axis3D from the given origin and direction.
     * 
     * @param o
     *            origin
     * @param dir
     *            direction
     */
    public gb_Axis(gb_Vector3 o, gb_Vector3 dir) {
        this.origin = o;
        this.dir = new gb_Vector3(dir).nor();
    }
}
