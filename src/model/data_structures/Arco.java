package model.data_structures;

public class Arco<K extends Comparable<K>, V > {

	private double costo;
	private Vertice<K, V> origen;
	private Vertice<K, V> destino;

	public Arco(Vertice<K,V> orig, Vertice<K,V> dest, double costoP){

		origen = orig;
		destino = dest;
		costo = costoP;
	}

	public Vertice<K, V> darOrigen(){

		return origen;
	}

	public Vertice<K, V> darDestino(){

		return destino;
	}

	public double darCosto(){

		return costo;
	}

	public void cambiarCosto(double pCosto){

		costo = pCosto;
	}

}
