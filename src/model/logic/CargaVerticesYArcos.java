package model.logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

import model.data_structures.Arco;
import model.data_structures.GrafoNoDirigido;
import model.data_structures.Haversine;
import model.data_structures.Vertice;

public class CargaVerticesYArcos {

	Haversine haversiana;

	public CargaVerticesYArcos(){

		haversiana = new Haversine();
	}


	public void cargarInformacion(GrafoNoDirigido<Integer, String> grafo, String direccion, String direccion2){

		File file = new File(direccion); 
		File file2 = new File(direccion2); 


		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			BufferedReader br2 = new BufferedReader(new FileReader(file2));

			String linea;
			String linea2;


			linea = br.readLine();

			// vertices
			while(linea !=null){

				String[] lineaPartida = linea.split(",");
				Integer idVertex = (Integer) Integer.parseInt(lineaPartida[0]);

				String infoVertex = lineaPartida[1] + "," + lineaPartida[2]; // longitud, y latitud separadas por una coma 

				grafo.agregarVertice(idVertex, infoVertex);

				linea = br.readLine();
			}

			linea2 = br2.readLine();

			while(linea2.charAt(0)=='#'){

				linea2 = br2.readLine();
			}

			// arcos
			while(linea2 !=null){

				String[] lineaPartida2 = linea2.split(" ");

				Integer idOrigen = (Integer) Integer.parseInt(lineaPartida2[0]);

				for(int i = 1; i<lineaPartida2.length; i++){

					Integer idDestino = Integer.parseInt(lineaPartida2[i]);

					Vertice<Integer, String> origin = grafo.darVertice(idOrigen);
					Vertice<Integer, String> destiny = grafo.darVertice(idDestino);

					if(origin !=null && destiny!=null){

						String info = origin.darInfo(); // longitud,latitud
						String info2 = destiny.darInfo();

						String[] coordenadas = info.split(",");
						String[] coordenadas2 = info2.split(",");

						String lon = coordenadas[0];
						String lat = coordenadas[1];
						double longitud = Double.parseDouble(lon);
						double latitud = Double.parseDouble(lat);

						String lon2 = coordenadas2[0];
						String lat2 = coordenadas2[1];
						double longitud2 = Double.parseDouble(lon2);
						double latitud2 = Double.parseDouble(lat2);

						// haversiana.distance(47.6788206, -122.3271205, 47.6788206, -122.5271205); // latitud-longitud, latitud-longitud
						double costo = haversiana.distance(latitud, longitud, latitud2, longitud2);
						grafo.agregarArco(idOrigen, idDestino, costo);

					}
				}

				linea2 = br2.readLine();
			}
			br.close();
			br2.close();


			System.out.println("La cantidad de vértices del grafo es: " + grafo.V());
			System.out.println("La cantidad de arcos del grado es: " + grafo.E());
			System.out.println();


		} 

		catch (Exception e) {


			e.printStackTrace();
		} 

	}

}
