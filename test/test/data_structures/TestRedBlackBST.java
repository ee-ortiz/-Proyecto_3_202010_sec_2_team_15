package test.data_structures;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import model.data_structures.RedBlackBST;

public class TestRedBlackBST {

	private RedBlackBST<Integer, Numero> redBlackBST;

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

		redBlackBST = new RedBlackBST<Integer, Numero>();
	}

	public void setUp2(){

		Numero cero = new Numero("Cero", 0);
		Numero uno = new Numero("Uno", 1);
		Numero dos = new Numero("Dos", 2);
		Numero tres = new Numero("Tres", 3);
		Numero cuatro = new Numero("Cuatro", 4);
		Numero cinco = new Numero("Cinco", 5);
		Numero seis = new Numero("Seis", 6);
		Numero siete = new Numero("Siete", 7);
		Numero ocho = new Numero("Ocho", 8);
		Numero nueve = new Numero("Nueve", 9);
		Numero diez = new Numero("Diez", 10);

		redBlackBST.put((Integer)cero.darNumero(), cero);
		redBlackBST.put((Integer)uno.darNumero(), uno);
		redBlackBST.put((Integer)dos.darNumero(), dos);
		redBlackBST.put((Integer)tres.darNumero(), tres);
		redBlackBST.put((Integer)cuatro.darNumero(), cuatro);
		redBlackBST.put((Integer)cinco.darNumero(), cinco);
		redBlackBST.put((Integer)seis.darNumero(), seis);
		redBlackBST.put((Integer)siete.darNumero(), siete);
		redBlackBST.put((Integer)ocho.darNumero(), ocho);
		redBlackBST.put((Integer)nueve.darNumero(), nueve);
		redBlackBST.put((Integer)diez.darNumero(), diez);

	}

	@Test
	public void testConstructorYEmpty(){

		setUp1();
		assertEquals(true, redBlackBST.isEmpty());

	}

	@Test
	public void testMetodosDeAgregarYBuscar(){

		setUp1();
		setUp2();

		Numero aIgualar = new Numero("Uno", 1);
		assertEquals( aIgualar.darNombre(), redBlackBST.get(1).darNombre());
		assertEquals( 11, redBlackBST.size());
		assertEquals( 3, redBlackBST.height());
		assertEquals(true, redBlackBST.contains(1));
		Numero aIgualar2 = new Numero("Cero", 0);
		assertEquals((Integer)aIgualar2.darNumero(), redBlackBST.min());
		Numero aIgualar3 = new Numero("Diez", 10);
		assertEquals((Integer)aIgualar3.darNumero(), redBlackBST.max());

	}

	@Test
	public void testIteradores(){

		setUp1();
		setUp2();

		Iterator<Integer> iter = redBlackBST.keys();

		int conteo = 0;
		while(iter.hasNext()){

			Integer actual = iter.next();
			assertEquals((Integer)conteo, actual);
			conteo++;
		}

		Iterator<Numero> iter2 = redBlackBST.valuesInRange(4, 6);

		conteo = 4;
		while(iter2.hasNext()){

			Numero actual = iter2.next();
			assertEquals(conteo, actual.darNumero());
			conteo++;
		}

		Iterator<Integer> iter3 = redBlackBST.keysInRange(4, 6);

		conteo = 4;
		while(iter3.hasNext()){

			Integer numero = iter3.next();
			assertEquals((Integer)conteo, numero);
			conteo++;
		}

	}

}
