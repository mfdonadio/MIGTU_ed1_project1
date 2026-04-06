package org.example;

//Implementacion final del MAIN :D

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Scanner; //Nos ayuda a parsear tipos de datos primitivos de manera sencilla

public class Main {

    private static final Scanner sc = new Scanner(System.in);

    //Estructuras activas del sistema
    private static BST<Interseccion> indice = null;
    private static ColaPrioridad<Evento> colaEventos = null;
    private static ArbolN_ario<String> ciudad = null;
    private static boolean usandoAVL = false;

    public static void main(String[] args) {
        imprimirBanner();
        boolean salir = false;

        while (!salir) {
            imprimirMenuPrincipal();
            switch (leerOpcion()) {
                case 1 -> menuBST();
                case 2 -> menuAVL();
                case 3 -> menuN_ario();
                case 4 -> menuColaPrioridad();
                case 5 -> menuBenchmarking();
                case 6 -> menuIntegracion();
                case 0 -> salir = true;
                default -> System.out.println("\n  [!] Opcion no valida.\n");

            }
        }
        System.out.println("\n  Sistema MIGTU finalizado. Hasta pronto.\n");
        sc.close();
    }

    //======================================== MENU #1 - BST =======================================
    private static void menuBST() {
        boolean salir = false;
        while (!salir) {
            System.out.println("""
                     ┌─────────────────────────────────────────┐
                     │  Arbol Binario de Busqueda (BST)        │
                     ├─────────────────────────────────────────┤
                     │  1. Demo insercion aleatoria (1.000)    │
                     │  2. Demo insercion ordenada (peor caso) │
                     │  3. Mostrar recorridos                  │
                     │  4. Mostrar estadisticas                │
                     │  0. Volver                              │
                     └─────────────────────────────────────────┘
                    """);
            System.out.print(" Opcion: ");
            switch (leerOpcion()) {
                case 1 -> demoBSTAleatorio();
                case 2 -> demoBSTOrdenado();
                case 3 -> demoBSTRecorridos();
                case 4 -> demoBSTEstadisticas();
                case 0 -> salir = true;
                default -> System.out.println("\n  [!] Opcion no valida.\n");
            }
        }
    }

    private static void demoBSTAleatorio() {
        System.out.println("\n >>> Demo BST insercion aleatoria (1,000 intersecciones)...");

        //Aqui usamos una lambda: criterio de orden por ID
        BST<Interseccion> bst =
                new BST<>((a, b) -> Integer.compare(a.getId(), b.getId()));
        List<Interseccion> datos = GeneradorDatos.interseccionesAleatorias(1000);
        Cronometro c = new Cronometro("BST insercion aleatoria");
        c.iniciar();
        for (Interseccion i : datos) bst.insertar(i);
        c.finalizar();

        //Otra lambda: cambiar criterio a nivel de congestion
        System.out.println("\n Cambiando criterio de orden a nivel de congestion...");
        bst.setComparator((a, b) -> Integer.compare(a.getNivelCongestion(),
                b.getNivelCongestion()));

        System.out.println(" " + c.resumen());
        bst.estadisticas();
    }

    private static void demoBSTOrdenado() {
        System.out.println("\n >>> Demo BST insercion ordenada (peor caso - 1000 inserciones)...");
        BST<Interseccion> bst =
                new BST<>(CentroComparaciones.porId()); //Primer criterio : por ID
        Cronometro c = new Cronometro("BST insercion ordenada (peor caso)");
        c.iniciar();
        for (Interseccion i : GeneradorDatos.interseccionesOrdenadas(1000)) bst.insertar(i);
        c.finalizar();

        System.out.println(" " + c.resumen());
        System.out.println("  [!] En insercion ordenada el BST se degenera a lista enlazada.");
        bst.estadisticas();
    }

    private static void demoBSTRecorridos() {
        System.out.println("\n >>> Recorridos del BST (20 nodos para ejemplificar)...");
        BST<Interseccion> bst =
                new BST<>(CentroComparaciones.porId()); //Primer criterio: por ID
        for (Interseccion i : GeneradorDatos.interseccionesAleatorias(20)) bst.insertar(i);
        System.out.print("\n  InOrder    : ");
        bst.inOrder();
        System.out.println();

        System.out.print("  PreOrder   : ");
        bst.preOrder();
        System.out.println();

        System.out.print("  PostOrder  : ");
        bst.postOrder();
        System.out.println();
        System.out.println("\n  Por niveles:");
        bst.recorridoPorNivel();
    }

