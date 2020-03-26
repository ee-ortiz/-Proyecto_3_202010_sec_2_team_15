package controller;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory.Default;

import model.data_structures.ArregloDinamico;
import model.data_structures.Comparendo;
import model.data_structures.IArregloDinamico;
import model.data_structures.ICola;
import model.data_structures.IPila;
import model.data_structures.LinearProbing;
import model.data_structures.Queue;
import model.data_structures.SeparateChaining;
import model.logic.GeoJSONProcessing;
import model.logic.Modelo;
import view.View;

public class Controller {

	/* Instancia del Modelo*/
	private Modelo modelo;

	/* Instancia de la Vista*/
	private View view;

	private boolean cargado;
	public static String PATH = "./data/comparendos_dei_2018_small.geojson";
	public static String PATH2 = "./data/Comparendos_dei_2018_Bogotá_D.C.geojson";
	public static String PATH3 = "./data/comparendos_mediano.geojson";
	private Comparable<Comparendo>[] aOrdenar;
	private Comparable<Comparendo>[] copiaPrimera;
	SeparateChaining<String, Comparendo> comps;
	LinearProbing<String, Comparendo> comps2;

	/**
	 * Crear la vista y el modelo del proyecto
	 * @param capacidad tamaNo inicial del arreglo
	 */
	public Controller ()
	{
		view = new View();
		modelo = new Modelo();
		cargado = false;
	}

