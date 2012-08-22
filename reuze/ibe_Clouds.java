package reuze;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Random;


public class ibe_Clouds {
	
	private static BufferedImage []octaves=new BufferedImage[4];
	private static BufferedImage []upsampledOctaves=new BufferedImage[4];
	private static BufferedImage []finalOctaves=new BufferedImage[4];
	private static BufferedImage []cloudMaps=new BufferedImage[6];	

	public static BufferedImage[] generateClouds() {
			for(int j=0;j<cloudMaps.length;j++) {
				cloudMaps[j]=new BufferedImage(256,256,BufferedImage.TYPE_INT_RGB);
				noiseGenerator();
				smoothedNoise();
				upSampling(0,256,256);
				upSampling(1,128,128);
				upSampling(2,64,64);
				upSampling(3,32,32);
				for(int i=0;i<finalOctaves.length;i++){
					finalOctaves[i]=new BufferedImage(256,256,BufferedImage.TYPE_INT_RGB);
				}
				tile(1,0);
				tile(2,1);
				tile(4,2);
				tile(8,3);
				addMap(cloudMaps[j]);
				finalProcess(cloudMaps[j]);	
				
			}			
			return cloudMaps;
	}
	
	  
	private static void upSampling(int mapNum,int newWidth,int newHeight) {
		int width=octaves[mapNum].getWidth();
		int height=octaves[mapNum].getHeight();
		upsampledOctaves[mapNum]=new BufferedImage(newWidth,newHeight, BufferedImage.TYPE_INT_RGB);
	
		Graphics2D graphics2D = upsampledOctaves[mapNum].createGraphics();
		graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics2D.drawImage(octaves[mapNum], 0, 0,newWidth ,newHeight,0,0,width,height, null);
		graphics2D.dispose();
	}
	
	//compute the cloud density for each pixel
	public static int  cloudDensity(int cloudColor){
		float cloudCover=(float) 0.1;
		float c=(float) Math.max((cloudColor/256.0)-(1-cloudCover), 0);
		float cloudSharpness=(float) 0.9;
		float cloudDensity=(float) (1.0-Math.pow(1.0-cloudSharpness,c*255));
		
		return (int) Math.pow(cloudColor, cloudDensity);
	}
	
	//add sky color and compute the cloud density
	public static void finalProcess(BufferedImage cloudMap){
		
		for(int y=0;y<cloudMap.getHeight();y++){	
			for(int x=0;x<cloudMap.getWidth();x++){
				int pixel=cloudMap.getRGB(x,y);
				Color color=new Color(pixel);
				int r=m_MathUtils.clampToByte(cloudDensity(color.getRed()));
				int g=m_MathUtils.clampToByte(cloudDensity(color.getGreen()));
				int b=m_MathUtils.clampToByte(cloudDensity(color.getBlue()));
				int a=color.getAlpha();
				r=m_MathUtils.clampToByte(r+135);
				g=m_MathUtils.clampToByte(g+196);
				b=m_MathUtils.clampToByte(b+250);
			
				int newpixel=new Color(r,g,b,a).getRGB();
				
				cloudMap.setRGB(x, y, newpixel);
			}
		}
	}
	
    //add up all the octaves into one cloud map
	public static void addMap(BufferedImage cloudMap){
		int [] division={1,2,4,8};
		for(int i=0;i<finalOctaves.length;i++){
			for(int y=0;y<finalOctaves[i].getHeight();y++){	
				for(int x=0;x<finalOctaves[i].getWidth();x++){
					int pixel=cloudMap.getRGB(x,y);
					Color color=new Color(pixel);
					int oPixel=finalOctaves[i].getRGB(x, y);
					Color oColor=new Color(oPixel);
					int r=m_MathUtils.clampToByte(color.getRed()+(oColor.getRed()/division[i]));
					int g=m_MathUtils.clampToByte(color.getGreen()+(oColor.getGreen()/division[i]));
					int b=m_MathUtils.clampToByte(color.getBlue()+(oColor.getBlue()/division[i]));
					int newpixel=new Color(r,g,b).getRGB();
					
					cloudMap.setRGB(x, y, newpixel);
					
				}
			}
		}
	}
	
	
	
	
	//tile the octaves by their ratio
	public static void tile(int tileFactor,int mapNum){
		for(int i=0;i<tileFactor;i++){
			int starty=i*upsampledOctaves[mapNum].getHeight();
			int stopy=i*upsampledOctaves[mapNum].getHeight()+upsampledOctaves[mapNum].getHeight();
			for(int j=0;j<tileFactor;j++){
				int startx=j*upsampledOctaves[mapNum].getWidth();
				int stopx=j*upsampledOctaves[mapNum].getHeight()+upsampledOctaves[mapNum].getWidth();
				int tempY=0;
				for(int y=starty;y<stopy;y++){
					int tempX=0;
					for(int x=startx;x<stopx;x++){					
						finalOctaves[mapNum].setRGB(x, y, upsampledOctaves[mapNum].getRGB(tempX, tempY));
						tempX++;
					}
					tempY++;
				}
			}
		}
		
	
	}
	//smooth the noise using gaussian filter
	public static void smoothedNoise(){
		ib_FilterConvolveGaussian filter=new ib_FilterConvolveGaussian();
		filter.setRadius(2);
		for(int i=0;i<octaves.length;i++){
			filter.filter(octaves[i], octaves[i]);
		}
	}
	
	//generate random noise
	public static void noiseGenerator(){
		int[] pixels=null;
		for(int i=0;i<octaves.length;i++){
			octaves[i]=new BufferedImage(32,32,BufferedImage.TYPE_INT_RGB);
			pixels=new int[octaves[i].getHeight()*octaves[i].getWidth()];
			for(int j=0;j<octaves[i].getHeight()*octaves[i].getWidth();j++){
					int val=new Random().nextInt(255);
					
					pixels[j] = new Color(val, val, val).getRGB();
			}
			octaves[i].setRGB(0, 0,octaves[i].getWidth() , octaves[i].getHeight(), pixels, 0, octaves[i].getWidth());
			
		}
	}

}
	
