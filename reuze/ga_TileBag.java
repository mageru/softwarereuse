package reuze;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;


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
//package straightedge.geom.util;

//import straightedge.geom.*;

/**
 * All the benefits of a Bag and a TileArray!
 * @author Keith
 */
public class ga_TileBag<T extends ga_i_PolygonHolder> implements Collection<T>{
	public ga_TileArray<T> tileArray;
	public d_Bag<T> bag;

	public ga_TileBag(ga_TileArray<T> tileArray, d_Bag<T> bag) {
		this.tileArray = tileArray;
		this.bag = bag;
	}

	public ga_TileBag(ga_Vector2 botLeft, float tileWidthAndHeight, int numRows, int numCols){
		init(botLeft, tileWidthAndHeight, numRows, numCols);
	}

	protected void init(ga_Vector2 botLeft, float tileWidthAndHeight, int numRows, int numCols){
		tileArray = new ga_TileArray<T>(botLeft, tileWidthAndHeight, numRows, numCols);
		bag = new d_Bag<T>();
	}

	public ga_TileBag(ga_Vector2 botLeft, ga_Vector2 approxTopRight, float tileWidthAndHeight){
		tileArray = new ga_TileArray<T>(botLeft, approxTopRight, tileWidthAndHeight);
		bag = new d_Bag<T>();
	}

	public ga_TileBag(ga_AABB aabb, float tileWidthAndHeight){
		this(aabb.p, aabb.p2, tileWidthAndHeight);
	}

	public ga_TileBag(Object[] polygonHolders, float tileWidthAndHeight){
		this(ga_AABB.getAABBEnclosingCenterAndRadius(polygonHolders), tileWidthAndHeight);
	}

	public ga_TileBag(Collection<ga_i_PolygonHolder> polygonHolders, float tileWidthAndHeight){
		this(ga_AABB.getAABBEnclosingCenterAndRadius(polygonHolders), tileWidthAndHeight);
	}

	public d_Bag<T> getBag() {
		return bag;
	}

	public ga_TileArray<T> getTileArray() {
		return tileArray;
	}

	/**
	 * Clears the TileArray and then re-adds everything to the TileArray.
	 * Needs to be done when the positions of the elements changes, otherwise getAllWithin will not work properly.
	 */
	public void resetTileArray(){
		tileArray.clear();
		for (int i = 0; i < bag.size(); i++) {
			tileArray.add(bag.get(i));
		}
	}

	public ArrayList<T> getAllWithin(ga_Vector2 point, float radius){
		return tileArray.getAllWithin(point, radius);
	}
	public ArrayList<T> getAllWithin(float x, float y, float radius){
		return tileArray.getAllWithin(x, y, radius);
	}

	public boolean add(T t) {
		tileArray.add(t);
		bag.add(t);
		return true;
	}

	public void addAll(d_Bag<T> newBag) {
		this.bag.addAll(newBag);
		for (int i = 0; i < newBag.size(); i++){
			tileArray.add(newBag.get(i));
		}
	}

	public boolean addAll(T[] array) {
		bag.addAll(array);
		for (int i = 0; i < array.length; i++){
			tileArray.add((T)array[i]);
		}
		return (array.length != 0 ? true : false);
	}

	public T get(int index) {
		return bag.get(index);
	}

	public T remove(int index) {
		T removed = bag.remove(index);
		tileArray.remove(removed);
		return removed;
	}

	public boolean remove(Object t) {
		int i = this.indexOf(t);
		if (i == -1) {
			return false;
		}
		this.remove(i);
		return true;
	}

	/**
	 * Tests if an element is present
	 *
	 * @param t
	 * @return <code>true</code> if t is present, <code>false</code>
	 *         otherwise
	 */
	public boolean contains(Object t) {
		return bag.contains(t);
	}

	/**
	 * Finds the index of an element
	 *
	 * @param t
	 * @return the index of t, or -1 if not found
	 */
	public int indexOf(Object t) {
		return bag.indexOf(t);
	}

	/**
	 * Removes all elements
	 */
	public void clear() {
		bag.clear();
		tileArray.clear();
	}

	/**
	 * The maximum number of elements allowed before the internal array
	 * will be grown
	 *
	 * @return The size of the internal array
	 */
	public int capacity() {
		return bag.capacity();
	}

	/**
	 * @return The number of held elements
	 */
	public int size() {
		return bag.size();
	}

	/**
	 * @return A new array of the data.
	 */
	public Object[] toArray() {
        return bag.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return bag.toArray(a);
    }

	public boolean addAll(Collection<? extends T> c) {
		Object[] array = c.toArray();
		bag.addAll(array);
		for (int i = 0; i < array.length; i++){
			tileArray.add((T)array[i]);
		}
		return (array.length != 0 ? true : false);
	}

	public boolean removeAll(Collection<?> c) {
		boolean modified = false;
		Iterator<?> e = iterator();
		while (e.hasNext()) {
			if (c.contains(e.next())) {
			e.remove();
			modified = true;
			}
		}
		return modified;
    }

	public boolean containsAll(Collection<?> c) {
		Iterator<?> e = c.iterator();
		while (e.hasNext()){
			if (contains(e.next()) == false){
				return false;
			}
		}
		return true;
    }
	public boolean isEmpty() {
		return bag.isEmpty();
    }

