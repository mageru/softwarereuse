package reuze;
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
public class mvc_Vector4f {
	public float x, y, z, w;

	public mvc_Vector4f() {
		this( 0, 0, 0, 0 );
	}
	
	public mvc_Vector4f( float[] x ) {
		this.x = x[0];
		this.y = x[1];
		this.z = x[2];
		this.w = x[2];
	}

	public mvc_Vector4f( float x, float y, float z, float w ) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public mvc_Vector4f( mvc_Vector4f t ) {
		this.x = t.x;
		this.y = t.y;
		this.z = t.z;
		this.w = t.w;
	}

	public void absolute() {
		x = Math.abs(x);
		y = Math.abs(y);
		z = Math.abs(z);
		w = Math.abs(w);
	}

	public void absolute( mvc_Vector4f t ) {
		x = Math.abs(t.x);
		y = Math.abs(t.y);
		z = Math.abs(t.z);
		w = Math.abs(t.w);
	}

	public void clamp( float min, float max ) {
		if ( x < min )
			x = min;
		else if ( x > max )
			x = max;
		if ( y < min )
			y = min;
		else if ( y > max )
			y = max;
		if ( z < min )
			z = min;
		else if ( z > max )
			z = max;
		if ( w < min )
			w = min;
		else if ( w > max )
			w = max;
	}

	public void set( float x, float y, float z, float w ) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public void set( float[] x ) {
		this.x = x[0];
		this.y = x[1];
		this.z = x[2];
		this.w = x[2];
	}

	public void set( mvc_Vector4f t ) {
		x = t.x;
		y = t.y;
		z = t.z;
		w = t.w;
	}

	public void get( mvc_Vector4f t ) {
		t.x = x;
		t.y = y;
		t.z = z;
		t.w = w;
	}

	public void get( float[] t ) {
		t[0] = x;
		t[1] = y;
		t[2] = z;
		t[3] = w;
	}

	public void negate() {
		x = -x;
		y = -y;
		z = -z;
		w = -w;
	}

	public void negate( mvc_Vector4f t ) {
		x = -t.x;
		y = -t.y;
		z = -t.z;
		w = -t.w;
	}

	public void interpolate( mvc_Vector4f t, float alpha ) {
		float a = 1-alpha;
		x = a*x + alpha*t.x;
		y = a*y + alpha*t.y;
		z = a*z + alpha*t.z;
		w = a*w + alpha*t.w;
	}

	public void scale( float s ) {
		x *= s;
		y *= s;
		z *= s;
		w *= s;
	}

	public void add( mvc_Vector4f t ) {
		x += t.x;
		y += t.y;
		z += t.z;
		w += t.w;
	}

	public void add( mvc_Vector4f t1, mvc_Vector4f t2 ) {
		x = t1.x+t2.x;
		y = t1.y+t2.y;
		z = t1.z+t2.z;
		w = t1.w+t2.w;
	}

	public void sub( mvc_Vector4f t ) {
		x -= t.x;
		y -= t.y;
		z -= t.z;
		w -= t.w;
	}

	public void sub( mvc_Vector4f t1, mvc_Vector4f t2 ) {
		x = t1.x-t2.x;
		y = t1.y-t2.y;
		z = t1.z-t2.z;
		w = t1.w-t2.w;
	}

	public String toString() {
		return "["+x+", "+y+", "+z+", "+w+"]";
	}
	
}
