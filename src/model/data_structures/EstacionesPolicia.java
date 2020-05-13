package model.data_structures;

public class EstacionesPolicia {

	public int OBJECTID;
	public String EPODESCRIP;
	public String EPODIR_SITIO;
	public double EPOLATITUD;
	public double EPOLONGITU;
	public String EPOSERVICIO;
	public String EPOHORARIO;
	public String EPOTELEFON;
	public String EPOIULOCAL;



	public String retornarInformacionCargar(){

		String rta = "OBJECTID: " + OBJECTID + " EPODESCRIP: " + EPODESCRIP + " EPODIR_SITIO: " + EPODIR_SITIO +
				" EPOLATITUD: " + EPOLATITUD + " EPOLONGITU: " + EPOLONGITU + " EPOSERVICIO: " + EPOSERVICIO + " EPOHORARIO: "	+
				EPOHORARIO + " EPOTELEFON: " + EPOTELEFON + " EPOIULOCAL: " + EPOIULOCAL; 

		return rta;

	}


}
