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

/**
 * A filter which simulates chrome.
 */
public class ib_FilterChrome extends ib_FilterLight {
	private float amount = 0.5f;
	private float exposure = 1.0f;

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public float getAmount() {
		return amount;
	}

	public void setExposure(float exposure) {
		this.exposure = exposure;
	}
	
	public float getExposure() {
		return exposure;
	}

    public BufferedImage filter( BufferedImage src, BufferedImage dst ) {
		setColorSource( ib_FilterLight.COLORS_CONSTANT );
		dst = super.filter( src, dst );
		ib_a_FilterTransfer tf = new ib_a_FilterTransfer() {
			protected float transferFunction( float v ) {
				v += amount * (float)Math.sin( v * 2 * Math.PI );
				return 1 - (float)Math.exp(-v * exposure);
			}
		};
        return tf.filter( dst, dst );
    }

	public String toString() {
		return "Effects/Chrome...";
	}
}

