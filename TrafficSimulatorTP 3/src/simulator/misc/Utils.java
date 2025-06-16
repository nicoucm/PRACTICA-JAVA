package simulator.misc;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    
    /**
     * Este método convierte un arreglo en una lista.
     * 
     * @param a El arreglo de tipo T que se quiere convertir.
     * @param <T> El tipo de los elementos del arreglo.
     * @return Una lista de tipo T que contiene los mismos elementos que el arreglo.
     */
    static public <T> List<T> arrayToList(T a[]) {
        // Se crea una nueva lista vacía.
        List<T> c = new ArrayList<>();
        
        // Se recorre el arreglo y se añade cada elemento a la lista.
        for (T x : a) {
            c.add(x);
        }
        
        // Se devuelve la lista con los elementos del arreglo.
        return c;
    }
}
