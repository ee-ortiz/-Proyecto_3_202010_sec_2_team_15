package model.logic;

import model.data_structures.ArregloDinamico;
import model.data_structures.Comparendo;
import model.data_structures.IArregloDinamico;
import model.data_structures.LinearProbing;
import model.data_structures.SeparateChaining;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;
/**
 * Definicion del modelo del mundo
 *
 */
public class Modelo {
	/**
	 * Atributos del modelo del mundo
	 */
	private SeparateChaining<String, Comparendo> comps;
	private LinearProbing<String, Comparendo> comps2;
	private GeoJSONProcessing objetoJsonGson;
	private int numeroMInicialSP;
	private int numeroMInicialLP;


	/**
	 * Constructor del modelo del mundo con capacidad predefinida
	 */
	public Modelo()
	{
		comps = new SeparateChaining<>(3011);
		comps2 = new LinearProbing<>(20011);
		numeroMInicialSP = comps.TamañoDeLaHastTable();
		numeroMInicialLP = comps2.darTamanoHashTable();
		objetoJsonGson = new GeoJSONProcessing();
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
	public ArrayList<Comparendo> getSet(String dato)
	{
		ArrayList<Comparendo> rta = comps.getSetArray(dato);

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

		objetoJsonGson.cargarDatos(comps, comps2, direccion);

	}


	public String RetornarDatos(Comparendo comp){
		//INFRACCION, OBJECTID,
		//FECHA_HORA, CLASE_VEHI, TIPO_SERVI, LOCALIDAD.
		String rta = "Codigo de infraccion: "+comp.INFRACCION +" ObjectID: " + comp.OBJECTID + " Fecha y hora: " + comp.FECHA_HORA + " Clase de vehiculo "+comp.CLASE_VEHI + " Tipo de servicio: " +
				comp.TIPO_SERVI + " Localidad: "+ comp.LOCALIDAD;
		return rta;
	}






	public SeparateChaining<String, Comparendo> darSeparateChaining(){

		return comps;
	}
	/**
	 * retorna el lineal probing
	 * @return
	 */
	public LinearProbing<String, Comparendo> darLinearProbing(){

		return comps2;
	}

	public GeoJSONProcessing darObjetoJsonGson(){

		return objetoJsonGson;
	}

	public int darNumeroMInicialSP(){

		return numeroMInicialSP;
	}

	public int darNumeroMInicialLP(){

		return numeroMInicialLP;
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

	public void requerimiento3(){

		Iterator<String> iter = comps.keys();
		int conteo = 0;

		double tiempoMinimoGetSC = 50.0;
		double tiempoPromedioGetSC = 0.0;
		double tiempoMaximoGetSC = -50.0;

		// 8000 llaves conocidas
		while(iter.hasNext() && conteo<8000 ){

			String keyAct = iter.next();

			long start = System.currentTimeMillis();

			// consulto la llave
			comps.getSet(keyAct);

			long end = System.currentTimeMillis();

			double tiempo = (end-start)/1000.0;

			if(tiempo>tiempoMaximoGetSC){
				tiempoMaximoGetSC = tiempo;
			}

			if(tiempo<tiempoMinimoGetSC){
				tiempoMinimoGetSC = tiempo;
			}

			tiempoPromedioGetSC = tiempoPromedioGetSC + tiempo;

			conteo++;
		}

		int conteo2 = 0;

		String prueba = "2017";

		// 2000 llaves desconocidas
		while(conteo2<2000 ){

			String keyAct1 = prueba + conteo;

			long start = System.currentTimeMillis();

			// consulto la llave
			comps.getSet(keyAct1);

			long end = System.currentTimeMillis();

			double tiempo = (end-start)/1000.0;

			if(tiempo>tiempoMaximoGetSC){
				tiempoMaximoGetSC = tiempo;
			}

			if(tiempo<tiempoMinimoGetSC){
				tiempoMaximoGetSC = tiempo;
			}

			tiempoPromedioGetSC = tiempoPromedioGetSC + tiempo;

			conteo2++;
		}

		tiempoPromedioGetSC = tiempoPromedioGetSC/10000;

		System.out.println("El tiempo minimo del metodo getSet() en Separate Chaining fue: " + tiempoMinimoGetSC + " segundos");
		System.out.println("El tiempo máximo del metodo getSet() en Separate Chaining fue: " + tiempoMaximoGetSC + " segundos");
		System.out.println("El tiempo promedio del metodo getSet() en Separate Chaining fue: " + tiempoPromedioGetSC + " segundos");

		Iterator<String> iter2 = comps2.keys();
		conteo = 0;

		double tiempoMinimoGetLP = 50.0;
		double tiempoPromedioGetLP = 0.0;
		double tiempoMaximoGetLP = -50.0;

		// 8000 llaves conocidas
		while(iter.hasNext() && conteo<8000 ){

			String keyAct = iter.next();

			long start = System.currentTimeMillis();

			// consulto la llave
			comps2.getSet(keyAct);

			long end = System.currentTimeMillis();

			double tiempo = (end-start)/1000.0;

			if(tiempo>tiempoMaximoGetLP){
				tiempoMaximoGetLP = tiempo;
			}

			if(tiempo<tiempoMinimoGetLP){
				tiempoMinimoGetLP = tiempo;
			}

			tiempoPromedioGetLP = tiempoPromedioGetLP + tiempo;

			conteo++;
		}

		conteo2 = 0;

		prueba = "2017";

		// 2000 llaves desconocidas
		while(conteo2<2000 ){

			String keyAct1 = prueba + conteo;

			long start = System.currentTimeMillis();

			// consulto la llave
			comps2.getSet(keyAct1);

			long end = System.currentTimeMillis();

			double tiempo = (end-start)/1000.0;

			if(tiempo>tiempoMaximoGetLP){
				tiempoMaximoGetLP = tiempo;
			}

			if(tiempo<tiempoMinimoGetLP){
				tiempoMaximoGetLP = tiempo;
			}

			tiempoPromedioGetLP = tiempoPromedioGetLP + tiempo;

			conteo2++;
		}

		tiempoPromedioGetLP = tiempoPromedioGetLP/10000;

		System.out.println("----------------");
		System.out.println("El tiempo minimo del metodo getSet() en Linear Probing fue: " + tiempoMinimoGetLP + " segundos");
		System.out.println("El tiempo máximo del metodo getSet() en Linear Probing fue: " + tiempoMaximoGetLP + " segundos");
		System.out.println("El tiempo promedio del metodo getSet() en Linear Probing fue: " + tiempoPromedioGetLP + " segundos");

	}

}