    private static void demoBSTEstadisticas() {
        System.out.println("\n  >>> Estadisticas BST (10.000 intersecciones aleatorias)...");
        BST<Interseccion> bst =
                new BST<>(CentroComparaciones.porId()); //Primer criterio : por ID
        for (Interseccion i : GeneradorDatos.interseccionesAleatorias(10_000)) bst.insertar(i);
        bst.estadisticas();

    }

    //======================================== MENU #2 - AVL =======================================
    private static void menuAVL() {
        boolean salir = false;
        while (!salir) {
            System.out.println("""
                     ┌────────────────────────────────────────────┐
                     │  Arbol AVL                                 │
                     ├────────────────────────────────────────────┤
                     │  1. Demo insercion aleatoria (1.000)       │
                     │  2. Demo insercion ordenada (AVL balancea) │
                     │  3. Comparacion altura BST vs AVL          │
                     │  4. Mostrar estadisticas  AVL              │
                     │  0. Volver                                 │
                     └────────────────────────────────────────────┘
                    """);
            System.out.print(" Opcion: ");
            switch (leerOpcion()) {
                case 1 -> demoAVLAleatorio();
                case 2 -> demoAVLOrdenado();
                case 3 -> demoComparacionBSTvsAVL();
                case 4 -> demoAVLEstadisticas();
                case 0 -> salir = true;
                default -> System.out.println("\n  [!] Opcion no valida.\n");
            }
        }
    }

    private static void demoAVLAleatorio() {
        System.out.println("\n >>> Demo AVL insercion aleatoria (1000 intersecciones)...");

        //Lambda: orden por ID pasado directamente al constructor
        AVL<Interseccion> avl = new AVL<>((a, b) -> Integer.compare(a.getId(), b.getId()));
        Cronometro c = new Cronometro("AVL insercion aleatoria");
        c.iniciar();
        for (Interseccion i : GeneradorDatos.interseccionesAleatorias(1000)) avl.insertar(i);
        c.finalizar();

        System.out.println(" " + c.resumen());
        avl.estadisticas();
    }

    private static void demoAVLOrdenado() {
        System.out.println("\n >>> Demo AVL insercion ordenada (AVL mantiene el balance)...");
        //Lambda: orden por ID pasado directamente al constructor
        AVL<Interseccion> avl = new AVL<>((a, b) -> Integer.compare(a.getId(), b.getId()));
        Cronometro c = new Cronometro("AVL insercion ordenada");
        c.iniciar();
        for (Interseccion i : GeneradorDatos.interseccionesOrdenadas(1000)) avl.insertar(i);
        c.finalizar();

        System.out.println("  " + c.resumen());
        System.out.println("  [NOTA] A diferencia del BST, el AVL se mantiene balanceado.");
        avl.estadisticasAVL();
    }

    private static void demoComparacionBSTvsAVL() {
        System.out.println("\n  >>> Comparacion BST vs AVL con insercion ordenada (1.000 nodos)...\n");

        BST<Interseccion> bst = new BST<>(CentroComparaciones.porId());
        AVL<Interseccion> avl = new AVL<>(CentroComparaciones.porId());

        for (Interseccion i : GeneradorDatos.interseccionesOrdenadas(1000)) {
            bst.insertar(i);
            avl.insertar(i);
        }

        /* '%' - inicio especificador
         '-' - alineacion a la izquierda
         'NO.' - caracteres a reservar
         's' y 'd' - tipo de dato
         */
        System.out.println("  " + "=".repeat(46));
        System.out.printf("  %-25s %-10s %-10s%n", "Metrica", "BST", "AVL");
        System.out.println("  " + "-".repeat(46));
        System.out.printf("  %-25s %-10d %-10d%n", "Altura", bst.altura(), avl.altura());
        System.out.printf("  %-25s %-10d %-10d%n", "Nodos totales", bst.contarNodos(), avl.contarNodos());
        System.out.printf("  %-25s %-10s %-10d%n", "Rotaciones", "N/A", avl.getRotacionesTotales());
        System.out.printf("  %-25s %-10s %-10d%n", "Factor balance raiz", "N/A", avl.getFactorBalance());
        System.out.printf("  %-25s %-10s %-10s%n", "Balanceado", bst.estaBalanceada(), avl.estaBalanceada());
        System.out.printf("  %-25s %-10,d %-10,d%n", "Comparaciones", bst.getComparacionesTotales(), avl.getComparacionesTotales());
        System.out.println("  " + "=".repeat(46));
    }

