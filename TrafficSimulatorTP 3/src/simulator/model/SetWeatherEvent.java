package simulator.model;

import java.util.ArrayList;
import java.util.List;

import simulator.misc.Pair;

public class SetWeatherEvent extends Event {
    
    private List<Pair<String, Weather>> _ws;

    // Constructor que recibe el tiempo del evento y la lista de pares (ID de carretera, clima)
    public SetWeatherEvent(int time, List<Pair<String, Weather>> ws) {
        super(time);
        
        if (ws == null) {
            throw new IllegalArgumentException("Null weather list");
        }
        
        _ws = new ArrayList<Pair<String, Weather>>(ws);
    }

    // Método que ejecuta el evento, asignando el clima a las carreteras especificadas
    @Override
    public void execute(RoadMap map) {
        for (Pair<String, Weather> w : _ws) {
            Road r = map.getRoad(w.getFirst());  // Obtiene la carretera por su ID
            
            if (r == null) {
                throw new IllegalArgumentException("Null road");
            }
            
            r.setWeather(w.getSecond());  // Establece el clima para la carretera
        }
    }

    // Método toString para representar el evento como una cadena de texto
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Set Weather for roads: ");
        
        // Itera sobre la lista de pares y construye la representación del evento
        for (Pair<String, Weather> p : _ws) {
            sb.append("\n  Road '").append(p.getFirst())
              .append("' -> Weather: ").append(p.getSecond());
        }
        
        return sb.toString();
    }

    // Getter que devuelve una copia de la lista de pares de carreteras y sus climas
    public List<Pair<String, Weather>> getWs() {
        return new ArrayList<>(_ws);  // Retorna una copia para preservar la inmutabilidad
    }
}
