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
		System.out.println("2. Consultar un comparendo por ObjectID");
		System.out.println("3."); 
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
