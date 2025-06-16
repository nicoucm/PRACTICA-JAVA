package simulator.model;

import java.util.Collection;

public interface TrafficSimObserver {
    
    // Método llamado cuando la simulación avanza en el tiempo
    void onAdvance(RoadMap map, Collection<Event> events, int time);

    // Método llamado cuando un nuevo evento es agregado a la simulación
    void onEventAdded(RoadMap map, Collection<Event> events, Event e, int time);

    // Método llamado cuando la simulación se reinicia
    void onReset(RoadMap map, Collection<Event> events, int time);

    // Método llamado cuando un observador se registra
    void onRegister(RoadMap map, Collection<Event> events, int time);

}
