package model.data_structures;

import java.util.Iterator;
import java.util.NoSuchElementException;

//@param <Item> the generic type of an item in this bag
public class Bag<Item> implements Iterable<Item> {
	private Node<Item> first;    // beginning of bag
	private int n;               // number of elements in bag

	// helper linked list class
	private static class Node<Item> {
		private Item item;
		private Node<Item> next;
	}

	/**
	 * Initializes an empty bag.
	 */
	public Bag() {
		first = null;
		n = 0;
	}

	/**
	 * Returns true if this bag is empty.
	 *
	 * @return {@code true} if this bag is empty;
	 *         {@code false} otherwise
	 */
	public boolean isEmpty() {
		return first == null;
	}

	/**
	 * Returns the number of items in this bag.
	 *
	 * @return the number of items in this bag
	 */
	public int size() {
		return n;
	}

	/**
	 * Adds the item to this bag.
	 *
	 * @param  item the item to add to this bag
	 */
	public void add(Item item) {
		Node<Item> oldfirst = first;
		first = new Node<Item>();
		first.item = item;
		first.next = oldfirst;
		n++;
	}

	public void delete(Item item){

		Node<Item> anterior = null;
		Node<Item> actual = first;
		boolean found = false;
		while(actual !=null && !found){
			if(actual == item){
				found = true;       	
			}
			else{
				anterior = actual;
				actual = actual.next;
			}
		}	
		if(found && anterior !=null){			
			anterior.next = actual.next;		
		}		
		else if(found && anterior == null){			
			first = first.next;
		}	
		else{	
			// no hago nada
		}
	}

	/**
	 * Returns an iterator that iterates over the items in this bag in arbitrary order.
	 *
	 * @return an iterator that iterates over the items in this bag in arbitrary order
	 */
	public Iterator<Item> iterator()  {
		return new LinkedIterator(first);  
	}

	// an iterator, doesn't implement remove() since it's optional
	private class LinkedIterator implements Iterator<Item> {
		private Node<Item> current;

		public LinkedIterator(Node<Item> first) {
			current = first;
		}

		public boolean hasNext()  { return current != null;                     }
		public void remove()      { throw new UnsupportedOperationException();  }

		public Item next() {
			if (!hasNext()) throw new NoSuchElementException();
			Item item = current.item;
			current = current.next; 
			return item;
		}
	}

}
