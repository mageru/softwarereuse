package reuze;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Random;


/*
** Copyright 2005 Huxtable.com. All rights reserved.
*/

public class ib_FilterScratch extends ib_a_Ops {
    private float density = 0.1f;
    private float angle;
    private float angleVariation = 1.0f;
    private float width = 0.5f;
    private float length = 0.5f;
    private int color = 0xffffffff;
    private int seed = 0;

    public ib_FilterScratch() {
      }
      
      public void setAngle( float angle ) {
            this.angle = angle;
      }

      public float getAngle() {
            return angle;
      }
      
      public void setAngleVariation( float angleVariation ) {
            this.angleVariation = angleVariation;
      }

      public float getAngleVariation() {
            return angleVariation;
      }
      
      public void setDensity( float density ) {
            this.density = density;
      }

      public float getDensity() {
            return density;
      }
      
      public void setLength( float length ) {
            this.length = length;
      }

      public float getLength() {
            return length;
      }
      
      public void setWidth( float width ) {
            this.width = width;
      }

      public float getWidth() {
            return width;
      }
      
      public void setColor( int color ) {
            this.color = color;
      }

      public int getColor() {
            return color;
      }

      public void setSeed( int seed ) {
            this.seed = seed;
      }

      public int getSeed() {
            return seed;
      }

    public BufferedImage filter( BufferedImage src, BufferedImage dst ) {
        if ( dst == null )
            dst = createCompatibleDestImage( src, null );

        int width = src.getWidth();
        int height = src.getHeight();
        int numScratches = (int)(density * width * height / 100);
        float l = length * width;
        Random random = new Random( seed );
        Graphics2D g = dst.createGraphics();
        g.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
        g.setColor( new Color( color ) );
        g.setStroke( new BasicStroke( this.width ) );
        for ( int i = 0; i < numScratches; i++ ) {
            float x = width * random.nextFloat();
            float y = height * random.nextFloat();
            float a = angle + m_MathUtils.TWO_PI * (angleVariation * (random.nextFloat() - 0.5f));
            float s = (float)Math.sin( a ) * l;
            float c = (float)Math.cos( a ) * l;
            float x1 = x-c;
            float y1 = y-s;
            float x2 = x+c;
            float y2 = y+s;
            g.drawLine( (int)x1, (int)y1, (int)x2, (int)y2 );
        }
        g.dispose();
        
        return dst;
    }
    
      public String toString() {
            return "Render/Scratches...";
      }
}