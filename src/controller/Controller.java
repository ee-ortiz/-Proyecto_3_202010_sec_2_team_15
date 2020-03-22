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
	public static String PATH2 = "./data/Comparendos_dei_2018_Bogot�_D.C.geojson";
	public static String PATH3 = "./data/comparendos_mediano.geojson";
	private Comparable<Comparendo>[] aOrdenar;
	private Comparable<Comparendo>[] copiaPrimera;
	SeparateChaining<String, Comparendo> comps;

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

					view.printMessage("El Total de comparendos leidos es: " + (int)comps.numeroTotalDeValores());

					modelo.darObjetoJsonGson().retornarPrimerYUltimoComparendo();

					view.printMessage("El numero de duplas  (K, V) del Separate Chaining (valor N) es: " + comps.size());

					view.printMessage("El tama�o inicial (valor M inicial) del Separate Chaining es: " + modelo.darNumeroMInicial());

					view.printMessage("El tama�o final (valor M final) del Separate Chaining es: " + comps.Tama�oDeLaHastTable());

					view.printMessage("El factor de carga final (N/M) del Separate Chaining es: " + (double)comps.size()/(double)comps.Tama�oDeLaHastTable());

					view.printMessage("El numero de rehashes que tuvo el Separate Chaining es: " + comps.darNumeroReHashes());

					cargado = true;

				}

				else{

					view.printMessage("Los datos contenidos en los archivos s�lo se pueden leer una vez" +"\n");
				}

				view.printMessage("");

				break;			


			case 2:
				//caso andres

				break;

			case 3:

				view.printMessage("Ingrese una fecha de la siguiente forma (2018/12/24) si el mes o dia es de un solo d�gito por favor ingrese un '0' antes (2018/02/04) ");

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

					view.printMessage("Fecha inv�lida");
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