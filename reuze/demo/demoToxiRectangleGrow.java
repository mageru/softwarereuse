package reuze.demo;
import java.util.ArrayList;
import java.util.List;

import processing.core.*;
import reuze.ga_Rectangle;
import reuze.vgu_e_ToxiclibsSupport;
import reuze.ga_Vector2;
public class demoToxiRectangleGrow  extends PApplet {
	List<ga_Vector2> points = new ArrayList<ga_Vector2>();
	ga_Rectangle bounds=new ga_Rectangle(200,200,0,0);

	vgu_e_ToxiclibsSupport gfx;

	public void setup() {
	  size(400,400);
	  smooth();
	  gfx=new vgu_e_ToxiclibsSupport(this);
	}

	public void draw() {
	  background(255);
	  noFill();
	  stroke(0);
	  gfx.rect(bounds);
	  fill(255,0,0);
	  noStroke();
	  for(ga_Vector2 p : points) {
	    gfx.circle(p,5);
	  }
	}

	public void mousePressed() {
	  ga_Vector2 p=new ga_Vector2(mouseX,mouseY);
	  points.add(p);
	  bounds.union(p);
	}
}