package reuze;
import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.CompositeContext;
import java.awt.RenderingHints;
import java.awt.image.ColorModel;

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

public final class vc_MiscComposite implements Composite {

	public final static int BLEND = 0;
	public final static int ADD = 1;
	public final static int SUBTRACT = 2;
	public final static int DIFFERENCE = 3;

	public final static int MULTIPLY = 4;
	public final static int DARKEN = 5;
	public final static int BURN = 6;
	public final static int COLOR_BURN = 7;

	public final static int SCREEN = 8;
	public final static int LIGHTEN = 9;
	public final static int DODGE = 10;
	public final static int COLOR_DODGE = 11;

	public final static int HUE = 12;
	public final static int SATURATION = 13;
	public final static int VALUE = 14;
	public final static int COLOR = 15;

	public final static int OVERLAY = 16;
	public final static int SOFT_LIGHT = 17;
	public final static int HARD_LIGHT = 18;
	public final static int PIN_LIGHT = 19;

	public final static int EXCLUSION = 20;
	public final static int NEGATION = 21;
	public final static int AVERAGE = 22;

	public final static int STENCIL = 23;
	public final static int SILHOUETTE = 24;

	private static final int MIN_RULE = BLEND;
	private static final int MAX_RULE = SILHOUETTE;

	public static String[] RULE_NAMES = {
		"Normal",
		"Add",
		"Subtract",
		"Difference",

		"Multiply",
		"Darken",
		"Burn",
		"Color Burn",
		
		"Screen",
		"Lighten",
		"Dodge",
		"Color Dodge",

		"Hue",
		"Saturation",
		"Brightness",
		"Color",
		
		"Overlay",
		"Soft Light",
		"Hard Light",
		"Pin Light",

		"Exclusion",
		"Negation",
		"Average",

		"Stencil",
		"Silhouette",
	};

	protected float extraAlpha;
	protected int rule;

	private vc_MiscComposite(int rule) {
		this(rule, 1.0f);
	}

	private vc_MiscComposite(int rule, float alpha) {
		if (alpha < 0.0f || alpha > 1.0f)
			throw new IllegalArgumentException("alpha value out of range");
		if (rule < MIN_RULE || rule > MAX_RULE)
			throw new IllegalArgumentException("unknown composite rule");
		this.rule = rule;
		this.extraAlpha = alpha;
	}

	public static Composite getInstance(int rule, float alpha) {
		switch ( rule ) {
		case vc_MiscComposite.BLEND:
			return AlphaComposite.getInstance( AlphaComposite.SRC_OVER, alpha );
		case vc_MiscComposite.ADD:
			return new vc_ComposeAdd( alpha );
		case vc_MiscComposite.SUBTRACT:
			return new vc_SubtractComposite( alpha );
		case vc_MiscComposite.DIFFERENCE:
			return new ib_FilterCompositeDifference( alpha );
		case vc_MiscComposite.MULTIPLY:
			return new ib_FilterMultiplyComposite( alpha );
		case vc_MiscComposite.DARKEN:
			return new ib_FilterDarken( alpha );
		case vc_MiscComposite.BURN:
			return new ib_FilterBurn( alpha );
		case vc_MiscComposite.COLOR_BURN:
			return new ib_FilterColorBurn( alpha );
		case vc_MiscComposite.SCREEN:
			return new vc_ScreenComposite( alpha );
		case vc_MiscComposite.LIGHTEN:
			return new ib_FilterLightenComposite( alpha );
		case vc_MiscComposite.DODGE:
			return new ib_FilterDodge( alpha );
		case vc_MiscComposite.COLOR_DODGE:
			return new ib_FilterColorDodgeComposite( alpha );
		case vc_MiscComposite.HUE:
			return new ib_FilterHue( alpha );
		case vc_MiscComposite.SATURATION:
			return new vc_SaturationComposite( alpha );
		case vc_MiscComposite.VALUE:
			return new vc_ValueComposite( alpha );
		case vc_MiscComposite.COLOR:
			return new ib_FilterColor( alpha );
		case vc_MiscComposite.OVERLAY:
			return new ib_FilterOverlayComposite( alpha );
		case vc_MiscComposite.SOFT_LIGHT:
			return new vc_SoftLightComposite( alpha );
		case vc_MiscComposite.HARD_LIGHT:
			return new ib_FilterHardLight( alpha );
		case vc_MiscComposite.PIN_LIGHT:
			return new vc_CompositePinLight( alpha );
		case vc_MiscComposite.EXCLUSION:
			return new ib_FilterExclusionComposite( alpha );
		case vc_MiscComposite.NEGATION:
			return new ib_FilterNegationComposite( alpha );
		case vc_MiscComposite.AVERAGE:
			return new ib_FilterAverage( alpha );
		case vc_MiscComposite.STENCIL:
			return AlphaComposite.getInstance( AlphaComposite.DST_IN, alpha );
		case vc_MiscComposite.SILHOUETTE:
			return AlphaComposite.getInstance( AlphaComposite.DST_OUT, alpha );
		}
		return new vc_MiscComposite(rule, alpha);
	}

	public CompositeContext createContext(ColorModel srcColorModel, ColorModel dstColorModel, RenderingHints hints) {
		//return new MiscCompositeContext( rule, extraAlpha, srcColorModel, dstColorModel );
		return null;
	}

	public float getAlpha() {
		return extraAlpha;
	}

	public int getRule() {
		return rule;
	}

	public int hashCode() {
		return (Float.floatToIntBits(extraAlpha) * 31 + rule);
	}

	public boolean equals(Object o) {
		if (!(o instanceof vc_MiscComposite))
			return false;
		vc_MiscComposite c = (vc_MiscComposite)o;

		if (rule != c.rule)
			return false;
		if (extraAlpha != c.extraAlpha)
			return false;
		return true;
	}
			
}
