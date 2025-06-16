package simulator.model;

import java.util.LinkedList;
import java.util.List;

public class MoveFirstStrategy implements DequeuingStrategy {

	@Override
	public List<Vehicle> dequeue(List<Vehicle> q) {
		// Crea una nueva lista vacía donde se almacenará el primer vehículo
		List<Vehicle> result =  new LinkedList<Vehicle>();
		
		// Agrega el primer vehículo de la cola a la nueva lista
		result.add(q.get(0));
		
		// Retorna la lista con el primer vehículo
		return result;
	}
}
