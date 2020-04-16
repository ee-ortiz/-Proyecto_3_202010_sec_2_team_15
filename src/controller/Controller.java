package controller;


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
	public static String PATH2 = "./data/Comparendos_dei_2018_Bogotá_D.C.geojson";
	public static String PATH3 = "./data/Comparendos_DEI_2018_Bogotá_D.C_50000_.geojson";


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
					modelo.cargar(PATH3);		
					long end = System.currentTimeMillis();
					view.printMessage("Tiempo de carga (s): " + (end-start)/1000.0);

					modelo.requerimiento1Cargar();

					cargado = true;

				}

				else{

					view.printMessage("Los datos contenidos en los archivos sólo se pueden leer una vez");
				}

				view.printMessage("");

				break;			


			case 2:

				view.printMessage("Ingresa un ObjectID de la siguiente forma(100000): ");
				int num = lector.nextInt();
				modelo.requerimiento2(num);
				System.out.println();
				break;

			case 3:
				view.printMessage("Ingresa un ObjectID de la siguiente forma(100000): el cual sera el menor valor");
				int num1 = lector.nextInt();
				view.printMessage("Ingresa un ObjectID de la siguiente forma(100000): el cual sera el mayor valor");
				int num2 = lector.nextInt();
				modelo.requerimiento3(num1, num2);
				System.out.println();
				// caso andrés
				break;

			default: 

				view.printMessage("--------- \n Opcion Invalida !! \n---------");
				break;
			}

		}

	}	
}