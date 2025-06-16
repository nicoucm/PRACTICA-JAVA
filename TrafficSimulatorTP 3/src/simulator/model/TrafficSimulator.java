package simulator.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.SortedArrayList;

public class TrafficSimulator implements Observable<TrafficSimObserver> {

    // Atributos de la clase
    private RoadMap _roadMap;             // Mapa de carreteras donde se realiza la simulación
    private List<Event> _events;          // Lista de eventos programados
    private int _time;                    // Tiempo actual de la simulación
    private List<TrafficSimObserver> _observers;   // Lista de observadores registrados
    private Map<String, int[]> junctionCoords; // Mapa para almacenar las coordenadas de los cruces (junctions)

    // Constructor: Inicializa los atributos
    public TrafficSimulator() {
        _roadMap = new RoadMap();                // Crea un nuevo RoadMap vacío
        _events = new SortedArrayList<Event>();   // Crea una lista ordenada de eventos
        _time = 0;                               // Inicializa el tiempo de la simulación
        _observers = new ArrayList<>();          // Crea una lista vacía de observadores
        junctionCoords = new HashMap<>();        // Mapa vacío para las coordenadas de los cruces
    }

    // Método para añadir un nuevo evento
    public void addEvent(Event e) {
        _events.add(e);  // Añade el evento a la lista de eventos
        // Si el evento es un NewJunctionEvent, guardamos las coordenadas del cruce
        if (e instanceof NewJunctionEvent) {
            NewJunctionEvent nje = (NewJunctionEvent) e;
            junctionCoords.put(nje.getId(), new int[]{nje.getX(), nje.getY()});
        }
        notifyEventAdded(e);  // Notifica a los observadores que se ha añadido un evento
    }

    // Método que ajusta las coordenadas de los cruces
    private void adjustJunctionCoordinates() {
        // Determinamos si todos los cruces tienen la misma coordenada Y
        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;
        for (int[] coords : junctionCoords.values()) {
            minY = Math.min(minY, coords[1]);  // Encontramos el valor mínimo de Y
            maxY = Math.max(maxY, coords[1]);  // Encontramos el valor máximo de Y
        }
        boolean sameY = (maxY - minY == 0); // Si la diferencia entre los valores de Y es 0, significa que todos son iguales

        // Si todos los cruces tienen la misma coordenada Y, ajustamos las coordenadas X
        if (sameY) {
            int x = 50;
            for (String id : junctionCoords.keySet()) {
                int[] coords = junctionCoords.get(id);
                coords[0] = x;   // Asignamos la coordenada X
                coords[1] = 50;  // Forzamos la coordenada Y a 50
                junctionCoords.put(id, coords);
                x += 100;  // Incrementamos X para el siguiente cruce
                System.out.println("Forzando coordenadas para " + id + ": [" + coords[0] + ", " + coords[1] + "]");
            }
        }
    }

    // Método que avanza la simulación un paso en el tiempo
    public void advance() {
        _time++;  // Aumentamos el tiempo de la simulación
        
        adjustJunctionCoordinates();  // Ajustamos las coordenadas de los cruces si es necesario

        // Ejecutamos los eventos que correspondan al tiempo actual (_time)
        for (int i = 0; i < _events.size(); i++) {
            if (_time == _events.get(i).getTime()) {
                Event e = _events.get(i);
                // Si el evento es un NewJunctionEvent, lo ajustamos antes de ejecutarlo
                if (e instanceof NewJunctionEvent) {
                    NewJunctionEvent nje = (NewJunctionEvent) e;
                    // Obtener las coordenadas ajustadas para el cruce
                    int[] coords = junctionCoords.get(nje.getId());
                    // Crear un nuevo evento con las coordenadas ajustadas
                    NewJunctionEvent adjustedEvent = new NewJunctionEvent(
                        nje.getTime(), nje.getId(), nje.getLsStrategy(), nje.getDqStrategy(), coords[0], coords[1]);
                    adjustedEvent.execute(_roadMap);  // Ejecutar el evento ajustado
                } else {
                    e.execute(_roadMap);  // Ejecutar el evento original
                }
            }
        }

        // Eliminar los eventos que ya han sido ejecutados
        for (int i = 0; i < _events.size(); i++) {
            if (_time == _events.get(i).getTime()) {
                _events.remove(i);  // Eliminar el evento de la lista
                i--;  // Ajustar el índice después de la eliminación
            }
        }
        
        // Avanzar el estado de los cruces (Junctions)
        for (Junction j : _roadMap.getJunctions()) {
            j.advance(_time);
        }
        
        // Avanzar el estado de las carreteras (Roads)
        for (Road r : _roadMap.getRoads()) {
            r.advance(_time);
        }

        notifyAdvance();  // Notificar a los observadores que se ha avanzado en la simulación
    }

    // Método para reiniciar la simulación
    public void reset() {
        _roadMap.reset();     // Reiniciar el mapa de carreteras
        _events.clear();       // Limpiar los eventos programados
        _time = 0;             // Restablecer el tiempo
        junctionCoords.clear(); // Limpiar las coordenadas de los cruces
        notifyReset();         // Notificar a los observadores que la simulación ha sido reiniciada
    }

    // Generar un informe del estado de la simulación en formato JSON
    public JSONObject report() {
        JSONObject jo = new JSONObject();
        jo.put("time", _time);              // Añadir el tiempo actual
        jo.put("state", _roadMap.report()); // Añadir el estado del mapa de carreteras
        return jo;
    }

    // Métodos para manejar observadores
    @Override
    public void addObserver(TrafficSimObserver o) {
        if (!_observers.contains(o)) {
            _observers.add(o);  // Añadir el observador a la lista
            notifyRegister(o);   // Notificar al observador que ha sido registrado
        }
    }

    @Override
    public void removeObserver(TrafficSimObserver o) {
        _observers.remove(o);  // Eliminar el observador de la lista
    }

    // Notificar a los observadores que la simulación ha avanzado
    private void notifyAdvance() {
        for (TrafficSimObserver o : _observers) {
            o.onAdvance(_roadMap, _events, _time);
        }
    }

    // Notificar a los observadores que se ha añadido un nuevo evento
    private void notifyEventAdded(Event e) {
        for (TrafficSimObserver o : _observers) {
            o.onEventAdded(_roadMap, _events, e, _time);
        }
    }

    // Notificar a los observadores que la simulación ha sido reiniciada
    private void notifyReset() {
        for (TrafficSimObserver o : _observers) {
            o.onReset(_roadMap, _events, _time);
        }
    }

    // Notificar a los observadores que un nuevo observador se ha registrado
    private void notifyRegister(TrafficSimObserver o) {
        o.onRegister(_roadMap, _events, _time);
    }
}
