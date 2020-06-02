package model.data_structures;

import java.util.Comparator;
import java.util.Iterator;

public class Vertice <K extends Comparable<K>, V > implements Comparable<Vertice<K, V>>{

	private K idVtce;
	private V infoVtce;
	private boolean marca;
	private int color;
	private Arco<K,V> arcoLlegada;
	private Bag<Arco<K,V>> adyacentes;
	private Bag<EstacionesPolicia> masCercanas;
	private Bag<Comparendo> comparendos;
	private double costoAcumulado;

	public Vertice( K id, V info ){

		adyacentes = new Bag<>();
		masCercanas = new Bag<>();
		comparendos = new Bag<>();
		idVtce = id;
		infoVtce = info;
		marca = false;
		arcoLlegada = null;
		color = -1; // se inicializa con un color menor que 0 (Para efectos practicos menor que 0 es un color invalido)
		costoAcumulado = 0; // se inicializa en 0 el costo acumulado, este costo servira para el algoritmo de dijkstra

	}

	public K darId( ){

		return idVtce;

	}

	public V darInfo( ){

		return infoVtce;
	}

	public void cambiarInformacion(V nuevaInfo){

		infoVtce = nuevaInfo;

	}

	public void agregarArco ( Arco<K,V> arco ){

		adyacentes.add(arco);
	}

	public void eliminarArco ( K idDest ){

		Iterator<Arco<K, V>> iter = darAdyacentes();

		while(iter!=null && iter.hasNext()){

			Arco<K, V> actual = iter.next();
			if(actual.darDestino().darId().compareTo(idDest)==0){

				adyacentes.delete(actual);
				break;
			}
		}
	}

	public Iterator<Arco<K,V>> darAdyacentes( ){

		return adyacentes.iterator();
	}

	public boolean darMarca( ){

		return marca;

	}

	public void marcar(int colorP, Arco<K,V> arcoLlega){

		marca = true;
		color = colorP;
		arcoLlegada = arcoLlega;

	}

	public void desmarcar( ){		
		marca = false;		
		color = -1;
		arcoLlegada = null;
	}

	// hace un dfs entre todos los vertices adyacentes
	public void bfs( int colorP, Arco<K,V> arcoLlega ){

		Queue<Vertice<K, V>> colaDFS = new Queue<>();

		if(!this.marca){

			colaDFS.enqueue(this);

			this.marcar(colorP, arcoLlega);

			colaDFS.dequeue();

		}
		Iterator<Arco<K, V>> iter = this.darAdyacentes();

		while(iter!=null && iter.hasNext()){

			Arco<K, V> actual = iter.next();

			Vertice<K, V> destino = actual.darDestino();

			if(!destino.marca){
				colaDFS.enqueue(destino);
				destino.marcar(colorP, actual);
			}

		} 

		while(!colaDFS.isEmpty()){
			Vertice<K, V> hijo = colaDFS.dequeue();
			hijo.bfs(colorP, hijo.arcoLlegada); 

		}

	}

	public void dfs( int colorP, Arco<K,V> arcoLlega ){

		if(!this.marca){
			this.marcar(colorP, arcoLlega);


			Iterator<Arco<K, V>> iter = this.darAdyacentes();

			while(iter.hasNext()){

				Arco<K, V> actual = iter.next();
				Vertice<K, V> act = actual.darDestino();
				act.dfs(colorP, actual);

			}
		}

	}

	public int darColor(){

		return color;
	}

	public Bag<Arco<K,V>> adjs(){

		return adyacentes;
	}

	public Bag<EstacionesPolicia> darMasCercanas(){

		return masCercanas;
	}

	public Bag<Comparendo> darComparendos(){

		return comparendos;
	}

	public double darCostoAcumulado(){

		return costoAcumulado;

	}

	public void cambiarCostoAcumulado(double costo){

		costoAcumulado = costo;
	}

	public void  cambiarArcoLlegada(Arco<K, V>  llegada){

		arcoLlegada = llegada;
	}

	public Arco<K, V> darArcoLlegada(){

		return arcoLlegada;
	}

	// comparador para costo1
	public int compareTo(Vertice<K, V> o) {

		double resultado = this.darCostoAcumulado() - o.darCostoAcumulado()*(-1);

		if(resultado<0){

			return -1;
		}

		else if(resultado >0){
			
			return 1;

		}

		else{
			return 0;
		}


	}
	public static class ComparadorXVertice implements Comparator<Vertice> {


		@Override
		public int compare(Vertice o1, Vertice o2) {
			// TODO Auto-generated method stub
			return o1.darComparendos().size()-o2.darComparendos().size();
		}
		
	}
	
	

}