package reuze.demo;
import processing.core.PApplet;
import processing.core.PImage;
import reuze.vgu_e_ToxiclibsSupport;
import reuze.ga_Vector2;
import reuze.ga_Voronoi;
import reuze.ga_Polygon;

public class demoToxiImageToVoronoi extends PApplet {
	vgu_e_ToxiclibsSupport gfx;
	ga_Voronoi voronoi;
	PImage reference;

	public void setup()
	{
	    size( 600, 894 );
	    
	    gfx = new vgu_e_ToxiclibsSupport( this );
	    voronoi = new ga_Voronoi();
	    
	    reference = loadImage("../data/elf/elf.jpg");
	    
	    for ( int i = 0; i < 2000; i++ ) {
	        voronoi.addPoint( new ga_Vector2( random(width), random(height) ) );
	    }
	    
	    noLoop();
	}

	public void draw()
	{
	    background( 0 );
	    fill( 0 );
	    smooth();
	    
	    int[] colors = new int[voronoi.getSites().size()];

	    for ( ga_Polygon polygon : voronoi.getRegions() ) {
	        for ( ga_Vector2 v : voronoi.getSites() ) {
	            if ( polygon.contains( v ) ) {
	                int c = reference.get( (int)v.x , (int)v.y );
	                fill( c );
	                gfx.polygon2D( polygon );
	            }
	        }
	    }
	}
}
