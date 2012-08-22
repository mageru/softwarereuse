package reuze.demo;

import processing.core.PApplet;
import processing.core.PGraphics;

public class demoTesting extends PApplet {

	//PGraphics orb;

	//float[] x = new float[2];
	//float[] y = new float[2];
	float yPos = (float) 0.0;
	int valueRed = 0;
	int valueGreen = 0;
	int valueBlue = 0;
	String selected = "none";
	int player=1;

	public void setup() {
	  size(200, 200);
	  smooth(); 
	  background(0);
	  noStroke();
	  fill(226);
	  //TOP LEFT
	  fill(0);
	  rect(0,0,height/3,width/3);
	  //TOP MIDDLE
	  fill(255);
	  rect(width/3,0,height/3,width/3);
	  rect(0,width/3,height/3,width/3);
	  rect((width/3)*2,width/3,height/3,width/3);
	  rect(width/3,width/3*2,height/3,width/3);
	  //TOP RIGHT
	  fill(0);
	  rect((width/3)*2,0,height/3,width/3);
	  //BOTTOM RIGHT
	  fill(0);
	  rect(0,(width/3)*2,height/3,width/3);
	  //BOTTOM LEFT
	  fill(0);
	  rect((width/3)*2,(width/3)*2,height/3,width/3);
	  //rectMode(CENTER);
	  fill(0);
	  rect(width/3,height/3,width/3,height/3);
	  //rect(width/2,height/2,width/3,height/3);
	  //rect(width/2,height/2,width/3,height/3);
	  //rect(width/2,height/2,width/3,height/3);
	}
	
	public void draw() {
		if(selected.equals("middle")) {
			fill(valueRed,valueGreen,valueBlue);
			rect(width/3,height/3,width/3,height/3);
		}
		else if(selected.equals("topleft")) {
			fill(valueRed,valueGreen,valueBlue);
			rect(0,0,height/3,width/3);
		}
	}
	
	public void mousePressed() {
		selected="none";
			if(player == 1) { 
				valueRed = 204;
				valueGreen = 102;
				valueBlue = 0;
				player=2;
			} 
			else if (player == 2){
				valueRed = 255;
				valueGreen = 0;
				valueBlue = 0;
				player=1;
			}
		if((height/3) < mouseX && mouseX < (width/3)*2
			&& mouseY > (height/3) && mouseY < (height/3*2)) {
			selected="middle";			
		}
		if((width/3) > mouseX && mouseY < (height/3)
				) {
			selected="topleft";			
		}
	}

