package reuze;
/*
 * Copyright (c) 2006-2011 Karsten Schmidt
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * http://creativecommons.org/licenses/LGPL/2.1/
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
 */



import reuze.gb_AABB3;
import reuze.gb_Sphere;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Implements a spatial subdivision tree to work efficiently with large numbers
 * of 3D particles. This octree can only be used for particle type objects and
 * does NOT support 3D mesh geometry as other forms of Octrees do.
 * 
 * For further reference also see the OctreeDemo in the /examples folder.
 * 
 */
public class gb_Octree extends gb_AABB3 /*implements Shape3D*/ {

    /**
     * alternative tree recursion limit, number of world units when cells are
     * not subdivided any further
     */
    protected float minNodeSize = 4;

    /**
	 * 
	 */
    protected gb_Octree parent;

    protected gb_Octree[] children;

    protected byte numChildren;

    protected ArrayList<gb_Vector3> points;

    protected float size, halfSize;

    protected gb_Vector3 offset;

    private int depth = 0;

    private boolean isAutoReducing = false;

    /**
     * Constructs a new PointOctree node within the AABB cube volume: {o.x, o.y,
     * o.z} ... {o.x+size, o.y+size, o.z+size}
     * 
     * @param p
     *            parent node
     * @param o
     *            tree origin
     * @param halfSize
     *            half length of the tree volume along a single axis
     */
    private gb_Octree(gb_Octree p, gb_Vector3 o, float halfSize) {
        super(o.cpy().add(halfSize, halfSize, halfSize), new gb_Vector3(halfSize,
                halfSize, halfSize));
        this.parent = p;
        this.halfSize = halfSize;
        this.size = halfSize * 2;
        this.offset = o;
        this.numChildren = 0;
        if (parent != null) {
            depth = parent.depth + 1;
            minNodeSize = parent.minNodeSize;
        }
    }

    /**
     * Constructs a new PointOctree node within the AABB cube volume: {o.x, o.y,
     * o.z} ... {o.x+size, o.y+size, o.z+size}
     * 
     * @param o
     *            tree origin
     * @param size
     *            size of the tree volume along a single axis
     */
    public gb_Octree(gb_Vector3 o, float size) {
        this(null, o, size / 2);
    }

    /**
     * Adds all points of the collection to the octree. IMPORTANT: Points need
     * be of type Vector3 or have subclassed it.
     * 
     * @param points
     *            point collection
     * @return true, if all points have been added successfully.
     */
    public boolean addAll(Collection<gb_Vector3> points) {
        boolean addedAll = true;
        for (gb_Vector3 p : points) {
            addedAll &= addPoint(p);
        }
        return addedAll;
    }

    /**
     * Adds a new point/particle to the tree structure. All points are stored
     * within leaf nodes only. The tree implementation is using lazy
     * instantiation for all intermediate tree levels.
     * 
     * @param p
     * @return true, if point has been added successfully
     */
    public boolean addPoint(gb_Vector3 p) {
        // check if point is inside cube
        if (contains(p)) {
            // only add points to leaves for now
            if (halfSize <= minNodeSize) {
                if (points == null) {
                    points = new ArrayList<gb_Vector3>();
                }
                points.add(p);
                return true;
            } else {
                gb_Vector3 plocal = p.tmp().sub(offset);
                if (children == null) {
                    children = new gb_Octree[8];
                }
                int octant = getOctantID(plocal);
                if (children[octant] == null) {
                    gb_Vector3 off =
                            offset.cpy().add(new gb_Vector3((octant & 1) != 0
                                    ? halfSize
                                    : 0, (octant & 2) != 0 ? halfSize : 0,
                                    (octant & 4) != 0 ? halfSize : 0));
                    children[octant] =
                            new gb_Octree(this, off, halfSize * 0.5f);
                    numChildren++;
                }
                return children[octant].addPoint(p);
            }
        }
        return false;
    }

    public boolean containsPoint(final gb_Vector3 p) {
        return this.isInAABB(p);
    }

    public void empty() {
        numChildren = 0;
        children = null;
        points = null;
    }

    /**
     * @return a copy of the child nodes array
     */
    public gb_Octree[] getChildren() {
        gb_Octree[] clones = new gb_Octree[8];
        System.arraycopy(children, 0, clones, 0, 8);
        return clones;
    }

    /**
     * @return the depth
     */
    public int getDepth() {
        return depth;
    }

    /**
     * Finds the leaf node which spatially relates to the given point
     * 
     * @param p
     *            point to check
     * @return leaf node or null if point is outside the tree dimensions
     */
    public gb_Octree getLeafForPoint(final gb_Vector3 p) {
        // if not a leaf node...
        if (this.isInAABB(p)) {
            if (numChildren > 0) {
                int octant = getOctantID(p.sub(offset));
                if (children[octant] != null) {
                    return children[octant].getLeafForPoint(p);
                }
            } else if (points != null) {
                return this;
            }
        }
        return null;
    }

    /**
     * Returns the minimum size of nodes (in world units). This value acts as
     * tree recursion limit since nodes smaller than this size are not
     * subdivided further. Leaf node are always smaller or equal to this size.
     * 
     * @return the minimum size of tree nodes
     */
    public float getMinNodeSize() {
        return minNodeSize;
    }

    public float getNodeSize() {
        return size;
    }

