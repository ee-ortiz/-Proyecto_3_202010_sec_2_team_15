package model.data_structures;

import java.util.ArrayList;
import java.util.Iterator;

public class GrafoNoDirigido <K extends Comparable<K>, V> {

	private int numVertices;

	private int numArcos;

	private LinearProbing<K, Vertice<K,V>> vertices;

	public GrafoNoDirigido(){

		numVertices = 0;
		numArcos = 0;
		vertices = new LinearProbing<>(siguientePrimo(100));

	}

	public int V(){

		return numVertices;
	}

	public int E(){

		return numArcos;
	}

	public Vertice<K, V> darVertice(K idVert){

		ArrayList<Vertice<K,V>> vertex = vertices.get(idVert);

		if(vertex == null){
			return null;
		}

		else{
			return vertex.get(0);
		}

	}

	public V getInfoVertex(K idVertex){

		Vertice<K, V> infor = darVertice(idVertex);

		if(infor == null){

			return null;
		}

		else{

			return infor.darInfo();
		}


	}

	public Iterator<K> adj (K idVertex){

		Queue<K> rta = new Queue<>();

		Vertice<K, V> infor = darVertice(idVertex);

		if(infor == null){
			return null;
		}
		else{
			Iterator<Arco<K, V>> iter = infor.darAdyacentes();

			while(iter!=null && iter.hasNext()){

				Arco<K, V> actual = iter.next();
				Vertice<K, V> destino = actual.darDestino();
				K identificador = destino.darId();
				rta.enqueue(identificador);
			}
		}

		return rta.iterator();

	}

	public Arco<K, V> darArco(K idOrig, K idDest){

		Arco<K, V> rta = null;

		Vertice<K, V> vertexOrigen = vertices.get(idOrig).get(0);

		Iterator<Arco<K, V>> iter = vertexOrigen.darAdyacentes();

		while(iter!=null && iter.hasNext()){

			Arco<K, V> actual = iter.next();
			if(actual.darDestino().darId().compareTo(idDest)==0){

				rta = actual;
				break;
			}
		}

		return rta;
	}

	public void agregarVertice(K idVert,  V infoVertex){

		Vertice<K, V> nuevo = new Vertice<K, V>(idVert, infoVertex);
		vertices.put(idVert, nuevo);
		numVertices++;

	}

	public void setInfoVertex(K idVertex,V infoVertex){

		Vertice<K, V> buscado = vertices.get(idVertex).get(0);

		if(buscado!=null){
			buscado.cambiarInformacion(infoVertex);
		}

	}

	public double getCostArc(K idVertexIni, K idVertexFin){

		double rta = -1.0;

		Arco<K, V> buscado = darArco(idVertexIni, idVertexFin);

		if(buscado !=null){

			rta = buscado.darCosto();
		}

		return rta;

	}

	public void  setCostArc(K idVertexIni, K idVertexFin,  double cost){

		Arco<K, V> buscado = darArco(idVertexIni, idVertexFin);

		if(buscado !=null){

			buscado.cambiarCosto(cost);
		}

	}

	public void eliminarVertice(K idVert){

		vertices.delete(idVert);
		numVertices--;
	}

	public boolean agregarArco(K idOrig, K idDest,double costo){


		if(darArco(idOrig, idDest)!=null){

			return false;
		}


		ArrayList<Vertice<K, V>> origenArray = vertices.get(idOrig);
		ArrayList<Vertice<K, V>> DestinoArray = vertices.get(idDest);

		if(origenArray == null || DestinoArray ==null){			
			return false;
		}		
		else{
			Vertice<K, V> origen = vertices.get(idOrig).get(0); 
			Vertice<K, V> destino = vertices.get(idDest).get(0); 

			Arco<K, V> nuevo = new Arco<>(origen, destino, costo); // se crea el nuevo arco
			Arco<K, V> nuevo1 = new Arco<>(destino, origen, costo); 
			origen.agregarArco(nuevo);
			destino.agregarArco(nuevo1); // agrego de los dos lados el arco
			numArcos++;
			return true;
		}

	}

	public void eliminarArco(K idOrig, K idDest){

		Vertice<K, V> vertexOrigen = vertices.get(idOrig).get(0);
		Vertice<K, V> vertexDestino = vertices.get(idDest).get(0);

		vertexOrigen.eliminarArco(idDest);
		vertexDestino.eliminarArco(idOrig); // elimino de los dos lados el arco

	}

	// desmarca todos los vertices
	public void desmarcar(){

		Iterator<K> iter = vertices.keys();

		while(iter.hasNext()){

			K actual = iter.next();
			Vertice<K, V> aDesmarcar = vertices.get(actual).get(0);
			aDesmarcar.desmarcar();
		}


	}

	// limpia el grafo desmarcando y reiniciando costos acumulados
	public void limpiar(){


		Iterator<K> iter = vertices.keys();

		while(iter.hasNext()){

			K actual = iter.next();
			Vertice<K, V> aDesmarcar = vertices.get(actual).get(0);
			aDesmarcar.desmarcar();
			aDesmarcar.cambiarCostoAcumulado(0);
		}


	}

