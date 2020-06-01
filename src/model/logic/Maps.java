package model.logic;

import java.awt.BorderLayout;
import java.util.Iterator;

import javax.swing.JFrame;

import model.data_structures.LinearProbing;
import model.data_structures.Pila;
import model.data_structures.Queue;
import model.data_structures.Vertice;
import model.data_structures.GrafoNoDirigido;

import com.teamdev.jxmaps.Circle;
import com.teamdev.jxmaps.CircleOptions;
import com.teamdev.jxmaps.ControlPosition;
import com.teamdev.jxmaps.InfoWindow;
//import com.teamdev.jxmaps.InfoWindowOptions;
import com.teamdev.jxmaps.LatLng;
import com.teamdev.jxmaps.LatLngBounds;
import com.teamdev.jxmaps.Map;
import com.teamdev.jxmaps.MapOptions;
import com.teamdev.jxmaps.MapReadyHandler;
import com.teamdev.jxmaps.MapStatus;
import com.teamdev.jxmaps.MapTypeControlOptions;
import com.teamdev.jxmaps.Marker;
import com.teamdev.jxmaps.Polyline;
import com.teamdev.jxmaps.PolylineOptions;
import com.teamdev.jxmaps.swing.MapView;

import model.data_structures.Arco;
import model.data_structures.EstacionesPolicia;


public class Maps extends MapView {

	// Objeto Google Maps
	private Map map;

	// Identificador del requerimiento a visualizar
	private String idRequerimiento;

	private GrafoNoDirigido<Integer, String> GrafoMapa;

	private Queue<Arco<Integer, String>> R2A;

	private Pila<Vertice<Integer, String>> pila;

	private Queue<Vertice<Integer, String>> req1A;

	/**
	 * Visualizacion Google map con camino, marcas, circulos y texto de localizacion
	 * @param idReq
	 */
	public Maps(String idReq, Queue<Vertice<Integer, String>> R1A, Queue<Arco<Integer, String>> R2A)
	{	

		GrafoMapa = new GrafoNoDirigido<>();
		idRequerimiento = idReq;
		req1A = R1A;
		this.R2A = R2A;


	}

	public void initMap(Map map)
	{
		MapOptions mapOptions = new MapOptions();
		MapTypeControlOptions controlOptions = new MapTypeControlOptions();
		controlOptions.setPosition(ControlPosition.BOTTOM_LEFT);
		mapOptions.setMapTypeControlOptions(controlOptions);

		map.setOptions(mapOptions);

		LatLng[] limites = {new LatLng(4.597714,-74.094723), new LatLng(4.621360, -74.062707),};

		LatLngBounds limits = new LatLngBounds(limites[0], limites[1]);

		map.fitBounds(limits);

	}

