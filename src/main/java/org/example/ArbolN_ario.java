package org.example;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Se nos solicita crear un arbol multicamino que modele la jerarquia urbana.
 * Haciendo uso de los nodos N-arios, claremente.
 * La jerarquia, como lo vimos en los nodos es: Ciudad -> Distrito -> Zona -> Avenida -> Interseccion
 */

public class ArbolN_ario<T> {
    //Estados importantes: la raiz y el numero maximmo de hijos
    private NodoN_ario<T> raiz;
    private final int maxHijos;

    //Constructor
    public ArbolN_ario(int maxHijos){
        this.maxHijos = maxHijos;
        this.raiz = null;
    }

    //=============================== Insercion en el  arbol N-ario ==================================
    //Primero, es muy necesario establecer una raiz, en este caso debemos de establecer la ciudad
    public boolean setRaiz(T dato){
        if (raiz != null) return false;
        raiz = new NodoN_ario<>(dato, NodoN_ario.Nivel.CIUDAD, maxHijos);
        return true;
    }

    //Segundo, agregamos el hijo al nodo padre ya establecido.
    //El nivel del hijo se determina a partir del nivel del padre
    public boolean insertar(T datoPadre, T datoHijo){
        NodoN_ario<T> padre = buscarNodo(raiz, datoPadre); //Para mantener el codigo en ccualauier circunstancia, mandamos a buscar al padre
        if (padre == null) return false; //En caso de que el padre no exista

        //Buscamos el nivel del hijo
        NodoN_ario.Nivel nivelHijo = nivelSiguiente(padre.nivel);
        if(nivelHijo == null) return false;

        //Creamos al hijo
        NodoN_ario<T> hijo = new NodoN_ario<>(datoHijo, nivelHijo, maxHijos);
        return padre.agregarHijo(hijo); //Ahora si lo agregamo
    }

    //Por ultimo, en el caso de que deseemos agregar un hijo en un nivel especifico, esta es la forma de hacerlo
    public boolean insertar(T datoPadre, T datoHijo, NodoN_ario.Nivel nivelHijo){
        NodoN_ario<T> padre = buscarNodo(raiz, datoPadre); //Buscamos al padre
        if (padre == null) return false; //En caso de que el padre no exista

        //Creamos al hijo
        NodoN_ario<T> hijo = new NodoN_ario<>(datoHijo, nivelHijo, maxHijos);
        return padre.agregarHijo(hijo); //Ahora si lo agregamos
    }

    //=============================== busqueda en el  arbol N-ario ==================================

    //Funcion publica
    public NodoN_ario<T> buscar(T dato){
        return buscarNodo(raiz, dato);
    }

    private NodoN_ario<T> buscarNodo(NodoN_ario<T> raiz, T dato){
        if (raiz == null) return null;
        if(raiz.dato == dato) return raiz;

        for(NodoN_ario<T> hijo : raiz.hijos){
            NodoN_ario<T> resultado = buscarNodo(hijo, dato);
            if (resultado != null) return resultado;
        }
        return null;
    }

    //=============================== Recorridos en el  arbol N-ario ==================================
    //Adaptados de las solciones hechas en clase de los LeetCode's: 589, 590, 429

    //PreOrder
    public List<NodoN_ario<T>> preOrder(NodoN_ario<T> raiz){
        List<NodoN_ario<T>> resultado = new ArrayList<>();
        preOrder(raiz, resultado);
        return resultado;
    }

    private void preOrder(NodoN_ario<T> raiz, List<NodoN_ario<T>> resultado){
        if (raiz == null) return;

        resultado.add(raiz);

        for(NodoN_ario<T> hijo : raiz.hijos){
            preOrder(hijo, resultado);
        }
    }

    //PostOrder
    public List<NodoN_ario<T>> postOrder(NodoN_ario<T> raiz){
        List<NodoN_ario<T>> resultado = new ArrayList<>();
        postOrder(raiz, resultado);
        return resultado;
    }

    private void postOrder(NodoN_ario<T> raiz, List<NodoN_ario<T>> resultado){
        if (raiz == null) return;

        for(NodoN_ario<T> hijo : raiz.hijos){
            postOrder(hijo, resultado);
        }

        resultado.add(raiz);

    }

