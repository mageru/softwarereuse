package reuze;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;


public class ib_FilterWaterDrop extends ib_a_Ops {
    MemoryImageSource source;
    Image image, offImage;
    Graphics offGraphics;

    short ripplemap[];
    int texture[];
    int ripple[];
    int oldind,newind,mapind,width,height,i,hwidth,hheight;
    int riprad;
    BufferedImage im;
    public ib_FilterWaterDrop(Component g, BufferedImage image) {
    	im=image;
    	width = im.getWidth();
        height = im.getHeight();
        hwidth = width>>1;
        hheight = height>>1;
        riprad=3;
        ripplemap = new short[width * (height+2) * 2];
        ripple = new int[width*height];
        texture = new int[width*height];
        oldind = width;
        newind = width * (height+3);
        PixelGrabber pg = new PixelGrabber(im,0,0,width,height,texture,0,width);
        try {
          pg.grabPixels();
          } catch (InterruptedException e) {}


        source = new MemoryImageSource(width, height, ripple, 0, width);
        source.setAnimated(true);
        source.setFullBufferUpdates(true);

        this.image = g.createImage(source);
        offImage = g.createImage(width, height);
        offGraphics = offImage.getGraphics();
    }
    public void disturb(int dx, int dy) {
        for (int j=dy-riprad;j<dy+riprad;j++) {
          for (int k=dx-riprad;k<dx+riprad;k++) {
            if (j>=0 && j<height && k>=0 && k<width) {
  	        ripplemap[oldind+(j*width)+k] += 512;            
            } 
          }
        }
      }

      private void newframe() {
        //Toggle maps each frame
        i=oldind;
        oldind=newind;
        newind=i;

        i=0;
        mapind=oldind;
        for (int y=0;y<height;y++) {
          for (int x=0;x<width;x++) {
  	        short data = (short)((ripplemap[mapind-width]+ripplemap[mapind+width]+ripplemap[mapind-1]+ripplemap[mapind+1])>>1);
            data -= ripplemap[newind+i];
            data -= data >> 5;
            ripplemap[newind+i]=data;

  	  //where data=0 then still, where data>0 then wave
  	  data = (short)(1024-data);

            //offsets
      int a=((x-hwidth)*data/1024)+hwidth;
      int b=((y-hheight)*data/1024)+hheight;

   	  //bounds check
            if (a>=width) a=width-1;
            if (a<0) a=0;
            if (b>=height) b=height-1;
            if (b<0) b=0;

            ripple[i]=texture[a+(b*width)];
            mapind++;
  	        i++;
          }
        }
      }
    public BufferedImage filter( BufferedImage src, BufferedImage dst ) {
        if ( dst == null )
            dst = createCompatibleDestImage( src, null );
        newframe();
        source.newPixels();
        offGraphics.drawImage(image,0,0,width,height,null);
        Graphics2D g=dst.createGraphics();
        g.drawImage(offImage,0,0,null);
        g.dispose();
        return dst;
    }
    
      public String toString() {
            return "Render/Water Drops...";
      }
}