package reuze;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Shape;
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

public class ib_FilterMirror extends ib_a_Ops {
    private float opacity = 1.0f;
	private float centreY = 0.5f;
    private float gap;

    public ib_FilterMirror() {
	}
	
	public void setGap( float gap ) {
		this.gap = gap;
	}

	public float getGap() {
		return gap;
	}
	
	public void setOpacity( float opacity ) {
		this.opacity = opacity;
	}

	public float getOpacity() {
		return opacity;
	}
	
	public void setCentreY( float centreY ) {
		this.centreY = centreY;
	}

	public float getCentreY() {
		return centreY;
	}
	
    public BufferedImage filter( BufferedImage src, BufferedImage dst ) {
        if ( dst == null )
            dst = createCompatibleDestImage( src, null );
		Shape clip;
		int width = src.getWidth();
		int height = src.getHeight();
		int h = (int)(centreY * height);
		int d = (int)(gap * height);

		Graphics2D g = dst.createGraphics();
		clip = g.getClip();
		g.clipRect( 0, 0, width, h );
		g.drawRenderedImage( src, null );
		g.setClip( clip );
		g.clipRect( 0, h+d, width, height-h-d );
		g.translate( 0, 2*h+d );
		g.scale( 1, -1 );
		g.drawRenderedImage( src, null );
		g.setPaint( new GradientPaint( 0, 0, new Color( 1.0f, 0.0f, 0.0f, 0.0f ), 0, h, new Color( 0.0f, 1.0f, 0.0f, opacity ) ) );
		g.setComposite( AlphaComposite.getInstance( AlphaComposite.DST_IN ) );
		g.fillRect( 0, 0, width, h );
		g.setClip( clip );
		g.dispose();
        
        return dst;
    }
    
	public String toString() {
		return "Effects/Mirror...";
	}
}
