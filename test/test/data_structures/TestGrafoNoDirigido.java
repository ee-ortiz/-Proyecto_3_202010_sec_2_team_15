package test.data_structures;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import model.data_structures.Arco;
import model.data_structures.GrafoNoDirigido;
import model.data_structures.Vertice;


public class TestGrafoNoDirigido {

	private GrafoNoDirigido<Integer, Numero> GND;

	private class Numero {
		private String nombre;           // key
		private Integer numero;         // associated data



		public Numero(String pNombre, Integer pNum) {
			this.nombre = pNombre;
			this.numero = pNum;

		}

		public String darNombre(){
			return nombre;
		}

		public int darNumero(){

			return (int) numero;
		}
	}

	@Before
	public void setUp1(){

		GND = new GrafoNoDirigido<Integer, Numero>();
	}

	public void setUp2(){

		Numero uno = new Numero("Uno", 1);
		Numero dos = new Numero("Dos", 2);
		Numero tres = new Numero("Tres", 3);
		Numero cuatro = new Numero("Cuatro", 4);
		Numero cinco = new Numero("Cinco", 5);
		Numero seis = new Numero("Seis", 6);
		Numero siete = new Numero("Siete", 7);
		Numero ocho = new Numero("Ocho", 8);


		// creo 8 vertices en el grafo
		GND.agregarVertice((Integer)uno.darNumero(), uno);
		GND.agregarVertice((Integer)dos.darNumero(), dos);
		GND.agregarVertice((Integer)tres.darNumero(), tres);
		GND.agregarVertice((Integer)cuatro.darNumero(), cuatro);
		GND.agregarVertice((Integer)cinco.darNumero(), cinco);
		GND.agregarVertice((Integer)seis.darNumero(), seis);
		GND.agregarVertice((Integer)siete.darNumero(), siete);
		GND.agregarVertice((Integer)ocho.darNumero(), ocho);

		//asocio verices de 2 en 2
		GND.agregarArco((Integer)1, (Integer)2, 1.0);
		GND.agregarArco((Integer)3, (Integer)4, 1.0);
		GND.agregarArco((Integer)5, (Integer)6, 1.0);
		GND.agregarArco((Integer)7, (Integer)8, 1.0);



	}

	@Test
	public void testDfsYCC(){

		setUp1();
		setUp2();
		int cc = GND.cc();
		assertEquals(4, cc);

	}

	@Test
	public void testNumVertexYArcos(){

		setUp1();
		setUp2();
		assertEquals(8, GND.V());
		assertEquals(4, GND.E());

	}

	@Test
	public void testGetCC(){

		setUp1();
		setUp2();
		GND.cc();
		Iterator<Integer> iter = GND.getCC((Integer)1);

		int conteo = 0;

		while(iter!=null && iter.hasNext()){

			iter.next();
			conteo++;
		}

		// los vectices adyacentes no cuentan al vertice origen
		assertEquals(1, conteo);

		GND.desmarcar();

		Vertice<Integer, Numero> vertex = GND.darVertice((Integer)1);
		boolean marcado = vertex.darMarca();

		assertEquals(marcado, false);


	}

}