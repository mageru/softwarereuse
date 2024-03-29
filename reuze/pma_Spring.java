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



//import toxi.geom.Vec2D;

/**
 * <p>
 * A spring class connecting two VerletParticles in space. Based on the
 * configuration of the spring instance and that of the physics engine, the
 * behavior of the spring can vary between springy and stiff/stick like.
 * </p>
 * 
 * <p>
 * The simulation takes particle weights into account and can be configured to
 * lock either particle in space in order to force the other one to move. This
 * is sometimes handy for resolving collisions (currently outside the scope of
 * this library).
 * </p>
 * 
 * @see toxi.physics3d.VerletPhysics3D
 */
public class pma_Spring {

    protected static final float EPS = 1e-6f;

    /**
     * Spring end points / particles
     */
    public pma_ParticleVerlet a, b;

    /**
     * Spring rest length to which it always wants to return too
     */
    protected float restLength, restLengthSquared;

    /**
     * Spring strength, possible value range depends on engine configuration
     * (time step, drag)
     */
    protected float strength;

    /**
     * Flag, if either particle is locked in space (only within the scope of
     * this spring)
     */
    protected boolean isALocked, isBLocked;

    /**
     * @param a
     *            1st particle
     * @param b
     *            2nd particle
     * @param len
     *            desired rest length
     * @param str
     *            spring strength
     */
    public pma_Spring(pma_ParticleVerlet a, pma_ParticleVerlet b, float len,
            float str) {
        this.a = a;
        this.b = b;
        restLength = len;
        strength = str;
    }

    public float getRestLength() {
        return restLength;
    }

    public float getStrength() {
        return strength;
    }

    /**
     * (Un)Locks the 1st end point of the spring. <b>NOTE: this acts purely
     * within the scope of this spring instance and does NOT call
     * {@link pma_ParticleVerlet#lock()}</b>
     * 
     * @param s
     * @return itself
     */
    public pma_Spring lockA(boolean s) {
        isALocked = s;
        return this;
    }

    /**
     * (Un)Locks the 2nd end point of the spring
     * 
     * @param s
     * @return itself
     */

    public pma_Spring lockB(boolean s) {
        isBLocked = s;
        return this;
    }

    public pma_Spring setRestLength(float len) {
        restLength = len;
        restLengthSquared = len * len;
        return this;
    }

    public pma_Spring setStrength(float strength) {
        this.strength = strength;
        return this;
    }

    /**
     * Updates both particle positions (if not locked) based on their current
     * distance, weight and spring configuration *
     */
    protected void update(boolean applyConstraints) {
        ga_Vector2 delta=new ga_Vector2(b).sub(a);
        // add minute offset to avoid div-by-zero errors
        float dist = delta.len() + EPS;
        float normDistStrength = (dist - restLength)
                / (dist * (a.invWeight + b.invWeight)) * strength;
        if (!a.isLocked && !isALocked) {
        	ga_Vector2 temp=new ga_Vector2(delta);
            a.add(temp.mul(normDistStrength * a.invWeight));
            if (applyConstraints) {
                a.applyConstraints();
            }
        }
        if (!b.isLocked && !isBLocked) {
            b.add(delta.mul(-normDistStrength * b.invWeight));
            if (applyConstraints) {
                b.applyConstraints();
            }
        }
    }
}
