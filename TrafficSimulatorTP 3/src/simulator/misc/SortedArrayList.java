package simulator.misc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

public class SortedArrayList<E> extends ArrayList<E> {

    // Serial version UID para la serialización de objetos
    private static final long serialVersionUID = 1L;

    // Comparador utilizado para ordenar los elementos
    private Comparator<E> _cmp;

    // Constructor que recibe un comparador para ordenar los elementos
    public SortedArrayList(Comparator<E> cmp) {
        super(); // Llama al constructor de ArrayList
        _cmp = cmp; // Asigna el comparador proporcionado
    }

    // Constructor sin parámetros, usa el comparador natural (Comparable)
    public SortedArrayList() {
        _cmp = new Comparator<E>() {
            @SuppressWarnings("unchecked")
            @Override
            public int compare(E o1, E o2) {
                // Usa el método compareTo del objeto si es Comparable
                return ((Comparable<E>) o1).compareTo(o2);
            }
        };
    }

    // Método para agregar un elemento manteniendo el orden
    @Override
    public boolean add(E e) {
        int j = size() - 1;

        // Comienza desde el final de la lista y busca el primer elemento
        // que sea menor o igual que el elemento a insertar
        while (j >= 0 && _cmp.compare(get(j), e) > 0) {
            j--; // Desciende por la lista
        }

        // Inserta el elemento en la posición correcta
        super.add(j + 1, e);

        return true; // Devuelve true siempre que se agregue el elemento
    }

    // Método para agregar todos los elementos de una colección manteniendo el orden
    @Override
    public boolean addAll(Collection<? extends E> c) {
        super.addAll(c); // Llama al método addAll de ArrayList
        for (E e : c) {
            add(e); // Agrega cada elemento manteniendo el orden
        }
        return true;
    }

    // No se permite insertar elementos en posiciones específicas en una lista ordenada
    @Override
    public void add(int index, E element) {
        throw new UnsupportedOperationException("Cannot insert to a sorted list");
    }

    // No se permite insertar todos los elementos de una colección en una posición específica
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException("Cannot insert to a sorted list");
    }

    // No se permite modificar un elemento en una lista ordenada
    @Override
    public E set(int index, E element) {
        throw new UnsupportedOperationException("Cannot set an element in a sorted list");
    }

}

