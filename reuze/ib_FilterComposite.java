package reuze;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
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
 * A filter which composites two images together with an optional transform.
 */
public class ib_FilterComposite extends ib_a_Ops {

	private Composite composite;
    private AffineTransform transform;
	
	public ib_FilterComposite() {
	}
	
	public ib_FilterComposite( Composite composite ) {
		this.composite = composite;
	}
	
	public ib_FilterComposite( Composite composite, AffineTransform transform ) {
		this.composite = composite;
		this.transform = transform;
	}
	
	public void setComposite( Composite composite ) {
		this.composite = composite;
	}
    
    public Composite getComposite() {
        return composite;
    }
	
	public BufferedImage filter( BufferedImage src, BufferedImage dst ) {
        if ( dst == null )
            dst = createCompatibleDestImage( src, null );

		Graphics2D g = dst.createGraphics();
        g.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
        g.setRenderingHint( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR );
        g.setComposite( composite );
        g.drawRenderedImage( src, transform );
        g.dispose();
		return dst;
	}

	public String toString() {
		return "Composite";
	}
}
