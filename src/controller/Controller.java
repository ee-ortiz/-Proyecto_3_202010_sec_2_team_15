package controller;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory.Default;

import model.data_structures.ArregloDinamico;
import model.data_structures.Comparendo;
import model.data_structures.GrafoNoDirigido;
import model.data_structures.IArregloDinamico;
import model.data_structures.ICola;
import model.data_structures.IPila;
import model.data_structures.LinearProbing;
import model.data_structures.Queue;
import model.data_structures.RedBlackBST;
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
	public static String PATH3 = "./data/Comparendos_DEI_2018_Bogot�_D.C_50000.geojson";
	public static String VERTICES = "./data/vertices.txt";
	public static String ARCOS = "./data/arcos.txt";
	public static String VERTICES2 = "./data/bogota_vertices.txt";
	public static String ARCOS2 = "./data/bogota_arcos.txt";

	public final static String GrafoJSON = "./data/GrafoJSON.geojson";




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
					modelo.cargarTodo(PATH3);		
					long end = System.currentTimeMillis();
					view.printMessage("Tiempo de carga (s): " + (end-start)/1000.0);

					cargado = true;

				}

				else{

					view.printMessage("Los datos contenidos en los archivos s�lo se pueden leer una vez");
				}

				view.printMessage("");

				break;			


			case 2:

				view.printMessage("Inserte latitud: ");
				double lat = lector.nextDouble();
				view.printMessage("Inserte longitud");
				double lon = lector.nextDouble();

				int rta = modelo.requerimientoParteInicial1(lat, lon);

				if(rta ==-1){
					view.printMessage("No hay vertices en el grafo");
				}

				else{
					view.printMessage("El id del vectice mas cercano a las coordenadas dadas es: " + rta);
				}

				break;

			case 3:


				modelo.requerimientoParteInicial2();

				break;

			case 4: 

				modelo.requerimientosParteInicial3();

				break;

			case 5:

				modelo.requerimientosParteInicial4();
				modelo.prueba();

				break;

			case 6:

				modelo.guardarGrafoCompleto();
				break;

			case 7:

				modelo.cargarGrafoCompleto();
				break;

			case 8:

				System.out.println("ingrese latitud inicial: ");
				double lat1 = lector.nextDouble();
				System.out.println("ingrese longitud inicial: ");
				double lon1 = lector.nextDouble();
				System.out.println("ingrese latitud final: ");
				double lat2 = lector.nextDouble();
				System.out.println("ingrese longitud final: ");
				double lon2 = lector.nextDouble();

				modelo.requerimiento1A(lat1, lon1, lat2, lon2);

				break;

			case 9:

				System.out.println("Ingrese un numero M:");
				int num = lector.nextInt();
		
				long start1 = System.currentTimeMillis();
				modelo.requerimiento2A(num);		
				long end1 = System.currentTimeMillis();
				view.printMessage("Tiempo de carga (s): " + (end1-start1)/1000.0);
				

				break;
			case 10:
				System.out.println("ingrese latitud inicial: ");
				double lat12 = lector.nextDouble();
				System.out.println("ingrese longitud inicial: ");
				double lon12 = lector.nextDouble();
				System.out.println("ingrese latitud final: ");
				double lat22 = lector.nextDouble();
				System.out.println("ingrese longitud final: ");
				double lon22 = lector.nextDouble();

				modelo.requerimiento1B(lat12, lon12, lat22, lon22);

			case 11:

				System.out.println("Ingrese un numero M:");
				int num1 = lector.nextInt();
		
				long start2 = System.currentTimeMillis();
				modelo.requerimiento2b(num1);		
				long end2 = System.currentTimeMillis();
				view.printMessage("Tiempo de carga (s): " + (end2-start2)/1000.0);
				

				break;
			case 12:

				System.out.println("Ingrese un numero M:");
				int num3 = lector.nextInt();
		
				long start3 = System.currentTimeMillis();
				modelo.requerimiento1C(num3);		
				long end3 = System.currentTimeMillis();
				view.printMessage("Tiempo de carga (s): " + (end3-start3)/1000.0);
				

				break;
			default: 
				

				view.printMessage("--------- \n Opcion Invalida !! \n---------");
				break;
			}

		}

	}	
}