	/**
	public void draw() {
	  background(226);
	  dragSegment(0, mouseX, mouseY);
	  dragSegment(1, x[0], y[0]);
	}

	void dragSegment(int i, float xin, float yin) {
	  float dx = xin - x[i];
	  float dy = yin - y[i];
	  float angle = atan2(dy, dx);  
	  x[i] = xin - cos(angle) * segLength;
	  y[i] = yin - sin(angle) * segLength;
	  segment(x[i], y[i], angle);
	}

	void segment(float x, float y, float a) {
	  pushMatrix();
	  translate(x, y);
	  rotate(a);
	  line(0, 0, segLength, 0);
	  popMatrix();
	}

	void setXY() {
	  x = x + xspeed; y = y + yspeed;
	  if (x >780) {xspeed = (int)(random(-2,-3));}
	  if (y < 20)  {yspeed = (int)(random(2,3));}
	  if (x < 20) {xspeed = (int)(random(2,3));}
	  if (y > 380) {yspeed = (int)(random(-2,-3));}
	  if (x>80 && x<720 && y>80 && y<320) {
	    if ((int)(random(6))<1) {
	      xspeed = xspeed + (int)(random(-3,3));
	      while(xspeed==0) {
	        xspeed = xspeed + (int)(random(-3,3));
	      }
	      yspeed = yspeed + (int)(random(-3,3));
	      while(yspeed==0) {
	        yspeed = yspeed + (int)(random(-3,3));
	      }
	    }
	  }
	}

	void orb() {
	  image(orb,x-95,y-95);
	  fill(255,243,0,127); stroke(0,20);
	  ellipse(x,y,random(28,38),random(28,38));
	}  

	void BlueGuy() {  
	  strokeWeight(2); stroke(0);
	  fill(127,182,247);
	  beginShape(); // torso
	  curveVertex(map(x,0,width,99,91), map(y,0,height,412,414));
	  curveVertex(map(x,0,width,80,104), map(y,0,height,315,307));
	  curveVertex(map(x,0,width,122,171), map(y,0,height,281,282));
	  curveVertex(map(x,0,width,114,224), map(y,0,height,173,246));
	  curveVertex(map(x,0,width,183,311), map(y,0,height,180,249));
	  curveVertex(map(x,0,width,216,276), map(y,0,height,274,288));
	  curveVertex(map(x,0,width,296,317), map(y,0,height,316,305));
	  curveVertex(map(x,0,width,303,304), map(y,0,height,426,409));
	  curveVertex(map(x,0,width,99,91), map(y,0,height,412,414));
	  curveVertex(map(x,0,width,80,104), map(y,0,height,315,307));
	  curveVertex(map(x,0,width,122,171), map(y,0,height,281,282));
	  endShape();

	  fill(165,203,247);   
	  beginShape(); // head
	  curveVertex(map(x,0,width,40,217), map(y,0,height,212,256));
	  curveVertex(map(x,0,width,47,179), map(y,0,height,151,161));
	  curveVertex(map(x,0,width,47,170), map(y,0,height,89,108));
	  curveVertex(map(x,0,width,84,212), map(y,0,height,42,69));
	  curveVertex(map(x,0,width,194,324), map(y,0,height,44,76));
	  curveVertex(map(x,0,width,230,360), map(y,0,height,121,135));
	  curveVertex(map(x,0,width,214,352), map(y,0,height,172,186));
	  curveVertex(map(x,0,width,178,357), map(y,0,height,225,259));
	  curveVertex(map(x,0,width,67,330), map(y,0,height,248,301));
	  curveVertex(map(x,0,width,40,217), map(y,0,height,212,256));
	  curveVertex(map(x,0,width,47,179), map(y,0,height,151,161));
	  curveVertex(map(x,0,width,47,170), map(y,0,height,89,108));
	  endShape();

	  fill(212,230,252); 
	  beginShape(); // left eye
	  curveVertex(map(x,0,width,42,236), map(y,0,height,115,186));
	  curveVertex(map(x,0,width,53,269), map(y,0,height,91,167));
	  curveVertex(map(x,0,width,74,293), map(y,0,height,109,187));
	  curveVertex(map(x,0,width,55,274), map(y,0,height,123,204));
	  curveVertex(map(x,0,width,42,236), map(y,0,height,115,186));
	  curveVertex(map(x,0,width,53,269), map(y,0,height,91,167));
	  curveVertex(map(x,0,width,74,293), map(y,0,height,109,187));
	  endShape();

	  beginShape(); // right eye
	  curveVertex(map(x,0,width,106,327), map(y,0,height,106,189));
	  curveVertex(map(x,0,width,130,343), map(y,0,height,90,167));
	  curveVertex(map(x,0,width,157,359), map(y,0,height,113,185));
	  curveVertex(map(x,0,width,129,345), map(y,0,height,125,205));
	  curveVertex(map(x,0,width,106,327), map(y,0,height,106,189));
	  curveVertex(map(x,0,width,130,343), map(y,0,height,90,167));
	  curveVertex(map(x,0,width,157,359), map(y,0,height,113,185));
	  endShape();

	  fill(58,122,198); 
	  beginShape(); // left eyebrow
	  curveVertex(map(x,0,width,40,228), map(y,0,height,104,158));
	  curveVertex(map(x,0,width,50,265), map(y,0,height,70,144));
	  curveVertex(map(x,0,width,79,300), map(y,0,height,75,173));
	  curveVertex(map(x,0,width,56,266), map(y,0,height,83,160));
	  curveVertex(map(x,0,width,40,228), map(y,0,height,104,158));
	  curveVertex(map(x,0,width,50,265), map(y,0,height,70,144));
	  curveVertex(map(x,0,width,79,300), map(y,0,height,75,173));
	  endShape();

	  beginShape();   // right eyebrow
	  curveVertex(map(x,0,width,101,329), map(y,0,height,74,172));
	  curveVertex(map(x,0,width,147,355), map(y,0,height,74,141));
	  curveVertex(map(x,0,width,182,362), map(y,0,height,109,161));
	  curveVertex(map(x,0,width,141,351), map(y,0,height,85,155));
	  curveVertex(map(x,0,width,101,329), map(y,0,height,74,172));
	  curveVertex(map(x,0,width,147,355), map(y,0,height,74,141));
	  curveVertex(map(x,0,width,182,362), map(y,0,height,109,161));
	  endShape();

	  fill(15,85,167); 
	  beginShape(); // left pupil
	  curveVertex(map(x,0,width,50,259), map(y,0,height,109,189));
	  curveVertex(map(x,0,width,56,270), map(y,0,height,100,181));
	  curveVertex(map(x,0,width,63,281), map(y,0,height,107,189));
	  curveVertex(map(x,0,width,57,271), map(y,0,height,113,196));
	  curveVertex(map(x,0,width,50,259), map(y,0,height,109,189));
	  curveVertex(map(x,0,width,56,270), map(y,0,height,100,181));
	  curveVertex(map(x,0,width,63,281), map(y,0,height,107,189));
	  endShape();

	  beginShape(); //right pupil 
	  curveVertex(map(x,0,width,119,336), map(y,0,height,106,189));
	  curveVertex(map(x,0,width,128,343), map(y,0,height,100,182));
	  curveVertex(map(x,0,width,139,352), map(y,0,height,107,188));
	  curveVertex(map(x,0,width,127,343), map(y,0,height,115,197));
	  curveVertex(map(x,0,width,119,336), map(y,0,height,106,189));
	  curveVertex(map(x,0,width,128,343), map(y,0,height,100,182));
	  curveVertex(map(x,0,width,139,352), map(y,0,height,107,188));
	  endShape();

	  fill(55,91,134);
	  beginShape(); // mouth
	  curveVertex(map(x,0,width,47,298), map(y,0,height,192,259));
	  curveVertex(map(x,0,width,72,336), map(y,0,height,184,262));
	  curveVertex(map(x,0,width,105,350), map(y,0,height,193,255));
	  curveVertex(map(x,0,width,70,337), map(y,0,height,196,264));
	  curveVertex(map(x,0,width,47,298), map(y,0,height,192,259));
	  curveVertex(map(x,0,width,72,336), map(y,0,height,184,262));
	  curveVertex(map(x,0,width,105,350), map(y,0,height,193,255));
	  endShape();

	  fill(175,213,255);  
	  beginShape(); // nose
	  curveVertex(map(x,0,width,30,299), map(y,0,height,159,241));
	  curveVertex(map(x,0,width,82,318), map(y,0,height,115,199));
	  curveVertex(map(x,0,width,99,378), map(y,0,height,161,242));
	  curveVertex(map(x,0,width,30,299), map(y,0,height,159,241));
	  curveVertex(map(x,0,width,82,318), map(y,0,height,115,199));
	  curveVertex(map(x,0,width,99,378), map(y,0,height,161,242));
	  endShape();  
	}
	**/

}