    private static void demoAVLEstadisticas() {
        System.out.println("\n  >>> Estadisticas AVL (10.000 intersecciones aleatorias)...");
        // Lambda: orden por ID pasado directamente al constructor
        AVL<Interseccion> avl = new AVL<>((a, b) -> Integer.compare(a.getId(), b.getId()));
        for (Interseccion i : GeneradorDatos.interseccionesAleatorias(10_000)) avl.insertar(i);
        avl.estadisticasAVL();
    }

    //======================================== MENU #3 - N-ario =======================================
    private static void menuN_ario() {
        boolean salir = false;
        while (!salir) {
            System.out.println("""
                    
                    ┌─────────────────────────────────────────┐
                    │  Arbol Multi-Camino (M-ary)             │
                    ├─────────────────────────────────────────┤
                    │  1. Demo jerarquia urbana               │
                    │  2. Recorrido por niveles               │
                    │  3. Recorrido en profundidad            │
                    │  4. Consultas especificas               │
                    │  5. Estadisticas del arbol              │
                    │  0. Volver                              │
                    └─────────────────────────────────────────┘
                    """);
            System.out.print("  Opcion: ");
            switch (leerOpcion()) {
                case 1 -> demoN_arioJerarquia();
                case 2 -> demoN_arioNiveles();
                case 3 -> demoN_arioProfundidad();
                case 4 -> demoN_arioConsultas();
                case 5 -> demoN_arioEstadisticas();
                case 0 -> salir = true;
                default -> System.out.println("\n  [!] Opcion no valida.\n");
            }
        }
    }

    //Creacion de la ciudad ---- en general
    private static ArbolN_ario<String> crearCiudadDemo() {
        ArbolN_ario<String> arbol = new ArbolN_ario<>(5);
        arbol.setRaiz("Guatemala");
        arbol.insertar("Guatemala", "Distrito Norte");
        arbol.insertar("Guatemala", "Distrito Sur");
        arbol.insertar("Guatemala", "Distrito Centro");
        arbol.insertar("Distrito Norte", "Zona 1");
        arbol.insertar("Distrito Norte", "Zona 2");
        arbol.insertar("Distrito Sur", "Zona 3");
        arbol.insertar("Distrito Centro", "Zona 4");
        arbol.insertar("Zona 1", "Reforma");
        arbol.insertar("Zona 1", "Bolivar");
        arbol.insertar("Zona 2", "Petapa");
        arbol.insertar("Zona 3", "Aguilar Batres");
        arbol.insertar("Zona 4", "Vista Hermosa");
        arbol.insertar("Reforma", "INT-001");
        arbol.insertar("Reforma", "INT-002");
        arbol.insertar("Bolivar", "INT-003");
        arbol.insertar("Petapa", "INT-004");
        arbol.insertar("Aguilar Batres", "INT-005");
        arbol.insertar("Vista Hermosa", "INT-006");
        return arbol;
    }

    //Establecemos la jerarquia de la ciudad
    private static void demoN_arioJerarquia() {
        System.out.println("\n Jerarquia urbana de Guatemala: \n");
        ciudad = crearCiudadDemo();
        //Usamos preOrder para mostrar la jerarquia descendentemente
        List<NodoN_ario<String>> nodos = ciudad.preOrder(ciudad.getRaiz()); //Creamos la lista de nodos de PreOrder
        for (NodoN_ario<String> nodo : nodos) { //Para cada nodo en la lista anterior....
            // Creamos sangría (identación) basada en el nivel para que parezca un árbol
            String sangria = "  ".repeat(obtenerProfundidadNivel(nodo.nivel)); //Llamamos al auxiliar: especializado en mantener la sangria
            System.out.println(sangria + "└── " + nodo.nivel + ": " + nodo.dato); //imrpimimos
        }
        System.out.println("\n [!] Ciudad cargada exitosamente.");
    }