    //Por niveles
    public List<List<T>> porNiveles(NodoN_ario<T> raiz){
        if (raiz == null) return List.of();

        Queue<NodoN_ario<T>> queue = new LinkedList<>();

        queue.offer(raiz);
        List<List<T>> resultado = new ArrayList<>();

        while(!queue.isEmpty()){
            int size = queue.size();
            List<T> nivel = new ArrayList<>(size);

            for(int i = 0; i < size; i++){
                NodoN_ario<T> nodo = queue.poll();
                nivel.add(nodo.dato);

                for(NodoN_ario<T> hijo : nodo.hijos){
                    queue.offer(hijo);
                }
            }
            resultado.add(nivel);
        }

        return resultado;
    }

    //============================== Metricas y Consultas necesarias para  lo solicitado en el proyecto :) =================

    //Para fines en el main, necesitamos contar las intersecciones dentro de un distrito dado...
    public int contarInterseccionesPorDistrito(T datoDistrito){
        NodoN_ario<T> distrito = buscarNodo(raiz, datoDistrito); //Buscamos el nodo
        if(distrito == null || distrito.nivel != NodoN_ario.Nivel.DISTRITO) return 0; //Si no existe o su nivel no se encuentra en los niveles establecidos... retorna
        return contarPorNivel(distrito, NodoN_ario.Nivel.INTERSECCION);
    }

    //Helper publico
    public int contarPorNivel(T dato, NodoN_ario.Nivel nivel){
        NodoN_ario<T> nodo = buscarNodo(raiz, dato); //Nuevamente buscamos el nodo
        if(nodo == null) return 0; //Si es nulo, retorna 0
        return contarPorNivel(nodo, nivel); //Llama al helper privado luego de validar
    }

    //Helper privado
    private int contarPorNivel(NodoN_ario<T> nodo, NodoN_ario.Nivel nivel) {
        if (nodo == null) return 0; //Si es nulo, retorna 0
        int cuenta = nodo.nivel == nivel ? 1 : 0; //Cuenta es igual al nivel del nodo, si este es igual a 'nivel' entonces se le asigna 1, de lo contrario 0
        for (NodoN_ario<T> hijo : nodo.hijos) cuenta += contarPorNivel(hijo, nivel); //Llama a la funcion para todos los hijos
        return cuenta; //Retorna la suma
    }

    //Ademas, surgió la necesidad de obtener todos los nodos de un nivel especifico.. por tanto
    public List<T> obtenerPorNivel(NodoN_ario.Nivel nivel) {
        List<T> resultado = new ArrayList<>(); //Creamos un nuevo arreglo en donde enlistar los nodos
        obtenerPorNivel(raiz, nivel, resultado);// Llamamos a la funcion privada
        return resultado;
    }

    //Funcion privada
    private void obtenerPorNivel(NodoN_ario<T> nodo, NodoN_ario.Nivel nivel, List<T> resultado) {
        if (nodo == null) return;
        if (nodo.nivel == nivel) resultado.add(nodo.dato); //Si el nivel coincide con el deseado, lo agregamos a al arreglo
        for (NodoN_ario<T> hijo : nodo.hijos) obtenerPorNivel(hijo, nivel, resultado); //Llama a la funcion para todos los hijos
    }

    public int profundidadMaxima() { return profundidadMaxima(raiz); }

    private int profundidadMaxima(NodoN_ario<T> nodo) {
        if (nodo == null) return 0;
        int maxProf = 0; //Inicializa la variable
        for (NodoN_ario<T> hijo : nodo.hijos) maxProf = Math.max(maxProf, profundidadMaxima(hijo));//Compara las profundidades en diversos caminos
        return 1 + maxProf; //Suma la profundidad de la raiz mas la profundidad maxima encontrada
    }

    public int contarNodos() { return contarNodos(raiz); } //Cuenta todos los nodos, incluyendo la raiz

    private int contarNodos(NodoN_ario<T> nodo) {
        if (nodo == null) return 0;
        int total = 1; //La raiz siempre cuenta
        for (NodoN_ario<T> hijo : nodo.hijos) total += contarNodos(hijo); //Cuenta todos los demas
        return total; //Retorna el total
    }

