package model.data_structures;

public class Arco<K extends Comparable<K>, V >implements Comparable<Arco<K, V>>{

	private double costo;
	private double costo2;
	private Vertice<K, V> origen;
	private Vertice<K, V> destino;
	boolean agregado;

	public Arco(Vertice<K,V> orig, Vertice<K,V> dest, double costoP){

		costo2 = 0;
		origen = orig;
		destino = dest;
		costo = costoP;
		agregado = false;
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

	public int compareTo(Arco<K, V> o) {


		double num = this.costo - o.darCosto();

		if(num<0){
			return -1;
		}
		else if(num >0 ){
			return 1;
		}
		else{
			return 0;
		}
	}

	public void marcar(){

		agregado = true;
	}

	public void desmarcar(){

		agregado = false;
	}

	public boolean darMarca(){

		return agregado;
	}


}