    // Funcion auxiliar para la estética de la sangría
    private static int obtenerProfundidadNivel(NodoN_ario.Nivel nivel) {
        return switch (nivel) {
            case CIUDAD -> 0;
            case DISTRITO -> 1;
            case ZONA -> 2;
            case AVENIDA -> 3;
            case INTERSECCION -> 4;
        };
    }

    private static void demoN_arioNiveles() {
        if (ciudad == null) ciudad = crearCiudadDemo(); //Si es nula, la creamos desde la 'base' ya establecida
        System.out.println("\n >>> Recorrido por niveles: \n");
        //Imprimimos la lista de listas que devuelve nuestro metodo porNiveles
        for(List<String> nivel : ciudad.porNiveles(ciudad.getRaiz())){
            System.out.println(nivel);
        }
    }

    private static void demoN_arioProfundidad() {
        if (ciudad == null) ciudad = crearCiudadDemo(); //Si es nula, la creamos desde la 'base' ya establecida
        System.out.println("\n >>> Recorrido en profundidad (DFS): \n");
        ciudad.preOrder(ciudad.getRaiz()).forEach(nodo-> System.out.print(nodo.dato + " -> ")); //Lambda para imprimir preOrder
        System.out.printf("%n >>> Profundidad maxima de la red: %d%n",
                ciudad.profundidadMaxima()); //Imprimimos la profundidad maxima
    }

    private static void demoN_arioConsultas() {
        if (ciudad == null) ciudad = crearCiudadDemo(); //Si es nula, la creamos desde la 'base' ya establecida
        System.out.println("\n  >>> Consultas especificas:\n");
        System.out.printf("  Intersecciones en Distrito Norte : %d%n",
                ciudad.contarInterseccionesPorDistrito("Distrito Norte"));
        System.out.printf("  Intersecciones en Distrito Sur : %d%n",
                ciudad.contarInterseccionesPorDistrito("Distrito Sur"));
        System.out.printf("  Profundidad maxima de la red : %d%n",
                ciudad.profundidadMaxima());
        System.out.printf("  Total intersecciones : %d%n",
                ciudad.obtenerPorNivel(NodoN_ario.Nivel.INTERSECCION).size());
    }

    private static void demoN_arioEstadisticas() {
        if (ciudad == null) ciudad = crearCiudadDemo(); //Si es nula, la creamos desde la 'base' ya establecida
        ciudad.estadisticas();
    }

    //======================================== MENU #4 - N-ario =======================================
    private static void menuColaPrioridad() {
        boolean salir = false;
        while (!salir) {
            System.out.println("""
                                    ┌─────────────────────────────────────────┐
                                    │  Cola de Prioridad (Heap)               │
                                    ├─────────────────────────────────────────┤
                                    │  1. Demo insercion y extraccion         │
                                    │  2. Demo modificar prioridad            │
                                    │  3. Demo cambio dinamico de criterio    │
                                    │  4. Procesar 10.000 eventos             │
                                    │  5. Estadisticas de la cola             │
                                    │  0. Volver                              │
                                    └─────────────────────────────────────────┘
                    """);
            System.out.print(" Opcion: ");
            switch (leerOpcion()) {
                case 1 -> demoHeapInsertarExtraer();
                case 2 -> demoHeapModificarPrioridad();
                case 3 -> demoHeapCambioCriterio();
                case 4 -> demoHeapProcesarEventos();
                case 5 -> demoHeapEstadisticas();
                case 0 -> salir = true;
                default -> System.out.println("\n [!] Opcion no valida. \n");

            }
        }
    }

    private static void demoHeapInsertarExtraer() {
        System.out.println("\n >>> Demo insercion y extraccion de eventos (Max-Heap)...\n");
        ColaPrioridad<Evento> heap = new ColaPrioridad<>(CentroComparaciones.porPrioridadDescendente()); //Creamos la cola

        List<Evento> eventos = GeneradorDatos.eventosAleatorios(10, 100);// Generamos 10 eventos aleatorios para intersecciones con ID entre 1 y 100
        for (Evento e : eventos) {
            heap.insertar(e); //Insertamos los eventos en la cola
            System.out.printf(" + %-25s prioridad = %d%n",
                    e.getTipo().getDescripcion(), e.getPrioridad()); //Imprimimos cada uno con su respectivo formato
        }

        System.out.println("\n Extrayendo por prioridad (mayor primero):");
        while (!heap.estaVacia()) { //Mientras no este vacia
            Evento e = heap.extraer(); //Extraemos el evento
            System.out.printf(" - %-25s prioridad + %d%n",
                    e.getTipo().getDescripcion(), e.getPrioridad()); //Imprimimos cada uno con su respectivo formato
        }
    }

