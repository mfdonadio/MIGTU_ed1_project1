package org.example;

import org.example.CentroComparaciones;
import org.example.AVL;
import org.example.BST;
import org.example.ColaPrioridad;
import org.example.Evento;
import org.example.Interseccion;
import org.example.Cronometro;
import org.example.Cronometro.Medicion;
import org.example.GeneradorDatos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Benchmarking completo del sistema MIGTU.
 *
 * Simula inserciones y busquedas sobre BST y AVL con:
 *   - 1.000 intersecciones
 *   - 10.000 intersecciones
 *   - 50.000 intersecciones
 *   - 100.000 intersecciones
 *
 * Mide tiempos con System.nanoTime() via Cronometro.
 * Exporta resultados a CSV para el documento tecnico.
 */

/**IMPORTANTE:
 * CLASE totalmente adaptada de la documentacion oficial de Java y de la IA.
 * Esta clase es la unica en la que encontramos la necesidad de utilizar codigo que no es de nuestra autoria en su totalidad.
 * Sin embargo, realizamos un gran esfuerzo en adaptar cada parte del mismo con el fin de entregar un proyecto excelente.
 */
public class BenchMark {

    private static final int[] TAMANIOS = {1_000, 10_000, 50_000, 100_000};

    // -------------------------------------------------------------------------
    // EJECUTAR TODO
    // -------------------------------------------------------------------------

    public static void ejecutarCompleto(String rutaCSV) {
        List<Medicion> mediciones = new ArrayList<>();

        System.out.println("\n>>> Iniciando Benchmark completo...\n");

        mediciones.addAll(benchmarkBST());
        mediciones.addAll(benchmarkAVL());
        mediciones.addAll(benchmarkComparacionBSTvsAVL());
        mediciones.addAll(benchmarkColaPrioridad());

        Cronometro.imprimirTabla(mediciones);
        Cronometro.exportarCSV(rutaCSV, mediciones);
    }

    // -------------------------------------------------------------------------
    // BST BENCHMARK
    // -------------------------------------------------------------------------

    public static List<Medicion> benchmarkBST() {
        List<Medicion> mediciones = new ArrayList<>();
        BST<Interseccion> bst =
                new BST<>(CentroComparaciones.porId());

        for (int n : TAMANIOS) {
            // --- Insercion aleatoria (caso promedio) ---
            List<Interseccion> datos = GeneradorDatos.interseccionesAleatorias(n);
            bst.limpiar();

            Cronometro c = new Cronometro("BST insercion aleatoria n=" + n);
            c.iniciar();
            for (Interseccion i : datos) bst.insertar(i);
            c.finalizar();
            mediciones.add(c.registrar(n, bst.getInsercionesCont()));

            // --- Busqueda aleatoria (50 busquedas) ---
            bst.resetearMetricas();
            List<Interseccion> muestra = datos.subList(0, Math.min(50, datos.size()));
            Cronometro cb = new Cronometro("BST busqueda aleatoria n=" + n);
            cb.iniciar();
            for (Interseccion i : muestra) bst.buscar(i);
            cb.finalizar();
            mediciones.add(cb.registrar(n, bst.getBusquedaCont()));

            // --- Insercion ordenada (peor caso) ---
            List<Interseccion> ordenados = GeneradorDatos.interseccionesOrdenadas(n);
            bst.limpiar();
            Cronometro co = new Cronometro("BST insercion ordenada (peor) n=" + n);
            co.iniciar();
            for (Interseccion i : ordenados) bst.insertar(i);
            co.finalizar();
            mediciones.add(co.registrar(n, bst.getInsercionesCont()));

            System.out.printf("[BST] n=%-7d altura_aleatorio=N/A | altura_ordenado=%d%n",
                    n, bst.altura());
        }

        return mediciones;
    }

    // -------------------------------------------------------------------------
    // AVL BENCHMARK
    // -------------------------------------------------------------------------