	// bfs
	public void bfs(K idOrigen, int color){

		Vertice<K, V> vertexOrigen = vertices.get(idOrigen).get(0);
		vertexOrigen.bfs(color, null);

	}

	public void dfs(K idOrigen, int color){

		Vertice<K, V> vertexOrigen = vertices.get(idOrigen).get(0);
		vertexOrigen.dfs(color, null);
	}



	// se utiliza dfs para visitar los arcos adyacentes y marcarlos
	public int cc(){

		Iterator<K> iter = vertices.keys();

		int conteo = 0;
		while(iter != null && iter.hasNext()){

			K actual = iter.next();	

			if(vertices.get(actual).get(0).darMarca()==false){

				dfs(actual, conteo);
				conteo++;
			}

		}

		return conteo;
	}

	// devuelve un iterador con las llaves de los vertices alcanzados (No se cuenta al vertice origen)
	public Iterator<K> getCC(K idOrigen){

		// se toma en cuenta que ya se ejecutaron los metodos cc o dfs
		Queue<K> llaves = new Queue<>();

		Vertice<K, V> buscado = vertices.get(idOrigen).get(0);
		int colorBuscado = buscado.darColor();

		Iterator<K> iter = vertices.keys();

		while(iter!=null && iter.hasNext()){

			K actual = iter.next();
			Vertice<K, V> act = vertices.get(actual).get(0);
			if(act!= buscado && act.darColor()==colorBuscado){

				llaves.enqueue(actual);
			}
		}

		return llaves.iterator();

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

	// retorna los vertices del grafo
	public LinearProbing<K, Vertice<K, V>> darVertices(){

		return vertices;
	}

	public void Dijkstra(K idInicial){


		Vertice<K, V> inicial = this.darVertice(idInicial);

		inicial.marcar(1, null);  // el arco llegada del vertice inicial es null

		MaxHeapCP<Vertice<K, V>> frenteExploracion = new MaxHeapCP<>(); // tiene que ser una priority queue orientada a costo

		Iterator<Arco<K, V>> iter = inicial.darAdyacentes();

		while(iter!= null && iter.hasNext()){

			Arco<K, V> actual = iter.next();
			Vertice<K, V> act = actual.darDestino();
			act.cambiarCostoAcumulado(actual.darCosto());
			act.cambiarArcoLlegada(actual);
			frenteExploracion.insert(act, null);
		}

		while(!frenteExploracion.isEmpty()){

			Vertice<K, V> sacado = frenteExploracion.delMax(null);

			if(!this.darVertice(sacado.darId()).darMarca()){

				this.darVertice(sacado.darId()).marcar(1, sacado.darArcoLlegada());

				Iterator<Arco<K, V>> iter2 = sacado.darAdyacentes();

				while(iter2!=null && iter2.hasNext()){

					Arco<K, V> adj = iter2.next();

					Vertice<K, V> dest = adj.darDestino();

					if(!dest.darMarca()){

						double acumulado = sacado.darCostoAcumulado() + adj.darCosto();

						if(dest.darCostoAcumulado()==0){

							dest.cambiarCostoAcumulado(acumulado);
							dest.cambiarArcoLlegada(adj);

						}

						else if(acumulado < dest.darCostoAcumulado()){

							dest.cambiarCostoAcumulado(acumulado);
							dest.cambiarArcoLlegada(adj);

						}

						frenteExploracion.insert(dest, null);
					}
				}
			}

		}

	}


	// algoritmo para MST
	public void MST(K idInicial){

		if(!darVertice(idInicial).darMarca()){

			Vertice<K, V> inicial = this.darVertice(idInicial);

			inicial.marcar(1, null);  // el arco llegada del vertice inicial es null

			MaxHeapCP<Arco<K, V>> frenteExploracion = new MaxHeapCP<>(); // PQ orientada a costo de arcos

			Iterator<Arco<K, V>> iter = inicial.darAdyacentes();

			while(iter!= null && iter.hasNext()){

				Arco<K, V> actual = iter.next();
				Vertice<K, V> act = actual.darDestino();
				act.cambiarArcoLlegada(actual);
				frenteExploracion.insert(actual, null);
			}

			while(!frenteExploracion.isEmpty()){

				Arco<K, V> sacado = frenteExploracion.delMax(null);

				if(!sacado.darDestino().darMarca()){

					sacado.darDestino().marcar(1, sacado);

					Iterator<Arco<K, V>> iter2 = sacado.darDestino().darAdyacentes();

					while(iter2!=null && iter2.hasNext()){

						Arco<K, V> adj = iter2.next();

						Vertice<K, V> dest = adj.darDestino();

						if(!dest.darMarca()){

							dest.cambiarArcoLlegada(adj);
							frenteExploracion.insert(adj, null);

						}

					}
				}
			}


		}

	}


}
