package reuze.pending;
/*
 * Copyright (c) 2008, Keith Woodward
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of Keith Woodward nor the names
 *    of its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 */


//import straightedge.geom.*;
//import straightedge.geom.vision.*;
//import straightedge.geom.util.*;
import java.util.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;

import reuze.d_Bag;
import reuze.ga_OccluderImpl;
import reuze.ga_Polygon;
import reuze.ga_PolygonAwtShape;
import reuze.ga_TileArrayIntersections;
import reuze.ga_TileBagIntersections;
import reuze.ga_Vector2;
import reuze.ga_VisionData;
import reuze.ga_VisionFinder;

import java.awt.*;
import java.awt.image.*;
import java.awt.geom.Point2D;

/**
 *
 * @author Keith
 */
public class demoVisionActiveRendering {
	JFrame frame;
	demoVisionActiveRendering.ViewPane view;
	volatile boolean keepRunning = true;

	final Object mutex = new Object();
	ArrayList<AWTEvent> events = new ArrayList<AWTEvent>();
	ArrayList<AWTEvent> eventsCopy = new ArrayList<AWTEvent>();
	ga_Vector2 lastMouseMovePoint = new ga_Vector2();

	ga_VisionFinder visionFinder;
	ga_VisionData visionData = null;
	double smallAmount = 0.0001;

	ArrayList<ga_OccluderImpl> movingOccluders;
	ga_TileBagIntersections<ga_OccluderImpl> stationaryOccluders;
	ArrayList<ga_OccluderImpl> allOccluders;

	Random rand = new Random(11);

