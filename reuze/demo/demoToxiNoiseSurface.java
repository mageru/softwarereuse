package reuze.demo;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import processing.core.*;
//import processing.opengl.*;
import reuze.gb_IsoSurfaceHash;
import reuze.gb_i_IsoSurface;
import reuze.m_NoiseSimplex;
import reuze.vgu_e_ToxiclibsSupport;
import reuze.gb_TriangleMesh;
import reuze.gb_Vector3;
import reuze.gb_a_VoxelSpace;
import reuze.gb_VoxelSpace;
public class demoToxiNoiseSurface  extends PApplet {
	int DIMX=128;
	int DIMY=32;
	int DIMZ=64;

	float ISO_THRESHOLD = 0.1f;
	float NS=0.03f;
	gb_Vector3 SCALE=new gb_Vector3(3,0.5f,1).mul(300);

	gb_i_IsoSurface surface;
	gb_TriangleMesh mesh;

	boolean isWireframe=false;
	float currScale=1;

	vgu_e_ToxiclibsSupport gfx;

	public void setup() {
	  size(1024,768,P3D);
	  hint(ENABLE_OPENGL_4X_SMOOTH);
	  gfx=new vgu_e_ToxiclibsSupport(this);
	  strokeWeight(0.5f);
	  gb_a_VoxelSpace volume=new gb_VoxelSpace(SCALE,DIMX,DIMY,DIMZ);
	  // fill volume with noise
	  for(int z=0; z<DIMZ; z++) {
	    for(int y=0; y<DIMY; y++) {
	      for(int x=0; x<DIMX; x++) {
	        volume.setVoxelAt(x,y,z,(float)m_NoiseSimplex.noise(x*NS,y*NS,z*NS)*0.5f);
	      } 
	    } 
	  }
	  volume.closeSides();
	  long t0=System.nanoTime();
	  // store in IsoSurface and compute surface mesh for the given threshold value
	  mesh=new gb_TriangleMesh("iso");
	  surface=new gb_IsoSurfaceHash(volume,0.333333f);
	  surface.computeSurfaceMesh(mesh,ISO_THRESHOLD);
	  float timeTaken=(System.nanoTime()-t0)*1e-6f;
	  println(timeTaken+"ms to compute "+mesh.getNumFaces()+" faces");
	}

	public void draw() {
	  background(128);
	  translate(width/2,height/2,0);
	  rotateX(mouseY*0.01f);
	  rotateY(mouseX*0.01f);
	  scale(currScale);
	  ambientLight(48,48,48);
	  lightSpecular(230,230,230);
	  directionalLight(255,255,255,0,-0.5f,-1);
	  specular(255,255,255);
	  shininess(16.0f);
	  if (isWireframe) {
	    stroke(255);
	    noFill();
	  } 
	  else {
	    noStroke();
	    fill(255);
	  }
	  gfx.mesh(mesh);
	}

	public void mousePressed() {
	  isWireframe=!isWireframe;
	}
	public void keyPressed() {
	  if(key=='-') currScale=max(currScale-0.1f,0.5f);
	  if(key=='+') currScale=min(currScale+0.1f,10);
	}
}