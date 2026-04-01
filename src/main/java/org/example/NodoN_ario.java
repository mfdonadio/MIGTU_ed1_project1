package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Nodos especificos para el arbol N-ARIO (Multicamino)
 * Cada nodo tiene que conocer su jerarquia en el sistema de simulacion urbana y,
 * ademas,el numero de hijos debe de swe configurable segun los requerimientos del proyecto.
 * El reto en la implementacion, aparte de el arbol en si, es poder darles a cada uno un nlugar
 * y que entiendan la 'prioridad' del mismo. Para esto se nos ocurre hacer un ENUM y a partir de eso vemos :)
 */
public class NodoN_ario<T> {

    //Niveles de jerarquia urbana
    public enum Nivel {
        CIUDAD, DISTRITO, ZONA, AVENIDA, INTERSECCION
    }

    public T dato;
    public final Nivel nivel;
    public final List<NodoN_ario<T>> hijos;
    private final int maxHijos;

    //Constructor
    public NodoN_ario(T dato, Nivel nivel, int maxHijos) {
        this.dato = dato;
        this.nivel = nivel;
        this.maxHijos = maxHijos;
        this.hijos = new ArrayList<>(maxHijos); //Esto ya lo comprobaremos 'maxHijos'
    }

    //En el caso de que se desee agregar un hijo, hay que ver que no se supere el limite establecido
    public boolean agregarHijo(NodoN_ario<T> hijo) {
        if(hijos.size() >= maxHijos) return false;
        hijos.add(hijo);
        return true;
    }

    public boolean esHoja() {return hijos.isEmpty();} //Si un nodo no tiene hijos, entonces es hoja
    public int cantidadHijos() {return hijos.size();} //La cantidad de hijos es el tamaño del arreglo 'hijos'
    public T obtenerDato() {return dato;}

    //Formato del string, otra vez jajajaja
    @Override
    public String toString(){
        return String.format("Nodo N-Ario { nivel = %s, dato = %s , hijos = %d}",
                nivel, dato, hijos.size());
    }

}