    private static void demoHeapModificarPrioridad() {
        System.out.println("\n  >>> Demo modificar prioridad dinamicamente...\n");
        ColaPrioridad<Evento> heap = new ColaPrioridad<>(CentroComparaciones.porPrioridadDescendente()); //Creamos la cola

        List<Evento> eventos = GeneradorDatos.eventosAleatorios(5, 50); // Generamos 5 eventos aleatorios para intersecciones con ID entre 1 y 50
        for (Evento e : eventos) heap.insertar(e); //Insertamos los eventos en nuestra cola

        Evento proximo = heap.peek(); //Peek porque nos interesa verlo, no moverlo y eliminarlo, que es lo que pasa con 'extraer()'
        System.out.printf(" Proximo a salir ANTES : %S (prioridad = %d)%n",
                proximo.getTipo().getDescripcion(), proximo.getPrioridad()); //El primero que saldria antes de la modificacion de prioridad

        Evento objetivo = eventos.get(eventos.size() - 1); //Tomamos el ultimo eventos
        int antes = objetivo.getPrioridad(); //Su prioridad anterior
        objetivo.setPrioridad(999); //Cambiamos su prioridad a un valor deseado, en este caso 999
        heap.modificarPrioridad(objetivo, objetivo); //Modificamos su posicion dentro de la cola
        System.out.printf("  Evento modificado      : %s (%d -> 999)%n",
                objetivo.getTipo().getDescripcion(), antes); //Mostramos el evento modificado
        System.out.printf("  Proximo a salir DESPUES: %s (prioridad=%d)%n",
                heap.peek().getTipo().getDescripcion(), heap.peek().getPrioridad()); //Por ultimo, como cambio la prioridad y cual seria el proximo a salir ahora
    }

    private static void demoHeapCambioCriterio() {
        System.out.println("\n  >>> Demo cambio dinamico de criterio (Max <-> Min Heap)...\n");
        ColaPrioridad<Evento> heap = new ColaPrioridad<>(CentroComparaciones.porPrioridadDescendente()); //Creamos la cola
        List<Evento> eventos = GeneradorDatos.eventosAleatorios(8, 100); // Generamos 8 eventos aleatorios para intersecciones con ID entre 1 y 100
        for (Evento e : eventos) heap.insertar(e); //Insertamos los eventos en nuestra cola

        System.out.printf("  [MAX-HEAP] Proximo: %-25s , prioridad = %d%n",
                heap.peek().getTipo().getDescripcion(), heap.peek().getPrioridad()); //Queremos mostrar el proximo a salir y su prioridad si usamos el Max-Heap

        // Lambda: cambiar a Min-Heap con lambda explicita (menor prioridad primero)
        heap.setComparator((a, b) -> Integer.compare(a.getPrioridad(), b.getPrioridad())); //Creamos y seteamos nuestro nuevo comparador
        System.out.printf("  [MIN-HEAP] Proximo: %-25s , prioridad = %d%n",
                heap.peek().getTipo().getDescripcion(), heap.peek().getPrioridad()); //Ahora, nos interesa mostrar el proximo a salir y su prioridad si usamos el Min-heap

        //volvemos a demostrar el cambio dinamico, ahora con la nueva prioridad y regresando a Max-Heap
        heap.setComparator(CentroComparaciones.porPrioridadDescendente()); //Seteamos el comparador a prioridad descendente de nuevo, para demostrar que la estructura se reorganiza correctamente
        System.out.printf("  [MAX-HEAP] Proximo: %-25s prioridad=%d%n",
                heap.peek().getTipo().getDescripcion(), heap.peek().getPrioridad());
    }

