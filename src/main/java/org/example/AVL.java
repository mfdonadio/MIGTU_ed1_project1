package org.example;

import java.util.Comparator;

/**
 * lA idea es implementar el AVL que hicimos en el lab 10, pero intentando utilizar el codigo empleado en el BST.
 *
 * Ademas, tenemos que contabilizar las rotaciones totales, ya sean simple o dobles, para poder estimar su rendimiento
 * y eficacia en situaciones de la vida diaria.
 */
public class AVL<T> extends BST<T> {

    //Contadores
    private long rotacionesSimples;
    private long rotacionesDobles;

    //Constructor
    public AVL(Comparator<T> comparador) {
        super(comparador); //Hereda del comparador, usado en el BST.
        this.rotacionesSimples = 0;
        this.rotacionesDobles  = 0;
    }

    @Override
    public boolean insertar(T valor) {
        int antes = contarNodos();
        raiz = insertar(raiz, valor);
        return contarNodos() > antes;
    }

    @Override
    public void eliminar(T valor) {
        raiz = eliminar(raiz, valor);
    }

    private Nodo<T> insertar(Nodo<T> nodo, T valor) {
        if (nodo == null) return new Nodo<>(valor);

        int cmp = comparador.compare(valor, nodo.dato);

        if (cmp < 0) {
            nodo.izquierdo = insertar(nodo.izquierdo, valor);
        } else if (cmp > 0) {
            nodo.derecho = insertar(nodo.derecho, valor);
        } else {
            return nodo;
        }

        nodo.altura = 1 + Math.max(getAltura(nodo.izquierdo), getAltura(nodo.derecho));

        int balance = getBalance(nodo);

        // Izquierda-Izquierda: hijo izquierdo pesa mas, insercion fue a la izquierda.
        if (balance > 1 && comparador.compare(valor, nodo.izquierdo.dato) < 0)
            return rotarDerecha(nodo);

        // Derecha-Derecha: hijo derecho pesa mas, insercion fue a la derecha.
        if (balance < -1 && comparador.compare(valor, nodo.derecho.dato) > 0)
            return rotarIzquierda(nodo);

        // Izquierda-Derecha: doble rotacion, primero izquierda luego derecha.
        if (balance > 1) {
            // Las 2 rotaciones simples internas ya se contaron dentro de rotarX()
            // restamos 2 y sumamos 1 doble para que el conteo sea correcto
            rotacionesSimples--;
            rotacionesDobles++;
            nodo.izquierdo = rotarIzquierda(nodo.izquierdo);
            return rotarDerecha(nodo);
        }

        // Derecha-Izquierda: doble rotacion, primero derecha luego izquierda
        if (balance < -1) {
            //lO MISMO AQUI
            rotacionesSimples--;
            rotacionesDobles++;
            nodo.derecho = rotarDerecha(nodo.derecho);
            return rotarIzquierda(nodo);
        }

        return nodo;
    }

    private Nodo<T> eliminar(Nodo<T> nodo, T valor) {
        if (nodo == null) return null;

        int cmp = comparador.compare(valor, nodo.dato);

        if (cmp < 0) {
            nodo.izquierdo = eliminar(nodo.izquierdo, valor);
        } else if (cmp > 0) {
            nodo.derecho = eliminar(nodo.derecho, valor);
        } else {
            if (nodo.izquierdo == null || nodo.derecho == null) {
                Nodo<T> aux = (nodo.izquierdo != null) ? nodo.izquierdo : nodo.derecho;
                nodo = (aux == null) ? null : aux;
            } else {
                // Dos hijos: reemplazar con el predecesor inorden (maximo del subarbol izquierdo)
                Nodo<T> tmp = getMaxValNode(nodo.izquierdo);
                nodo.dato = tmp.dato;
                nodo.izquierdo = eliminar(nodo.izquierdo, tmp.dato);
            }
        }

        if (nodo == null) return null;

        nodo.altura = 1 + Math.max(getAltura(nodo.izquierdo), getAltura(nodo.derecho));

        int balance = getBalance(nodo);

        if (balance > 1 && getBalance(nodo.izquierdo) >= 0)
            return rotarDerecha(nodo);

        if (balance < -1 && getBalance(nodo.derecho) <= 0)
            return rotarIzquierda(nodo);

        if (balance > 1 && getBalance(nodo.izquierdo) < 0) {
            rotacionesSimples--;
            rotacionesDobles++;
            nodo.izquierdo = rotarIzquierda(nodo.izquierdo);
            return rotarDerecha(nodo);
        }

        if (balance < -1 && getBalance(nodo.derecho) > 0) {
            rotacionesSimples--;
            rotacionesDobles++;
            nodo.derecho = rotarDerecha(nodo.derecho);
            return rotarIzquierda(nodo);
        }

        return nodo;
    }

    private Nodo<T> rotarIzquierda(Nodo<T> nodo) {
        Nodo<T> nuevaRaiz   = nodo.derecho;
        nodo.derecho        = nuevaRaiz.izquierdo;
        nuevaRaiz.izquierdo = nodo;
        actualizarAltura(nodo);
        actualizarAltura(nuevaRaiz);
        rotacionesSimples++;
        return nuevaRaiz;
    }

    private Nodo<T> rotarDerecha(Nodo<T> nodo) {
        Nodo<T> nuevaRaiz  = nodo.izquierdo;
        nodo.izquierdo     = nuevaRaiz.derecho;
        nuevaRaiz.derecho  = nodo;
        actualizarAltura(nodo);
        actualizarAltura(nuevaRaiz);
        rotacionesSimples++;
        return nuevaRaiz;
    }

    private void actualizarAltura(Nodo<T> nodo) {
        if (nodo == null) return;
        nodo.altura = 1 + Math.max(getAltura(nodo.izquierdo), getAltura(nodo.derecho));
    }

    private int getAltura(Nodo<T> nodo) {
        return nodo == null ? 0 : nodo.altura;
    }

    private int getBalance(Nodo<T> nodo) {
        return nodo == null ? 0 : getAltura(nodo.izquierdo) - getAltura(nodo.derecho);
    }

    private Nodo<T> getMaxValNode(Nodo<T> nodo) {
        Nodo<T> aux = nodo;
        while (aux.derecho != null) aux = aux.derecho;
        return aux;
    }

    public long getRotacionesSimples() { return rotacionesSimples; }
    public long getRotacionesDobles()  { return rotacionesDobles; }
    public long getRotacionesTotales() { return rotacionesSimples + rotacionesDobles * 2; }
    public int  getFactorBalance()     { return getBalance(raiz); }

    @Override
    public void resetearMetricas() {
        super.resetearMetricas();
        rotacionesSimples = 0;
        rotacionesDobles  = 0;
    }

    public void estadisticasAVL() {
        System.out.println("=".repeat(45));
        System.out.println("  Estadisticas AVL");
        System.out.println("=".repeat(45));
        System.out.printf("  Altura              : %d%n",  altura());
        System.out.printf("  Nodos totales       : %d%n",  contarNodos());
        System.out.printf("  Hojas               : %d%n",  contarHojas());
        System.out.printf("  Balanceado          : %s%n",  estaBalanceada());
        System.out.printf("  Factor balance raiz : %d%n",  getFactorBalance());
        System.out.printf("  Rotaciones simples  : %,d%n", rotacionesSimples);
        System.out.printf("  Rotaciones dobles   : %,d%n", rotacionesDobles);
        System.out.printf("  Rotaciones totales  : %,d%n", getRotacionesTotales());
        System.out.println("=".repeat(45));
    }
}