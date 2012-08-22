package reuze.demo;
import java.util.Iterator;

import processing.core.PApplet;
import reuze.vc_ColorGradient;
import reuze.vc_ColorList;
import reuze.vc_e_Colors;
import reuze.m_InterpolateValueCosine;

public class demoToxiColorGradient  extends PApplet {
	public void setup() {
		  size(1000,200);
		  //noLoop();
		}

		public void draw() {
		  vc_ColorGradient grad=new vc_ColorGradient();
		  // use alternative interpolation function when mouse is pressed
		  if (!mousePressed) {
		    grad.setInterpolator(new m_InterpolateValueCosine());
		  }
		  grad.addColorAt(0,vc_e_Colors.getColor("BLACK"));
		  grad.addColorAt(width,vc_e_Colors.getColor("BLUE"));
		  grad.addColorAt(mouseX,vc_e_Colors.getColor("RED"));
		  grad.addColorAt(width-mouseX,vc_e_Colors.getColor("YELLOW"));
		  vc_ColorList l=grad.calcGradient(0,width);
		  int x=0;
		  for(Iterator i=l.iterator(); i.hasNext();) {
		    vc_e_Colors c=(vc_e_Colors)i.next();
		    int y=c.toARGB();
		    stroke(y);
		    line(x,0,x,height);
		    x++;
		  }
		}
}