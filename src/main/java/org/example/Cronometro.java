package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/*NOTA: IMPLEMENTACION A PARTIR DE DOCUMENTACION, VIDEOS E IA*/

/**
 * Esta clase nos va a ayudar para hacer el benchmarking de las estructuras de datos.
 *
 * Uso basico:
 *  Cronometro c = new Cronometro("BST insercion 10000");
 *  c.iniciar();
 *  // ... operacion a medir ...
 *  c.detener();
 *  System.out.println(c.resumen());
 *
 *  Ademas, podriamos exportar multiples mediciones a CSV, mas o menos asi:
 *      Cronometro.exportarCSV("resultados.csv", listaMediciones);
 */

public class Cronometro {

    //Clase interna: para mediciones individuales
    public static class Medicion {
        public final String etiqueta;
        public final long tiempoNanoSegundos;
        public final long elementos;
        public final long operaciones;

        //Constructor
        public Medicion(String etiqueta, long tiempoNanoSegundos, long elementos, long operaciones) {
            this.etiqueta = etiqueta;
            this.tiempoNanoSegundos = tiempoNanoSegundos;
            this.elementos = elementos;
            this.operaciones = operaciones;
        }

        //Pasar el tiempo de nanosegundos a milisegundos
        public double tiempoMiliSegundos() {
            return tiempoNanoSegundos / 1000000.0;
        }

        //Sacar el tiempo promedio en nanosegundos
        public double tiempoPromedioNanoSegundos() {
            return operaciones > 0 ? (double) tiempoNanoSegundos / operaciones : 0;
        }

        //Formato para el string
        @Override
        public String toString() {
            return String.format("%-35s | %10.3f ms | %8d elem | %8d ops | %8.1f ns/op",
                    etiqueta, tiempoMiliSegundos(), elementos, operaciones, tiempoPromedioNanoSegundos());
        }

        //Para la importacion y creacion del CSV
        public String toCSV() {
            return String.format("%s,%.3f,%d,%d,%.1f",
                    etiqueta, tiempoMiliSegundos(), elementos, operaciones, tiempoPromedioNanoSegundos());
        }
    }

    //En general, el estado interno del cronometro
    private final String etiqueta;
    private long inicio;
    private long fin;
    private long elementos;
    private long operaciones;
    private boolean corriendo;

    //Constructor
    public Cronometro(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    //Interfaz de medicion (segun lo investigado, como una API para las mediciones)
    public void iniciar(){
        corriendo = true;
        inicio = System.nanoTime();
    }

    public void finalizar(){
        fin = System.nanoTime();
        corriendo = false;
    }

    public Medicion registrar(long elementos, long operaciones){
        if(corriendo) finalizar();
        this.elementos = elementos;
        this.operaciones = operaciones;
        return new Medicion(etiqueta, fin - inicio, elementos, operaciones);
    }

    public long tiempoNanoSegundos(){return fin - inicio;}
    public double tiempoMiliSegundos(){return tiempoNanoSegundos()/1000000.0;}

    public String resumen(){
        return String.format("[%s] %.3f ms (%,d ns)", etiqueta, tiempoMiliSegundos(), tiempoNanoSegundos());
    }

    //Por ultimo, la exportacion a CSV
    public static void exportarCSV(String ruta, List<Medicion> mediciones) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ruta))) {
            pw.println("etiqueta,tiempo_ms,elementos,operaciones,ns_por_operacion");
            for (Medicion m : mediciones) {
                pw.println(m.toCSV());
            }
            System.out.println("[CSV] Exportado: " + ruta + " (" + mediciones.size() + " filas)");
        } catch (IOException e) {
            System.err.println("[CSV] Error al exportar: " + e.getMessage());
        }
    }

    //Imprimir una tabla de resultados con encabezado formateado.
    public static void imprimirTabla(List<Medicion> mediciones) {
        System.out.println("\n" + "=".repeat(95));
        System.out.printf("%-35s | %10s | %8s | %8s | %10s%n",
                "Operacion", "Tiempo ms", "Elementos", "Ops", "ns/op");
        System.out.println("-".repeat(95));
        for (Medicion m : mediciones) {
            System.out.println(m);
        }
        System.out.println("=".repeat(95));
    }
}
