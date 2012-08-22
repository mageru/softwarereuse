package reuze.demo;
import java.util.ArrayList;

import processing.core.PApplet;
import reuze.gb_i_Mesh;
import reuze.gb_i_OctreeVisitor;
import reuze.gb_PointOctree;
import reuze.gb_Sphere;
import reuze.vgu_e_ToxiclibsSupport;
import reuze.gb_TriangleMesh;
import reuze.gb_Vector3;

public class demoToxiOctTree  extends PApplet {
	float radius=200;

	vgu_e_ToxiclibsSupport gfx;
	TreeMeshBuilder builder;

	public void setup() {
	  size(640, 480, P3D);
	  gfx=new vgu_e_ToxiclibsSupport(this);
	  // create the root node of the octree
	  gb_PointOctree tree=new gb_PointOctree(new gb_Vector3(-radius, -radius, -radius), radius*2);
	  tree.setMinNodeSize(2);
	  // create a sphere mesh (slightly smaller than tree max bounds)
	  gb_i_Mesh sphere=new gb_Sphere(radius*0.9f).toMesh(20);
	  // add all sphere vertices to tree
	  tree.addAll(new ArrayList<gb_Vector3>(sphere.getVertices()));
	  // starting at the root node, recursively apply the mesh builder (see below) as visitor to all nodes
	  builder=new TreeMeshBuilder();
	  tree.applyVisitor(builder);
	}

	public void draw() {
	  background(255);
	  noFill();
	  stroke(0, 100);
	  translate(width/2, height/2, 0);
	  rotateX(mouseY*0.01f);
	  rotateY(mouseX*0.01f);
	  // display the box mesh
	  gfx.mesh(builder.mesh);
	}

	/**
	 * This class is a tree visitor implementation and creates a triangle mesh of
	 * all octree nodes.
	 */
	class TreeMeshBuilder implements gb_i_OctreeVisitor {

	  gb_TriangleMesh mesh=new gb_TriangleMesh();

	  public void visitNode(gb_PointOctree node) {
	    // Octree nodes are AABBs and therefore have a toMesh() method
	    // simply add each to the main mesh... 
	    mesh.addMesh(node.toMesh());
	  }
	}
}