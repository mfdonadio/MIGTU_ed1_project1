package org.example;

import java.util.*;


//CODIGO IMPLEMENTADO DEL LAB 5 :D

public class BST<T> {
    protected Nodo<T> raiz;
    protected Comparator<T> comparador;
    private  ArrayList<T> ordenInsercion;

    //METRICAS PARA LAS PRUEBAS (necesarias para el proyecto jeje)
    private long comparacionesTotales;
    private long comparacionesUltimaOperacion;
    private long insercionesTotales;
    private long eliminacionesTotales;
    private long busquedasTotales;

    public BST(Comparator<T> comparador){
        this.comparador = comparador;
        this.raiz = null;
        this.ordenInsercion = new ArrayList<>();

        this.comparacionesUltimaOperacion = 0;
        this.comparacionesTotales = 0;
    }

    public void imprimirNodos() {
        System.out.print("Valores insertados: ");
        for(T valor: ordenInsercion){
            System.out.print(valor + " ");
        }
        System.out.println();
    }

    public void preOrder() {
        if(this.raiz == null) return;

        Stack<Nodo<T>> stack = new Stack<>();
        stack.push(this.raiz);

        while(!stack.isEmpty()){
            Nodo<T> actual = stack.pop();
            System.out.print(actual.obtenerDato() + " "); //primero la raiz

            if(actual.derecho!= null)
                stack.push(actual.derecho);
            if(actual.izquierdo != null)
                stack.push(actual.izquierdo);
        }

    }

    public void inOrder() {
        if(this.raiz == null) return;

        Stack<Nodo<T>> stack = new Stack<>();
        Nodo<T> actual = this.raiz;

        while(!stack.isEmpty() || actual != null){
            //Primero, vamos hasta el nodo mas a la izquierda
            while(actual != null){
                stack.push(actual);
                actual = actual.izquierdo;
            }

            //Procesamos el nodo raiz
            actual = stack.pop();
            System.out.print(actual.obtenerDato() + " ");

            //Ahora vamos pa el subarbol derecho.
            actual = actual.derecho;
        }

    }

    public Nodo<T> buscar(T valor){
        comparacionesUltimaOperacion = 0;
        busquedasTotales++;
        return buscar(this.raiz, valor);
    }

    public Nodo<T> buscar(Nodo<T> nodo, T valor){
        if(nodo == null) return null;

        comparacionesUltimaOperacion++; //INCREMENTA
        comparacionesTotales++; //INCREMENTA
        if(comparador.compare(valor, nodo.obtenerDato()) == 0){
            return nodo;
        }

        if(comparador.compare(valor, nodo.obtenerDato()) < 0){
            return buscar(nodo.izquierdo, valor);
        }
        return buscar(nodo.derecho, valor);
    }

    public void eliminar(T valor){
        comparacionesUltimaOperacion = 0; //INICIALIZA
        eliminacionesTotales++; //INCREMENTA
        this.raiz = eliminar(this.raiz, valor);
    }

    public void postOrder() {
        Stack<Nodo<T>> stack1 = new Stack<>();
        Stack<Nodo<T>> stack2 = new Stack<>();

        stack1.push(this.raiz);

        while(!stack1.isEmpty()){
            Nodo<T> actual = stack1.pop();
            stack2.push(actual); //Se guarda en el auxiliar

            //Izq luego va el derecho
            if(actual.izquierdo != null)
                stack1.push(actual.izquierdo);
            if(actual.derecho != null)
                stack1.push(actual.derecho);
        }

        //Segundo stack ayuda a imprimir de la raiz a el final
        while(!stack2.isEmpty()){
            System.out.print(stack2.pop().obtenerDato() + " "); //Obtiene - elimina - muestra
        }
    }

    public void recorridoPorNivel() {
        if (this.raiz == null) return;

        Queue<Nodo<T>> queue = new LinkedList<>();
        queue.add(this.raiz);
        int nivelActual = 1;

        while(!queue.isEmpty()){
            int nodosEnNivel = queue.size();
            System.out.print("Nivel " + nivelActual + ".... ");

            for(int i = 0; i <  nodosEnNivel; i++) {
                Nodo<T> actual = queue.poll(); //Saca el primer nodo
                System.out.print(actual.obtenerDato());

                if (i < nodosEnNivel - 1) {
                    System.out.print(" ");
                }

                //Ahora los hijos (primero izq y luego der)
                if (actual.izquierdo != null)
                    queue.add(actual.izquierdo);
                if (actual.derecho != null)
                    queue.add(actual.derecho);
            }
            System.out.println();
            nivelActual++;
        }

    }

    public boolean insertar(T valor){
        //Valores menores a la izquierda
        //Valores mayores a la derecha
        //No se permiten repetidos

        comparacionesUltimaOperacion = 0; //INICIALIZA
        if(this.raiz == null) {
            this.raiz = new Nodo<>(valor);
            ordenInsercion.add(valor);
            return true;
        }
        boolean insertado = insertar(valor, this.raiz) != null;
        if(insertado) {
            ordenInsercion.add(valor);  // Guardar solo si se insertó
        }
        return insertado;
    }


    public int contarNodos() {
        return contarNodos(raiz);
    }

    public int contarHojas() {
        return contarHojas(raiz);
    }

    public int altura() {
        return altura(raiz);
    }

    public int sumarNodos() {

        return sumarNodos(raiz);
    }

    public T minimo() {
        return minimo(raiz);
    }

    public int maximo() {
        return maximo(raiz);
    }

    public String estaBalanceada(){
        if (estaBalanceada(raiz)) return "Si";
        else return "No";
    }

