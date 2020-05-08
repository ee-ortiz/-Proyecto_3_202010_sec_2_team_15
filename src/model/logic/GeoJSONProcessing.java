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
import model.data_structures.RedBlackBST;
import model.data_structures.SeparateChaining;



public class GeoJSONProcessing {

	private int valorMinimoObjectID;
	private int valorMaximoObjectID;
	private int tamano;

	// Solucion de carga de datos publicada al curso Estructuras de Datos 2020-10
	public void cargarDatos(RedBlackBST<Integer, Comparendo> comps, String direccion){

		JsonReader reader;
		try {
			reader = new JsonReader(new FileReader(direccion));
			JsonElement elem = JsonParser.parseReader(reader);
			JsonArray e2 = elem.getAsJsonObject().get("features").getAsJsonArray();

			SimpleDateFormat parser=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");


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

				if(c.TIPO_SERVI.equals("Oficial")){
					c.TIPO_SERVI = "Oficial   ";
				}
				c.INFRACCION = e.getAsJsonObject().get("properties").getAsJsonObject().get("INFRACCION").getAsString();
				c.DES_INFRAC = e.getAsJsonObject().get("properties").getAsJsonObject().get("DES_INFRACCION").getAsString();	
				c.LOCALIDAD = e.getAsJsonObject().get("properties").getAsJsonObject().get("LOCALIDAD").getAsString();
				c.MUNICIPIO = e.getAsJsonObject().get("properties").getAsJsonObject().get("MUNICIPIO").getAsString();

				c.longitud = e.getAsJsonObject().get("geometry").getAsJsonObject().get("coordinates").getAsJsonArray()
						.get(0).getAsDouble();

				c.latitud = e.getAsJsonObject().get("geometry").getAsJsonObject().get("coordinates").getAsJsonArray()
						.get(1).getAsDouble();


				comps.put(c.OBJECTID, c);


			}

			valorMaximoObjectID = comps.max();
			valorMinimoObjectID = comps.min();
			tamano = comps.size();


		} 
		catch (FileNotFoundException | ParseException e) {

			e.printStackTrace();

		}

	}

	public String convertirIntAString(int n){

		return Integer.toString(n);

	}

	public void retornarRequerimientoCargar(){

		System.out.println("El total de comparendos leidos es: " + tamano);
		System.out.println("El valor mínimo de ObjectID leido es: " + valorMinimoObjectID);
		System.out.println("El valor máximo de ObjectID leido es: " + valorMaximoObjectID);

	}

}