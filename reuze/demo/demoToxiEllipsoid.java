package reuze.demo;
import java.util.ArrayList;
import java.util.Iterator;

import processing.core.*;
//import processing.opengl.*;
import reuze.m_a_Wave;
import reuze.gb_i_SurfaceFunction;
import reuze.m_WaveSine;
import reuze.gb_SuperEllipsoid;
import reuze.gb_SurfaceMeshBuilder;
import reuze.vgu_e_ToxiclibsSupport;
import reuze.gb_TriangleMesh;
public class demoToxiEllipsoid  extends PApplet {
	gb_TriangleMesh mesh = new gb_TriangleMesh();

	m_a_Wave modX, modY;

	boolean isWireFrame;
	boolean showNormals;

	vgu_e_ToxiclibsSupport gfx;

	public void setup() {
	  size(1024,576, P3D);
	  modX = new m_WaveSine(0, 0.01f, 2.5f, 2.5f);
	  modY = new m_WaveSine(PI, 0.017f, 2.5f, 2.5f);
	  gfx=new vgu_e_ToxiclibsSupport(this);
	}

	public void draw() {
	  gb_i_SurfaceFunction functor=new gb_SuperEllipsoid(modX.update(), modY.update());
	  gb_SurfaceMeshBuilder b = new gb_SurfaceMeshBuilder(functor);
	  mesh = (gb_TriangleMesh)b.createMesh(null,80, 80);
	  mesh.computeVertexNormals();
	  background(0);
	  lights();
	  translate(width / 2, height / 2, 0);
	  rotateX(mouseY * 0.01f);
	  rotateY(mouseX * 0.01f);
	  gfx.origin(300);
	  if (isWireFrame) {
	    noFill();
	    stroke(255);
	  } 
	  else {
	    fill(255);
	    noStroke();
	  }
	  scale(2);
	  gfx.mesh(mesh, !isWireFrame, showNormals ? 10 : 0);
	}


	public void keyPressed() {
	  if (key == 'w') {
	    isWireFrame = !isWireFrame;
	  }
	  if (key == 'n') {
	    showNormals = !showNormals;
	  }
	}
}