    private static void demoHeapProcesarEventos() {
        System.out.println("\n  >>> Procesando 10.000 eventos en tiempo real...\n");
        ColaPrioridad<Evento> heap =
                new ColaPrioridad<>(CentroComparaciones.porPrioridadDescendente(), 10000); //Creamos la cola, con la capacidad establecida de 10000 eventos

        List<Evento> eventos = GeneradorDatos.eventosAleatorios(10000, 5000); //Generamos 10000 eventos aleatorios para intersecciones con ID entre 1 y 5000

        Cronometro ci = new Cronometro("Heap insercion 10.000 eventos");
        ci.iniciar(); //Iniciamos el cronometro
        for (Evento e : eventos) heap.insertar(e);
        ci.finalizar(); //Finalizamos el cronometro

        Cronometro ce = new Cronometro("Heap extraccion 10.000 eventos");
        ce.iniciar(); //Iniciamos el cronometro
        int procesados = 0; //Inicializamos el contador
        while (!heap.estaVacia()) {
            heap.extraer();
            procesados++;
        } //Mientras no este vacia, extraemos los eventos y los contabilizamos (aumentamos el contador)
        ce.finalizar(); //Finalizamos el cronometro

        System.out.println("  " + ci.resumen()); //Resumen de insercion
        System.out.println("  " + ce.resumen()); //Resumen de extraccion
        System.out.printf("  Eventos procesados   : %,d%n", procesados); //Total eventos procesados
        System.out.printf("  Intercambios totales : %,d%n", heap.getIntercambiosTotales()); //Total intercambios totales
    }

    private static void demoHeapEstadisticas() {
        ColaPrioridad<Evento> heap = new ColaPrioridad<>(CentroComparaciones.porPrioridadDescendente()); //Creamos la cola
        List<Evento> eventos = GeneradorDatos.eventosAleatorios(1000, 500); // Generamos 1000 eventos aleatorios para intersecciones con ID entre 1 y 500
        for (Evento e : eventos) heap.insertar(e); //Insertamos los eventos en nuestra cola
        heap.estadisticas(); //Mostramos la estadistcias generales
    }

    //======================================== MENU #5 - Benchmarking =======================================
    private static void menuBenchmarking() {
        boolean salir = false;
        while (!salir) {
            System.out.println("""
                    
                    ┌─────────────────────────────────────────────┐
                    │  Simulacion y Benchmarking                  │
                    ├─────────────────────────────────────────────┤
                    │  1. Benchmark completo (exporta CSV)        │
                    │  2. Solo BST  (1K / 10K / 50K / 100K)       │
                    │  3. Solo AVL  (1K / 10K / 50K / 100K)       │
                    │  4. Comparacion BST vs AVL                  │
                    │  5. Benchmark Cola de Prioridad             │
                    │  0. Volver                                  │
                    └─────────────────────────────────────────────┘
                    """);
            System.out.print("  Opcion: ");
            switch (leerOpcion()) {
                case 1 -> BenchMark.ejecutarCompleto("resultados_benchmark.csv");
                case 2 -> Cronometro.imprimirTabla(BenchMark.benchmarkBST());
                case 3 -> Cronometro.imprimirTabla(BenchMark.benchmarkAVL());
                case 4 -> Cronometro.imprimirTabla(BenchMark.benchmarkComparacionBSTvsAVL());
                case 5 -> Cronometro.imprimirTabla(BenchMark.benchmarkColaPrioridad());
                case 0 -> salir = true;
                default -> System.out.println("\n  [!] Opcion no valida.\n");
            }
        }
    }

    //======================================== MENU #6 - MENU FINAL =======================================
    private static void menuIntegracion() {
        boolean salir = false;
        while (!salir) {
            System.out.println("""
                    
                    ┌─────────────────────────────────────────┐
                    │  Integracion Final del Sistema          │
                    ├─────────────────────────────────────────┤
                    │  1. Cargar ciudad desde archivo         │
                    │  2. Elegir estructura (BST o AVL)       │
                    │  3. Procesar eventos en tiempo real     │
                    │  4. Mostrar estadisticas estructurales  │
                    │  0. Volver                              │
                    └─────────────────────────────────────────┘
                    """);
            System.out.print("  Opcion: ");
            switch (leerOpcion()) {
                case 1 -> cargarCiudadDesdeArchivo();
                case 2 -> elegirEstructura();
                case 3 -> procesarEventosTiempoReal();
                case 4 -> mostrarEstadisticasEstructurales();
                case 0 -> salir = true;
                default -> System.out.println("\n  [!] Opcion no valida.\n");
            }
        }
    }

