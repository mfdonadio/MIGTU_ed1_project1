package org.example;

public class Nodo <T> {
    public  T dato;
    protected Nodo<T> izquierdo;
    protected Nodo<T> derecho;
    protected int altura;

    public Nodo(T dato){
        this.dato = dato;
        this.altura = 1;
    }

    @Override
    public String toString(){
        return String.format("Nodo{dato=%s} (izq=%s, der=%s)",
                dato,
                izquierdo != null ? izquierdo.dato : "null",
                derecho != null ? derecho.dato : "null");
    }

    public T obtenerDato() {
        return dato;
    }

}