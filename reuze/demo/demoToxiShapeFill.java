package reuze.demo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import processing.core.*;
//import processing.opengl.*;
import reuze.ga_Circle;
import reuze.vc_e_Colors;
import reuze.ga_Ellipse;
import reuze.vgu_e_ToxiclibsSupport;
import reuze.ga_Vector2;
public class demoToxiShapeFill  extends PApplet {
	// container to store shapes with an associated color
	HashMap<ga_Circle,vc_e_Colors> shapes=new HashMap<ga_Circle,vc_e_Colors>();

	// helper class for rendering
	vgu_e_ToxiclibsSupport gfx;

	public void setup() {
	  size(680, 382);
	  smooth();
	  noFill();
	  background(255);
	  gfx=new vgu_e_ToxiclibsSupport(this);
	  // define pairs of shapes & colors
	  // here we're making use of polymorphism since
	  // each of these classes implements the Shape2D interface
	  shapes.put(new ga_Circle(new ga_Vector2(100, 100), 80), vc_e_Colors.getColor("red"));
	  //shapes.put(new Ellipses(new Vector2(300, 100), new Vector2(80, 50)), Colors.getColor("green"));
	  //shapes.put(new Triangle2D(new Vector2(400, 20), new Vector2(650, 100), new Vector2(500, 180)), Colors.getColor("blue"));
	  //shapes.put(Rectangle.fromCenterExtent(new Vector2(200,280), new Vector2(100,60)), Colors.getColor("cyan"));
	  //shapes.put(new Circles(new Vector2(450,280),80).toPolygon(5), Colors.getColor("magenta"));
	  // draw shape outlines
	  for (ga_Circle s : shapes.keySet()) {
	    gfx.polygon2D(s.toPolygon2D());
	  }
	}

	public void draw() {
	  noStroke();
	  // randomly sample all shapes
	  for (ga_Circle s : shapes.keySet()) {
	    // use color associated with shape
	    // and create a slightly modified/varying version for each dot
	    fill(shapes.get(s).analog(0.5f,0.5f).toARGB());
	    gfx.circle(s.getRandomPoint(), 5);
	  }
	}
}