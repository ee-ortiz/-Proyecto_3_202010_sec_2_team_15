package model.data_structures;

public class EstacionesPolicia {

	public String nombre;
	public int OBJECTID;
	public double EPOLATITUD;
	public double EPOLONGITU;


	public String retornarInformacion(){

		String rta = "-Nombre:" + nombre + " ObjectID: " + OBJECTID + " Latitud: " + EPOLATITUD + " Longitud: " + EPOLONGITU;

		return rta;

	}


}
