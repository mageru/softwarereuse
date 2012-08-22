package reuze.demo;
import processing.core.*;
//import processing.opengl.*;
import reuze.vgu_e_ToxiclibsSupport;
import reuze.ga_Vector2;
import reuze.ga_Rectangle;
import reuze.gb_Vector3;
import reuze.gb_Triangle;
import reuze.gb_Sphere;
import reuze.gb_AABB3;
import reuze.ga_Ellipse;
import reuze.gb_Cone;
import reuze.gb_CylinderXAxisAligned;
public class demoToxiSupport  extends PApplet {
	vgu_e_ToxiclibsSupport gfx;

	public void setup() {
	  size(1024, 576, P3D);
	  
	  // attach drawing utils
	  gfx = new vgu_e_ToxiclibsSupport(this);
	}

	public void draw() {
	  gfx.line(new ga_Vector2(50,50), new ga_Vector2(250,250));
	  gfx.circle(new ga_Vector2(250,250), 20);
	  gfx.rect(new ga_Rectangle(250,250,60,40));
	  gfx.triangle(new gb_Triangle(new gb_Vector3(50,50,0),new gb_Vector3(50,150,0),new gb_Vector3(100,75,-30)));
	  gfx.sphere(new gb_Sphere(new gb_Vector3(50,350,0), 30), 20);
	  gfx.box(new gb_AABB3(new gb_Vector3(350,150,-10), 20));
	  gfx.ellipse(new ga_Ellipse(new ga_Vector2(350,200),30));
          gb_Cone con=new gb_Cone(new gb_Vector3(350,250,0),new gb_Vector3(400,350,140),20,30,80);
	  gfx.cone(con,20,true);
	  gfx.cylinder(new gb_CylinderXAxisAligned(new gb_Vector3(50,250,0),20,60));
	}

}