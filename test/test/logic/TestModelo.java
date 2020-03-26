package test.logic;

import static org.junit.Assert.*;

import model.data_structures.Comparendo;
import model.logic.Modelo;

import org.junit.Before;
import org.junit.Test;

public class TestModelo {

	private Modelo modelo;
	public static String PATH = "./data/comparendos_dei_2018_small.geojson";
	public static String PATH2 = "./data/comparendos_dei_2018.geojson";

	@Before
	public void setUp1() {

		modelo = new Modelo();

	}


	public void setUp2() {


	}



	@Test
	public void testComprobarCargaYAgregar() {



	}


}
