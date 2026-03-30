package org.example;

import org.example.Evento;
import org.example.Interseccion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Tal y como lo indica el nombre, esta clase nos permitira generar datos de prueba
 * para simulaciones y benchmarking.
 *
 * Amen porque funcione jeje
 */
public final class GeneradorDatos {
    private GeneradorDatos() {}

    private static final Random random = new Random(42); //Semilla fija

    private static final String[] DISTRITOS = {"Norte", "Sur", "Este", "Oeste", "Centro"};
    private static final String[] ZONAS = {"Z1", "Z2",  "Z3", "Z4", "Z5", "Z6", "Z7", "Z8",
                                            "Z9", "Z10", "Z11", "Z12", "Z13", "Z14", "Z15",
                                            "Z16", "Z17", "Z18", "Z19", "Z20", "Z21",};

    private static final String[] AVENIDAS = {"Reforma", "Petapa", "Bolivar", "Aguilar Batres",
                                                "Vista Hermosa","Las Americas", "Hincapie", "Roosevelt",
                                                "Centra Norte", "Centra Sur"};

    //INTERSECCIONES

    /**INTERSECCIONES ALEATORIAS:
     * la idea es poder generar 'n' intersecciones con IDs en orden ALEATORIO.
    Simulando, para estos fines, el caso promedio para el BST y el AVL.*/
    public static List<Interseccion> interseccionesAleatorias(int n){
        List<Integer> ids = new ArrayList<>();
        for(int i = 0; i < n; i++) ids.add(i);
        Collections.shuffle(ids, random); //Mezcla de manera aletoria la lista de IDs creada

        List<Interseccion> lista = new ArrayList<>(n);
        for(int id : ids){
            lista.add(crearInterseccion(id));
        }
        return lista;
    }

    /**INTERSECCIONES ORDENADA:
     * similar al anterior, pero aquí la idea es generar esas 'n' intersecciones con IDs en
     * orden ASCENDENTE. Basandonos en la teoria, esto permitiria simular el peor caso para un
     * BST........ un arbol degenerado. La idea es exponer el codigo a lo peor no?????
     */
    public static List<Interseccion> interseccionesOrdenadas(int n){
        List<Interseccion> lista = new ArrayList<>(n);
        for(int i = 0; i < n; i++){
            lista.add(crearInterseccion(i));
        }
        return lista;
    }

    /**INTERSECCIONES DESCENDENTES:
     * lo mismo que ordenadas, pero ahora con el orden invertido, ósea, descendentemente
     */
    public static List<Interseccion> interseccionesDescendentes(int n){
        List<Interseccion> lista = new ArrayList<>(n);
        for(int i = n; i >= 1; i--){ //Recorre la lista al revés
            lista.add(crearInterseccion(i));
        }
        return lista;
    }

    /**CREAR LAS INTERSECCIONES, INDEPENDIENTEMENTE DEL CASO*/
    public static Interseccion crearInterseccion(int id){
        String distrito =  DISTRITOS[random.nextInt(DISTRITOS.length)];
        String zona = ZONAS[random.nextInt(ZONAS.length)];
        String avenida = AVENIDAS[random.nextInt(AVENIDAS.length)];
        String nombre = "INTERSECCION -" + id + " - " + avenida.substring(0,3).toUpperCase();

        Interseccion interseccion = new Interseccion(id, nombre, avenida, zona, distrito);
        interseccion.setNivelCongestion(random.nextInt(101));
        interseccion.setNivelCongestion(random.nextInt(11));

        return interseccion;
    }

    //EVENTO
    /**EVENTOS ALEATORIOS:
     * parecido a lo que hicimos con las intersecciones.
     * Generará 'n' eventos aleatorios para las intersecciones, en un rango de [1, maxInterseccionId]
     */
    public static List<Evento> eventosAleatorios(int n, int maxInterseccionId){
        Evento.TipoEvento[] tipos = Evento.TipoEvento.values();
        List<Evento> lista = new ArrayList<>(n);
        for(int i = 0; i < n; i++){
            Evento.TipoEvento tipo = tipos[random.nextInt(tipos.length)];
            int interseccionId = random.nextInt(maxInterseccionId) + 1;
            lista.add(new Evento(tipo, interseccionId, tipo.getDescripcion() +
                    " en la INTERSECCION - " + interseccionId));
        }
        return lista;
    }

    //UTIL: recomendacion de la IA y de documentacion
    /** Nos ayudará a devolver una copia mezclada de la lista, sin ningun tipo de modificacion
     * en la original. Como para tener una idea de que es lo que generamos y predecir el coportamiento
     * o encontrar mas fácilmente errores importantes.
     */
    public static <T> List<T> mezclar(List<T> lista) {
        List<T> copia = new ArrayList<>(lista);
        Collections.shuffle(copia, random);
        return copia;

    }
}