	public boolean retainAll(Collection<?> c) {
		boolean modified = false;
		Iterator<T> e = iterator();
		while (e.hasNext()) {
			if (!c.contains(e.next())) {
				e.remove();
				modified = true;
			}
		}
		return modified;
    }

	public Iterator<T> iterator() {
		return new Itr();
    }

	private class Itr implements Iterator<T> {
		/**
		 * Index of element to be returned by subsequent call to next.
		 */
		int cursor = 0;

		/**
		 * Index of element returned by most recent call to next or
		 * previous.  Reset to -1 if this element is deleted by a call
		 * to remove.
		 */
		int lastRet = -1;

		public boolean hasNext() {
				return cursor != size();
		}

		public T next() {
				checkForComodification();
			try {
				T next = get(cursor);
				lastRet = cursor++;
				return next;
			} catch (IndexOutOfBoundsException e) {
				checkForComodification();
				throw new NoSuchElementException();
			}
		}

		public void remove() {
			if (lastRet == -1)
				throw new IllegalStateException();
					checkForComodification();

			try {
				ga_TileBag.this.remove(lastRet);
				if (lastRet < cursor)
					cursor--;
				lastRet = -1;
			} catch (IndexOutOfBoundsException e) {
				throw new ConcurrentModificationException();
			}
		}

		final void checkForComodification() {
		}
    }

	public static void main(String[] args){
		float w = 2000;
		float h = 2000;
		ga_Vector2 botLeft = new ga_Vector2(0, 0);
		ga_Vector2 topRight = new ga_Vector2(w, h);
		ga_TileBag<ga_Polygon> tileBag = new ga_TileBag(new ga_TileArray<ga_Polygon>(botLeft, topRight, 100), new d_Bag());
//		System.out.println(TileArray.class.getSimpleName()+": tileArray == "+tileArray);

		int numPolygons = 5000;
		ArrayList<ga_Polygon> allPolygons = new ArrayList<ga_Polygon>();
		Random rand = new Random(0);
		for (int i = 0; i < numPolygons; i++){
			{
				ArrayList<ga_Vector2> points = new ArrayList<ga_Vector2>();
				float width = 25;
				float height = 25;
				ga_Vector2 point = new ga_Vector2(w*0.05f + rand.nextFloat()*w*0.9f, h*0.05f + rand.nextFloat()*h*0.9f);
				points.add(new ga_Vector2(point.x, point.y));
				points.add(new ga_Vector2(point.x, point.y + height));
				points.add(new ga_Vector2(point.x + width, point.y + height));
				points.add(new ga_Vector2(point.x + width, point.y));
				ga_Polygon poly = new ga_Polygon(points);
				poly.rotate(rand.nextFloat());
				allPolygons.add(poly);
			}
			{
				ArrayList<ga_Vector2> points = new ArrayList<ga_Vector2>();
				int numPoints = 3 + rand.nextInt(10);
				float radius = rand.nextFloat()*200;
				ga_Polygon poly = ga_Polygon.createRegularPolygon(numPoints, radius);
				ga_Vector2 point = new ga_Vector2(w*0.05f + rand.nextFloat()*w*0.9f, h*0.05f + rand.nextFloat()*h*0.9f);
				poly.translateTo(point);
				poly.rotate(rand.nextFloat());
				allPolygons.add(poly);
			}
		}
/*
		System.out.println(TileBag.class.getSimpleName()+": tileBag == "+tileBag);
		CodeTimer ct = new CodeTimer("TileBag", CodeTimer.Output.Nanos, CodeTimer.Output.Nanos);
		ct.setEnabled(false);
*/

		for (int j = 0; j < allPolygons.size(); j++){
			ga_Polygon poly = allPolygons.get(j);
			tileBag.add(poly);
		}
//		tileBag.getTileArray().useMap = true;
//		tileBag.getTileArray().distCheckFirst = true;
		int numTests = 500000;
		for (int i = 0; i < numTests; i++){
			if (i == 1024){
				/*ct.setEnabled(true)*/;
			}
			ga_Vector2 c = new ga_Vector2(w/rand.nextFloat(), h/rand.nextFloat());
			float r = rand.nextFloat()*400;
			//ct.click("getAllWithin");
			ArrayList<ga_Polygon> polygons = tileBag.getAllWithin(c, r);
			//ct.lastClick((i == numTests-1));
		}
//		int numTests = 500;
//		for (int i = 0; i < numTests; i++){
//			if (i == 1024){
//				ct.setEnabled(true);
//			}
//			ct.click("add");
//			for (int j = 0; j < allPolygons.size(); j++){
//				Polygon poly = allPolygons.get(j);
//				tileBag.add(poly);
//			}
//			ct.click("getAllWithin");
//			for (int j = 0; j < allPolygons.size(); j++){
//				Polygon poly = allPolygons.get(j);
//				Vector2 c = new Vector2(w/rand.nextFloat(), h/rand.nextFloat());
//				float r = rand.nextFloat()*50;
//				ArrayList<Polygon> polygons = tileBag.getAllWithin(c, r);
//			}
//			ct.click("clear");
//			tileBag.clear();
//			ct.lastClick((i == numTests-1));
//		}
		//try{Thread.sleep(1000);}catch(Exception e){}
	}

}

