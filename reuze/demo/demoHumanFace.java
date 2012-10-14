package reuze.demo;

import processing.core.PApplet;
import processing.core.PGraphics;

/**
 * 3D Face implemented with Processing libraries.
 * @author Justin Miller
 *
 */
public class demoHumanFace extends PApplet {

	private static final long serialVersionUID = -9078222686705451689L;
	int x, y, xspeed, yspeed;
	boolean starting = true;
	PGraphics orb;

	public void setup() {
		size(400, 400);
		frameRate(30);
		smooth();
		x = 250;
		y = 250;
		xspeed = 0;
		yspeed = -4;
	}

	public void draw() {
		background(255);
		x = mouseX;
		y = mouseY;
		strokeWeight(2);
		stroke(0);
		fill(10, 110, 150, 127);
		
		beginShape(); // start - skull
		fill(239, 208, 207, 255);
		curveVertex(map(x, 0, width, 77, 154), map(y, 0, height, 63, 110));
		curveVertex(map(x, 0, width, 150, 241), map(y, 0, height, 24, 80));
		curveVertex(map(x, 0, width, 242, 327), map(y, 0, height, 66, 107));
		curveVertex(map(x, 0, width, 241, 336), map(y, 0, height, 184, 236));
		curveVertex(map(x, 0, width, 120, 286), map(y, 0, height, 244, 320));
		curveVertex(map(x, 0, width, 77, 167), map(y, 0, height, 182, 231));
		curveVertex(map(x, 0, width, 77, 154), map(y, 0, height, 63, 110));
		curveVertex(map(x, 0, width, 150, 241), map(y, 0, height, 24, 80));
		curveVertex(map(x, 0, width, 242, 327), map(y, 0, height, 66, 107));
		endShape(); // end - skull

		beginShape(); // right ear
		curveVertex(map(x, 0, width, 228, 335), map(y, 0, height, 110, 177));
		curveVertex(map(x, 0, width, 240, 335), map(y, 0, height, 105, 171));
		curveVertex(map(x, 0, width, 247, 335), map(y, 0, height, 116, 181));
		curveVertex(map(x, 0, width, 232, 329), map(y, 0, height, 127, 190));
		curveVertex(map(x, 0, width, 236, 335), map(y, 0, height, 117, 182));
		curveVertex(map(x, 0, width, 228, 335), map(y, 0, height, 110, 177));
		curveVertex(map(x, 0, width, 240, 335), map(y, 0, height, 105, 171));
		curveVertex(map(x, 0, width, 247, 335), map(y, 0, height, 116, 181));
		endShape();

		beginShape(); // left ear
		curveVertex(map(x, 0, width, 72, 155), map(y, 0, height, 104, 173));
		curveVertex(map(x, 0, width, 73, 161), map(y, 0, height, 93, 162));
		curveVertex(map(x, 0, width, 74, 174), map(y, 0, height, 102, 167));
		curveVertex(map(x, 0, width, 75, 164), map(y, 0, height, 109, 176));
		curveVertex(map(x, 0, width, 72, 168), map(y, 0, height, 125, 191));
		curveVertex(map(x, 0, width, 72, 155), map(y, 0, height, 104, 173));
		curveVertex(map(x, 0, width, 73, 161), map(y, 0, height, 93, 162));
		curveVertex(map(x, 0, width, 74, 174), map(y, 0, height, 102, 167));
		endShape();

		beginShape(); // start - left eye
		fill(255, 255, 255, 255); // bluish
		// red - fill(179,30,30,127);
		curveVertex(map(x, 0, width, 75, 198), map(y, 0, height, 111, 180));
		curveVertex(map(x, 0, width, 98, 229), map(y, 0, height, 90, 186));
		curveVertex(map(x, 0, width, 116, 258), map(y, 0, height, 111, 181));
		curveVertex(map(x, 0, width, 96, 232), map(y, 0, height, 106, 199));
		curveVertex(map(x, 0, width, 75, 198), map(y, 0, height, 111, 180));
		curveVertex(map(x, 0, width, 98, 229), map(y, 0, height, 90, 186));
		curveVertex(map(x, 0, width, 116, 258), map(y, 0, height, 111, 181));
		endShape(); // end left eye

		beginShape(); // start - nose
		fill(239, 208, 207, 255); // skin ish color
		curveVertex(map(x, 0, width, 116, 271), map(y, 0, height, 145, 223));
		curveVertex(map(x, 0, width, 124, 272), map(y, 0, height, 136, 217));
		curveVertex(map(x, 0, width, 125, 279), map(y, 0, height, 146, 222));
		curveVertex(map(x, 0, width, 120, 276), map(y, 0, height, 145, 232));
		curveVertex(map(x, 0, width, 116, 271), map(y, 0, height, 145, 223));
		curveVertex(map(x, 0, width, 124, 272), map(y, 0, height, 136, 217));
		curveVertex(map(x, 0, width, 125, 279), map(y, 0, height, 146, 222));
		endShape(); // end nose

		beginShape(); // start - right eye
		fill(255, 255, 255, 255); // bluish
		curveVertex(map(x, 0, width, 146, 290), map(y, 0, height, 111, 183));
		curveVertex(map(x, 0, width, 178, 306), map(y, 0, height, 89, 187));
		curveVertex(map(x, 0, width, 215, 333), map(y, 0, height, 112, 179));
		curveVertex(map(x, 0, width, 177, 312), map(y, 0, height, 107, 199));
		curveVertex(map(x, 0, width, 146, 290), map(y, 0, height, 111, 183));
		curveVertex(map(x, 0, width, 178, 306), map(y, 0, height, 89, 187));
		curveVertex(map(x, 0, width, 215, 333), map(y, 0, height, 112, 179));
		endShape(); // end - right eye

		// start eyeball
		beginShape(); //
		fill(37, 87, 179, 127); // bluish
		curveVertex(map(x, 0, width, 94, 227), map(y, 0, height, 96, 191));
		curveVertex(map(x, 0, width, 99, 230), map(y, 0, height, 89, 188));
		curveVertex(map(x, 0, width, 103, 239), map(y, 0, height, 97, 192));
		curveVertex(map(x, 0, width, 96, 230), map(y, 0, height, 105, 199));
		curveVertex(map(x, 0, width, 94, 227), map(y, 0, height, 96, 191));
		curveVertex(map(x, 0, width, 99, 230), map(y, 0, height, 89, 188));
		curveVertex(map(x, 0, width, 103, 239), map(y, 0, height, 97, 192));
		endShape();

		beginShape();
		fill(37, 87, 179, 127); // bluish
		curveVertex(map(x, 0, width, 173, 307), map(y, 0, height, 99, 192));
		curveVertex(map(x, 0, width, 179, 308), map(y, 0, height, 90, 187));
		curveVertex(map(x, 0, width, 184, 317), map(y, 0, height, 96, 192));
		curveVertex(map(x, 0, width, 177, 312), map(y, 0, height, 107, 199));
		curveVertex(map(x, 0, width, 173, 307), map(y, 0, height, 99, 192));
		curveVertex(map(x, 0, width, 179, 308), map(y, 0, height, 90, 187));
		curveVertex(map(x, 0, width, 184, 317), map(y, 0, height, 96, 192));
		endShape();
		// end eyeball

		beginShape(); // start - mouth
		fill(255, 255, 255, 255);
		curveVertex(map(x, 0, width, 93, 243), map(y, 0, height, 161, 271));
		curveVertex(map(x, 0, width, 117, 282), map(y, 0, height, 167, 265));
		curveVertex(map(x, 0, width, 176, 316), map(y, 0, height, 165, 275));
		curveVertex(map(x, 0, width, 118, 282), map(y, 0, height, 186, 257));
		curveVertex(map(x, 0, width, 93, 243), map(y, 0, height, 161, 271));
		curveVertex(map(x, 0, width, 117, 282), map(y, 0, height, 167, 265));
		curveVertex(map(x, 0, width, 176, 316), map(y, 0, height, 165, 275));
		endShape(); // end - mouth

		beginShape(); // start - hat
		fill(0, 0, 0, 255);
		curveVertex(map(x, 0, width, 78, 144), map(y, 0, height, 69, 100));
		curveVertex(map(x, 0, width, 102, 174), map(y, 0, height, 20, 51));
		curveVertex(map(x, 0, width, 227, 293), map(y, 0, height, 19, 50));
		curveVertex(map(x, 0, width, 250, 343), map(y, 0, height, 63, 94));
		curveVertex(map(x, 0, width, 78, 144), map(y, 0, height, 69, 100));
		curveVertex(map(x, 0, width, 102, 174), map(y, 0, height, 20, 51));
		curveVertex(map(x, 0, width, 227, 293), map(y, 0, height, 19, 50));
		endShape(); // end - hat
	}
}
