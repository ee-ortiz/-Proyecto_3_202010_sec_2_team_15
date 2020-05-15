package view;

import model.logic.Modelo;

public class View 
{
	/**
	 * Metodo constructor
	 */
	public View()
	{

	}

	public void printMenu()
	{
		System.out.println("1. Cargar Datos");
		System.out.println("2. Requerimiento 1 parte inicial");
		System.out.println("3. Requerimiento 2 parte inicial");
		System.out.println("4. Requerimiento 3 parte inicial");
		System.out.println("5. Requerimiento 4 parte inicial");

		System.out.println("6. Guardar Grafo completo");
		System.out.println("7. Cargar Grafo completo");


		System.out.println("Dar el numero de opcion a resolver, luego oprimir tecla Return:");

	}

	public void printMessage(String mensaje) {

		System.out.println(mensaje);
	}		

	public void printModelo(Modelo modelo)
	{
		// TODO implementar
		System.out.println(modelo);
	}
}
