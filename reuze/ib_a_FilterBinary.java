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

public abstract class ib_a_FilterBinary extends ib_a_FilterWholeImage {

	protected int newColor = 0xff000000;
	protected vc_i_PredicateIsBlack blackFunction = new vc_PredicateIsBlack();
	protected int iterations = 1;
	protected vc_i_ColorMap colormap;

	public void setIterations(int iterations) {
		this.iterations = iterations;
	}

	public int getIterations() {
		return iterations;
	}

	public void setColormap(vc_i_ColorMap colormap) {
		this.colormap = colormap;
	}

	public vc_i_ColorMap getColormap() {
		return colormap;
	}

	public void setNewColor(int newColor) {
		this.newColor = newColor;
	}

	public int getNewColor() {
		return newColor;
	}

	public void setBlackFunction(vc_i_PredicateIsBlack blackFunction) {
		this.blackFunction = blackFunction;
	}

	public vc_i_PredicateIsBlack getBlackFunction() {
		return blackFunction;
	}

}

