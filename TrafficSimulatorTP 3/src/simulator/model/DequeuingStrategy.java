package simulator.model;

import java.util.List;

public interface DequeuingStrategy {

    /**
     * Método para definir la estrategia de extracción de vehículos de una cola.
     * 
     * @param q Lista de vehículos en la cola (que representan los vehículos esperando para ser procesados).
     * @return Lista de vehículos que serán extraídos (desencolados) de la cola según la estrategia.
     */
    public List<Vehicle> dequeue(List<Vehicle> q);
}
