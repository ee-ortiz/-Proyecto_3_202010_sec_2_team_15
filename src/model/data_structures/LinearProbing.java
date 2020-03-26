package model.data_structures;

import java.util.ArrayList;
import java.util.Iterator;

public class LinearProbing<Key, Value> {

	int capacidadInicial;

	private static final int INIT_CAPACITY = 4;

    private int n;           // number of key-value pairs in the symbol table
    private int m;           // size of linear probing table
    private Key[] keys;      // the keys
    private Value[] vals;    // the values
    public static final double factorDeCargaMaximo = 5.0;

	    

	    /**
	     * Initializes an empty	 symbol table.
	     */
	    public LinearProbing() {
	        this(INIT_CAPACITY);
	    }

	    /**
	     * Initializes an empty symbol table with the specified initial capacity.
	     *
	     * @param capacity the initial capacity
	     * @return 
	     */
	    public LinearProbing(int capacity) {
	        m = capacity;
	        n = 0;
	        keys = (Key[])   new Object[m];
	        vals = (Value[]) new Object[m];
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
	                temp.put(keys[i], vals[i]);
	            }
	        }
	        keys = temp.keys;
	        vals = temp.vals;
	        m    = temp.m;
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

	        // double table size if 50% full
	        if (n >= m/2) resize(2*m);

	        int i;
	        for (i = hash(key); keys[i] != null; i = (i + 1) % m) 
	        {
	            if (keys[i].equals(key)&&vals[i].equals(vals[0])) 
	            {
	                vals[i] = val;
	                return;
	            }
	            else{
	            	boolean listo=false;
	            	int j;
	            	for(j=i;listo==false;j=j*2 ){
	            		if(vals[j].equals(vals[0])){
	            		vals[j]=val;
	            		listo=true;
	            		
	            		}
	            	}
	            	
	            }
	        }
	        keys[i] = key;
	        vals[i] = val;
	        n++;
	        if ((double) n/(double)m > factorDeCargaMaximo) resize(siguientePrimo(m));
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

	    /**
	     * Returns the value associated with the specified key.
	     * @param key the key
	     * @return the value associated with {@code key};
	     *         {@code null} if no such value
	     * @throws IllegalArgumentException if {@code key} is {@code null}
	     */
	    public Value get(Key key) {
	    	boolean encontrado=false;
	        if (key == null) throw new IllegalArgumentException("argument to get() is null");
	        for (int i = hash(key); keys[i] != null; i = (i + 1) % m)
	            if (keys[i].equals(key))
	                return vals[i];
	            else for(int j=i;encontrado==false; j=j*2 )
	            {
	            	if(keys[j].equals(key)&&!vals[j].equals(vals[0])){
	            		return vals[j];
	            	}
	            }
	        return null;
	    }
	    public ArrayList getArray(Key key)
	    {
	    	ArrayList temp= new ArrayList();
	    	boolean encontrado=false;
	        if (key == null) throw new IllegalArgumentException("argument to get() is null");
	        for (int i = hash(key); keys[i] != null; i = (i + 1) % m)
	           for(int j=i;encontrado==false; j=j*2 )
	            {
	            	if(keys[j].equals(key)&&(j%i)==0){
	            		temp.add(vals[j]);	            	
	            		}
	            }
	        return temp;
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
	            Value valToRehash = vals[i];
	            keys[i] = null;
	            vals[i] = null;
	            n--;
	            put(keyToRehash, valToRehash);
	            i = (i + 1) % m;
	        }

	        n--;

	        // halves size of array if it's 12.5% full or less
	        if (n > 0 && n <= m/8) resize(m/2);

	        assert check();
	    }

	    /**
	     * Returns all keys in this symbol table as an {@code Iterable}.
	     * To iterate over all of the keys in the symbol table named {@code st},
	     * use the foreach notation: {@code for (Key key : st.keys())}.
	     *
	     * @return all keys in this symbol table
	     */
	    public Iterable<Key> keys() {
	        Queue<Key> queue = new Queue<Key>();
	        for (int i = 0; i < m; i++)
	            if (keys[i] != null) queue.enqueue(keys[i]);
	        return queue;
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


	}