    public static List<Medicion> benchmarkAVL() {
        List<Medicion> mediciones = new ArrayList<>();
        AVL<Interseccion> avl =
                new AVL<>(CentroComparaciones.porId());

        for (int n : TAMANIOS) {
            // --- Insercion aleatoria ---
            List<Interseccion> datos = GeneradorDatos.interseccionesAleatorias(n);
            avl.limpiar();

            Cronometro c = new Cronometro("AVL insercion aleatoria n=" + n);
            c.iniciar();
            for (Interseccion i : datos) avl.insertar(i);
            c.finalizar();
            mediciones.add(c.registrar(n, avl.getInsercionesCont()));

            // --- Busqueda ---
            avl.resetearMetricas();
            List<Interseccion> muestra = datos.subList(0, Math.min(50, datos.size()));
            Cronometro cb = new Cronometro("AVL busqueda aleatoria n=" + n);
            cb.iniciar();
            for (Interseccion i : muestra) avl.buscar(i);
            cb.finalizar();
            mediciones.add(cb.registrar(n, avl.getBusquedaCont()));

            // --- Insercion ordenada (AVL mantiene balance) ---
            List<Interseccion> ordenados = GeneradorDatos.interseccionesOrdenadas(n);
            avl.limpiar();
            Cronometro co = new Cronometro("AVL insercion ordenada n=" + n);
            co.iniciar();
            for (Interseccion i : ordenados) avl.insertar(i);
            co.finalizar();
            mediciones.add(co.registrar(n, avl.getInsercionesCont()));

            System.out.printf("[AVL] n=%-7d altura_ordenado=%d | rotaciones=%,d%n",
                    n, avl.altura(), avl.getRotacionesTotales());
        }

        return mediciones;
    }

    // -------------------------------------------------------------------------
    // COMPARACION BST vs AVL (la clave del documento tecnico)
    // -------------------------------------------------------------------------

    public static List<Medicion> benchmarkComparacionBSTvsAVL() {
        List<Medicion> mediciones = new ArrayList<>();

        for (int n : TAMANIOS) {
            List<Interseccion> ordenados = GeneradorDatos.interseccionesOrdenadas(n);

            BST<Interseccion> bst =
                    new BST<>(CentroComparaciones.porId());
            AVL<Interseccion> avl =
                    new AVL<>(CentroComparaciones.porId());

            for (Interseccion i : ordenados) {
                bst.insertar(i);
                avl.insertar(i);
            }

            System.out.printf(
                    "[COMPARACION n=%-7d] BST altura=%d | AVL altura=%d | " +
                            "BST comparaciones=%,d | AVL rotaciones=%,d%n",
                    n, bst.altura(), avl.altura(),
                    bst.getComparacionesTotales(), avl.getRotacionesTotales());

            // Registrar alturas como medicion
            Medicion mBST = new Medicion("ALTURA BST ordenado n=" + n, 0, n, bst.altura());
            Medicion mAVL = new Medicion("ALTURA AVL ordenado n=" + n, 0, n, avl.altura());
            mediciones.add(mBST);
            mediciones.add(mAVL);
        }

        return mediciones;
    }

    // -------------------------------------------------------------------------
    // COLA DE PRIORIDAD BENCHMARK
    // -------------------------------------------------------------------------

    public static List<Medicion> benchmarkColaPrioridad() {
        List<Medicion> mediciones = new ArrayList<>();

        int[] tamaniosEventos = {1_000, 5_000, 10_000};

        for (int n : tamaniosEventos) {
            List<Evento> eventos = GeneradorDatos.eventosAleatorios(n, 1000);

            // --- Heap: insercion ---
            ColaPrioridad<Evento> heap =
                    new ColaPrioridad<>(CentroComparaciones.porPrioridadDescendente());

            Cronometro ci = new Cronometro("Heap insercion n=" + n);
            ci.iniciar();
            for (Evento e : eventos) heap.insertar(e);
            ci.finalizar();
            mediciones.add(ci.registrar(n, heap.getInsercionesCont()));

            // --- Heap: extraccion de 10.000 eventos ---
            Cronometro ce = new Cronometro("Heap extraccion n=" + n);
            ce.iniciar();
            while (!heap.estaVacia()) heap.extraer();
            ce.finalizar();
            mediciones.add(ce.registrar(n, heap.getExtraccionesCont()));

            // --- Lista ordenada: comparacion ---
            List<Evento> listaOrdenada = new ArrayList<>(eventos);
            Cronometro cl = new Cronometro("Lista ordenada insercion n=" + n);
            cl.iniciar();
            for (Evento e : eventos) {
                listaOrdenada.add(e);
                // insertion sort en cada insercion (peor caso lista)
                Collections.sort(listaOrdenada, CentroComparaciones.porPrioridadDescendente());
            }
            cl.finalizar();
            mediciones.add(cl.registrar(n, n));

            System.out.printf("[HEAP] n=%-6d intercambios=%,d%n",
                    n, heap.getIntercambiosTotales());
        }

        return mediciones;
    }
}