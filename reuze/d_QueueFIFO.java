package reuze;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): pg 80.<br>
 * <br>
 * First-in, first-out or FIFO queue, which pops the oldest element of the
 * queue;
 * 
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public class d_QueueFIFO<E> extends LinkedList<E> implements d_i_Queue<E> {
	private static final long serialVersionUID = 1;

	public d_QueueFIFO() {
		super();
	}

	public d_QueueFIFO(Collection<? extends E> c) {
		super(c);
	}

	//
	// START-Queue
	public boolean isEmpty() {
		return 0 == size();
	}

	public E pop() {
		return poll();
	}

	public void push(E element) {
		this.addLast(element);
	}

	public d_i_Queue<E> insert(E element) {
		if (offer(element)) {
			return this;
		}
		return null;
	}
	// END-Queue
	//
}