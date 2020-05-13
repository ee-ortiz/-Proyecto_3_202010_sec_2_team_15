package model.logic;

import model.data_structures.Arco;
import model.data_structures.Comparendo;
import model.data_structures.EstacionesPolicia;
import model.data_structures.GrafoNoDirigido;

import model.data_structures.Queue;
import model.data_structures.Vertice;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Iterator;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

/**
 * Definicion del modelo del mundo
 *
 */
public class Modelo {
	/**
	 * Atributos del modelo del mundo
	 */
	private Queue<Comparendo> comps;
	private GeoJSONProcessing objetoJsonGson;
	private CargaVerticesYArcos carga;
	private GrafoNoDirigido<Integer, String> grafo;
	public final static String PATH = "./data/GrafoJSON.geojson";
	public final static String PATH2 = "./data/estacionpolicia.geojson";
	private Maps mapa;
	private Queue<EstacionesPolicia> cola;
	public static final int maximoNumeroDatos = 20;

	/**
	 * Constructor del modelo del mundo con capacidad predefinida
	 */
	public Modelo()
	{
		cola = new Queue<>();
		comps = new Queue<>();
		objetoJsonGson = new GeoJSONProcessing();
		grafo = new GrafoNoDirigido<>();
		carga = new CargaVerticesYArcos();
	}

	//cantidad de comparendos cargados
	public int darTamano(){

		return comps.size();
	}

	// solo es permitido leer una vez los archivos
	public void cargarTodo(String comparendos){

		cargar(comparendos); 		// comparendos 2018
		objetoJsonGson.retornarRequerimientoCargar();
		cargarEstacionesPolicia();	// archivo estaciones policia
		abrirGrafoJSON(this.grafo); // abre (carga) grafo json creado 


	}


	public void cargar(String direccion){

		objetoJsonGson.cargarDatos(comps, direccion);

	}

	public void cargarGrafo(String direccion, String direccion2){

		carga.cargarInformacion(grafo, direccion, direccion2);
	}


	public String RetornarDatos(Comparendo comp){
		//INFRACCION, OBJECTID,
		//FECHA_HORA, CLASE_VEHI, TIPO_SERVI, LOCALIDAD.
		String rta = "Codigo de infraccion: "+comp.INFRACCION +" ObjectID: " + comp.OBJECTID + " Fecha y hora: " + comp.FECHA_HORA + " Clase de vehiculo "+comp.CLASE_VEHI + " Tipo de servicio: " +
				comp.TIPO_SERVI + " Localidad: "+ comp.LOCALIDAD;
		return rta;
	}

	public Queue<Comparendo> darColaComparendos(){

		return comps;
	}

	public GeoJSONProcessing darObjetoJsonGson(){

		return objetoJsonGson;
	}

	public void abrirGrafoJSON(GrafoNoDirigido<Integer, String> pGrafo){

		JsonReader reader;
		try {
			reader = new JsonReader(new FileReader(PATH));
			JsonElement elem = JsonParser.parseReader(reader);
			JsonArray e2 = elem.getAsJsonArray();

			Vertice<Integer, String> mayor = null;
			Integer idMayor = -1;
			double latitud = 0;
			double longitud = 0;

			for (JsonElement e: e2)
			{
				Integer idVertex =(Integer) e.getAsJsonObject().get("IdVertex").getAsInt();

				String infoVertex = e.getAsJsonObject().get("infoVertex").getAsString();

				pGrafo.agregarVertice(idVertex, infoVertex);

				if(idVertex > idMayor){

					mayor = pGrafo.darVertice(idVertex);
					idMayor = idVertex;
					String info = mayor.darInfo(); 			// longitud,latitud
					String[] informacion = info.split(",");
					longitud = Double.parseDouble(informacion[0]);
					latitud = Double.parseDouble(informacion[1]);

				}

			}

			for (JsonElement e: e2)
			{
				Integer idVertex =(Integer) e.getAsJsonObject().get("IdVertex").getAsInt();
				String[] ArcosDestino = e.getAsJsonObject().get("ArcosDestino").getAsString().split("/");
				String[] CostoArcos = e.getAsJsonObject().get("CostoArcos").getAsString().split("/");

				if(ArcosDestino.length!=0){

					for(int i = 0; i<ArcosDestino.length; i++){

						if(!ArcosDestino[i].equals("") && !CostoArcos[i].equals("")){
							Integer idDestino = (Integer) Integer.parseInt(ArcosDestino[i]);
							double costo = Double.parseDouble(CostoArcos[i]);

							pGrafo.agregarArco(idVertex, idDestino, costo);
						}

					}
				}

			}

			System.out.println("El total de vertices que hay en la malla vial de Bogota es: " + pGrafo.V());
			System.out.println("El vertice con el mayor ID encontrado es:\n-" + "ID: " + mayor.darId() +
					" Latitud: " + latitud + " Longitud: " + longitud);
			System.out.println("El total de arcos de la maya vial de Bogota es: " + pGrafo.E());
			System.out.println("La informacion de los arcos del vertice con mayor ID encontrado es:");

			Iterator<Arco<Integer, String>> iter = mayor.darAdyacentes();

			if(mayor.adjs().size()>20){
				System.out.println("Se muestran los primeros 20:");
			}

			int conteo = 0;
			while(iter!= null && iter.hasNext() && conteo < maximoNumeroDatos){

				Arco<Integer, String> actual = iter.next();
				Integer idOrigen = actual.darOrigen().darId();
				Integer idDestino = actual.darDestino().darId();
				conteo++;

				System.out.println("-ID Origen: " + idOrigen + " ID Destino: " + idDestino);
			}

			System.out.println();


		}

		catch (FileNotFoundException e) {

			e.printStackTrace();

		}

	}

