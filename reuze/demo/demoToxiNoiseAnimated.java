package reuze.demo;
import processing.core.PApplet;
import reuze.gb_IsoSurfaceArrayVertices;
import reuze.gb_i_IsoSurface;
import reuze.m_NoiseSimplex;
import reuze.vgu_e_ToxiclibsSupport;
import reuze.gb_TriangleMesh;
import reuze.gb_Vector3;
import reuze.gb_VoxelSpace;

public class demoToxiNoiseAnimated  extends PApplet {
	int DIMX=48;
	int DIMY=48;
	int DIMZ=48;

	float ISO_THRESHOLD = 0.1f;
	float NS=0.03f;
	gb_Vector3 SCALE=new gb_Vector3(1,1,1).mul(300);

	boolean isWireframe=false;
	float currScale=1;

	gb_VoxelSpace volume=new gb_VoxelSpace(SCALE,DIMX,DIMY,DIMZ);
	gb_i_IsoSurface surface=new gb_IsoSurfaceArrayVertices(volume);
	gb_TriangleMesh mesh;

	vgu_e_ToxiclibsSupport gfx;

	public void setup() {
	  size(1024,576,OPENGL);
	  gfx=new vgu_e_ToxiclibsSupport(this);
	}

	public void draw() {
	  float[] volumeData=volume.getData();
	  // fill volume with noise
	  for(int z=0,index=0; z<DIMZ; z++) {
	    for(int y=0; y<DIMY; y++) {
	      for(int x=0; x<DIMX; x++) {
	        volumeData[index++]=(float)m_NoiseSimplex.noise(x*NS,y*NS,z*NS,frameCount*NS)*0.5f;
	      } 
	    } 
	  }
	  volume.closeSides();
	  long t0=System.nanoTime();
	  // store in IsoSurface and compute surface mesh for the given threshold value
	  surface.reset();
	  mesh=(gb_TriangleMesh)surface.computeSurfaceMesh(mesh, ISO_THRESHOLD);
	  float timeTaken=(System.nanoTime()-t0)*1e-6f;
	  println(timeTaken+"ms to compute "+mesh.getNumFaces()+" faces");
	  background(128);
	  translate(width/2,height/2,0);
	  rotateX(mouseY*0.01f);
	  rotateY(mouseX*0.01f);
	  scale(currScale);
	  ambientLight(48,48,48);
	  lightSpecular(230,230,230);
	  directionalLight(255,255,255,0f,-0.5f,-1);
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
	  gfx.mesh(mesh,true);
	}

	public void mousePressed() {
	  isWireframe=!isWireframe;
	}
	public void keyPressed() {
	  if(key=='-') currScale=max(currScale-0.1f,0.5f);
	  if(key=='+') currScale=min(currScale+0.1f,10f);
	}
}