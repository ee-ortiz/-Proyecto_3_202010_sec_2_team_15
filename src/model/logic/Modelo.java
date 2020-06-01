package model.logic;

import model.data_structures.Arco;
import model.data_structures.Bag;
import model.data_structures.Comparendo;
import model.data_structures.EstacionesPolicia;
import model.data_structures.GrafoNoDirigido;
import model.data_structures.Haversine;
import model.data_structures.MaxHeapCP;
import model.data_structures.Pila;
import model.data_structures.Queue;
import model.data_structures.Vertice;
import model.data_structures.Comparendo.ComparadorXGravedad;
import sun.security.provider.certpath.Vertex;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Iterator;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.sun.javafx.geom.Arc2D;
import com.sun.org.apache.xpath.internal.axes.HasPositionalPredChecker;
import com.teamdev.jxmaps.Map;
import com.teamdev.jxmaps.ac;

import javafx.scene.shape.Arc;

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
	public final static String PATHGRAFO = "./data/GrafoCompleto.geojson";	
	public final static String PATH2 = "./data/estacionpolicia.geojson";
	private Maps mapa;
	private Queue<EstacionesPolicia> cola; // estaciones policia
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

				c.EPODESCRIP = c.EPODESCRIP.replaceAll("EstaciÃ³n de PolicÃ­a", "Estación de Policia");
				c.EPODESCRIP = c.EPODESCRIP.replaceAll("FontibÃ³", "Fontibó"); c.EPODESCRIP = c.EPODESCRIP.replaceAll("ariÃ±o", "Nariño");
				c.EPODESCRIP = c.EPODESCRIP.replaceAll("MartirÃ©s", "Mártires"); c.EPODESCRIP = c.EPODESCRIP.replaceAll("CristobÃ¡l", "Cristobal");
				c.EPODESCRIP = c.EPODESCRIP.replaceAll("CandelarÃ­a", "Candelaria"); c.EPODESCRIP = c.EPODESCRIP.replaceAll("BolÃ­var", "Bolivar");

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

	public Integer requerimientoParteInicial1(double latitud, double longitud){

		Integer rta = -1;

		double minima = 0;

		Iterator<Integer> iter = grafo.darVertices().keys();

		boolean iniciado = false;
		while(iter!= null && iter.hasNext()){

			Integer keyAct = iter.next();

			double lat = darLatitud(keyAct);
			double lon = darLongitud(keyAct);

			double haversiana = Haversine.distance(latitud, longitud, lat, lon);

			if(!iniciado){
				rta = keyAct;
				minima = haversiana;
				iniciado = true;
			}

			else{

				if(haversiana<minima){

					rta = keyAct;
					minima = haversiana;

				}
			}

		}

		return rta; // retorna el id del vertice ma cercano, si no hay vertices retorna -1;


	}

	public void requerimientoParteInicial2(){

		Iterator<Comparendo> iter = comps.iterator();

		while(iter!=null && iter.hasNext()){

			Comparendo actual = iter.next();

			double lat = actual.latitud;
			double lon = actual.longitud;

			Integer keyVerticeMasCercano = requerimientoParteInicial1(lat, lon);

			grafo.darVertice(keyVerticeMasCercano).darComparendos().add(actual);

		}

		System.out.println("Informacion añadida\n");

	}

	public void requerimientosParteInicial3(){

		Iterator<Integer> iter = grafo.darVertices().keys();

		while(iter!=null && iter.hasNext()){

			Integer actual = iter.next();

			Vertice<Integer, String> vertex = grafo.darVertice(actual);

			Iterator<Arco<Integer, String>> iter2 = vertex.darAdyacentes();

			while(iter2!=null && iter2.hasNext()){

				Arco<Integer, String> act = iter2.next();

				Vertice<Integer, String> destino = act.darDestino();

				int costo = vertex.darComparendos().size() + destino.darComparendos().size(); // total de comparendos entre los dos vertices

				act.cambiarCosto2(costo); 

			}
		}

		System.out.println("Informacion añadida\n");

	}

	public void requerimientosParteInicial4(){

		Iterator<EstacionesPolicia> iter = cola.iterator();

		while(iter!=null && iter.hasNext()){

			EstacionesPolicia actual = iter.next();
			double lat = actual.EPOLATITUD;
			double lon = actual.EPOLONGITU;

			Integer idVertexMasCercano = requerimientoParteInicial1(lat, lon);

			grafo.darVertice(idVertexMasCercano).darMasCercanas().add(actual);

		}

		System.out.println("Informacion añadida\n");

	}

	public void guardarGrafoCompleto(){

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
			String costoArcos2 = "";

			while(iter2 != null && iter2.hasNext()){

				Arco<Integer, String> ac = iter2.next();

				double costo = ac.darCosto();
				double costo2 = ac.darCosto2();
				Integer idDestino = ac.darDestino().darId();

				arcosDestino += idDestino + "/"	;	// id destino separados por /
				costoArcos += costo + "/"; // costos arcos separados por /
				costoArcos2 += costo2 + "/";

			}

			features1.addProperty("ArcosDestino", arcosDestino);
			features1.addProperty("CostoArcos", costoArcos);
			features1.addProperty("CostoArcos2", costoArcos2);

			Iterator<Comparendo> iter3 = grafo.darVertice(id).darComparendos().iterator();

			JsonArray comparendos = new JsonArray();

			while(iter3 != null && iter3.hasNext()){

				JsonObject comp = new JsonObject();
				Comparendo actual = iter3.next();

				double lat = actual.latitud;
				double lon = actual.longitud;
				String tipoServ = actual.TIPO_SERVI;
				String infraccion = actual.INFRACCION;
				comp.addProperty("Latitud", lat);
				comp.addProperty("Longitud", lon);
				comp.addProperty("tipoServ", tipoServ);
				comp.addProperty("infrac", infraccion);
				comparendos.add(comp);

			}

			features1.add("Comparendos", comparendos);

			Iterator<EstacionesPolicia> iter4 = grafo.darVertice(id).darMasCercanas().iterator();

			JsonArray estaciones = new JsonArray();

			while(iter4 != null && iter4.hasNext()){

				JsonObject est = new JsonObject();
				EstacionesPolicia actual = iter4.next();

				double lat = actual.EPOLATITUD;
				double lon = actual.EPOLONGITU;
				int objectid = actual.OBJECTID;
				String nombre = actual.EPODESCRIP;
				est.addProperty("Latitud", lat);
				est.addProperty("Longitud", lon);
				est.addProperty("ID", objectid);
				est.addProperty("nombre", nombre);
				estaciones.add(est);

			}

			features1.add("Estaciones", estaciones);

			features.add(features1);

		}

		try{

			FileWriter file = new FileWriter(PATHGRAFO);

			file.write(features.toString());
			file.flush();
			file.close();
		}

		catch(Exception e){

			e.printStackTrace();
		}

	}

	public void cargarGrafoCompleto(){

		grafo = null;
		grafo = new GrafoNoDirigido<Integer, String>();

		JsonReader reader;
		try {
			reader = new JsonReader(new FileReader(PATHGRAFO));
			JsonElement elem = JsonParser.parseReader(reader);
			JsonArray e2 = elem.getAsJsonArray();

			for (JsonElement e: e2)
			{
				Integer idVertex =(Integer) e.getAsJsonObject().get("IdVertex").getAsInt();

				String infoVertex = e.getAsJsonObject().get("infoVertex").getAsString();

				grafo.agregarVertice(idVertex, infoVertex);

				try{

					JsonArray cmps = e.getAsJsonObject().get("Comparendos").getAsJsonArray();
					int cantidadComps = cmps.size();

					for(int i = 0; i<cantidadComps; i++){

						Comparendo c = new Comparendo();

						c.latitud = cmps.get(i).getAsJsonObject().get("Latitud").getAsDouble();
						c.longitud = cmps.get(i).getAsJsonObject().get("Longitud").getAsDouble();
						c.TIPO_SERVI = cmps.get(i).getAsJsonObject().get("tipoServ").getAsString();
						c.INFRACCION = cmps.get(i).getAsJsonObject().get("infrac").getAsString();

						grafo.darVertice(idVertex).darComparendos().add(c);

					}



				}

				catch(Exception exc){

					exc.printStackTrace();

				}

				try{

					JsonArray est = e.getAsJsonObject().get("Estaciones").getAsJsonArray();
					int cantidadEst = est.size();

					for(int j = 0; j<cantidadEst; j++){

						EstacionesPolicia c1 = new EstacionesPolicia();

						c1.EPOLATITUD = est.get(j).getAsJsonObject().get("Latitud").getAsDouble();

						c1.EPOLATITUD = est.get(j).getAsJsonObject().get("Longitud").getAsDouble();

						c1.OBJECTID= est.get(j).getAsJsonObject().get("ID").getAsInt();

						c1.EPODESCRIP = est.get(j).getAsJsonObject().get("nombre").getAsString();

						grafo.darVertice(idVertex).darMasCercanas().add(c1);

					}

				}

				catch(Exception exc2){

					exc2.printStackTrace();

				}

			}

			for (JsonElement e: e2)
			{
				Integer idVertex =(Integer) e.getAsJsonObject().get("IdVertex").getAsInt();
				String[] ArcosDestino = e.getAsJsonObject().get("ArcosDestino").getAsString().split("/");
				String[] CostoArcos = e.getAsJsonObject().get("CostoArcos").getAsString().split("/");
				String[] CostoArcos2 = e.getAsJsonObject().get("CostoArcos2").getAsString().split("/");

				if(ArcosDestino.length!=0){

					for(int i = 0; i<ArcosDestino.length; i++){

						if(!ArcosDestino[i].equals("") && !CostoArcos[i].equals("") && !CostoArcos2[i].equals("")){
							Integer idDestino = (Integer) Integer.parseInt(ArcosDestino[i]);
							double costo = Double.parseDouble(CostoArcos[i]);
							double costo2 = Double.parseDouble(CostoArcos2[i]);

							grafo.agregarArco(idVertex, idDestino, costo);
							grafo.darArco(idVertex, idDestino).cambiarCosto2(costo2);
							grafo.darArco(idDestino, idVertex).cambiarCosto2(costo2);


						}

					}
				}

			}

			int vertices = grafo.V();
			int arcos = grafo.E();
			int numComparendos = 0;
			int numEstaciones = 0;

			Iterator<Integer> iterador = grafo.darVertices().keys();

			while(iterador != null && iterador.hasNext()){

				Integer actual = iterador.next();

				numComparendos += grafo.darVertice(actual).darComparendos().size();
				numEstaciones += grafo.darVertice(actual) .darMasCercanas().size();
			}

			System.out.println("Vertices = " + vertices);
			System.out.println("Arcos = " + arcos);
			System.out.println("Comparendos = " + numComparendos);
			System.out.println("Estaciones = " + numEstaciones);

		}

		catch (FileNotFoundException e) {

			e.printStackTrace();

		}

	}

	public void prueba(){

		int vertices = grafo.V();
		int arcos = grafo.E();
		int numComparendos = 0;
		int numEstaciones = 0;

		Iterator<Integer> iterador = grafo.darVertices().keys();

		while(iterador != null && iterador.hasNext()){

			Integer actual = iterador.next();

			numComparendos += grafo.darVertice(actual).darComparendos().size();
			numEstaciones += grafo.darVertice(actual).darMasCercanas().size();
		}

		System.out.println("Vertices = " + vertices);
		System.out.println("Arcos = " + arcos);
		System.out.println("Comparendos = " + numComparendos);
		System.out.println("Estaciones = " + numEstaciones);
	}

	public double darLatitud(Integer idVertex){

		Vertice<Integer, String> vertex = grafo.darVertice(idVertex);

		if(vertex !=null){

			String informacion = vertex.darInfo();

			String[] info = informacion.split(",");

			double latitud = Double.parseDouble(info[1]);

			return latitud;
		}

		else{

			return -10; // valor determinado para decir que una latitud es invalida dado que no es encontro el vertice buscado
		}

	}

	public double darLongitud(Integer idVertex){

		Vertice<Integer, String> vertex = grafo.darVertice(idVertex);

		if(vertex !=null){

			String informacion = vertex.darInfo();

			String[] info = informacion.split(",");

			double longitud = Double.parseDouble(info[0]);

			return longitud;
		}

		else{

			return -10; // valor determinado para decir que una longitud es invalida dado que no es encontro el vertice buscado
		}

	}

	public void requerimiento1A(double latInicial, double lonInicial, double latFinal, double lonFinal){

		// como  es camino de costo minimo tengo que hacer dijkstra sobre el el vertice inicial

		Integer idInicial = requerimientoParteInicial1(latInicial, lonInicial);

		Integer idFinal = requerimientoParteInicial1(latFinal, lonFinal);

		grafo.Dijkstra(idInicial);

		Pila<Vertice<Integer, String>> pila = new Pila<>(20);

		Vertice<Integer, String> actual = grafo.darVertice(idFinal);

		Arco<Integer, String> llegada = actual.darArcoLlegada();

		if(llegada != null){

			while(actual.darId() != idInicial){

				pila.push(actual);
				actual = actual.darArcoLlegada().darOrigen();
			}

			// cuando termine el while actual sera igual al vertice inicial

			pila.push(actual);

			Queue<Vertice<Integer, String>> camino = new Queue<>();


			System.out.println("Total de vertices que hay en el camino es: " + pila.consultarTamano());
			System.out.println("\nEl camino a seguir es el siguiente: \n");
			System.out.println("Vertices (En orden: id,longitud,latitud - Costo):");

			double costoAcumulado = 0;

			while(!pila.estaVacia()){

				Vertice<Integer, String> tope = pila.pop();
				camino.enqueue(tope);

				Arco<Integer, String> llega = tope.darArcoLlegada();

				double costoMinimo = 0;

				if(llega!=null){

					costoMinimo = llega.darCosto();
				}

				System.out.println("- " + tope.darId()+"," + tope.darInfo() + " -Costo: " + costoMinimo);

				costoAcumulado += costoMinimo;

			}

			System.out.println();
			System.out.println("Distancia estimada : " + costoAcumulado);

			mapa = new Maps("Requerimiento 1A", camino, null);
			mapa.visuaLizarGrafo1A();
			mapa.initFrame();

			grafo.limpiar(); // se desmarcan los vertices para que los otros requerimientos  se puedan realizar correctamente



		}

		else{

			System.out.println();
			System.out.println("No existe un camino entre los los vertices dados");

		}

		System.out.println();


		// ahora falta pintar en el mapa


	}

	public void requerimiento2A(int M){

		/*Determinar la red de comunicaciones que soporte la instalación de cámaras de video
		en los M puntos donde se presentan los comparendos de mayor gravedad.
		Se debe ingresar el número M de comparendos que se requieren.
		El distrito quiere instalar una red de comunicaciones que le permita la instalación de
		cámaras de video en M sitios; sin embargo, se requiere que esta red tenga el menor 
		costo de instalación posible. El costo de instalación de la red es de U$10000 por cada
		kilómetro extendido.
		Con la finalidad de que la red sea eficiente se seleccionaron como puntos de supervisión
		los M vértices donde se presentan los comparendos de mayor gravedad. Para saber si
		un comparendo es más grave que otro primero se mira el tipo de servicio: Público es
		más grave que Oficial y Oficial es más grave que Particular; si dos comparendos tienen
		el mismo tipo de servicio se compara el código de la infracción (campo INFRACCION)
		usando el orden lexicográfico (forma de comparación de los Strings en Java, A12 es más
		grave que A11 y B10 es más grave que A10).*/

		// primero se iteran los comparendos del grafo para agregarlos a una cola de prioridad por gravedadd

		Iterator<Integer> iter = grafo.darVertices().keys();

		ComparadorXGravedad comparar = new ComparadorXGravedad();

		MaxHeapCP<Comparendo> gravedad = new MaxHeapCP<>();

		while(iter!=null && iter.hasNext()){

			Integer actual = iter.next();

			Vertice<Integer, String> act = grafo.darVertice(actual);


			Iterator<Comparendo> iter2 = act.darComparendos().iterator();

			while(iter2!=null && iter2.hasNext()){

				Comparendo current = iter2.next();

				gravedad.insert(current, comparar); 

			}

		}

		// ahora un iterator por los M comparendos mas graves para  hacer la red de comunicaciones

		Iterator<Comparendo> iter3 = gravedad.iterator(comparar);

		int conteo = 0;
		while(iter3!=null && conteo<M &&  iter3.hasNext()){


			Comparendo actual = iter3.next();

			double lat = actual.latitud;
			double  lon = actual.longitud;

			Integer id = requerimientoParteInicial1(lat, lon);

			grafo.MST(id);

			conteo++;

		}	

		// al final tendré todos M comparendos mas graves con MST en sus vertices


		// ahora hay que agregar todos los arcos correspondientes a caminos entre los vertices de los comparendos mas graves

		Queue<Arco<Integer, String>> req2A = new Queue<>();

		Iterator<Comparendo> iter4 = gravedad.iterator(comparar); 

		conteo = 0;
		while(iter4!= null && conteo <M && iter4.hasNext()){

			Comparendo actual = iter4.next();

			double lat = actual.latitud;
			double  lon = actual.longitud;

			Integer id = requerimientoParteInicial1(lat, lon); // id a vertice del comparendo

			Vertice<Integer, String> current = grafo.darVertice(id);

			Arco<Integer, String> anterior = current.darArcoLlegada();

			while(anterior!=null){

				if(!anterior.darMarca()){

					anterior.marcar();
					req2A.enqueue(anterior);
					anterior = anterior.darOrigen().darArcoLlegada();

				}


			}

			conteo++;

			// en este punto ya tengo una cola con todos los arcos de el arbol de costo minimo del los comparendos mas graves, lo muestro en consola

		}


		System.out.println("\nTotal de vertices: " + (req2A.size() + 1));

		System.out.println("Informacion: idVertice Inicial - idVertice final:");

		double costoTotal = 0;

		Iterator<Arco<Integer, String>> iter5 = req2A.iterator();

		while(iter5!=null && iter5.hasNext()){

			Arco<Integer, String> act = iter5.next();
			act.desmarcar();
			System.out.println(act.darOrigen().darId() + " - " + act.darDestino().darId());
			costoTotal += act.darCosto();

		}

		System.out.println("\nCostoTotal: U$" + costoTotal*10000);

		// ahora mostrar el mapa

		mapa = new Maps("Requerimiento 1A", null, req2A);
		mapa.visuaLizarGrafo2A();
		mapa.initFrame();

		grafo.limpiar();


	}

}
