package model.logic;

import model.data_structures.Arco;
import model.data_structures.ArregloDinamico;
import model.data_structures.Comparendo;
import model.data_structures.EstacionesPolicia;
import model.data_structures.GrafoNoDirigido;
import model.data_structures.IArregloDinamico;
import model.data_structures.LinearProbing;
import model.data_structures.Queue;
import model.data_structures.RedBlackBST;
import model.data_structures.SeparateChaining;
import model.data_structures.Vertice;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import javafx.scene.shape.Arc;
import jdk.nashorn.internal.ir.CatchNode;
/**
 * Definicion del modelo del mundo
 *
 */
public class Modelo {
	/**
	 * Atributos del modelo del mundo
	 */
	private RedBlackBST<Integer, Comparendo> comps;
	private GeoJSONProcessing objetoJsonGson;
	private CargaVerticesYArcos carga;
	private GrafoNoDirigido<Integer, String> grafo;
	public final static String PATH = "./data/GrafoJSON.geojson";
	public final static String PATH2 = "./data/estacionpolicia.geojson";
	private Maps mapa;
	private Queue<EstacionesPolicia> cola;

	/**
	 * Constructor del modelo del mundo con capacidad predefinida
	 */
	public Modelo()
	{
		cola = new Queue<>();
		comps = new RedBlackBST<Integer, Comparendo>();
		objetoJsonGson = new GeoJSONProcessing();
		grafo = new GrafoNoDirigido<>();
		carga = new CargaVerticesYArcos();
	}

	/**
	 * Servicio de consulta de numero de elementos presentes en el modelo 
	 * @return numero de elementos presentes en el modelo
	 */
	public int darTamano()
	{
		return comps.size();
	}


	public void shellSort(Comparable datos[]){

		int tamano = datos.length;
		int h = 1;
		while (h < tamano/3){

			h = 3*h + 1; // 1, 4, 13, 40, 121, 364, ...

		}
		while (h >= 1)
		{ // h-sort the array.
			for (int i = h; i < tamano; i++)
			{
				for (int j = i; j >= h && less(datos[j], datos[j-h]); j -= h){

					exch(datos, j, j-h);
				}

			}
			h = h/3;
		}

	}

	/* This function takes last element as pivot, 
    places the pivot element at its correct 
    position in sorted array, and places all 
    smaller (smaller than pivot) to left of 
    pivot and all greater elements to right 
    of pivot */

	// solucion adaptada de: https://www.geeksforgeeks.org/quick-sort/
	public static int partition(Comparable datos[], int low, int high) 
	{ 
		Comparable pivote = datos[high];  
		int i = (low-1); // index of smaller element 
		for (int j=low; j<high; j++) 
		{ 
			// Si el elemento actual es menor que el pivote
			if (less(datos[j], pivote)) 
			{ 
				i++; 
				// swap datos[i] and datos[j]  
				exch(datos, i, j);
			} 
		} 

		// swap datos[i+1] and datos[high] (o pivote) 
		exch(datos, i+1, high);

		return i+1; 
	} 

	//relacionado al quicksort, de aqui inicia.
	public static void sort(Comparable[] a)
	{
		StdRandom.shuffle(a);
		sort(a, 0, a.length - 1);
	} 
	/* The main function that implements QuickSort() 
   datos[] --> Array to be sorted, 
   low  --> Starting index, 
   high  --> Ending index */

	// solucion adaptada de: https://www.geeksforgeeks.org/quick-sort/
	public static void sort(Comparable datos[], int low, int high) 
	{ 
		if (low < high) 
		{ 
			/* pi is partitioning index, arr[pi] is  
           now at right place */
			int pi = partition(datos, low, high); 

			// Recursively sort elements before 
			// partition and after partition 
			sort(datos, low, pi-1); 
			sort(datos, pi+1, high); 
		} 
	} 

	public static void exch(Comparable[] a, int i, int j ){

		Comparable temporal = a[i];
		a[i] = a[j];
		a[j] = temporal;
	}

	public static boolean less(Comparable a, Comparable b){

		if(a.compareTo(b)<0){
			return true;
		}
		else{
			return false; //mayor o igual a cero
		}
	}
	/*
	 * paso 2 algoritmo mergeSort
	 */
	private static void sortParaMergeSort(Comparable[] a, Comparable[] aux, int lo, int hi, String orden, Comparator<Comparendo> comparador)
	{
		if (hi <= lo) return;
		int mid = lo + (hi - lo) / 2;
		sortParaMergeSort(a, aux, lo, mid, orden, comparador);
		sortParaMergeSort(a, aux, mid+1, hi, orden, comparador);
		merge(a, aux, lo, mid, hi, orden, comparador);
	}

	/*
	 *  aqui inicia el algoritmo mergeSort sacado del libro algorithms 4 edicion
	 */
	public static void sortParaMerge(Comparable[] a, String orden, Comparator<Comparendo> comparador)
	{
		Comparable[] aux = new Comparable[a.length];
		sortParaMergeSort(a, aux, 0, a.length - 1, orden, comparador);
	}