	public void run() 
	{
		Scanner lector = new Scanner(System.in);
		boolean fin = false;


		while( !fin ){
			view.printMenu();

			int option = lector.nextInt();
			switch(option){
			case 1:
				if(cargado == false){
					modelo = new Modelo();

					long start = System.currentTimeMillis();
					modelo.cargar(PATH2);		
					long end = System.currentTimeMillis();
					view.printMessage("Tiempo de carga (s): " + (end-start)/1000.0);

					comps = modelo.darSeparateChaining();	

					comps2 = modelo.darLinearProbing();

					view.printMessage("El Total de comparendos leidos es: " + (int)comps.numeroTotalDeValores());

					modelo.darObjetoJsonGson().retornarPrimerYUltimoComparendo();

					System.out.println("----------------");

					view.printMessage("El numero de duplas  (K, V) del Separate Chaining (valor N) es: " + comps.size());

					view.printMessage("El tamaño inicial (valor M inicial) del Separate Chaining es: " + modelo.darNumeroMInicialSP());

					view.printMessage("El tamaño final (valor M final) del Separate Chaining es: " + comps.TamañoDeLaHastTable());

					view.printMessage("El factor de carga final (N/M) del Separate Chaining es: " + (double)comps.size()/(double)comps.TamañoDeLaHastTable());

					view.printMessage("El numero de rehashes que tuvo el Separate Chaining es: " + comps.darNumeroReHashes());

					System.out.println("----------------");

					view.printMessage("El numero de duplas  (K, V) del Linear Probing (valor N) es: " + comps2.size());

					view.printMessage("El tamaño inicial (valor M inicial) del Linear Probing es: " + modelo.darNumeroMInicialLP());

					view.printMessage("El tamaño final (valor M final) del Linear Probing es: " + comps2.darTamanoHashTable());

					view.printMessage("El factor de carga final (N/M) del Linear Probing es: " + (double)comps2.size()/(double)comps2.darTamanoHashTable());

					view.printMessage("El numero de rehashes que tuvo el Linear Probing es: " + comps2.darNumeroReHashes());


					cargado = true;

				}

				else{

					view.printMessage("Los datos contenidos en los archivos sólo se pueden leer una vez" +"\n");
				}

				view.printMessage("");

				break;			


			case 2:
				view.printMessage("Ingrese una fecha de la siguiente forma (2018/12/24) si el mes o dia es de un solo dígito por favor ingrese un '0' antes (2018/02/04) ");

				String key1 = "";
				String fechaKey1 = lector.next();
				String fechasKeys1[] = fechaKey1.split("/");

				try{
					if(fechasKeys1[1].length() ==1){
						fechasKeys1[1] = "0" + fechasKeys1[1];
					}
					if(fechasKeys1[2].length() ==1){
						fechasKeys1[2] = "0" + fechasKeys1[2];
					}

					fechaKey1 = fechasKeys1[0] + fechasKeys1[1] + fechasKeys1[2];

				}

				catch (Exception e){

					view.printMessage("Fecha inválida");
					break;

				}

				view.printMessage("Ingrese una clase de vehiculo de la siguiente forma (MOTOCICLETA) si la palabra tiene tilde por favor escribala ");
				String claseVehiKey1 = lector.next().trim();

				view.printMessage("Ingrese una infraccion de la siguiente forma (C02) ");
				String infraccionKey1 = lector.next().trim();

				key1 = fechaKey1+ claseVehiKey1 + infraccionKey1;

				Iterator<Comparendo> iter1 = comps.getSet(key1);

				if(iter1!=null){

					IArregloDinamico<Comparendo> buscados = new ArregloDinamico<>(20);

					while(iter1.hasNext()){

						Comparendo elemento = iter1.next();

						buscados.agregar(elemento);

					}

					Comparable[] comparableBuscados = modelo.copiarArreglo(buscados);
					modelo.sortParaMerge(comparableBuscados, "descendente", null);

					IArregloDinamico<Comparendo> rta = modelo.retornarArregloDeComparendos(comparableBuscados);

					view.printMessage("El numero de comparendos existentes con la fecha, clase de vehiculo e infraccion que ingresaste es: " + rta.darTamano());
					view.printMessage("Los comparendos son: ");
					for(int i = 0; i<rta.darTamano(); i++){

						view.printMessage("-" + rta.darElemento(i).retornarDatosTaller5());
					}

				}

				else{

					view.printMessage("La llave ingresada no existe");
				}

				view.printMessage("");

				break;

			case 3:

				view.printMessage("Ingrese una fecha de la siguiente forma (2018/12/24) si el mes o dia es de un solo dígito por favor ingrese un '0' antes (2018/02/04) ");

				String key = "";
				String fechaKey = lector.next();
				String fechasKeys[] = fechaKey.split("/");

				try{
					if(fechasKeys[1].length() ==1){
						fechasKeys[1] = "0" + fechasKeys[1];
					}
					if(fechasKeys[2].length() ==1){
						fechasKeys[2] = "0" + fechasKeys[2];
					}

					fechaKey = fechasKeys[0] + fechasKeys[1] + fechasKeys[2];

				}

				catch (Exception e){

					view.printMessage("Fecha inválida");
					break;

				}

				view.printMessage("Ingrese una clase de vehiculo de la siguiente forma (MOTOCICLETA) si la palabra tiene tilde por favor escribala ");
				String claseVehiKey = lector.next().trim();

				view.printMessage("Ingrese una infraccion de la siguiente forma (C02) ");
				String infraccionKey = lector.next().trim();

				key = fechaKey+ claseVehiKey + infraccionKey;

				Iterator<Comparendo> iter = comps.getSet(key);

				if(iter!=null){

					IArregloDinamico<Comparendo> buscados = new ArregloDinamico<>(20);

					while(iter.hasNext()){

						Comparendo elemento = iter.next();

						buscados.agregar(elemento);

					}

					Comparable[] comparableBuscados = modelo.copiarArreglo(buscados);
					modelo.sortParaMerge(comparableBuscados, "descendente", null);

					IArregloDinamico<Comparendo> rta = modelo.retornarArregloDeComparendos(comparableBuscados);

					view.printMessage("El numero de comparendos existentes con la fecha, clase de vehiculo e infraccion que ingresaste es: " + rta.darTamano());
					view.printMessage("Los comparendos son: ");
					for(int i = 0; i<rta.darTamano(); i++){

						view.printMessage("-" + rta.darElemento(i).retornarDatosTaller5());
					}

				}

				else{

					view.printMessage("La llave ingresada no existe");
				}

				view.printMessage("");

				break;

			case 4:

				modelo.requerimiento3();

				break;

			default: 

				view.printMessage("--------- \n Opcion Invalida !! \n---------");
				break;
			}

		}

	}	
}