	public demoVisionActiveRendering(){
		frame = new JFrame(this.getClass().getSimpleName());
		frame.setSize(500, 500);
		frame.setLocationRelativeTo(null);
		view = new demoVisionActiveRendering.ViewPane();
		frame.add(view);

		frame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				keepRunning = false;
				System.exit(0);
			}
		});
		frame.addComponentListener(new ComponentAdapter(){
			public void componentResized(ComponentEvent e){
				synchronized (mutex){
					events.add(e);
				}
			}
		});
		view.addMouseMotionListener(new MouseMotionListener(){
			public void mouseMoved(MouseEvent e){ synchronized (mutex){ events.add(e); } }
			public void mouseDragged(MouseEvent e){ synchronized (mutex){ events.add(e); } }
		});

		int numPoints = 20;
		float radius = 200;
		ga_Polygon boundaryPolygon = ga_Polygon.createRegularPolygon(numPoints, radius);
		// By making the eye (or light source) slightly offset from (0,0), it will prevent problems caused by collinearity.
		ga_Vector2 eye = new ga_Vector2(smallAmount, smallAmount);
		visionData = new ga_VisionData(eye, boundaryPolygon);
		visionFinder = new ga_VisionFinder();

		makeOccluders();

		frame.setVisible(true);

		Thread gameLoopThread = new Thread("GameLoop"){
			public void run(){
				long lastUpdateNanos = System.nanoTime();
				while(keepRunning){
					long currentNanos = System.nanoTime();
					float seconds = (currentNanos - lastUpdateNanos)/1000000000f;
					update(seconds);
					view.render();
					try{ Thread.sleep(1);}catch(Exception e){}
					lastUpdateNanos = currentNanos;
				}
			}
		};
		gameLoopThread.setDaemon(false);
		gameLoopThread.start();
	}

	public void makeOccluders(){
		ArrayList<ga_Polygon> polygons = makePolygons();
		allOccluders = new ArrayList<ga_OccluderImpl>();
		for (int i = 0; i < polygons.size(); i++){
			ga_Polygon polygon = polygons.get(i);
			allOccluders.add(new ga_OccluderImpl(polygon));
		}

		// Set up the stationaryOccluders TileArrayIntersections which will store
		// the stationary occluders and their intersection points.
		ga_TileArrayIntersections tileArrayIntersections = new ga_TileArrayIntersections(allOccluders, 100);
		stationaryOccluders = new ga_TileBagIntersections<ga_OccluderImpl>(tileArrayIntersections, new d_Bag());

		movingOccluders = new ArrayList<ga_OccluderImpl>();
		for (int i = 0; i < allOccluders.size(); i++){
			if (rand.nextBoolean() == true){
				movingOccluders.add(allOccluders.get(i));
			}else{
				stationaryOccluders.add(allOccluders.get(i));
			}
		}
	}

	public ArrayList<ga_Polygon> makePolygons(){
		ArrayList<ga_Polygon> polygons = new ArrayList<ga_Polygon>();

		// make some rectangles
		for (int i = 0; i < 4; i++){
			ga_Vector2 p = new ga_Vector2((float)rand.nextFloat()*frame.getWidth(), (float)rand.nextFloat()*frame.getHeight());
			ga_Vector2 p2 = new ga_Vector2((float)rand.nextFloat()*frame.getWidth(), (float)rand.nextFloat()*frame.getHeight());
			float width = 10 + 30*rand.nextFloat();
			ga_Polygon rect = ga_Polygon.createRectOblique(p, p2, width);
			polygons.add(rect);
		}
		// make a cross
		polygons.add(ga_Polygon.createRectOblique(40, 70, 100, 70, 20));
		polygons.add(ga_Polygon.createRectOblique(70, 40, 70, 100, 20));
		// make some stars
		for (int i = 0; i < 4; i++){
			ArrayList<ga_Vector2> pointList = new ArrayList<ga_Vector2>();
			int numPoints = 4 + rand.nextInt(4)*2;
			double angleIncrement = Math.PI*2f/(numPoints*2);
			float rBig = 40 + rand.nextFloat()*90;
			float rSmall = 20 + rand.nextFloat()*70;
			double currentAngle = 0;
			for (int k = 0; k < numPoints; k++){
				double x = rBig*Math.cos(currentAngle);
				double y = rBig*Math.sin(currentAngle);
				pointList.add(new ga_Vector2((float)x, (float)y));
				currentAngle += angleIncrement;
				x = rSmall*Math.cos(currentAngle);
				y = rSmall*Math.sin(currentAngle);
				pointList.add(new ga_Vector2((float)x, (float)y));
				currentAngle += angleIncrement;
			}
			ga_Polygon poly = new ga_Polygon(pointList);
			assert poly.isCounterClockWise();
			poly.translate(20 + (float)rand.nextFloat()*frame.getWidth(), 20 + (float)rand.nextFloat()*frame.getHeight());
			polygons.add(poly);
		}
		return polygons;
	}

	public void update(float seconds){
		// copy the events in a thread-safe manner. Note that the events are generated on Swing's EDT, but this update method and render occur on the game loop thread.
		synchronized(mutex){
			if (events.size() > 0){
				eventsCopy.addAll(events);
				events.clear();
			}
		}
		// process the events:
		if (eventsCopy.size() > 0){
			for (int i = 0; i < eventsCopy.size(); i++){
				AWTEvent awtEvent = eventsCopy.get(i);
				if (awtEvent instanceof MouseEvent){
					MouseEvent e = (MouseEvent)awtEvent;
					if (e.getID() == MouseEvent.MOUSE_MOVED){
						lastMouseMovePoint.x = e.getX();
						lastMouseMovePoint.y = e.getY();
					}
				}else if (awtEvent instanceof ComponentEvent){
					ComponentEvent e = (ComponentEvent)awtEvent;
					if (e.getID() == ComponentEvent.COMPONENT_RESIZED){
						this.makePolygons();
					}
				}
			}
			eventsCopy.clear();
		}

		// rotate the moving occluders
		float rotateSpeed = (float)(Math.PI*2f/16f);
		for (int i = 0; i < movingOccluders.size(); i++){
			ga_OccluderImpl occluder = movingOccluders.get(i);
			occluder.getPolygon().rotate(rotateSpeed*seconds);
		}

		// Move the eye and boundaryPolygon to wherever they need to be.
		// By making the eye slightly offset from its integer coordinate by smallAmount,
		// it will prevent problems caused by collinearity.
		visionData.eye.set((float)(lastMouseMovePoint.x + smallAmount), ((float)lastMouseMovePoint.y + smallAmount));
		visionData.boundaryPolygon.translateTo(visionData.eye);
		visionFinder.calc(visionData, stationaryOccluders, movingOccluders);
		/* Note that the above line is the fast way to process shadows since the
		 * stationaryOccluders TileBagIntersections caches all intersection points
		 * of the stationary occluders which means that the intersection points
		 * are only calculated once.
		 * The alternative slower but more convenient method just accepts a list
		 * of occluders, but every occluder is intersected with every other occluder
		 * every time this method is called:
		 * visionFinder.calc(visionData, allOccluders);
		 */
	}

	public class ViewPane extends JComponent {
		Image backImage;
		Graphics2D backImageGraphics2D;

		public void render() {
			if (getWidth() <= 0 || getHeight() <= 0) {
				System.out.println(this.getClass().getSimpleName() + ": width &/or height <= 0!!!");
				return;
			}
			if (backImage == null || getWidth() != backImage.getWidth(null) || getHeight() != backImage.getHeight(null)) {
				backImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TRANSLUCENT);
			}
			backImageGraphics2D = (Graphics2D)backImage.getGraphics();
			renderWorld();
			// It's always best to dispose of your Graphics objects.
			backImageGraphics2D.dispose();
			if (getGraphics() != null) {
				getGraphics().drawImage(backImage, 0, 0, null);
				Toolkit.getDefaultToolkit().sync(); // to flush the graphics commands to the graphics card.  see http://www.javagaming.org/forums/index.php?topic=15000.msg119601;topicseen#msg119601
			}
		}

		protected void renderWorld() {
			Graphics2D g = backImageGraphics2D;

			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
			float backGroundGrey = 77f/255f;
			g.setColor(new Color(backGroundGrey, backGroundGrey, backGroundGrey));
			g.fillRect(0, 0, getWidth(), getHeight());

			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			float g4 = 0.1f;
			g.setColor(new Color(g4, g4, g4));
			for (int i = 0; i < allOccluders.size(); i++) {
				g.fill(ga_PolygonAwtShape.getPolygon(allOccluders.get(i).getPolygon()));
			}

			if (visionData.visiblePolygon != null){
				Point2D.Double center = new Point2D.Double(visionData.eye.x, visionData.eye.y);
				float[] dist = {0.0f, 1.0f};
				float a = 0.9f;
				float c = backGroundGrey;
				Color[] colors = {new Color(a, a, a, 0.7f), new Color(c, c, c, 0.0f)};
				RadialGradientPaint paint = new RadialGradientPaint(center, (float)visionData.maxEyeToBoundaryPolygonPointDist, dist, colors);//, CycleMethod.REFLECT);
				g.setPaint(paint);
				g.fill(ga_PolygonAwtShape.getPolygon(visionData.visiblePolygon));

//				g.setColor(Color.RED);
//				for (int i = 0; i < visiblePolygon.getPoints().size(); i++){
//					KPoint p1 = visiblePolygon.getPoints().get(i);
//					g.drawString(""+i, (float)p1.x, (float)p1.y+10);
//				}
			}
			
			g.setColor(Color.RED);
			float r = 1f;
			g.fill(new Ellipse2D.Double(lastMouseMovePoint.x - r, lastMouseMovePoint.y - r, 2*r, 2*r));
		}
	}

	public static void main(String[] args){
		new demoVisionActiveRendering();
	}
}
