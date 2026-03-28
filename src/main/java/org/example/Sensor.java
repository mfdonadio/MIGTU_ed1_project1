package org.example;

/**
 * Sensor de trafico que aplicaremos directamente a las intersecciones
 * Registra, idealmente, la velocidad promedio, volumen vehicular y estado operativo.
 */
public class Sensor {
    public enum Estado { ACTIVO, INACTIVO, FALLA}

    private final int id;
    private final int interseccionId;
    private double velocidadPromedio; // en Km/h
    private int volumenVehicular; // vehiculos/hora
    private Estado estado;

    public Sensor(int id, int interseccionId) {
        this.id = id;
        this.interseccionId = interseccionId;
        this.velocidadPromedio = 0.0;
        this.volumenVehicular = 0;
        this.estado = Estado.ACTIVO;
    }

    /**
     * Tenemos que, de cierta forma, calcular el nivel de congestion de (0-100)
     * basado en la velocidad promedio y el volumen vehicular.
     * Por ejemplo: a menor velocidad promedio y mayor volumen, entonces habria una mayor congestion.
     */
    public int calcularNivelCongestion(double velocidadMaxima, int capacidadMaxima){
        if(estado != Estado.ACTIVO) return 0; //Si esta inactivo o fallando, su calculo siempre sera 0.
        double factorVelocidad = 1.0 - (velocidadPromedio / velocidadMaxima);
        double factorVolumen = (double) volumenVehicular / capacidadMaxima;
        return (int) Math.min(100, ((factorVolumen + factorVelocidad)/2.0)*100);
    }

    //Formato para el string, otra vez
    @Override
    public String toString(){
        return String.format("Sensor{ID = %d, Insterseccion = %d, Velocidad Promedio = %.1f km/h," +
                "Volumen Vehicular = %d veh/h, Estado = %s}",
                id, interseccionId, velocidadPromedio, volumenVehicular,
                estado);
    }

    //Getter y setters
    public int getId() { return id; }
    public int getInterseccionId() { return interseccionId; }
    public double getVelocidadPromedio() { return velocidadPromedio; }
    public int getVolumenVehicular() { return volumenVehicular; }
    public Estado getEstado() { return estado; }

    public void setVelocidadPromedio(double v) {this.velocidadPromedio = Math.max(0 , v);}
    public void setVolumenVehicular(int v) {this.volumenVehicular = Math.max(0 , v);}
    public void setEstado(Estado e) {this.estado = e;}



}
