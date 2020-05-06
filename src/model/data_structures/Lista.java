package model.data_structures;

import java.util.Iterator;

public class Lista  <T extends Comparable <T>>  {

	private int longitud;

	private NodoLista<T> primero;

	private NodoLista<T> ultimo;

	public Lista(){

		longitud = 0;
	}


	public T eliminar(T pElemento) throws Exception{

		T rta = null;

		if(primero == null){

			throw new Exception("No existe ningun elemento que eliminar");
		}

		else if (primero.darElemento().compareTo(pElemento)==0){

			NodoLista<T> siguiente = primero.darSiguiente();
			rta = primero.darElemento();
			primero = siguiente;
		}

		else{

			NodoLista<T> anterior = null; 
			NodoLista<T> actual = primero;
			boolean encontro = false;
			while(actual != null && encontro == false){

				if(actual.darElemento().compareTo(pElemento)==0){

					encontro = true;
				}

				else{
					anterior = actual;
					actual = actual.darSiguiente();
				}
			}

			if(encontro ==  true){
				rta = actual.darElemento();
				anterior.cambiarSiguiente(actual.darSiguiente());
			}
		}
		return rta;

	}

	public T buscar(T num){

		T rta = null;

		if(primero.darElemento().compareTo(num)==0){
			rta = primero.darElemento();
		}

		else{

			NodoLista<T> actual = primero;

			boolean encontro = false;

			while(actual != null && encontro == false){

				if(actual.darElemento().compareTo(num)==0){
					encontro = true;
				}

				else{

					actual = actual.darSiguiente();
				}
			}

			if(encontro ==true){

				rta = actual.darElemento();
			}
		}

		return rta;
	}

	public int darLongitud(){

		return longitud;
	}


	public Object[] darArreglo(){

		Object[] arreglo = new Object[this.darLongitud()];

		NodoLista<T> actual = primero;

		for(int i = 0; i< longitud; i++){

			arreglo[i] = actual;
			actual = actual.darSiguiente();
		}

		return arreglo;
	}

	public void agregar(T pElemento){

		NodoLista<T> nuevo = new NodoLista<T>(pElemento);

		if(primero == null){

			primero = nuevo;
			longitud++;
			ultimo = nuevo;
		}

		else{

			ultimo.cambiarSiguiente(nuevo);
			longitud++;
			ultimo = nuevo;
		}

	}

	public T darPrimero(){

		return primero.darElemento();
	}

	public T darUltimo(){

		return ultimo.darElemento();
	}

	public NodoLista<T> darPrimerNodo(){

		return primero;
	}
	
	public NodoLista<T> darUltioNodo(){
		
		return ultimo;
	}

}