package reuze;
import java.awt.Color;

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
 * Vector math package, converted to look similar to javax.vecmath.
 */
public class vc_Color4f extends mvc_Vector4f {

	public vc_Color4f() {
		this( 0, 0, 0, 0 );
	}
	
	public vc_Color4f( float[] x ) {
		this.x = x[0];
		this.y = x[1];
		this.z = x[2];
		this.w = x[3];
	}

	public vc_Color4f( float x, float y, float z, float w ) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public vc_Color4f( vc_Color4f t ) {
		this.x = t.x;
		this.y = t.y;
		this.z = t.z;
		this.w = t.w;
	}

	public vc_Color4f( mvc_Vector4f t ) {
		this.x = t.x;
		this.y = t.y;
		this.z = t.z;
		this.w = t.w;
	}

	public vc_Color4f( Color c ) {
		set( c );
	}

	public void set( Color c ) {
		set( c.getRGBComponents( null ) );
	}

	public Color get() {
		return new Color( x, y, z, w );
	}
}
