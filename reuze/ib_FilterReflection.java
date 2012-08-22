package reuze;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.image.BufferedImage;

public class ib_FilterReflection extends ib_a_Ops {

    public ib_FilterReflection() {
	}
	
    public BufferedImage filter( BufferedImage src, BufferedImage dst ) {
    	int gap = 20;
    	int height = src.getHeight()*2+gap;
    	if ( dst == null )
        	dst = new BufferedImage(src.getWidth(), height,
                    BufferedImage.TYPE_INT_ARGB);
        int imageWidth = src.getWidth();
        int imageHeight = src.getHeight();
        float opacity = 0.4f;
        float fadeHeight = 0.3f;
        Graphics2D g2d = dst.createGraphics();
        g2d.setPaint( new GradientPaint( 0, 0, Color.black, 0, height, Color.darkGray ) );
        g2d.fillRect( 0, 0, imageWidth, height );
        g2d.translate( (imageWidth-imageWidth)/2, height/2-imageHeight );
        g2d.drawRenderedImage( src, null );
        g2d.translate( 0, 2*imageHeight+gap );
        g2d.scale( 1, -1 );

        BufferedImage reflection = new BufferedImage( imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB );
		Graphics2D rg = reflection.createGraphics();
        rg.drawRenderedImage( src, null );
		rg.setComposite( AlphaComposite.getInstance( AlphaComposite.DST_IN ) );
        rg.setPaint( 
            new GradientPaint( 
                0, imageHeight*fadeHeight, new Color( 0.0f, 0.0f, 0.0f, 0.0f ),
                0, imageHeight, new Color( 0.0f, 0.0f, 0.0f, opacity )
            )
        );
        rg.fillRect( 0, 0, imageWidth, imageHeight );
        rg.dispose();
        g2d.drawRenderedImage( reflection, null );
        g2d.dispose();
        return dst;
    }
    
	public String toString() {
		return "Effects/Reflection...";
	}
}