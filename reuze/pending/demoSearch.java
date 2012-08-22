package reuze.pending;


import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;

import javax.imageio.ImageIO;

import processing.core.PApplet;
import processing.core.PImage;
import reuze.vg_e_Processing;
import reuze.das_GraphAstarCrowFlight;
import reuze.das_GraphAstarManhattan;
import reuze.dag_GraphFloatingEdges;
import reuze.vgu_GraphDraw;
import reuze.dag_GraphEdge;
import reuze.ib_GraphTiled;
import reuze.dag_GraphNode;
import reuze.dag_GraphSearch_Astar;
import reuze.dag_GraphSearch_BFS;
import reuze.dag_GraphSearch_DFS;
import reuze.dag_GraphSearch_Dijkstra;
import reuze.f_dag_GraphFloatingEdges;
import reuze.dag_i_GraphSearch;


public class demoSearch extends PApplet {
	//  Graph graph;
	dag_GraphFloatingEdges[] gs = new dag_GraphFloatingEdges[4];
	PImage[] graphImage = new PImage[4];
	int start[] = new int[4];
	int end[] = new int[4];
	float nodeSize[] = new float[4];
	int graphNo = 0;
	float offX = 10, offY = 10;

	dag_GraphNode[] rNodes;
	dag_GraphEdge[] exploredEdges;

	dag_i_GraphSearch pathFinder;

	// Used to indicate the start and end nodes as selected by the user.
	dag_GraphNode startNode, endNode;
	boolean selectMode = false;

	int algorithm;
    String search="";
	long time;

	public void setup() {
		vg_e_Processing app=new vg_e_Processing(this);
		dag_GraphNode[] gNodes;
		size(800, 440);
		cursor(CROSS);
		smooth();
		ellipseMode(CENTER);
		textFont(createFont("SansSerif", 24));
		/* MAP 0 : Cityscape
		 * map created from B&W image. Nodes are only created in 
		 * white areas of the image.
		 * Edges are created between adjacent nodes including 
		 * diagonals and the traverse cost is based on distance
		 * between the nodes. 
		 */
		graphNo = 0;
		nodeSize[graphNo] = 10.0f;
		BufferedImage bi=null;
		InputStream is=createInput("map03.png");
		try {
		    bi = ImageIO.read(is);
		    is.close();
		} catch (IOException e) {
		}
		graphImage[graphNo] = loadImage("map03.png");
		gs[graphNo] = new dag_GraphFloatingEdges();
		ib_GraphTiled.makeGraphFromBWimage(gs[graphNo], bi, null, 30, 30, false);
		gNodes =  gs[graphNo].toArray();
		end[graphNo] = gNodes[(int) random(0, gNodes.length / 4)].id();
		do
			start[graphNo] = gNodes[(int) random((3 * gNodes.length) / 4, gNodes.length/4)].id();
		while (start[graphNo] == end[graphNo]);
		gs[graphNo].compact();

		/* MAP 1 : The maze 
		 * created in the same way as the first map but has
		 * single width passages to make a maze like map.
		 */
		graphNo = 1;
		nodeSize[graphNo] = 10.0f;
		is=createInput("maze2.png");
		try {
		    bi = ImageIO.read(is);
		    is.close();
		} catch (IOException e) {
		}
		graphImage[graphNo] = loadImage("maze2.png");
		gs[graphNo] = new dag_GraphFloatingEdges();
		ib_GraphTiled.makeGraphFromBWimage(gs[graphNo], bi, null, 20, 20, false);
		gNodes =  gs[graphNo].toArray();
		end[graphNo] = gNodes[(int) random(0, gNodes.length / 4)].id();
		do
			start[graphNo] = gNodes[(int) random((3 * gNodes.length) / 4, gNodes.length/4)].id();
		while (start[graphNo] == end[graphNo]);
		gs[graphNo].compact();

		/* MAP 2 : Landscape
		 * Created form 2 images the first is a grey-scale image 
		 * and nodes are created in non-black areas with edges
		 * created between adjacent nodes. The cost of traversing 
		 * and edge depends on the level of grey-scale under the
		 * 'from' node's position.
		 * The second image is used as the background image for 
		 * the map. The grey-scale image is obviously based on
		 * the background image.
		 */
		graphNo = 2;
		nodeSize[graphNo] = 10.0f;
		BufferedImage bic=null;
		InputStream isc=null;
		is=createInput("map1a.png");
		isc=createInput("map1b.png");
		try {
		    bi = ImageIO.read(is);
		    is.close();
		    bic = ImageIO.read(isc);
		    isc.close();
		} catch (IOException e) {
		}
		graphImage[graphNo] = loadImage("map1a.png");
		gs[graphNo] = new dag_GraphFloatingEdges();
		ib_GraphTiled.makeGraphFromBWimage(gs[graphNo], bi, bic, 20, 20, true);
		gNodes =  gs[graphNo].toArray();
		end[graphNo] = gNodes[(int) random(0, gNodes.length / 4)].id();
		do
			start[graphNo] = gNodes[(int) random((3 * gNodes.length) / 4, gNodes.length/4)].id();
		while (start[graphNo] == end[graphNo]);
		gs[graphNo].compact();

		/* MAP 3 : Middle earth
		 * This graph is created from a text file containing
		 * descriptions of the node positions and edges/costs.
		 * For details of the format used for this text file 
		 * see mearth.txt.
		 * This map has two one-way routes, for those familiar
		 * with the Lord of the Rings these are the Mines of 
		 * Moria (east > west) and the Paths of the Dead 
		 * (north > south)
		 */
		graphNo = 3;
		nodeSize[graphNo] = 12.0f;
		graphImage[graphNo] = loadImage("mearth.jpg");
		gs[graphNo] = new dag_GraphFloatingEdges();
		f_dag_GraphFloatingEdges.addToGraphFromFile(gs[graphNo], "mearth.txt");
		start[graphNo] = 107;
		end[graphNo] = 106;
		gs[graphNo].compact();

		// The GUI will need to know initial options selected
		algorithm = 3;
		graphNo = 3;

		// Create a path finder object based on the algorithm
		pathFinder = makePathFinder(gs[graphNo], algorithm);
		usePathFinder(pathFinder);
	}

