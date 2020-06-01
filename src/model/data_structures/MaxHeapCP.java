package model.data_structures;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;


public class MaxHeapCP<T extends Comparable<T>>  {
	private T[] pq;                    // store items at indices 1 to n
	private int n;                       // number of items on priority queue

	/**
	 * Initializes an empty priority queue with the given initial capacity.
	 *
	 * @param  initCapacity the initial capacity of this priority queue
	 */
	public MaxHeapCP(int initCapacity) {
		pq = (T[]) new Comparable[initCapacity + 1];
		n = 0;
	}

	/**
	 * Initializes an empty priority queue.
	 */
	public MaxHeapCP() {
		this(1);
	}

	/**
	 * Returns true if this priority queue is empty.
	 *
	 * @return {@code true} if this priority queue is empty;
	 *         {@code false} otherwise
	 */
	public boolean isEmpty() {
		return n == 0;
	}

	/**
	 * Returns the number of keys on this priority queue.
	 *
	 * @return the number of keys on this priority queue
	 */
	public int size() {
		return n;
	}

	/**
	 * Returns a largest key on this priority queue.
	 *
	 * @return a largest key on this priority queue
	 * @throws NoSuchElementException if this priority queue is empty
	 */
	public T max() {
		if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
		return pq[1];
	}

	// helper function to double the size of the heap array
	private void resize(int capacity) {
		assert capacity > n;
		T[] temp = (T[]) new Comparable[capacity];
		for (int i = 1; i <= n; i++) {
			temp[i] = pq[i];
		}
		pq = temp;
	}


	/**
	 * Adds a new key to this priority queue.
	 *
	 * @param  x the new key to add to this priority queue
	 */
	public void insert(T x, Comparator<Comparendo> pComparador) {

		// double size of array if necessary
		if (n == pq.length - 1) resize(2 * pq.length);

		// add x, and percolate it up to maintain heap invariant
		pq[++n] = x;
		swim(n, pComparador);
		assert isMaxHeap();
	}

	/**
	 * Removes and returns a largest key on this priority queue.
	 *
	 * @return a largest key on this priority queue
	 * @throws NoSuchElementException if this priority queue is empty
	 */
	public T delMax(Comparator<Comparendo> pComparador) {
		if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
		T max = pq[1];
		exch(1, n--);
		sink(1, pComparador);
		pq[n+1] = null;     // to avoid loitering and help with garbage collection
		if ((n > 0) && (n == (pq.length - 1) / 4)) resize(pq.length / 2);
		assert isMaxHeap();
		return max;
	}


	/***************************************************************************
	 * Helper functions to restore the heap invariant.
	 * @param pComparador 
	 ***************************************************************************/

	// hacer 
	private void swim(int k, Comparator<Comparendo> pComparador) {

		if(pComparador == null){
			while (k > 1 && less(k/2, k)) {
				exch(k, k/2);
				k = k/2;
			}
		}
		else{

			while (k > 1 && pComparador.compare((Comparendo)pq[k/2], (Comparendo)pq[k])<0) {
				exch(k, k/2);
				k = k/2;
			}
		}

	}

	private void sink(int k, Comparator<Comparendo> pComparador) {
		if(pComparador == null){
			while (2*k <= n) {
				int j = 2*k;
				if (j < n && less(j, j+1)) j++;
				if (!less(k, j)) break;
				exch(k, j);
				k = j;
			}
		}
		else{
			while (2*k <= n) {
				int j = 2*k;
				if (j < n && pComparador.compare((Comparendo) pq[j], (Comparendo) pq[j+1])<0) j++;
				if (pComparador.compare((Comparendo) pq[k], (Comparendo) pq[j])>=0) break;
				exch(k, j);
				k = j;
			}
		}
	}

	/***************************************************************************
	 * Helper functions for compares and swaps.
	 ***************************************************************************/
	private boolean less(int i, int j) {
		return ((Comparable<T>) pq[i]).compareTo(pq[j]) < 0;
	}

	private void exch(int i, int j) {
		T swap = pq[i];
		pq[i] = pq[j];
		pq[j] = swap;
	}

	// is pq[1..n] a max heap?
	private boolean isMaxHeap() {
		for (int i = 1; i <= n; i++) {
			if (pq[i] == null) return false;
		}
		for (int i = n+1; i < pq.length; i++) {
			if (pq[i] != null) return false;
		}
		if (pq[0] != null) return false;
		return isMaxHeapOrdered(1);
	}

	// is subtree of pq[1..n] rooted at k a max heap?
	private boolean isMaxHeapOrdered(int k) {
		if (k > n) return true;
		int left = 2*k;
		int right = 2*k + 1;
		if (left  <= n && less(k, left))  return false;
		if (right <= n && less(k, right)) return false;
		return isMaxHeapOrdered(left) && isMaxHeapOrdered(right);
	}


	/***************************************************************************
	 * Iterator.
	 ***************************************************************************/

	/**
	 * Returns an iterator that iterates over the keys on this priority queue
	 * in descending order.
	 * The iterator doesn't implement {@code remove()} since it's optional.
	 *
	 * @return an iterator that iterates over the keys in descending order
	 */
	public Iterator<T> iterator(Comparator<Comparendo> pComparador) {
		return new HeapIterator(pComparador);
	}

	private class HeapIterator implements Iterator<T> {

		// create a new pq
		private MaxHeapCP<T> copy;
		private Comparator<Comparendo> pComparador;

		// add all items to copy of heap
		// takes linear time since already in heap order so no keys move
		public HeapIterator(Comparator<Comparendo> pComparador) {
			this.pComparador = pComparador;
			copy = new MaxHeapCP<T>(size());

			for (int i = 1; i <= n; i++)
				copy.insert(pq[i], this.pComparador);
		}

		public boolean hasNext()  { return !copy.isEmpty();                     }
		public void remove()      { throw new UnsupportedOperationException();  }

		public T next() {
			if (!hasNext()) throw new NoSuchElementException();
			return copy.delMax(this.pComparador);
		}
	}

}