    //Para cargar la ciudad desde un archivo

    /**
     * Estamos utilizando las librerias de Java:
     * - java.io.BufferedReader;
     * - java.io.File;
     * - java.io.FileReader;
     * - java.io.IOException;
     * <p>
     * Para poder leer apropiadamente el archivo a cargar en el programa.
     * Toda su implementacion esta basada en videos y en la documentacion de JAVA
     *
     * Su funcion es:
     * Leer un archivo de texto y cargar las intersecciones en la estructura de datos seleccionada.
     * Utiliza BufferedReader para optimizar la lectura de archivos grandes.
     */
    private static void cargarCiudadDesdeArchivo() {
        System.out.print("\n  Ruta del archivo (.csv o .txt): ");
        String ruta = sc.nextLine().trim();

        File archivo = new File(ruta);
        if (!archivo.exists()) {
            System.out.println("  [!] Archivo no encontrado: " + ruta);
            System.out.println("  Formato esperado: id,nombre,avenida,zona,distrito");
            return;
        }

        // Decidimos dinámicamente qué estructura instanciar según la preferencia del usuario
        indice = usandoAVL
                ? new AVL<>(CentroComparaciones.porId())
                : new BST<>(CentroComparaciones.porId());

        int cargadas = 0;
        int errores = 0;

        //Aqui usamos un nuevo conepto: utilizamos try-with-resources para asegurar el cierre automático del archivo ----> evita memory leaks :D
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            boolean primeraLinea = true;
            while ((linea = br.readLine()) != null) {
                // Ignoramos el encabezado del CSV (id, nombre, etc.)
                if (primeraLinea) {
                    primeraLinea = false;
                    continue;
                }
                linea = linea.trim();
                if (linea.isEmpty()) continue;
                String[] p = linea.split(",");
                if (p.length < 5) {
                    errores++; // Línea mal formateada (faltan columnas)
                    continue;
                }
                try {
                    // Parsing de los datos y construcción del objeto Interseccion
                    indice.insertar(new Interseccion(
                            Integer.parseInt(p[0].trim()),
                            p[1].trim(), p[2].trim(), p[3].trim(), p[4].trim()));
                    cargadas++;
                } catch (NumberFormatException e) {
                    errores++; //Error si el ID no es un número válido
                }
            }
        } catch (IOException e) {
            System.out.println("  [!] Error leyendo archivo: " + e.getMessage());
            return;
        }