    //METODOS FULL NUEVOS
    public void setComparator(Comparator<T> nuevoComparador){
        this.comparador = nuevoComparador;
    }

    //Limpiar lo ya analizado
    public void limpiar(){
        raiz = null;
        ordenInsercion.clear();
        resetearMetricas();
    }

    //Alistarlos para diferentes pruebas
    public void resetearMetricas(){
        comparacionesTotales = 0;
        comparacionesUltimaOperacion = 0;
        insercionesTotales = 0;
        eliminacionesTotales = 0;
        busquedasTotales = 0;
    }

    public void estadisticas() {
        System.out.println("=".repeat(45)); //Crea divisiones para verlo mejor visualmente
        System.out.println("  Estadisticas BST");
        System.out.println("=".repeat(45)); //Lo mismo aqui
        System.out.printf("  Altura            : %d%n",  altura());
        System.out.printf("  Nodos totales     : %d%n",  contarNodos());
        System.out.printf("  Hojas             : %d%n",  contarHojas());
        System.out.printf("  Balanceado        : %s%n",  estaBalanceada());
        System.out.printf("  Comparaciones tot : %,d%n", comparacionesTotales);
        System.out.printf("  Inserciones       : %,d%n", insercionesTotales);
        System.out.printf("  Busquedas         : %,d%n", busquedasTotales);
        System.out.println("=".repeat(45)); //Y aqui
    }

    // Getters de métricas (obtener nuestros datos)
    public long getComparacionesTotales()  { return comparacionesTotales; }
    public long getComparacionesUltimaOp() { return comparacionesUltimaOperacion; }
    public long getInsercionesCont()       { return insercionesTotales; }
    public long getBusquedaCont()          { return busquedasTotales; }


    //==================================================================================================================================================================================================

    private Nodo<T> eliminar(Nodo<T> raiz, T valor){
        if(raiz == null) return null;

        comparacionesUltimaOperacion++; //INCREMENTA
        comparacionesTotales++;    //INCREMENTA
        int cmp = comparador.compare(valor, raiz.obtenerDato());

        if(cmp < 0){
            raiz.izquierdo = eliminar(raiz.izquierdo, valor);
        } else if(cmp > 0){
            raiz.derecho = eliminar(raiz.derecho, valor);
        } else {
            // Sin hijo izquierdo
            if(raiz.izquierdo == null) return raiz.derecho;
            // Sin hijo derecho
            if(raiz.derecho == null) return raiz.izquierdo;

            // Dos hijos: reemplazar con mínimo del subárbol derecho
            T min = minimo(raiz.derecho);
            raiz.dato = min;
            raiz.derecho = eliminar(raiz.derecho, min);
        }

        return raiz;
    }


    private Nodo<T> insertar(T valor, Nodo<T> nodo) {
        if (nodo == null) {
            nodo = new Nodo<>(valor);
            return nodo;
        }

        comparacionesUltimaOperacion++; //INCREMENTA
        comparacionesTotales++; //INCREMENTA
        if (comparador.compare(valor, nodo.obtenerDato()) == 0)
            return null;
        if (comparador.compare(valor, nodo.obtenerDato()) < 0){
            Nodo<T> aux = insertar(valor, nodo.izquierdo);
            if(aux == null) return null;
            nodo.izquierdo = aux;
            return nodo;
        }

        Nodo<T> aux = insertar(valor, nodo.derecho);
        if(aux == null) return null;
        nodo.derecho = aux;

        return nodo;
    }


    private void imprimirNodos(Nodo<T> nodo){
        if(nodo == null) return;

        imprimirNodos(nodo.izquierdo);
        System.out.print(nodo.obtenerDato() + " ");
        imprimirNodos(nodo.derecho);
    }

    private int contarNodos(Nodo<T> nodo){
        if(nodo == null) return 0;
        else {
            return 1 + contarNodos(nodo.izquierdo) +
                    contarNodos(nodo.derecho);
        }
    }

    private int contarHojas(Nodo<T> nodo){
        if(nodo == null) return 0;
        if(nodo.izquierdo == null && nodo.derecho == null) return 1;//Seria un hoja
        return contarHojas(nodo.izquierdo) + contarHojas(nodo.derecho);
    }

    private int altura(Nodo<T> nodo){
        if(nodo == null) return 0;
        return 1 + Math.max(altura(nodo.izquierdo), altura(nodo.derecho)); //Math max ayuda a saber cual de los dos arboles es mayor
    }

    private int sumarNodos(Nodo<T> nodo){
        if(nodo == null) return 0;
        return (Integer)nodo.obtenerDato() +
                sumarNodos(nodo.izquierdo) +
                sumarNodos(nodo.derecho);
    }

    private T minimo(Nodo<T> nodo){
        if(nodo.izquierdo == null)
            return nodo.obtenerDato();
        return minimo(nodo.izquierdo);
    }

    private int maximo(Nodo<T> nodo){
        if(nodo.derecho == null)
            return (Integer)nodo.obtenerDato();
        return maximo (nodo.derecho);
    }

    //==============================================================================================================================================================================================

    private boolean estaBalanceada(Nodo<T> nodo){
        if(nodo == null) return true;
        if (Math.max(altura(nodo.izquierdo),altura(nodo.derecho)) -
                Math.min(altura(nodo.izquierdo),altura(nodo.derecho)) == 1 ||
                Math.max(altura(nodo.izquierdo),altura(nodo.derecho)) -
                        Math.min(altura(nodo.izquierdo),altura(nodo.derecho)) == 0) {
            return estaBalanceada(nodo.izquierdo) && estaBalanceada(nodo.derecho);
        } return false;
    }
}