	void usePathFinder(dag_i_GraphSearch pf) {
		time = System.nanoTime();
		pf.search(start[graphNo], end[graphNo], true);
		time = System.nanoTime() - time;
		rNodes = pf.getRoute();
		exploredEdges = pf.getExaminedEdges();
	}

	dag_i_GraphSearch makePathFinder(dag_GraphFloatingEdges graph, int pathFinder) {
		dag_i_GraphSearch pf = null;
		float f = (graphNo == 2) ? 2.0f : 1.0f;
		switch(pathFinder) {
		case 0:
			pf = new dag_GraphSearch_DFS(gs[graphNo]);
			search="Depth-First search";
			break;
		case 1:
			pf = new dag_GraphSearch_BFS(gs[graphNo]);
			search="Breadth-First search";
			break;
		case 2:
			pf = new dag_GraphSearch_Dijkstra(gs[graphNo]);
			search="Dijkstra's Shortest Path search";
			break;
		case 3:
			pf = new dag_GraphSearch_Astar(gs[graphNo], new das_GraphAstarCrowFlight(f));
			search="A* Crow-Flight search";
			break;
		case 4:
			pf = new dag_GraphSearch_Astar(gs[graphNo], new das_GraphAstarManhattan(f));
			search="A* Manhattan search";
			break;
		}
		return pf;
	}

	public void mousePressed() {
		// Only consider a mouse press if over the map
		if (mouseX < offX + 400 && mouseY < offY + 400 && mouseX > offX && mouseY > offY) {
			startNode = gs[graphNo].getNodeNear(mouseX - offX, mouseY - offY, 0.0, 16.0);
			if (startNode != null)
				selectMode = true;
		}
	}

	public void mouseDragged() {
		if (selectMode)
			endNode = gs[graphNo].getNodeNear(mouseX - offX, mouseY - offY, 0, 16.0f);
	}

	public void mouseReleased() {
		if (selectMode && endNode!= null && startNode != null && startNode != endNode) {
			start[graphNo] = startNode.id();
			end[graphNo] = endNode.id();
			usePathFinder(pathFinder);
		}
		selectMode = false;
		startNode = endNode = null;
	}
	
	public void keyTyped() {
		if (Character.isDigit(key)) {
			algorithm=(key-'0')%5;
			pathFinder = makePathFinder(gs[graphNo], algorithm);
			usePathFinder(pathFinder);
		} else if (key=='a' || key=='b' || key=='c' || key=='d') {
			graphNo=key-'a';
			pathFinder = makePathFinder(gs[graphNo], algorithm);
			usePathFinder(pathFinder);
		}
	}

	public void draw() {
		background(160, 160, 255);
		text(search,420,50);
		pushMatrix();
		translate(offX, offY);
		if (graphImage[graphNo] != null)
			image(graphImage[graphNo], 0, 0);

		if (graphNo == 3)
			vgu_GraphDraw.drawEdges(gs[graphNo], nodeSize[graphNo], color(192, 192, 192, 128), 1.0f, true);
		else
			vgu_GraphDraw.drawEdges(gs[graphNo], nodeSize[graphNo], color(240, 192, 240, 160), 1.0f, false); 

		vgu_GraphDraw.drawEdges(exploredEdges, nodeSize[graphNo], color(0, 0, 255), 1.8f, false);

		vgu_GraphDraw.drawNodes(gs[graphNo], nodeSize[graphNo], color(255, 0, 255, 72));

		vgu_GraphDraw.drawNodes(rNodes, nodeSize[graphNo], color(200, 0, 0), 5.0f);

		if (selectMode) {
			stroke(0);
			strokeWeight(1.5f);
			if (endNode != null)
				line(startNode.xf(), startNode.yf(), endNode.xf(), endNode.yf());
			else
				line(startNode.xf(), startNode.yf(), mouseX - offX, mouseY - offY);
		}
		popMatrix();
	}
}