	public void initFrame()
	{
		JFrame frame = new JFrame(idRequerimiento);
		frame.setSize(600, 600);
		frame.add(this, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

	}

	public void cargarGrafoMapa(GrafoNoDirigido<Integer, String> pGrafo){


		// Longitud min= -74.094723, Latitud min= 4.597714 - Longitud max= -74.062707, Latitud max= 4.621360. 
		double longitudMin = -74.094723; double latitudMin = 4.597714;
		double longitudMax = -74.062707; double latitudMax = 4.621360;

		LinearProbing<Integer, Vertice<Integer, String>> vertices = pGrafo.darVertices();

		Iterator<Integer> iter = vertices.keys();

		while(iter != null && iter.hasNext()){

			Integer actual = iter.next();
			Vertice<Integer, String> act = vertices.get(actual).get(0);
			String info = act.darInfo(); // longitud, y latitud separadas por una coma 
			String[] informacion = info.split(",");
			double longitud = Double.parseDouble(informacion[0]);
			double latitud = Double.parseDouble(informacion[1]);

			// si el vertice se encuentra dentro de los limites delimitados
			if((latitud>= latitudMin && latitud <= latitudMax) && (longitud>= longitudMin && longitud <= longitudMax)){

				GrafoMapa.agregarVertice(actual, info);
			}

		}

		Iterator<Integer> iter2 = vertices.keys();

		while(iter2!=null && iter2.hasNext()){

			Integer actual = iter2.next();
			Vertice<Integer, String> act = vertices.get(actual).get(0);

			Iterator<Arco<Integer, String>> adyacentes = act.darAdyacentes();

			while(adyacentes!=null && adyacentes.hasNext()){

				Arco<Integer, String> current = adyacentes.next();
				Integer idDestino = current.darDestino().darId();

				if(GrafoMapa.darVertice(actual) !=null && GrafoMapa.darVertice(idDestino)!=null){

					GrafoMapa.agregarArco(actual, idDestino, current.darCosto());

				}
			}

		}

	}

	public void visuaLizarGrafo1A(){

		setOnMapReadyHandler( new MapReadyHandler() {
			@Override
			public void onMapReady(MapStatus status)
			{

				if ( status == MapStatus.MAP_STATUS_OK )
				{
					map = getMap();



					//Configuracion circulos
					CircleOptions vertexLocOpt= new CircleOptions(); 
					vertexLocOpt.setFillColor("#00FF00");  // color de relleno
					vertexLocOpt.setFillOpacity(0.5);
					vertexLocOpt.setStrokeWeight(1.0);

					//Configuracion de la linea del camino
					PolylineOptions pathOpt = new PolylineOptions();
					pathOpt.setStrokeColor("#0000FF");	  // color de linea	
					pathOpt.setStrokeOpacity(1.75);
					pathOpt.setStrokeWeight(1.5);
					pathOpt.setGeodesic(false);


					Iterator<Vertice<Integer, String>> iter = req1A.iterator();


					while(iter!= null && iter.hasNext()){

						Vertice<Integer, String> act = iter.next();

						String info = act.darInfo(); 

						String[] informacion = info.split(",");
						double latitud = Double.parseDouble(informacion[1]);
						double longitud = Double.parseDouble(informacion[0]);

						LatLng nuevo = new LatLng(latitud,longitud);

						Circle startLoc = new Circle(map);
						startLoc.setOptions(vertexLocOpt);
						startLoc.setCenter(nuevo); 
						startLoc.setRadius(13); //Radio del circulo

					}

					while(!req1A.isEmpty()){

						Vertice<Integer, String> act = req1A.dequeue();

						String info = act.darInfo(); 

						String[] informacion = info.split(",");
						double latitud = Double.parseDouble(informacion[1]);
						double longitud = Double.parseDouble(informacion[0]);

						LatLng nuevo = new LatLng(latitud,longitud);

						if(!req1A.isEmpty()){

							Vertice<Integer, String> sig = req1A.peek();

							String info2 = sig.darInfo(); 

							String[] informacion2 = info2.split(",");
							double latitud2 = Double.parseDouble(informacion2[1]);
							double longitud2 = Double.parseDouble(informacion2[0]);

							LatLng nuevo2 = new LatLng(latitud2,longitud2);		

							LatLng[] arco = new LatLng[2];

							arco[0] = nuevo;
							arco[1] = nuevo2;

							Polyline path = new Polyline(map); 														
							path.setOptions(pathOpt); 
							path.setPath(arco);

						}




					}

					initMap( map );
				}
			}

		} );


	}

	public void agregarEstaciones(Queue<EstacionesPolicia> pCola){

		double longitudMin = -74.094723; double latitudMin = 4.597714;
		double longitudMax = -74.062707; double latitudMax = 4.621360;

		//Configuracion circulos
		CircleOptions vertexLocOpt= new CircleOptions(); 
		vertexLocOpt.setFillColor("#FF0000");  // color de relleno
		vertexLocOpt.setFillOpacity(0.5);
		vertexLocOpt.setStrokeWeight(1.0);

		Iterator<EstacionesPolicia> iter = pCola.iterator();

		int conteo = 0;
		while(iter!= null && iter.hasNext()){

			EstacionesPolicia actual = iter.next();

			if((actual.EPOLATITUD>= latitudMin && actual.EPOLATITUD <= latitudMax) && (actual.EPOLONGITU>= longitudMin && actual.EPOLONGITU <= longitudMax)){

				LatLng nuevo = new LatLng(actual.EPOLATITUD,actual.EPOLONGITU);

				Circle startLoc = new Circle(map);
				startLoc.setOptions(vertexLocOpt);
				startLoc.setCenter(nuevo); 
				startLoc.setRadius(4); //Radio del circulo
				conteo++;



			}

		}

		System.out.println(conteo);

	}

	public void visuaLizarGrafo2A() {

		setOnMapReadyHandler( new MapReadyHandler() {
			@Override
			public void onMapReady(MapStatus status)
			{

				if ( status == MapStatus.MAP_STATUS_OK )
				{
					map = getMap();



					//Configuracion circulos
					CircleOptions vertexLocOpt= new CircleOptions(); 
					vertexLocOpt.setFillColor("#00FF00");  // color de relleno
					vertexLocOpt.setFillOpacity(0.5);
					vertexLocOpt.setStrokeWeight(1.0);

					//Configuracion de la linea del camino
					PolylineOptions pathOpt = new PolylineOptions();
					pathOpt.setStrokeColor("#0000FF");	  // color de linea	
					pathOpt.setStrokeOpacity(1.75);
					pathOpt.setStrokeWeight(1.5);
					pathOpt.setGeodesic(false);

					Iterator<Arco<Integer, String>> iter = R2A.iterator();


					while(iter!= null && iter.hasNext()){

						Arco<Integer, String> act = iter.next();

						Vertice<Integer, String> origen = act.darOrigen();
						Vertice<Integer, String> destino = act.darDestino();


						String info1 = origen.darInfo(); 

						String[] informacion1 = info1.split(",");
						double latitud1 = Double.parseDouble(informacion1[1]);
						double longitud1 = Double.parseDouble(informacion1[0]);

						LatLng nuevo1 = new LatLng(latitud1,longitud1);

						Circle startLoc1 = new Circle(map);
						startLoc1.setOptions(vertexLocOpt);
						startLoc1.setCenter(nuevo1); 
						startLoc1.setRadius(13); //Radio del circulo
						
						
						String info2 = destino.darInfo();
						
						String[] informacion2 = info2.split(",");
						double latitud2 = Double.parseDouble(informacion2[1]);
						double longitud2 = Double.parseDouble(informacion2[0]);

						LatLng nuevo2 = new LatLng(latitud2,longitud2);

						Circle startLoc2 = new Circle(map);
						startLoc2.setOptions(vertexLocOpt);
						startLoc2.setCenter(nuevo2); 
						startLoc2.setRadius(13); //Radio del circulo
						
						LatLng[] arco = new LatLng[2];

						arco[0] = nuevo1;
						arco[1] = nuevo2;

						Polyline path = new Polyline(map); 														
						path.setOptions(pathOpt); 
						path.setPath(arco);

					}

					}

					initMap( map );
				}
			

		} );



	}

}
