package test.logic;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import model.data_structures.ArregloDinamico;
import model.data_structures.IArregloDinamico;
import model.logic.Modelo;

public class TestAlgoritmosOrdenamiento {


	/*Definir las pruebas unitarias del ordenamiento ShellSort para tres conjuntos de datos de
	prueba: datos aleatorios, datos ordenados ascendentemente y datos ordenados
	descendentemente.
	AYUDA 1: Para generar una muestra de datos aleatorios, puede generar números reales
	(double) pseudoaleatorios con el método
	double Math.random()
	AYUDA 2: Para generar una muestra de datos ordenados ascendentemente puede generar
	una muestra de números enteros usando un ciclo for con valor inicial i = 0 y avance i++.
	AYUDA 3: Para generar una muestra de datos ordenados descendentemente puede generar
	una muestra de números enteros usando un ciclo for con valor inicial i = 0 y avance i--. 
	 */

	private Modelo modelo;
	private IArregloDinamico<Double> arregloDoubles;
	private IArregloDinamico<Integer> arregloIntegers;
	private Comparable[] doubles;
	private Comparable[] ints;


	@Before
	public void setUp1() {

		modelo = new Modelo();
		arregloDoubles = new ArregloDinamico<Double>(20);
		arregloIntegers = new ArregloDinamico<Integer>(20);
		doubles = new Comparable[20];
		ints = new Comparable[20];

	}

	public void setUp2(){

		for(int i = 0; i<20; i++){
			ints[i] = arregloIntegers.darElemento(i);
			doubles[i] = arregloDoubles.darElemento(i);

		}

	}
	// arreglo aleatorio
	public void setUp1Arreglo(){

		for(int i = 0; i<20; i++){
			double aAgregar = Math.random();
			arregloDoubles.agregar(aAgregar);

		}

	}
	//arreglo ordenado ascendentemente
	public void setUp2Arreglo(){

		for(int i = 0; i<20; i++ ){
			arregloIntegers.agregar(i);
		}

	}
	// arreglo ordenado descendentemente
	public void setUp3Arreglo(){

		for(int i = 0; i> -20; i--){
			arregloIntegers.agregar(i);
		}

	}

	@Test
	public void testShellSortArregloAleatorio() {

		setUp1();
		setUp1Arreglo();
		setUp2();
		modelo.shellSort(doubles);
		for(int i= 0; i<19; i++){

			assertTrue(doubles[i].compareTo(doubles[i+1])<=0);

		}

	}

	@Test
	public void testShellSortOrdenado() {

		setUp1();
		setUp2Arreglo();
		setUp2();
		modelo.shellSort(ints);
		for(int i= 0; i<20; i++){

			assertEquals(i, ints[i]);

		}

	}

	@Test
	public void testShellSortOrdenadoDescendente() {

		setUp1();
		setUp3Arreglo();
		setUp2();
		modelo.shellSort(ints);
		for(int i= 0; i<20; i++){

			assertEquals(-19+i, ints[i]);
		}

	}

	@Test
	public void testQuickSortAleatorio() {

		setUp1();
		setUp1Arreglo();
		setUp2();
		modelo.sort(doubles, 0, doubles.length-1);
		for(int i= 0; i<19; i++){

			assertTrue(doubles[i].compareTo(doubles[i+1])<=0);
		}

	}

	@Test
	public void testQuickSortOrdenado() {

		setUp1();
		setUp2Arreglo();
		setUp2();
		modelo.sort(ints, 0, ints.length-1);
		for(int i= 0; i<20; i++){

			assertEquals(i, ints[i]);

		}

	}

	@Test
	public void testQuickSortOrdenadoDescendente() {

		setUp1();
		setUp3Arreglo();
		setUp2();
		modelo.sort(ints, 0, ints.length-1);
		for(int i= 0; i<20; i++){

			assertEquals(-19+i, ints[i]);
		}

	}
	@Test
	public void testMergeSortAleatorio()
	{


	}

	@Test
	public void testMergeSortOrdenadoAscendentemente()
	{

	}
	@Test
	public void testMergeSortAleatorioOrdenadoDescendentemente()
	{

	}
}
