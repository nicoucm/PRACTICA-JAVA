package simulator.model;

import java.util.ArrayList;
import java.util.List;
import simulator.misc.Pair;

public class SetContClassEvent extends Event {
    
    private List<Pair<String, Integer>> _cs;

    // Constructor que recibe el tiempo del evento y la lista de pares (ID de vehículo, clase de contaminación)
    public SetContClassEvent(int time, List<Pair<String, Integer>> cs) {
        super(time);
        
        if (cs == null) {
            throw new IllegalArgumentException("Null weather list");
        }
        
        _cs = new ArrayList<Pair<String, Integer>>(cs);
    }

    // Método que ejecuta el evento, asignando la clase de contaminación a cada vehículo.
    @Override
    public void execute(RoadMap map) {
        for (Pair<String, Integer> c : _cs) {
            Vehicle v = map.getVehicle(c.getFirst());  // Busca el vehículo por su ID
            
            if (v == null) {
                throw new IllegalArgumentException("Null vehicle");
            }
            
            v.setContClass(c.getSecond());  // Establece la clase de contaminación para el vehículo
        }
    }

    // Método toString para representar el evento como una cadena de texto.
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Set ContClass for vehicles: ");
        
        // Itera sobre la lista de pares y construye la representación del evento
        for (Pair<String, Integer> p : _cs) {
            sb.append("\n  Vehicle '").append(p.getFirst())
              .append("' -> ContClass: ").append(p.getSecond());
        }
        
        return sb.toString();
    }
    
    // Getter que devuelve una copia de la lista de pares de vehículos y clases de contaminación
    public List<Pair<String, Integer>> getCs() {
        return new ArrayList<>(_cs);  // Retorna una copia para preservar la inmutabilidad
    }
}