    public int contarHojas() { return contarPorTipo(raiz, true); } //Cuenta solo las hojas

    public int contarNodosInternos() { return contarPorTipo(raiz, false); } //No cuenta los nodos hoja

    //Funciones adapatados de StackOverflow, consultas con la IA y VIDEOS

    //Dependiendo del booleano que le ingrese, cuenta o no las hojas. Es una funcion 2 en 1
    private int contarPorTipo(NodoN_ario<T> raiz, boolean contarHojas) {
        if (raiz == null) return 0; //si el nodo es null, retorna 0
        int cuenta = 0; //Inicializa la cuenta
        boolean esHoja = raiz.esHoja(); //Obtiene el valor booleano de si es hoja o no
        if (contarHojas && esHoja)   cuenta++; // Si queremos contar las hojas y, en efecto tenemos una hoja, sumamos a la cuenta
        if (!contarHojas && !esHoja) cuenta++; //Si no queremos contar las hojas y, por consiguiente, no tenemos un nodo hoja, sumamos a la cuenta
        for (NodoN_ario<T> hijo : raiz.hijos) cuenta += contarPorTipo(hijo, contarHojas); //Luego recorre recursivamente la funcion en cada hijo del nodo raiz (o el ingresado pues)
        return cuenta; //Regresa la cuenta total
    }


    //Nos interesa saber el promedio de hijos por cada nodo que puede tener hijos, osea, por cada padre
    public double factorPromedioRamificacion() {
        int[] acumulador = {0, 0}; //Crea un arreglo de dos posiciones, cada una con valor '0'.
        factorRamificacion(raiz, acumulador); //Hace una llamada
        return acumulador[1] == 0 ? 0 : (double) acumulador[0] / acumulador[1]; //se divide la cantidad total de hijos encotrada entre la cantidad total de padres.
        //El 0 ? 0 nos ayuda en caso de que tengamos una division dentro de 0, para que no explote el programa
    }

    private void factorRamificacion(NodoN_ario<T> raiz, int[] acumulador) {
        if (raiz == null || raiz.esHoja()) return; //Si la raiz es null o, si la 'raiz' es un nodo hoja, retorna null
        acumulador[0] += raiz.cantidadHijos(); //En la posicion [0] del arreglo almacena la cantidad de hijos del padre
        acumulador[1]++; //Ahora, en la posicion [1] del arreglo, almacena la cantidad de padres
        for (NodoN_ario<T> hijo : raiz.hijos) factorRamificacion(hijo, acumulador); //Hace el mismo proceso en sus hijos
    }
    public void estadisticas() {
        System.out.println("=".repeat(45));
        System.out.println("  Estadisticas Arbol N-ario");
        System.out.println("=".repeat(45));
        System.out.printf("  Max hijos config    : %d%n",    maxHijos);
        System.out.printf("  Profundidad maxima  : %d%n",    profundidadMaxima());
        System.out.printf("  Nodos totales       : %d%n",    contarNodos());
        System.out.printf("  Hojas               : %d%n",    contarHojas());
        System.out.printf("  Nodos internos      : %d%n",    contarNodosInternos());
        System.out.printf("  Factor ramificacion : %.2f%n",  factorPromedioRamificacion());
        System.out.println("=".repeat(45));
    }

    //Nos ayuda encontrar el nivel siguiente, el de los hijos, a partir de el del padre
    private NodoN_ario.Nivel nivelSiguiente(NodoN_ario.Nivel actual) {
        return switch (actual) {
            case CIUDAD       -> NodoN_ario.Nivel.DISTRITO; //Si el padre es ciudad, el hijo esta en distrito
            case DISTRITO     -> NodoN_ario.Nivel.ZONA; //Si el padre es distrito, el hijo esta en zona
            case ZONA         -> NodoN_ario.Nivel.AVENIDA; // Si el padre es zona, el hijo esta en avenida
            case AVENIDA      -> NodoN_ario.Nivel.INTERSECCION; //Si el padre es avenida, el hijo esta en intersecciones
            case INTERSECCION -> null; //Si el padre esta en interseccion, el hijo es nulo porque no existe, 'interseccion' es el ultimo nivel
        };
    }

    public NodoN_ario<T> getRaiz() { return raiz; }
}

