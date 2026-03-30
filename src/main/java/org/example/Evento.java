package org.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Representa un evento critico en la red de simulacion (accidente, congestion, emergencia, clima).
 * Implementa Comparable para el ordenamiento, en este caso natural por prioridad descendente
 *
 * La prioridad de los eventos es un valor entero: mayor número = mayor urgencia
 */
public class Evento implements Comparable<Evento> {

    // Definimos el formateador como una constante para que toString lo use
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("HH:mm:ss");

    // ENUM: aquí listamos los eventos, su descripcion y su prioridad
    public enum TipoEvento {
        ACCIDENTE_GRAVE(100, "Accidente grave"),
        AMBULANCIA_EN_RUTA(90, "Ambulancia en ruta"),
        CONGESTION_ALTA(70, "Congestion alta"),
        SEMAFORO_DESCOMPUESTO(50, "Semaforo descompuesto"),
        EVENTO_CLIMATICO(40, "Evento climatico"),
        CONGESTION_MEDIA(30, "Congestion media"),
        REPORTE_GENERAL(10, "Reporte general");

        private final int prioridadBase;
        private final String descripcion;

        TipoEvento(int prioridadBase, String descripcion) {
            this.prioridadBase = prioridadBase;
            this.descripcion = descripcion;
        }

        public int getPrioridadBase() { return prioridadBase; }
        public String getDescripcion() { return descripcion; } // Necesario para el toString
    }

    // Atributos
    private static int contadorGlobal = 0;
    private final int id;
    private final TipoEvento tipo;
    private int prioridad;
    private final int interseccionId;
    private final LocalDateTime timestamp; //Nos ayuda con la antigüedad de los eventos
    private String descripcion;

    // Constructor
    public Evento(TipoEvento tipo, int interseccionId, String descripcion) {
        this.id = ++contadorGlobal;
        this.tipo = tipo;
        this.prioridad = tipo.getPrioridadBase();
        this.interseccionId = interseccionId;
        this.timestamp = LocalDateTime.now();
        this.descripcion = descripcion;
    }

    // compareTo: Mayor prioridad de primero (Priority Queue)
    @Override
    public int compareTo(Evento otro) {
        /* Asegurarnos que la cola de prioridad saque primero al de mayor prioridad.
           Al comparar 'otro' con 'this', invertimos el orden natural. */
        return Integer.compare(otro.prioridad, this.prioridad);
    }

    // toString: nos permite y ayuda a definir un formato para la salida de nuestro objeto
    @Override
    public String toString() {
        return String.format("Evento{ID = %d, Tipo = '%s', Prioridad = %d, " +
                        "Interseccion = %d, Hora = %s}",
                id, tipo.getDescripcion(), prioridad,
                interseccionId, timestamp.format(FMT));
    }

    // Getters y Setters
    public int getId() { return id; }
    public TipoEvento getTipo() { return tipo; }
    public int getPrioridad() { return prioridad; }
    public int getInterseccionId() { return interseccionId; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getDescripcion() { return descripcion; }

    // Nos permite modificar la prioridad de la cola dinamicamente.
    public void setPrioridad(int prioridad) {
        this.prioridad = Math.max(0, prioridad);
    }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
