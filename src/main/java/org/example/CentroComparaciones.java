package org.example;
import java.util.Comparator;

/**
 * El proyecto nos solicita poder cambiar los criterios de ordenamiento asi que
 * consideramos una mejor opcion tener una clase dedicada a ello.
 * Esta sera nuestra 'fabrica' de Comparators para Interseccion y Evento.
 * Aqui podemos usar lambdas.
 * IMPORTANTE: las ED no pueden ser modificadas.
 */
public final class CentroComparaciones {
    private CentroComparaciones(){}

    //COMPARATORS PARA INTERSECCION

    //1. Criterio natural: por ID ascendente (osea, criterior por defecto del BST/AVL)
    public static Comparator<Interseccion> porId(){
        return Comparator.comparingInt(Interseccion::getId);
    }

    //2, Por nivel de congestion descendente (mas congestionada primero ---> menos congestionada de ultimo)
    public static Comparator<Interseccion> porCongestionDescendente(){
        return Comparator.comparingInt(Interseccion::getNivelCongestion).reversed();
    }

    //3. Por nivel de congestion ascendente (menos congestionada primero ---> mas congestionado de ultimo)
    public static Comparator<Interseccion> porCongestionAscendente(){
        return Comparator.comparingInt(Interseccion::getNivelCongestion)
                .thenComparingInt(Interseccion::getId);
    }

    //4. Por nivel de riesgo descendente (mas riesgosa primero)
    public static Comparator<Interseccion> porRiesgoDescendente(){
        return Comparator.comparingInt(Interseccion::getNivelRiesgo).reversed();
    }

    //5. Por zona, y despues por ID
    public static Comparator<Interseccion> porZonaLuegoId(){
        return Comparator.comparing(Interseccion::getZona)
                .thenComparingInt(Interseccion::getId);
    }

    //6. Por distrito, luego por zona, luego por ID
    public static Comparator<Interseccion> porJerarquia(){
        return Comparator.comparing(Interseccion::getDistrito)
                .thenComparing(Interseccion::getZona)
                .thenComparingInt(Interseccion::getId);
    }

    //COMPARATORS PARA EVENTO

    //1. Por prioridad descendente (evento mas urgente primero)
    public static Comparator<Evento> porPrioridadDescendente(){
        return (a, b) -> Integer.compare(a.getPrioridad(), b.getPrioridad());
    }

    //2. Por prioridad ascendente (avento menos urgente de primero)
    public static Comparator<Evento> porPrioridadAscendente(){

        return Comparator.comparing(Evento::getPrioridad);
    }

    //3. Por 'timestamp' ascendete (evento mas antiguo primero, algo asi como FIFO)
    public static Comparator<Evento> porTiempo(){
        return Comparator.comparing(Evento::getTimestamp);
    }

    //4. Por tipo de evento basandonos en su prioridad base y liefo por 'timestamp'
    public static Comparator<Evento> porTipoLuegoTiempo(){
        return Comparator.comparingInt((Evento evento)-> evento.getTipo().getPrioridadBase())
                .reversed()
                .thenComparing(Evento::getTimestamp);
    }

    //5. Por interseccion afectada (util en caso de agrupaar eventos de una misma zona)
    public static Comparator<Evento> porInterseccion(){
        return Comparator.comparingInt(Evento::getInterseccionId);
    }
}
