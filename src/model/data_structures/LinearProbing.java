package model.data_structures;

import java.util.ArrayList;
import java.util.Iterator;

public class LinearProbing<Key, Value> {

	private int n;           // number of key-value pairs in the symbol table
	private int m;           // size of linear probing table
	private Key[] keys;      // the keys
	private ArrayList<Value>[] vals;    // the values
	public static final double factorDeCargaMaximo = 0.75;
	int numeroReHashes;


	/**
	 * Initializes an empty symbol table with the specified initial capacity.
	 *
	 * @param capacity the initial capacity
	 * @return 
	 */
	public LinearProbing(int capacity) {
		m = capacity;
		n = 0;
		keys = (Key[]) new Object[m];
		vals = (ArrayList<Value>[]) new ArrayList[m];
		numeroReHashes = 0;
		initialize();
	}

	/**
	 * Returns the number of key-value pairs in this symbol table.
	 *
	 * @return the number of key-value pairs in this symbol table
	 */
	public int size() {
		return n;
	}

	/**
	 * Returns true if this symbol table is empty.
	 *
	 * @return {@code true} if this symbol table is empty;
	 *         {@code false} otherwise
	 */
	public boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * Returns true if this symbol table contains the specified key.
	 *
	 * @param  key the key
	 * @return {@code true} if this symbol table contains {@code key};
	 *         {@code false} otherwise
	 * @throws IllegalArgumentException if {@code key} is {@code null}
	 */
	public boolean contains(Key key) {
		if (key == null) throw new IllegalArgumentException("argument to contains() is null");
		return get(key) != null;
	}

	// hash function for keys - returns value between 0 and M-1
	private int hash(Key key) {
		return (key.hashCode() & 0x7fffffff) % m;
	}

	// resizes the hash table to the given capacity by re-hashing all of the keys
	private void resize(int capacity) {
		LinearProbing<Key, Value> temp = new LinearProbing<Key, Value>(capacity);
		for (int i = 0; i < m; i++) {
			if (keys[i] != null) {
				for(int j = 0; j<vals[i].size(); j++){

					temp.put(keys[i], vals[i].get(j));
				}
			}
		}
		keys = temp.keys;
		vals = temp.vals;
		m    = temp.m;
		numeroReHashes++;
	}

	/**
	 * Inserts the specified key-value pair into the symbol table, overwriting the old 
	 * value with the new value if the symbol table already contains the specified key.
	 * Deletes the specified key (and its associated value) from this symbol table
	 * if the specified value is {@code null}.
	 *
	 * @param  key the key
	 * @param  val the value
	 * @throws IllegalArgumentException if {@code key} is {@code null}
	 */
	public void put(Key key, Value val) {
		if (key == null) throw new IllegalArgumentException("first argument to put() is null");

		if (val == null) {
			delete(key);
			return;
		}

		int i;
		for (i = hash(key); keys[i] != null; i = (i + 1) % m) {

			if (keys[i].equals(key)) {
				vals[i].add(val);
				return;
			}
		}

		keys[i] = key;
		vals[i].add(val);
		n++;

		if ((double)n/(double)m > factorDeCargaMaximo ) resize(siguientePrimo(m));
	}

	/**
	 * Returns the value associated with the specified key.
	 * @param key the key
	 * @return the value associated with {@code key};
	 *         {@code null} if no such value
	 * @throws IllegalArgumentException if {@code key} is {@code null}
	 */
	public ArrayList<Value> get(Key key) {
		if (key == null) throw new IllegalArgumentException("argument to get() is null");
		for (int i = hash(key); keys[i] != null; i = (i + 1) % m)
			if (keys[i].equals(key))
				return vals[i];
		return null;
	}

	public Iterator<Value> getSet(Key key){

		Iterator<Value> rta = null;

		try{
			rta = get(key).iterator();
		}

		catch(Exception e)
		{

		}

		return rta;
	}

	/**
	 * Removes the specified key and its associated value from this symbol table     
	 * (if the key is in this symbol table).    
	 *
	 * @param  key the key
	 * @throws IllegalArgumentException if {@code key} is {@code null}
	 */
	public void delete(Key key) {
		if (key == null) throw new IllegalArgumentException("argument to delete() is null");
		if (!contains(key)) return;

		// find position i of key
		int i = hash(key);
		while (!key.equals(keys[i])) {
			i = (i + 1) % m;
		}

		// delete key and associated value
		keys[i] = null;
		vals[i] = null;

		// rehash all keys in same cluster
		i = (i + 1) % m;
		while (keys[i] != null) {
			// delete keys[i] an vals[i] and reinsert
			Key   keyToRehash = keys[i];
			ArrayList<Value> valToRehash = vals[i];
			keys[i] = null;
			vals[i] = null;
			n--;
			for(int j = 0; j<valToRehash.size(); j++){

				put(keyToRehash, valToRehash.get(j));

			}
			i = (i + 1) % m;
		}

		n--;

		// halves size of array if it's 12.5% full or less
		if (n > 0 && n <= m/8) resize(m/2);

		assert check();
	}



	public Iterator<Value> deleteSet(Key key){

		Iterator<Value> respuesta = null;

		ArrayList<Value> rta = get(key);

		delete(key);

		try{

			respuesta = rta.iterator();
		}

		catch(Exception e){

		}

		return respuesta;

	}

	/**
	 * Returns all keys in this symbol table as an {@code Iterable}.
	 * To iterate over all of the keys in the symbol table named {@code st},
	 * use the foreach notation: {@code for (Key key : st.keys())}.
	 *
	 * @return all keys in this symbol table
	 */
	public Iterable<Key> keysToIterate() {
		Queue<Key> queue = new Queue<Key>();
		for (int i = 0; i < m; i++)
			if (keys[i] != null) queue.enqueue(keys[i]);
		return queue;
	}

	public Iterator<Key> keys(){

		Iterator<Key> rta = null;

		try{
			rta = keysToIterate().iterator();

		}

		catch (Exception e){

		}

		return rta;
	}

	// integrity check - don't check after each put() because
	// integrity not maintained during a delete()
	private boolean check() {

		// check that hash table is at most 50% full
		if (m < 2*n) {
			System.err.println("Hash table size m = " + m + "; array size n = " + n);
			return false;
		}

		// check that each key in table can be found by get()
		for (int i = 0; i < m; i++) {
			if (keys[i] == null) continue;
			else if (get(keys[i]) != vals[i]) {
				System.err.println("get[" + keys[i] + "] = " + get(keys[i]) + "; vals[i] = " + vals[i]);
				return false;
			}
		}
		return true;
	}

	public void initialize(){

		for (int i = 0; i < m; i++) { 
			vals[i] = new ArrayList<Value>(); 
		} 

	}

	public int darNumeroReHashes(){

		return numeroReHashes;
	}

	public  int siguientePrimo(int m) {

		int i = m+1;
		int primo = 0;
		boolean esPrimo = false;
		while(esPrimo ==false){

			boolean posiblePrimo = true;
			int j = 2;
			while(j<i){

				if(i%j==0){

					posiblePrimo = false;
					break;
				}

				j++;
			}

			if(posiblePrimo==true){

				primo = i;
				esPrimo = true;

			}

			i++;

		}

		return primo;
	}

	public int darTamanoHashTable(){

		return m;
	}
}

