package org.example;

import java.util.*;


//CODIGO IMPLEMENTADO DEL LAB 5 :D

public class BST<T> {
    protected Nodo<T> raiz;
    protected Comparator<T> comparador;
    private  ArrayList<T> ordenInsercion;

    //METRICAS PARA LAS PRUEBAS (necesarias para el proyecto jeje)
    private long eliminacionesTotales;
    private long busquedasTotales;

    protected long comparacionesTotales;
    protected long comparacionesUltimaOperacion;
    protected long insercionesTotales;

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

    public Nodo<T> buscar(T valor){
        comparacionesUltimaOperacion = 0;
        busquedasTotales++;
        return buscar(this.raiz, valor);
    }

    public Nodo<T> buscar(Nodo<T> nodo, T valor){
        if(nodo == null) return null;

        int cmp = comparador.compare(valor, nodo.obtenerDato());
        comparacionesUltimaOperacion++; //INCREMENTA
        comparacionesTotales++; //INCREMENTA
        if(cmp == 0){
            return nodo;
        }

        if(cmp < 0){
            return buscar(nodo.izquierdo, valor);
        }
        return buscar(nodo.derecho, valor);
    }

    public void eliminar(T valor){
        comparacionesUltimaOperacion = 0; //INICIALIZA
        eliminacionesTotales++; //INCREMENTA
        this.raiz = eliminar(this.raiz, valor);
    }

    //Luego de realizar las pruebas, el BST falla en los 50K, 100K o mas eventos
    //Tenemos que cambiar del enfoque recurivo al iteratico
    public boolean insertar(T valor){
        //Valores menores a la izquierda
        //Valores mayores a la derecha
        //No se permiten repetidos

        comparacionesUltimaOperacion = 0; //INICIALIZA
        if(this.raiz == null) {
            this.raiz = new Nodo<>(valor);
            ordenInsercion.add(valor);
            insercionesTotales++;
            return true;
        }
        boolean insertado = insertar(valor, this.raiz) != null;
        if(insertado) {
            ordenInsercion.add(valor);  // Guardar solo si se insertó
            insercionesTotales++;
        }
        return insertado;
    }


    public int contarNodos() {
        return conteoNodosPrivado(raiz);
    }

    public int contarHojas() {
        return contarHojas(raiz);
    }

    public int altura() {
        return altura(raiz);
    }


    public T minimo() {
        return minimo(raiz);
    }

    public T maximo() {
        return maximo(raiz);
    }

    //Implementacion final, mucho mas eficiente
    //Añadimos un  try - catch para evitar al 100% el Overflow
    public boolean estaBalanceada(){
        try { return verificarBalance(raiz) != -1;}
        catch (StackOverflowError e){ return false;} //Si llega a ver un overflow, aseguramos regrese un false y que no truene el programa
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
        Nodo<T> actual = nodo;
        Nodo<T> padre = null;

        //Logica iterativa, ya que la recursisva fallo
        while(actual != null){
            padre = actual;
            int comp =  comparador.compare(valor, actual.obtenerDato());

            comparacionesUltimaOperacion++;
            comparacionesTotales++;

            if(comp == 0){
                return null; //Si son iguales, retornamos null, evitamos valores repetidos
            } else if(comp < 0){
                actual = actual.izquierdo; //Si es menor, es el hijo izq
            } else{
                actual = actual.derecho; //Si es mayor, es el hijo der.
            }
        }

        // Creamos el nuevo nodo y lo 'colgamos' del padre encontrado
        Nodo<T> nuevo = new Nodo<>(valor);
        if (comparador.compare(valor, padre.obtenerDato()) < 0) {
            padre.izquierdo = nuevo;
        } else {
            padre.derecho = nuevo;
        }

        return nuevo; // Retornamos el nodo insertado
    }


    private void imprimirNodos(Nodo<T> nodo){
        if(nodo == null) return;

        imprimirNodos(nodo.izquierdo);
        System.out.print(nodo.obtenerDato() + " ");
        imprimirNodos(nodo.derecho);
    }

    //Luego de varias pruebas, la solucion recursiva no es la mas eficiente y falla... cambio a iterativa
    //Tipo BFS y algo que ya hicimos en clase. Nos permite llevar una memoria de que Nodos ya recorrimos y saber cuales faltan nivel a nivel.
    private int conteoNodosPrivado(Nodo<T> nodo) {
        if (nodo == null) return 0; // Si no hay nodo, el conteo es cero

        int count = 0;//Inicializamod la cuenta en 0
        Queue<Nodo<T>> q = new LinkedList<>(); //Creamos una cola
        q.add(nodo); //encolamos el nodo
        //Mientras no este vacia
        while (!q.isEmpty()) {
            Nodo<T> actual = q.poll(); //Sacamos el actual
            count++; //Aumentamos la cuenta
            if(actual.izquierdo != null)q.add(actual.izquierdo);  //Agregamos al hijo izquierdo
            if(actual.derecho != null)q.add(actual.derecho); //Agregamos al hijo derecho
        }
        return count;
    }

    private int contarHojas(Nodo<T> nodo){
        if(nodo == null) return 0;
        if(nodo.izquierdo == null && nodo.derecho == null) return 1;//Seria un hoja
        return contarHojas(nodo.izquierdo) + contarHojas(nodo.derecho);
    }

    //La solucion iterativa tampoco nos sirve, colapsa la memoria ... Stack Overflow
    private int altura(Nodo<T> nodo){
        if(nodo == null) return 0;

        java.util.Queue<Nodo<T>> cola = new java.util.LinkedList<>();
        cola.add(nodo);
        int altura = 0;

        while(!cola.isEmpty()){
            int nodosEnNivel = cola.size();
            altura++; //Cada vez que terminamos de procesar un nivel completo, sumamos 1

            // Ahora vaciamos todos los nodos del nivel actual y metemos los del siguiente
            for (int i = 0; i < nodosEnNivel; i++) {
                Nodo<T> actual = cola.poll();
                if (actual.izquierdo != null) cola.add(actual.izquierdo);
                if (actual.derecho != null) cola.add(actual.derecho);
            }
        }
        return altura;
    }

    private T minimo(Nodo<T> nodo){
        if(nodo.izquierdo == null)
            return nodo.obtenerDato();
        return minimo(nodo.izquierdo);
    }

    private T maximo(Nodo<T> nodo){
        if(nodo.derecho == null)
            return nodo.obtenerDato();
        return maximo (nodo.derecho);
    }

    //==============================================================================================================================================================================================

    private  int verificarBalance(Nodo<T> nodo){
        if(nodo == null) return 0; //Si es nulo retornamos 0

        int alturaIzquierda = verificarBalance(nodo.izquierdo); //Calculamos la altura del sub arbol izquierdo
        if(alturaIzquierda == -1) return -1;  //Si es -1, retornamos -1 --> indicaria un tipo de 'falla'

        int alturaDerecho = verificarBalance(nodo.derecho); //Calculamos la altura del sub arbol derecho
        if(alturaDerecho == -1) return -1; //Si es -1, retornamos -1 --> indicaria un tipo de 'falla'

        if(Math.abs(alturaIzquierda - alturaDerecho) > 1) return -1; //Si el valor absoluto de la resta es > a 1, retornamos -1 --> indicaria un tipo de 'falla'

        return 1 + Math.max(alturaIzquierda, alturaDerecho); //De lo contrario, sumamos 1 + el maximo entre la altura izquierda y derecha
    }
}
