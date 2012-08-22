package reuze.demo;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import processing.core.*;
//import processing.opengl.*;
import reuze.m_a_Wave;
import reuze.gb_IsoSurfaceArrayVertices;
import reuze.gb_i_IsoSurface;
import reuze.gb_VoxelSpaceBrushRound;
import reuze.m_WaveSine;
import reuze.gb_Sphere;
import reuze.vgu_e_ToxiclibsSupport;
import reuze.gb_TriangleMesh;
import reuze.gb_Vector3;
import reuze.gb_a_VoxelSpaceBrush;
import reuze.gb_a_VoxelSpace;
import reuze.gb_VoxelSpace;
public class demoToxiVolumeBrush  extends PApplet {
	int DIMX=128;
	int DIMY=128;
	int DIMZ=128;

	float ISO_THRESHOLD = 0.1f;
	gb_Vector3 SCALE=new gb_Vector3(1,1,1).mul(100);

	gb_a_VoxelSpace volume;
	gb_a_VoxelSpaceBrush brush;
	gb_i_IsoSurface surface;
	gb_TriangleMesh mesh;

	m_a_Wave brushSize;

	boolean isWireframe=false;
	boolean doSave=false;

	float currScale=4;
	float density=0.5f;

	vgu_e_ToxiclibsSupport gfx;

	public void setup() {
	  size(1024,768,P3D);
	  hint(ENABLE_OPENGL_4X_SMOOTH);
	  gfx=new vgu_e_ToxiclibsSupport(this);
	  strokeWeight(0.5f);
	  volume=new gb_VoxelSpace(SCALE,DIMX,DIMY,DIMZ);
	  brush=new gb_VoxelSpaceBrushRound(volume,SCALE.x/2);
	  brushSize=new m_WaveSine(0,0.1f,SCALE.x*0.07f,SCALE.x*0.1f);
	  surface=new gb_IsoSurfaceArrayVertices(volume);
	  mesh=new gb_TriangleMesh();
	}

	public void draw() {
	  brush.setSize(brushSize.update());
	  gb_Vector3 mousePos=new gb_Vector3((mouseX-width/2)*0.5f,(mouseY-height/2)*0.5f,sin(frameCount*0.05f)*SCALE.z*0.5f);
	  if (mousePressed) {
		System.out.println(brush.toString());
	    brush.drawAtAbsolutePos(mousePos,density);
	    volume.closeSides();  
	    surface.reset();
	    surface.computeSurfaceMesh(mesh,ISO_THRESHOLD);
	  }
	  background(128);
	  translate(width/2,height/2,0);
	  lightSpecular(230,230,230);
	  directionalLight(255,255,255,1,1,-1);
	  shininess(1.0f);
	  rotateX(-0.4f);
	  rotateY(frameCount*0.05f);
	  scale(currScale);
	  beginShape(TRIANGLES);
	  if (isWireframe) {
	    stroke(255);
	    noFill();
	  } 
	  else {
	    noStroke();
	    fill(255);
	  }
	  gfx.mesh(mesh);
	  //endShape();
	  noFill();
	  stroke(255,0,0);
	  gfx.sphere(new gb_Sphere(mousePos,brushSize.value),20);
	}

	public void keyPressed() {
	  if(key=='-') currScale=max(currScale-0.1f,0.5f);
	  if(key=='+') currScale=min(currScale+0.1f,10);
	  if(key=='w') { 
	    isWireframe=!isWireframe; 
	    return;
	  }
	  if (key>='1' && key<='9') {
	    density=-0.5f+(key-'1')*0.1f;
	    println(density);
	  }
	  if (key=='0') density=0.5f;
	  if (key>='a' && key<='z') ISO_THRESHOLD=(key-'a')*0.019f+0.01f;
	}
}