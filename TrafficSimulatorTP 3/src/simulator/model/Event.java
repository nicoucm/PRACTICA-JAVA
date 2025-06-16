package simulator.model;

public abstract class Event implements Comparable<Event> {
    
    // Contador estático para generar un valor único para el time_stamp de cada evento.
    private static long _counter = 0;
    
    // Atributos de la clase, donde 'time' es el tiempo del evento y 'time_stamp' es un identificador único.
    protected int time;
    protected long time_stamp;

    /**
     * Constructor de la clase Event.
     * 
     * @param time El tiempo en que ocurre el evento. Debe ser positivo.
     * @throws IllegalArgumentException Si el tiempo es menor que 1.
     */
    Event(int time) {
        if (time < 1) {
            // Si el tiempo es menor que 1, lanzamos una excepción.
            throw new IllegalArgumentException("Time must be positive (" + time + ")");
        } else {
            // Si el tiempo es válido, asignamos el valor al atributo 'time' y 
            // generamos un identificador único para 'time_stamp' utilizando un contador estático.
            this.time = time;
            this.time_stamp = _counter++;
        }
    }

    /**
     * Método getter para obtener el tiempo del evento.
     * 
     * @return El tiempo en que ocurre el evento.
     */
    public int getTime() {
        return time;
    }

    /**
     * Método para comparar eventos entre sí. Los eventos se comparan por su tiempo.
     * Si dos eventos ocurren en el mismo tiempo, se comparan por su time_stamp, asegurando que
     * los eventos con el mismo tiempo se procesen en el orden en que fueron creados.
     * 
     * @param o El evento con el que se va a comparar.
     * @return -1 si este evento ocurre antes que el evento dado, 1 si ocurre después, o 0 si son iguales.
     */
    @Override
    public int compareTo(Event o) {
        if (this.time < o.time) {
            return -1; // El evento actual ocurre antes.
        } else if (this.time > o.time) {
            return 1; // El evento actual ocurre después.
        } else {
            // Si los eventos ocurren en el mismo tiempo, se compara por el time_stamp para asegurar el orden.
            return Long.compare(this.time_stamp, o.time_stamp);
        }
    }

    /**
     * Método abstracto que debe ser implementado por las clases que extienden Event.
     * Este método define la lógica para ejecutar el evento en el mapa de carreteras.
     * 
     * @param map El mapa de carreteras donde se ejecutará el evento.
     */
    public abstract void execute(RoadMap map);
}
