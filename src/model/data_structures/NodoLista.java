package model.data_structures;

public class NodoLista<T> {

	private T elemento;

	private NodoLista<T> siguiente;

	public NodoLista(T pElemento){

		elemento = pElemento;

	}

	public void cambiarSiguiente(NodoLista<T> pSiguientne){

		siguiente = pSiguientne;
	}

	public T darElemento(){

		return elemento;
	}

	public NodoLista<T> darSiguiente(){

		return siguiente;
	}

}
