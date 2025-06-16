package simulator.misc;

public class Pair<T1, T2> {
    // Atributos privados para almacenar los dos elementos del par
    private T1 _first;
    private T2 _second;

    // Constructor que inicializa los dos elementos del par
    public Pair(T1 first, T2 second) {
        _first = first;
        _second = second;
    }

    // Método getter para obtener el primer elemento del par
    public T1 getFirst() {
        return _first;
    }

    // Método getter para obtener el segundo elemento del par
    public T2 getSecond() {
        return _second;
    }
}
