package reuze.demo;

import processing.core.PApplet;

public class demoLifeEmergence extends PApplet {
	/* OpenProcessing Tweak of *@*http://www.openprocessing.org/sketch/1307*@* */
	/* !do not delete the line above, required for linking your tweak if you re-upload */
	int NUM_OF_AGENTS = 40;
	int HORIZON = 50;
	Agent[] agents = new Agent[NUM_OF_AGENTS];

	public void setup() {
	  size(250, 250);
	  ellipseMode(CENTER);
	  for(int i = 0; i < NUM_OF_AGENTS; i++) {
	    agents[i] = new Agent();
	  }
	}

	public void draw() {
	  float sumR = 0.0f;
	  float sumG = 0.0f;
	  float sumB = 0.0f;
	  int numNeightbours = 1;
	  background(0xCCCCCC);
	  for(int i = 0; i < NUM_OF_AGENTS; i++) {
	    int cs = agents[i].getColor();
	    sumR = red(cs)+0.0f;
	    sumG = green(cs)+0.0f;
	    sumB = blue(cs)+0.0f;
	    numNeightbours = 1;
	    for(int j = 0; j < NUM_OF_AGENTS; j++) {
	      if(j != i) {
	        boolean b = agents[i].attractionLaw(agents[j]);
	        if (b == true) {
	          int c = agents[j].getColor();
	          sumR += red(c);
	          sumG += green(c);
	          sumB += blue(c);
	          numNeightbours += 1;
	        }
	      }
	    }
	    if(numNeightbours > 1) {
	      agents[i].colorConvergenceLaw(color(round(sumR/numNeightbours), round(sumG/numNeightbours), round(sumB/numNeightbours)));
	      agents[i].updateColor();
	    }
	    // border collision avoiding
	    if(agents[i].position.x < 0.0) {
	      agents[i].speed.x += 5;
	    }
	    else if(agents[i].position.x > width) {
	      agents[i].speed.x -= 5;
	    }
	    if(agents[i].position.y < 0.0) {
	      agents[i].speed.y += 5;
	    }
	    else if(agents[i].position.y > height) {
	      agents[i].speed.y -= 5;
	    }
	    // end border collision avoiding
	    // drag force
	    agents[i].speed.x -= agents[i].speed.x*0.15;
	    agents[i].speed.y -= agents[i].speed.y*0.15;
	    // end drag force
	    agents[i].updatePosition();
	    fill(agents[i].getColor());
	    ellipse(agents[i].position.x, agents[i].position.y, 10, 10);
	  }
	}

	public void mouseClicked() {
	  for(int i = 0; i < NUM_OF_AGENTS; i++) {
	    agents[i].c = color(floor(random(255)), floor(random(255)), floor(random(255)));
	  }
	}

	class Agent {
	  public int c;
	  protected int nextC;
	  public Vector position;
	  public Vector speed;
	  
	  Agent() {
	    this.position = new Vector(random(width), random(height));
	    this.speed = new Vector(random(-10, 10), random(-10, 10));
	    this.c = color(floor(random(255)), floor(random(255)), floor(random(255)));
	  }
	  
	  float colorDistance(int c2) {
	    float distR = red(this.c) - red(c2);
	    float distG = green(this.c) - green(c2);
	    float distB = blue(this.c) - blue(c2);
	    return(sqrt(pow(distR, 2) + pow(distG, 2) + pow(distB, 2)));
	  }
	  
	  boolean attractionLaw(Agent other) {
	    float COEFF = 0.09f;
	    float d = dist(this.position.x, this.position.y, other.position.x, other.position.y);
	    if(d > HORIZON) {
	      // return false if other is over the horizon
	      return false;
	    }
	    
	    float f = (10 - 0.1f*colorDistance(other.c))*COEFF;
	    if(f < (-10.0f)*COEFF) {
	      f = (-10.0f)*COEFF;
	    }
	    if(colorDistance(other.c) < 20) {
	      f = (-5.0f)*COEFF;
	    }
	    
	    // collision avoiding
	    if(d < 20) {
	      f -= (100/(d+1)-(100/21))*COEFF;
	    }
	    Vector attractionForce = new Vector(f*((other.position.x-this.position.x)/d), f*((other.position.y-this.position.y)/d));
	    this.speed.x += attractionForce.x;
	    this.speed.y += attractionForce.y;
	    if(d < 40) {
	      return true;
	    }
	    return false;
	  }
	  
	  void colorConvergenceLaw(int averageColor) {
	    float COEFF = 0.05f;
	    float distR = red(averageColor) - red(this.c);
	    float distG = green(averageColor) - green(this.c);
	    float distB = blue(averageColor) - blue(this.c);
	    int r = round(red(this.c) + COEFF*distR);
	    int g = round(green(this.c) + COEFF*distG);
	    int b = round(blue(this.c) + COEFF*distB);
	    this.nextC = color(r, g, b);
	  }
	  
	  int getColor() {
	    return this.c;
	  }
	  
	  void updateColor() {
	    this.c = this.nextC;
	  }
	  
	  void updatePosition() {
	    this.position.x += this.speed.x;
	    this.position.y += this.speed.y;
	  }
	}

	class Vector {
	  public float x;
	  public float y;
	  
	  Vector(float x, float y) {
	    this.x = x;
	    this.y = y;
	  }
	}
}
