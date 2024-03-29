package reuze.demo;
import processing.core.PApplet;
import reuze.vgu_e_ToxiclibsSupport;
import reuze.ga_Vector2;
import reuze.ga_Voronoi;
import reuze.ga_Polygon;

public class demoToxiVoronoi extends PApplet {
	vgu_e_ToxiclibsSupport gfx;
	ga_Voronoi voronoi;

	public void setup()
	{
	    size( 450, 360 );
	    
	    gfx = new vgu_e_ToxiclibsSupport( this );
	    voronoi = new ga_Voronoi();
	        
	    for ( int i = 0; i < 50; i++ ) {
	        voronoi.addPoint( new ga_Vector2( random(width), random(height) ) );
	    }
	    
	    noLoop();
	}

	public void draw()
	{
	    background( 0 );
	    fill( 0 );
	    smooth();
	    
	    for ( ga_Polygon polygon : voronoi.getRegions() ) {
	        strokeWeight( 1 );
	        stroke( 255 );
	        gfx.polygon2D( polygon );
	        stroke( 255, 0, 0 );
	        strokeWeight( 4 );
	        ga_Vector2 v = polygon.getCentroid();
	        point( v.x, v.y );
	    }
	    
	    strokeWeight( 4 );
	    stroke( 0, 255, 0 );
	    for ( ga_Vector2 v : voronoi.getSites() ) {
	        point( v.x, v.y );
	    }
	    
	    //saveFrame("images/toxiclibs-voronoi-002.png");
	}
}
