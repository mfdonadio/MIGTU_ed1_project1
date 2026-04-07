package org.example;

/**
 * La clase interseccion es la encargada de representar
 * una interseccion vial dentro de la simulacion de
 * la red urbana.
 * Podemos verla como la entidad principal a indexar en
 * el BST y AVL
 * Implementa de una el comparable para el ordenamiento
 * por ID (a implementarse y evaluarse claro)
 */
public class Interseccion implements Comparable<Interseccion> {

    private Sensor sensor;
    private final int id;
    private String nombre;
    private String avenida;
    private String zona;
    private String distrito;
    private int nivelCongestion; // 0 (libre) - 100 (bloqueado)
    private int nivelRiesgo; // 0 (sin riesgo) - 10 (critico)
    private boolean activa;

    //Constructor
    public Interseccion(int id, String nombre, String avenida, String zona,
                        String distrito){
        this.id = id;
        this.nombre = nombre;
        this.avenida = avenida;
        this.zona = zona;
        this.distrito = distrito;
        this.nivelCongestion = 0;
        this.nivelRiesgo = 0;
        this.sensor = new Sensor(id, id);
        this.activa = false;
    }

    //Comparable: ordenamiento natural por ID
    @Override
    public int compareTo(Interseccion otra){
        return Integer.compare(this.id, otra.id);
    }

    /* Equals: nos asegura de que Java entienda que dos objetos distintos
    en memoria son el 'mismo' si comparten su ID. Se acerca a la realidad.*/

    @Override
    public boolean equals(Object otra){
        if(this == otra) return true; //Verifica si los objetos son los mismos.
        if(!(otra instanceof Interseccion)) return false; //Verifica de comparar una Interseccion con otra Interseccion
        return this.id == ((Interseccion) otra).id;//Aunque sean dos objetos diferentes, aqui el programa decide si son lo mismo por su ID
    }

    /* hashCode: nos permite que Java genere un numero exclusivo para cada id, osea una 'etiqueta'.
       Esta posibilidad nos permite ahorrarnos el tener que comparar x cantidad de ids para ver si estan repetidos.*/
    @Override
    public int hashCode(){
        return Integer.hashCode(id);//Convierte el id en una direccion de busqueda rapida.
    }

    /*toString: nos permite y ayuda a definir un formato para la salida de nuestro objeto*/
    @Override
    public String toString(){
        return String.format("Interseccion{ID = %d, Nombre = '%s', Zona = '%s'," +
                "Nivel de Congestion = %d, Nivel de Riesgo = %d, Activa = %b}", id, nombre, zona,
                nivelCongestion, nivelRiesgo, activa);
    }

    //Getters y Setters

    public int getId() { return id;}
    public String getNombre() { return nombre;}
    public String getAvenida() { return avenida;}
    public String getZona() { return zona;}
    public String getDistrito() { return distrito;}
    public int getNivelCongestion() { return nivelCongestion;}
    public int getNivelRiesgo() { return nivelRiesgo;}
    public boolean isActiva() { return activa;}
    public Sensor getSensor() {return sensor;}

    public void setNombre(String nombre) { this.nombre = nombre;}
    public void setAvenida(String avenida) { this.avenida = avenida;}
    public void setZona(String zona) { this.zona = zona;}
    public void setDistrito(String distrito) { this.distrito = distrito;}
    public void setNivelRiesgo(int nivel) {this.nivelRiesgo = Math.max(0, Math.min(10,nivel));}
    public void setActiva(boolean activa) { this.activa = activa;}

    //Implemtacion final de sensor ---> tiene como proposito el ser dinamico jeje.. algo mas que un simple setter
    public void actualizarCongestionDinamica(double velMax, int capMax){
        this.nivelCongestion = sensor.calcularNivelCongestion(velMax, capMax);
    }
}
