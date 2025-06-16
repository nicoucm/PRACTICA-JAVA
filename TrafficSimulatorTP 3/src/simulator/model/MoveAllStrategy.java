package simulator.model;

import java.util.LinkedList;
import java.util.List;

public class MoveAllStrategy implements DequeuingStrategy {

	@Override
	public List<Vehicle> dequeue(List<Vehicle> q) {
		// Crea una nueva lista que contiene todos los vehículos de la cola
		List<Vehicle> result =  new LinkedList<Vehicle>(q);
		
		// Retorna la lista de vehículos a mover
		return result;
	}
}
