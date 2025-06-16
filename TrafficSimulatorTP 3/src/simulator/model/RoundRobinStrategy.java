package simulator.model;

import java.util.List;

public class RoundRobinStrategy implements LightSwitchingStrategy {
    
    private int _timeSlot;

    // Constructor que recibe el tiempo que cada semáforo permanece verde.
    public RoundRobinStrategy(int timeSlot) {
        _timeSlot = timeSlot;
    }
    
    @Override
    public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime, int currTime) {
        int result = -1;
        
        if (roads != null) {
            // Si no hay semáforo verde actualmente, asignamos el primer semáforo (índice 0)
            if (currGreen == -1) {
                result = 0;
            } 
            // Si no ha pasado el tiempo para cambiar el semáforo, mantenemos el actual
            else if (currTime - lastSwitchingTime < _timeSlot) {
                result = currGreen;
            } 
            // Si ya ha pasado el tiempo asignado, cambiamos al siguiente semáforo en el orden
            else {
                result = (currGreen + 1) % qs.size();
            }
        }
        
        return result;
    }
}