    /**
     * @return the number of child nodes (max. 8)
     */
    public int getNumChildren() {
        return numChildren;
    }

    /**
     * Computes the local child octant/cube index for the given point
     * 
     * @param plocal
     *            point in the node-local coordinate system
     * @return octant index
     */
    protected final int getOctantID(final gb_Vector3 plocal) {
        return (plocal.x >= halfSize ? 1 : 0) + (plocal.y >= halfSize ? 2 : 0)
                + (plocal.z >= halfSize ? 4 : 0);
    }

    /**
     * @return the offset
     */
    public gb_Vector3 getOffset() {
        return offset;
    }

    /**
     * @return the parent
     */
    public gb_Octree getParent() {
        return parent;
    }

    /**
     * @return the points
     */
    public List<gb_Vector3> getPoints() {
        List<gb_Vector3> results = null;
        if (points != null) {
            results = new ArrayList<gb_Vector3>(points);
        } else if (numChildren > 0) {
            for (int i = 0; i < 8; i++) {
                if (children[i] != null) {
                    List<gb_Vector3> childPoints = children[i].getPoints();
                    if (childPoints != null) {
                        if (results == null) {
                            results = new ArrayList<gb_Vector3>();
                        }
                        results.addAll(childPoints);
                    }
                }
            }
        }
        return results;
    }

    /**
     * Selects all stored points within the given axis-aligned bounding box.
     * 
     * @param b
     *            AABB
     * @return all points with the box volume
     */
    public ArrayList<gb_Vector3> getPointsWithinBox(gb_AABB3 b) {
        ArrayList<gb_Vector3> results = null;
        if (this.intersectsBox(b)) {
            if (points != null) {
                for (gb_Vector3 q : points) {
                    if (b.isInAABB(q)) {
                        if (results == null) {
                            results = new ArrayList<gb_Vector3>();
                        }
                        results.add(q);
                    }
                }
            } else if (numChildren > 0) {
                for (int i = 0; i < 8; i++) {
                    if (children[i] != null) {
                        ArrayList<gb_Vector3> points =
                                children[i].getPointsWithinBox(b);
                        if (points != null) {
                            if (results == null) {
                                results = new ArrayList<gb_Vector3>();
                            }
                            results.addAll(points);
                        }
                    }
                }
            }
        }
        return results;
    }

    /**
     * Selects all stored points within the given sphere volume
     * 
     * @param s
     *            sphere
     * @return selected points
     */
    public ArrayList<gb_Vector3> getPointsWithinSphere(gb_Sphere s) {
        ArrayList<gb_Vector3> results = null;
        if (this.intersectsSphere(s)) {
            if (points != null) {
                for (gb_Vector3 q : points) {
                    if (s.contains(q)) {
                        if (results == null) {
                            results = new ArrayList<gb_Vector3>();
                        }
                        results.add(q);
                    }
                }
            } else if (numChildren > 0) {
                for (int i = 0; i < 8; i++) {
                    if (children[i] != null) {
                        ArrayList<gb_Vector3> points =
                                children[i].getPointsWithinSphere(s);
                        if (points != null) {
                            if (results == null) {
                                results = new ArrayList<gb_Vector3>();
                            }
                            results.addAll(points);
                        }
                    }
                }
            }
        }
        return results;
    }

    /**
     * Selects all stored points within the given sphere volume
     * 
     * @param sphereOrigin
     * @param clipRadius
     * @return selected points
     */
    public ArrayList<gb_Vector3> getPointsWithinSphere(gb_Vector3 sphereOrigin,
            float clipRadius) {
        return getPointsWithinSphere(new gb_Sphere(sphereOrigin, clipRadius));
    }

    /**
     * @return the size
     */
    public float getSize() {
        return size;
    }

    private void reduceBranch() {
        if (points != null && points.size() == 0) {
            points = null;
        }
        if (numChildren > 0) {
            for (int i = 0; i < 8; i++) {
                if (children[i] != null && children[i].points == null) {
                    children[i] = null;
                }
            }
        }
        if (parent != null) {
            parent.reduceBranch();
        }
    }

    /**
     * Removes a point from the tree and (optionally) tries to release memory by
     * reducing now empty sub-branches.
     * 
     * @param p
     *            point to delete
     * @return true, if the point was found & removed
     */
    public boolean remove(final gb_Vector3 p) {
        boolean found = false;
        gb_Octree leaf = getLeafForPoint(p);
        if (leaf != null) {
            if (leaf.points.remove(p)) {
                found = true;
                if (isAutoReducing && leaf.points.size() == 0) {
                    leaf.reduceBranch();
                }
            }
        }
        return found;
    }

    public void removeAll(Collection<gb_Vector3> points) {
        for (final gb_Vector3 p : points) {
            remove(p);
        }
    }

    /**
     * @param minNodeSize
     */
    public void setMinNodeSize(float minNodeSize) {
        this.minNodeSize = minNodeSize * 0.5f;
    }

    /**
     * Enables/disables auto reduction of branches after points have been
     * deleted from the tree. Turned off by default.
     * 
     * @param state
     *            true, to enable feature
     */
    public void setTreeAutoReduction(boolean state) {
        isAutoReducing = state;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.AABB#toString()
     */
    public String toString() {
        return "<octree> offset: " + super.toString() + " size: " + size;
    }
}
