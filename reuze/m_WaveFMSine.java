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
 * <p>
 * Frequency modulated sine wave. Uses a secondary wave to modulate the
 * frequency of the main wave.
 * </p>
 * 
 * <p>
 * <strong>Note:</strong> You must NEVER call the update() method on the
 * modulating wave.
 * </p>
 */
public class m_WaveFMSine extends m_a_Wave {

    public m_a_Wave fmod;

    public m_WaveFMSine(float phase, float freq, m_a_Wave fmod) {
        super(phase, freq);
        this.fmod = fmod;
    }

    public m_WaveFMSine(float phase, float freq, float amp, float offset) {
        this(phase, freq, amp, offset, new m_WaveConstant(0));
    }

    public m_WaveFMSine(float phase, float freq, float amp, float offset,
            m_a_Wave fmod) {
        super(phase, freq, amp, offset);
        this.fmod = fmod;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.math.waves.AbstractWave#pop()
     */
    @Override
    public void pop() {
        super.pop();
        fmod.pop();
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.math.waves.AbstractWave#push()
     */
    @Override
    public void push() {
        super.push();
        fmod.push();
    }

    /**
     * Resets this wave and its modulating wave as well.
     * 
     * @see toxi.math.waves.m_a_Wave#reset()
     */
    public void reset() {
        super.reset();
        fmod.reset();
    }

    /**
     * Progresses the wave and updates the result value. You must NEVER call the
     * update() method on the modulating wave since this is handled
     * automatically by this method.
     * 
     * @see toxi.math.waves.m_a_Wave#update()
     */
    public float update() {
        value = (float) (Math.sin(phase) * amp) + offset;
        cyclePhase(frequency + fmod.update());
        return value;
    }

}
