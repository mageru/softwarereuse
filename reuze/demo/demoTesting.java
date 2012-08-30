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
	  //WHITE BLOCKS
	  fill(0);
	  rect(0,0,height/3,width/3); //TOP LEFT
	  rect((width/3)*2,0,height/3,width/3); //TOP RIGHT
	  rect(0,(width/3)*2,height/3,width/3); //BOTTOM RIGHT
	  rect((width/3)*2,(width/3)*2,height/3,width/3); //BOTTOM LEFT
	  rect(width/3,height/3,width/3,height/3); // MIDDLE
	  //BLACK BLOCKS
	  fill(255);
	  rect(width/3,0,height/3,width/3); //TOP MIDDLE
	  rect(0,width/3,height/3,width/3);
	  rect((width/3)*2,width/3,height/3,width/3);
	  rect(width/3,width/3*2,height/3,width/3);
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

}
