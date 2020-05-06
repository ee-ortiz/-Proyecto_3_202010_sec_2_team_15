package model.data_structures;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;


public class SeparateChaining<Key, Value>
{
	private int N; // number of key-value pairs

	private int M; // hash table size

	private SequentialSearchST<Key, Value>[] st;  // array of linked-list symbol tables

	public static final double factorDeCargaMaximo = 5.0;

	int numeroReHashes;

	public SeparateChaining(int m) {
		this.M = m;
		st = (SequentialSearchST<Key, Value>[]) new SequentialSearchST[m];
		for (int i = 0; i < m; i++)
			st[i] = new SequentialSearchST<Key, Value>();
		numeroReHashes = 0;
	} 

	// resize the hash table to have the given number of chains,
	// rehashing all of the keys
	private void resize(int chains) {
		SeparateChaining<Key, Value> temp = new SeparateChaining<Key, Value>(chains);
		for (int i = 0; i < M; i++) {
			for (Key key : st[i].keys() ) {
				temp.putAHoleKeyWithValues(key, st[i].getValues(key));
			}
		}
		this.M  = temp.M;
		this.N  = temp.N;
		this.st = temp.st;
		numeroReHashes ++;

	}


	private int hash(Key key){ 
		return (key.hashCode() & 0x7fffffff) % M; 
	}

	public int size() {
		return N;
	} 

	public ArrayList<Value> getSetArray(Key key) {
		if (key == null) throw new IllegalArgumentException("argument to get() is null");
		int i = hash(key);
		return st[i].getValues(key);
	} 

	public Iterator<Value> getSet(Key key){

		Iterator<Value> rta = null;
		
		try{ rta = getSetArray(key).iterator();
		
		}
		
		catch (Exception e){
			
		}
		
		return rta;
	}

	public void putInSet(Key key, Value val) {
		if (key == null) throw new IllegalArgumentException("first argument to put() is null");
		if (val == null) {
			delete(key);
			return;
		}

		int i = hash(key);
		if (!st[i].contains(key)) N++;
		st[i].put(key, val);

		if ((double) N/(double)M > factorDeCargaMaximo) resize(siguientePrimo(2*M));
	} 

	public void putAHoleKeyWithValues(Key key, ArrayList<Value> vals){

		for(int i = 0; i<vals.size(); i++){
			Value aAgregar = vals.get(i);		
			putInSet(key, aAgregar);

		}
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

	// return keys in symbol table as an Iterable
	public Iterable<Key> keysToIterate() {
		Queue<Key> queue = new Queue<Key>();
		for (int i = 0; i < M; i++) {
			for (Key key : st[i].keys())
				queue.enqueue(key);
		}
		return queue;
	} 

	public Iterator<Key> keys(){

		return keysToIterate().iterator();
	}


	public void delete(Key key) {
		if (key == null) throw new IllegalArgumentException("argument to delete() is null");

		int i = hash(key);
		if (st[i].contains(key)) N--;
		st[i].delete(key);

	} 

	// hay que cambiar esto
	public ArrayList<Value> deleteSetArray(Key key){

		ArrayList<Value> rta = getSetArray(key);
		delete(key);
		return rta;

	}

	Iterator<Value> deleteSet(Key key){

		return deleteSetArray(key).iterator();

	}

	public double numeroTotalDeValores(){

		double total = 0.0;
		Iterator<Key> iter = keys();
		while(iter.hasNext()){

			Key element = (Key) iter.next();
			int valor = getSetArray(element).size();
			total = total + valor;

		}

		return total;
	}

	public int TamañoDeLaHastTable(){

		return M;
	}

	public SequentialSearchST<Key, Value> darST(int n){

		return st[n];
	}

	public int darNumeroReHashes(){

		return numeroReHashes;
	}
}