	/*
	 * ultimo paso
	 */
	private static void merge(Comparable[] a, Comparable[] aux, int lo, int mid, int hi, String orden, Comparator<Comparendo> comparador )
	{
		for (int k = lo; k <= hi; k++)
			aux[k] = a[k];
		int i = lo, j = mid+1;
		for (int k = lo; k <= hi; k++)
		{
			if(comparador==null){

				if(orden.equalsIgnoreCase("descendente")){
					if (i > mid) a[k] = aux[j++];
					else if (j > hi) a[k] = aux[i++];
					else if (less(aux[j], aux[i])) //si izquierda menor true
					{ a[k] = aux[j++];}
					else a[k] = aux[i++];
				}

				if(orden.equalsIgnoreCase("ascendente")){
					if (i > mid) a[k] = aux[j++];
					else if (j > hi) a[k] = aux[i++];
					else if (!less(aux[j], aux[i])) //si izquierda no es menor true
					{ a[k] = aux[j++];}
					else a[k] = aux[i++];
				}
			}

			else{

				if(orden.equalsIgnoreCase("descendente")){
					if (i > mid) a[k] = aux[j++];
					else if (j > hi) a[k] = aux[i++];
					else if (comparador.compare(cambiarDeComparableAComparendo(aux[j]) , cambiarDeComparableAComparendo(aux[i]) )<0) //si izquierda menor true
					{ a[k] = aux[j++];}
					else a[k] = aux[i++];
				}

				if(orden.equalsIgnoreCase("ascendente")){
					if (i > mid) a[k] = aux[j++];
					else if (j > hi) a[k] = aux[i++];
					else if (comparador.compare(cambiarDeComparableAComparendo(aux[j]) , cambiarDeComparableAComparendo(aux[i]) )>=0) //si izquierda no es menor true
					{ a[k] = aux[j++];}
					else a[k] = aux[i++];
				}

			}

		}
	}
	/**
	 * Requerimiento buscar dato
	 * @param dato Dato a buscar
	 * @return dato encontrado
	 */
	public Comparendo get(int dato)
	{
		Comparendo rta = comps.get(dato);

		return rta;
	}

	/**
	 * Requerimiento eliminar dato
	 * @param dato Dato a eliminar
	 * @return dato eliminado
	 */
	public String eliminar(String dato)
	{
		return null;
	}


	public void cargar(String direccion){

		objetoJsonGson.cargarDatos(comps, direccion);

	}

	public void cargarGrafo(String direccion, String direccion2){

		carga.cargarInformacion(grafo, direccion, direccion2);
	}


	public String RetornarDatos(Comparendo comp){
		//INFRACCION, OBJECTID,
		//FECHA_HORA, CLASE_VEHI, TIPO_SERVI, LOCALIDAD.
		String rta = "Codigo de infraccion: "+comp.INFRACCION +" ObjectID: " + comp.OBJECTID + " Fecha y hora: " + comp.FECHA_HORA + " Clase de vehiculo "+comp.CLASE_VEHI + " Tipo de servicio: " +
				comp.TIPO_SERVI + " Localidad: "+ comp.LOCALIDAD;
		return rta;
	}

	public RedBlackBST<Integer, Comparendo> darRedBlackBST(){

		return comps;
	}

	public GeoJSONProcessing darObjetoJsonGson(){

		return objetoJsonGson;
	}


	public static Comparendo cambiarDeComparableAComparendo(Comparable a){

		Comparendo rta = (Comparendo) a;
		return rta;
	}

	public Comparable[] copiarArreglo(IArregloDinamico<Comparendo> pComps){

		Comparable[] rta = new Comparable[pComps.darTamano()];
		int i = 0;
		while(i < pComps.darTamano()){
			rta[i] = pComps.darElemento(i);
			i++;
		}

		return rta;

	}

	public IArregloDinamico<Comparendo> retornarArregloDeComparendos(Comparable[] a){

		IArregloDinamico<Comparendo> rta = new ArregloDinamico<>(100);

		for(int i =0; i<a.length; i++){

			Comparable actual = a[i];
			Comparendo aAgregar = cambiarDeComparableAComparendo(actual);
			rta.agregar(aAgregar);
		}

		return rta;

	}


	public void requerimiento1Cargar(){

		objetoJsonGson.retornarRequerimientoCargar();
		int max= comps.getHeight(comps.max());
		int min= comps.getHeight(comps.min());
		System.out.println(comps.heightLeft()+"a");
		System.out.println(comps.heightRight()+"a");
	}

	public void requerimiento2(int objectID){

		Comparendo rta = comps.get(objectID);

		if(rta != null) System.out.println("Comparendo encontrado, sus datos son: " + rta.retornarDatosTaller6());
		else System.out.println("No existe un comparendo con ese ObjectID");

	}
	public void requerimiento3(int objectID_Inferior, int objectID_Superior){
		Comparendo rta = comps.get(objectID_Inferior);

		Comparendo rta2 = comps.get(objectID_Superior);
		Iterator<Comparendo> a= comps.valuesInRange(objectID_Inferior, objectID_Superior);
		if(rta!= null &&rta2!=null){
			while(a.hasNext())
			{
				System.out.println(a.next().retornarDatos());

			};
		}else
			System.out.println("No existe un comparendo con alguno de esos ObjectIDS");

	}

