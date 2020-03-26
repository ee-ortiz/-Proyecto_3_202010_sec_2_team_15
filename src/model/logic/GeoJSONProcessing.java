package model.logic;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import model.data_structures.Cola;
import model.data_structures.Comparendo;
import model.data_structures.IArregloDinamico;
import model.data_structures.ICola;
import model.data_structures.IPila;
import model.data_structures.LinearProbing;
import model.data_structures.Pila;
import model.data_structures.SeparateChaining;



public class GeoJSONProcessing {

	private Comparendo primerComparendo;
	private Comparendo ultimoComparendo;
	int tamaño=0;
	// Solucion de carga de datos publicada al curso Estructuras de Datos 2020-10
	public void cargarDatos(SeparateChaining<String, Comparendo> hashTable1, String direccion){

		JsonReader reader;
		try {
			reader = new JsonReader(new FileReader(direccion));
			JsonElement elem = JsonParser.parseReader(reader);
			JsonArray e2 = elem.getAsJsonObject().get("features").getAsJsonArray();

			SimpleDateFormat parser=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

			Comparendo primero = null;
			Comparendo ultimo = null;
			boolean compPrimero = false;

			for(JsonElement e: e2) {
				Comparendo c = new Comparendo();
				c.OBJECTID = e.getAsJsonObject().get("properties").getAsJsonObject().get("OBJECTID").getAsInt();

				String s = e.getAsJsonObject().get("properties").getAsJsonObject().get("FECHA_HORA").getAsString();	
				c.FECHA_HORA = parser.parse(s);

				c.MEDIO_DETE = e.getAsJsonObject().get("properties").getAsJsonObject().get("MEDIO_DETECCION").getAsString();
				c.CLASE_VEHI = e.getAsJsonObject().get("properties").getAsJsonObject().get("CLASE_VEHICULO").getAsString();
				if(c.CLASE_VEHI.equals("CAMIÃ“N")){
					c.CLASE_VEHI = "CAMIÓN";
				}
				if(c.CLASE_VEHI.equals("AUTOMÃ“VIL")){
					c.CLASE_VEHI = "AUTOMÓVIL";
				}

				c.TIPO_SERVI = e.getAsJsonObject().get("properties").getAsJsonObject().get("TIPO_SERVICIO").getAsString();
				if(c.TIPO_SERVI.equals("PÃºblico")){
					c.TIPO_SERVI = "Público   ";
				}
				c.INFRACCION = e.getAsJsonObject().get("properties").getAsJsonObject().get("INFRACCION").getAsString();
				c.DES_INFRAC = e.getAsJsonObject().get("properties").getAsJsonObject().get("DES_INFRACCION").getAsString();	
				c.LOCALIDAD = e.getAsJsonObject().get("properties").getAsJsonObject().get("LOCALIDAD").getAsString();
				c.MUNICIPIO = e.getAsJsonObject().get("properties").getAsJsonObject().get("MUNICIPIO").getAsString();

				c.longitud = e.getAsJsonObject().get("geometry").getAsJsonObject().get("coordinates").getAsJsonArray()
						.get(0).getAsDouble();

				c.latitud = e.getAsJsonObject().get("geometry").getAsJsonObject().get("coordinates").getAsJsonArray()
						.get(1).getAsDouble();


				Calendar fecha = Calendar.getInstance();
				fecha.setTime(c.FECHA_HORA);
				int ano = fecha.get(Calendar.YEAR);
				int mes = fecha.get(Calendar.MONTH) + 1;
				int dia = fecha.get(Calendar.DAY_OF_MONTH);

				String mes1 = convertirIntAString(mes);
				String dia1 = convertirIntAString(dia);

				if(mes1.length() ==1){
					mes1 = "0" + mes1;
				}

				if(dia1.length() ==1){
					dia1 = "0" + dia1;
				}


				//(FECHA (año/mes/día),CLASE_VEHICULO, INFRACCION
				String key = convertirIntAString(ano) + mes1 + dia1 +  c.CLASE_VEHI.trim() + c.INFRACCION.trim();

				hashTable1.putInSet(key, c);

				ultimo = c;

				if(compPrimero == false){
					primero = c;
					compPrimero = true;				
				}

			}

			primerComparendo = primero;
			ultimoComparendo = ultimo;


		} 
		catch (FileNotFoundException | ParseException e) {

			e.printStackTrace();

		}

	}
	public void cargarDatos(LinearProbing<String, Comparendo> hashTable1, String direccion){

		JsonReader reader;
		try {
			reader = new JsonReader(new FileReader(direccion));
			JsonElement elem = JsonParser.parseReader(reader);
			JsonArray e2 = elem.getAsJsonObject().get("features").getAsJsonArray();

			SimpleDateFormat parser=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

			Comparendo primero = null;
			Comparendo ultimo = null;
			boolean compPrimero = false;

			for(JsonElement e: e2) {
				Comparendo c = new Comparendo();
				c.OBJECTID = e.getAsJsonObject().get("properties").getAsJsonObject().get("OBJECTID").getAsInt();

				String s = e.getAsJsonObject().get("properties").getAsJsonObject().get("FECHA_HORA").getAsString();	
				c.FECHA_HORA = parser.parse(s);

				c.MEDIO_DETE = e.getAsJsonObject().get("properties").getAsJsonObject().get("MEDIO_DETECCION").getAsString();
				c.CLASE_VEHI = e.getAsJsonObject().get("properties").getAsJsonObject().get("CLASE_VEHICULO").getAsString();
				if(c.CLASE_VEHI.equals("CAMIÃ“N")){
					c.CLASE_VEHI = "CAMIÓN";
				}
				if(c.CLASE_VEHI.equals("AUTOMÃ“VIL")){
					c.CLASE_VEHI = "AUTOMÓVIL";
				}

				c.TIPO_SERVI = e.getAsJsonObject().get("properties").getAsJsonObject().get("TIPO_SERVICIO").getAsString();
				if(c.TIPO_SERVI.equals("PÃºblico")){
					c.TIPO_SERVI = "Público   ";
				}
				c.INFRACCION = e.getAsJsonObject().get("properties").getAsJsonObject().get("INFRACCION").getAsString();
				c.DES_INFRAC = e.getAsJsonObject().get("properties").getAsJsonObject().get("DES_INFRACCION").getAsString();	
				c.LOCALIDAD = e.getAsJsonObject().get("properties").getAsJsonObject().get("LOCALIDAD").getAsString();
				c.MUNICIPIO = e.getAsJsonObject().get("properties").getAsJsonObject().get("MUNICIPIO").getAsString();

				c.longitud = e.getAsJsonObject().get("geometry").getAsJsonObject().get("coordinates").getAsJsonArray()
						.get(0).getAsDouble();

				c.latitud = e.getAsJsonObject().get("geometry").getAsJsonObject().get("coordinates").getAsJsonArray()
						.get(1).getAsDouble();


				Calendar fecha = Calendar.getInstance();
				fecha.setTime(c.FECHA_HORA);
				int ano = fecha.get(Calendar.YEAR);
				int mes = fecha.get(Calendar.MONTH) + 1;
				int dia = fecha.get(Calendar.DAY_OF_MONTH);

				String mes1 = convertirIntAString(mes);
				String dia1 = convertirIntAString(dia);

				if(mes1.length() ==1){
					mes1 = "0" + mes1;
				}

				if(dia1.length() ==1){
					dia1 = "0" + dia1;
				}


				//(FECHA (año/mes/día),CLASE_VEHICULO, INFRACCION
				String key = convertirIntAString(ano) + mes1 + dia1 +  c.CLASE_VEHI.trim() + c.INFRACCION.trim();

				hashTable1.put(key, c);

				ultimo = c;

				if(compPrimero == false){
					primero = c;
					compPrimero = true;				
				}

			}

			primerComparendo = primero;
			ultimoComparendo = ultimo;


		} 
		catch (FileNotFoundException | ParseException e) {

			e.printStackTrace();

		}

	}

	public String convertirIntAString(int n){

		return Integer.toString(n);

	}

	public void retornarPrimerYUltimoComparendo(){

		System.out.println("El primer comparendo leido es: " + primerComparendo.retornarDatos());
		System.out.println("El ultimo comparendo leido es: " + ultimoComparendo.retornarDatos());

	}

}