	public void convertirAJSON(){

		if(grafo.V() == 0){
			return;
		}

		JsonArray features = new JsonArray();

		Iterator<Integer> iter = grafo.darVertices().keys();

		while(iter!= null && iter.hasNext()){

			JsonObject features1 = new JsonObject();

			Integer id = iter.next();

			features1.addProperty("IdVertex", id);;


			String informacion = grafo.darVertice(id).darInfo();

			features1.addProperty("infoVertex", informacion);

			Iterator<Arco<Integer, String>> iter2 = grafo.darVertice(id).darAdyacentes();

			String arcosDestino = "";
			String costoArcos = "";

			while(iter2 != null && iter2.hasNext()){

				Arco<Integer, String> ac = iter2.next();

				double costo = ac.darCosto();
				Integer idDestino = ac.darDestino().darId();
				arcosDestino += idDestino + "/"	;	// id destino separados por /
				costoArcos += costo + "/"; // costos arcos separados por /

			}

			features1.addProperty("ArcosDestino", arcosDestino);
			features1.addProperty("CostoArcos", costoArcos);

			features.add(features1);

		}

		try{

			FileWriter file = new FileWriter(PATH);

			file.write(features.toString());
			file.flush();
			file.close();
		}

		catch(Exception e){

			e.printStackTrace();
		}

	}

	public void mostrarMapa(){

		mapa = new Maps("Grafo Bogot·", grafo, cola);
		mapa.initFrame();

	}

	public void cargarEstacionesPolicia(){

		JsonReader reader;
		try {
			reader = new JsonReader(new FileReader(PATH2));
			JsonElement elem = JsonParser.parseReader(reader);
			JsonArray e2 = elem.getAsJsonObject().get("features").getAsJsonArray();


			EstacionesPolicia mayor = null;
			int mayorObjectID = -1;

			for(JsonElement e: e2) {
				EstacionesPolicia c = new EstacionesPolicia();

				c.OBJECTID = e.getAsJsonObject().get("properties").getAsJsonObject().get("OBJECTID").getAsInt();

				if(c.OBJECTID > mayorObjectID){
					mayorObjectID = c.OBJECTID;
					mayor = c;

				}

				c.EPODESCRIP = e.getAsJsonObject().get("properties").getAsJsonObject().get("EPODESCRIP").getAsString();

				c.EPODESCRIP = c.EPODESCRIP.replaceAll("Estaci√≥n de Polic√≠a", "EstaciÛn de Policia");
				c.EPODESCRIP = c.EPODESCRIP.replaceAll("Fontib√≥", "FontibÛ"); c.EPODESCRIP = c.EPODESCRIP.replaceAll("ari√±o", "NariÒo");
				c.EPODESCRIP = c.EPODESCRIP.replaceAll("Martir√©s", "M·rtires"); c.EPODESCRIP = c.EPODESCRIP.replaceAll("Cristob√°l", "Cristobal");
				c.EPODESCRIP = c.EPODESCRIP.replaceAll("Candelar√≠a", "Candelaria"); c.EPODESCRIP = c.EPODESCRIP.replaceAll("Bol√≠var", "Bolivar");

				c.EPODIR_SITIO = e.getAsJsonObject().get("properties").getAsJsonObject().get("EPODIR_SITIO").getAsString();

				c.EPOLATITUD = e.getAsJsonObject().get("properties").getAsJsonObject().get("EPOLATITUD").getAsDouble();

				c.EPOLONGITU = e.getAsJsonObject().get("properties").getAsJsonObject().get("EPOLONGITU").getAsDouble();

				c.EPOSERVICIO = e.getAsJsonObject().get("properties").getAsJsonObject().get("EPOSERVICIO").getAsString();

				c.EPOHORARIO = e.getAsJsonObject().get("properties").getAsJsonObject().get("EPOHORARIO").getAsString();

				c.EPOTELEFON = e.getAsJsonObject().get("properties").getAsJsonObject().get("EPOTELEFON").getAsString();

				c.EPOIULOCAL = e.getAsJsonObject().get("properties").getAsJsonObject().get("EPOIULOCAL").getAsString();

				cola.enqueue(c);

			}

			System.out.println("El total de estaciones de policia es: " + cola.size());

			System.out.println("La estacion de policia con el mayor ObjectID encontrado es:" + "\n-" + mayor.retornarInformacionCargar() + "\n");


		} 
		catch (FileNotFoundException e) {

			e.printStackTrace();

		}


	}
}