	public void abrirGrafoJSON(String direccion, GrafoNoDirigido<Integer, String> pGrafo){

		JsonReader reader;
		try {
			reader = new JsonReader(new FileReader(PATH));
			JsonElement elem = JsonParser.parseReader(reader);
			JsonArray e2 = elem.getAsJsonArray();

			for (JsonElement e: e2)
			{
				Integer idVertex =(Integer) e.getAsJsonObject().get("IdVertex").getAsInt();

				String infoVertex = e.getAsJsonObject().get("infoVertex").getAsString();

				pGrafo.agregarVertice(idVertex, infoVertex);

			}

			for (JsonElement e: e2)
			{
				Integer idVertex =(Integer) e.getAsJsonObject().get("IdVertex").getAsInt();
				String[] ArcosDestino = e.getAsJsonObject().get("ArcosDestino").getAsString().split("/");
				String[] CostoArcos = e.getAsJsonObject().get("CostoArcos").getAsString().split("/");

				if(ArcosDestino.length!=0){

					for(int i = 0; i<ArcosDestino.length; i++){

						if(!ArcosDestino[i].equals("") && !CostoArcos[i].equals("")){
							Integer idDestino = (Integer) Integer.parseInt(ArcosDestino[i]);
							double costo = Double.parseDouble(CostoArcos[i]);

							pGrafo.agregarArco(idVertex, idDestino, costo);
						}

					}
				}

			}

		}

		catch (FileNotFoundException e) {

			e.printStackTrace();

		}

	}

	public void convertirAJSON(){

		if(grafo.V() == 0){
			return;
		}

		JsonArray features = new JsonArray();

		Iterator<Integer> iter = grafo.darVertices().keys();

		while(iter!= null && iter.hasNext()){

			JsonObject features1 = new JsonObject();

			Integer id = iter.next();

			features1.addProperty("IdVertex", id);;


			String informacion = grafo.darVertice(id).darInfo();

			features1.addProperty("infoVertex", informacion);

			Iterator<Arco<Integer, String>> iter2 = grafo.darVertice(id).darAdyacentes();

			String arcosDestino = "";
			String costoArcos = "";

			while(iter2 != null && iter2.hasNext()){

				Arco<Integer, String> ac = iter2.next();

				double costo = ac.darCosto();
				Integer idDestino = ac.darDestino().darId();
				arcosDestino += idDestino + "/"	;	// id destino separados por /
				costoArcos += costo + "/"; // costos arcos separados por /

			}

			features1.addProperty("ArcosDestino", arcosDestino);
			features1.addProperty("CostoArcos", costoArcos);

			features.add(features1);

		}

		try{

			FileWriter file = new FileWriter(PATH);

			file.write(features.toString());
			file.flush();
			file.close();
		}

		catch(Exception e){

			e.printStackTrace();
		}

	}

	public void mostrarMapa(){

		mapa = new Maps("Grafo Bogot·", grafo, cola);
		mapa.initFrame();

	}

	public void cargarEstacionesPolicia(){

		JsonReader reader;
		try {
			reader = new JsonReader(new FileReader(PATH2));
			JsonElement elem = JsonParser.parseReader(reader);
			JsonArray e2 = elem.getAsJsonObject().get("features").getAsJsonArray();

			for(JsonElement e: e2) {
				EstacionesPolicia c = new EstacionesPolicia();

				c.OBJECTID = e.getAsJsonObject().get("properties").getAsJsonObject().get("OBJECTID").getAsInt();

				c.nombre = e.getAsJsonObject().get("properties").getAsJsonObject().get("EPODESCRIP").getAsString();

				c.nombre = c.nombre.replaceAll("Estaci√≥n de Polic√≠a", "EstaciÛn de Policia");
				c.nombre = c.nombre.replaceAll("Fontib√≥", "FontibÛ"); c.nombre = c.nombre.replaceAll("ari√±o", "NariÒo");
				c.nombre = c.nombre.replaceAll("Martir√©s", "M·rtires"); c.nombre = c.nombre.replaceAll("Cristob√°l", "Cristobal");
				c.nombre = c.nombre.replaceAll("Candelar√≠a", "Candelaria"); c.nombre = c.nombre.replaceAll("Bol√≠var", "Bolivar");

				c.EPOLATITUD = e.getAsJsonObject().get("properties").getAsJsonObject().get("EPOLATITUD").getAsDouble();

				c.EPOLONGITU = e.getAsJsonObject().get("properties").getAsJsonObject().get("EPOLONGITU").getAsDouble();



				cola.enqueue(c);

			}

			System.out.println("Se cargaron las " + cola.size() + " estaciones de policia, se muestran sus datos: ");

			Iterator<EstacionesPolicia> iter = cola.iterator();

			while(iter.hasNext()){

				EstacionesPolicia actual = iter.next();

				System.out.println(actual.retornarInformacion());
			}

		} 
		catch (FileNotFoundException e) {

			e.printStackTrace();

		}


	}
}

