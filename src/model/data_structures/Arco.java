package model.data_structures;

public class Arco<K extends Comparable<K>, V > {

	private double costo;
	private double costo2;
	private Vertice<K, V> origen;
	private Vertice<K, V> destino;

	public Arco(Vertice<K,V> orig, Vertice<K,V> dest, double costoP){

		costo2 = 0;
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

	public double darCosto2(){

		return costo2;
	}

	public void cambiarCosto(double pCosto){

		costo = pCosto;
	}

	public void cambiarCosto2(double pCosto){

		costo2 = pCosto;
	}

}