        // Reporte final de la operación y métricas de la estructura cargada
        System.out.printf("%n  Carga completada : %d intersecciones, %d errores.%n",
                cargadas, errores);
        System.out.printf("  Estructura usada : %s%n", usandoAVL ? "AVL" : "BST");
        indice.estadisticas();

    }

    //Creamos la seleccion de estructura jeje
    private static void elegirEstructura() {
        System.out.println("""

                  Elegir estructura de indexacion:
                  1. BST (Arbol Binario de Busqueda)
                  2. AVL (Arbol Balanceado)
                """);
        System.out.print("  Opcion: ");
        switch (leerOpcion()) {
            case 1 -> {
                usandoAVL = false; //Si elegimos BST, obvio no usaremos un AVL... descartamos la posibilidad
                indice    = new BST<>(CentroComparaciones.porId()); //Creamos un nuevo BST desde los ID
                System.out.println("  Estructura seleccionada: BST");
            }
            case 2 -> {
                usandoAVL = true; //Al contrario, si elgimos AVL, logicamente ni usaremos un BST... descartamos la posibilidad
                indice    = new AVL<>(CentroComparaciones.porId()); //Creamos un nuevo AVL desde los ID
                System.out.println("  Estructura seleccionada: AVL");
            }
            default -> System.out.println("  [!] Opcion no valida."); //Si el usuario no selecciona una opcion valido
        }
    }

    //Procesamiento de eventos en tiempo real... un toque extra a nuestro proyecto que nos permite demostrar su eficiencia
    private static void procesarEventosTiempoReal() {
        System.out.println("\n  >>> Procesando 1.000 eventos en tiempo real...\n");
        colaEventos = new ColaPrioridad<>(CentroComparaciones.porPrioridadDescendente(), 1000); //Creamos una cola con capcidad para 1000 eventos
        List<Evento> eventos = GeneradorDatos.eventosAleatorios(1000, 500); // Generamos 1000 eventos aleatorios para intersecciones con ID entre 1 y 500

        Cronometro c = new Cronometro("Procesamiento de 1.000 eventos"); //creamos un nuevo cornometro
        c.iniciar(); //Iniciamos el cronometro
        for (Evento e : eventos) colaEventos.insertar(e); //Insertamos los eventos en nuestra cola
        c.finalizar(); //Finalizamos el cronometro

        //Mostramos el resumen
        System.out.println("  " + c.resumen());
        System.out.println("\n  Top 5 eventos mas prioritarios:");
        System.out.println("  " + "-".repeat(52));

        // Lambda: criterio de extraccion por prioridad descendente ---> hacemos una nueva cola que nos sirva de copia
        ColaPrioridad<Evento> copia = new ColaPrioridad<>((a, b) -> Integer.compare(a.getPrioridad(), b.getPrioridad()));
        for (Evento e : colaEventos.obtenerTodos()) copia.insertar(e); //Obtiene todos los eventos en orden y los inserta en la copia

        // Extraemos y mostramos el "Top 5" de eventos con mayor prioridad.
        // Al usar extraer(), el Heap se reordena automáticamente para darnos el siguiente máximo.
        for (int i = 0; i < 5 && !copia.estaVacia(); i++) {
            Evento e = copia.extraer(); // Remueve la raíz (el de mayor prioridad)
            System.out.printf("  %d. %-25s prioridad=%-4d interseccion=%d%n",
                    i + 1, e.getTipo().getDescripcion(),
                    e.getPrioridad(), e.getInterseccionId());
        }
        System.out.println("  " + "-".repeat(52));
        // Mostramos las métricas de rendimiento (comparaciones y movimientos)
        // realizadas durante la inserción masiva de los 1,000 eventos.
        colaEventos.estadisticas();
    }

    /** Genera un reporte detallado del estado interno de todas las estructuras de datos.
     * Muestra métricas de balanceo para árboles (BST/AVL) y eficiencia para la cola de prioridad.
     */
    private static void mostrarEstadisticasEstructurales() {
        System.out.println("\n  >>> Estadisticas estructurales del sistema:\n");

        //Verificamos que exista un indice, en este caso, un Arbol en si
        if (indice == null) {
            System.out.println("  [!] Sin estructura cargada. Use opcion 1 o 2 primero.");
        } else {
            System.out.printf("  Estructura activa : %s%n%n", usandoAVL ? "AVL" : "BST");
            // Muestra las estadísticas generales comunes para cualquier árbol
            indice.estadisticas();

            // Si estamos usando un AVL, entramos a sus estadísticas específicas para ver detalles como las rotaciones y el factor de equilibrio.
            if (usandoAVL) ((AVL<Interseccion>) indice).estadisticasAVL();
        }

        // Si la ciudad ya fue creada, mostramos su estado
        if (ciudad != null) {
            System.out.println();
            ciudad.estadisticas();
        }
        // Si hay una cola de eventos activa, mostramos su eficiencia
        if (colaEventos != null) {
            System.out.println();
            colaEventos.estadisticas();
        }
    }

    //======================================= FUNCIONES AUXILIARES (para reciclar codigo) ===============================

    private static void imprimirBanner() {
        System.out.println("""

                ╔═══════════════════════════════════════════════════════════╗
                ║   MIGTU - Motor Inteligente de Gestión de Tráfico Urbano  ║
                ║   Estructura de Datos I  ·  2026S1  ·  URL               ║
                ╚═══════════════════════════════════════════════════════════╝
                """);
    }

    private static void imprimirMenuPrincipal() {
        System.out.println("""
                ┌───────────────────────────────────────────────┐
                │  Menu Principal                               │
                ├───────────────────────────────────────────────┤
                │  1. Arbol Binario de Busqueda (BST)           │
                │  2. Arbol AVL                                 │
                │  3. Arbol Multi-Camino (M-ary)                │
                │  4. Cola de Prioridad (Heap)                  │
                │  5. Simulacion y Benchmarking                 │
                │  6. Integracion Final del Sistema             │
                │  0. Salir                                     │
                └───────────────────────────────────────────────┘
                """);
        System.out.print("  Opcion: ");
    }

    private static int leerOpcion() {
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}

