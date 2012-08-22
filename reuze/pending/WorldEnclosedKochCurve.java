package reuze.pending;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */




import java.awt.Container;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/*import straightedge.geom.AABB;
import straightedge.geom.KMultiPolygon;
import straightedge.geom.Vector2;
import straightedge.geom.Polygon;
import straightedge.geom.vision.CollinearOverlapChecker;*/

import reuze.ga_AABB;
import reuze.ga_PolygonCollinearOverlapChecker;
import reuze.ga_Polygon;
import reuze.ga_PolygonMultiAwt;
import reuze.ga_Vector2;


/**
 * See book 'The Algorithmic Beauty of Plants' (http://algorithmicbotany.org/papers/#abop)
 * @author Keith
 */
public class WorldEnclosedKochCurve extends World{
	public WorldEnclosedKochCurve(demoGameShooterMain main){
		super(main);
	}
	public void fillMultiPolygonsList(){
		Container cont = main.getParentFrameOrApplet();
		double contW = cont.getWidth() - (cont.getInsets().right + cont.getInsets().left);
		double contH = cont.getHeight() - (cont.getInsets().top + cont.getInsets().bottom);
		ga_Vector2 center = new ga_Vector2(contW/2f, contH/2f);
		ga_Polygon screenPoly = ga_Polygon.createRect(0f,0f, (float)contW, (float)contH);
		
		ArrayList<ga_Polygon> finalPolygons = new ArrayList<ga_Polygon>();
		ArrayList<ga_Polygon> allPolygons = new ArrayList<ga_Polygon>();
		// From book 'The Algorithmic Beauty of Plants' (http://algorithmicbotany.org/papers/#abop)
		
		// Enclosed Koch Curve
		String initiator = "F-F-F-F";
		String regex = "F";
		String replacer = "FF-F--F-F";
		int numIterations = 3;
		double dist = 17;
		double width = 4;
		double angleIncrement = Math.PI/2f;
		
		String instr = initiator;
		for (int i = 0; i < numIterations; i++){
			instr = instr.replaceAll(regex, replacer);
		}
//		// take off the last forward move to make a hole in the boundary.
//		instr = instr.substring(0, instr.lastIndexOf(regex)); 
		
		double angle = Math.PI/2f;	// direction starts facing up.
		ga_Vector2 p = new ga_Vector2(0,0);
		ga_Vector2 oldP = p.copy();
		for (int i = 0; i < instr.length(); i++){
			char c = instr.charAt(i);
			if (c == ('F') || c == ('L') || c == ('R')){
				p.x += Math.cos(angle)*dist;
				p.y += Math.sin(angle)*dist;
//				System.out.println(this.getClass().getSimpleName()+": p == "+p);
				allPolygons.add(ga_Polygon.createRectOblique(p, oldP, (float)width));
				oldP = p.copy();
			}else if (c == ('+')){
				angle += angleIncrement;
			}else if (c == ('-')){
				angle -= angleIncrement;
			}
		}
		
		// Move the polygons into the middle of the screen, 
		// and if there are to many then chop off the excess ones.
		ga_AABB bounds = ga_AABB.getAABBEnclosingCenterAndRadius(allPolygons.toArray());
		ga_Vector2 centerBounds = bounds.getCenter();
		for (int i = 0; i < allPolygons.size(); i++){
			ga_Polygon poly = allPolygons.get(i);
			poly.translate(center.x-centerBounds.x, center.y-centerBounds.y);
			if (screenPoly.contains(poly)){
				finalPolygons.add(poly);
			}
		}
		
		System.out.println(this.getClass().getSimpleName()+": finalPolygons.size() == "+finalPolygons.size());
		
		ga_PolygonCollinearOverlapChecker coc = new ga_PolygonCollinearOverlapChecker();
		coc.fixCollinearOverlaps(finalPolygons);
		
		for (int i = 0; i < finalPolygons.size(); i++){
			ga_PolygonMultiAwt multiPolygon = new ga_PolygonMultiAwt(finalPolygons.get(i).getPolygon().copy());
			allMultiPolygons.add(multiPolygon);
		}
	}
}
