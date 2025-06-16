package simulator.model;

import java.util.List;

public class MostCrowdedStrategy implements LightSwitchingStrategy {
	
	private int _timeSlot;

    // Constructor que define el tiempo de duración de un ciclo de semáforo
	public MostCrowdedStrategy(int timeSlot) {
		_timeSlot = timeSlot;
	}
	
	@Override
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime, int currTime) {
		int result = -1; // Indice de la carretera con el semáforo verde
		int max = 0; // Tamaño máximo de la cola
		int k = 0; // Índice de la carretera
		int ind = 0; // Índice de la carretera seleccionada
		int qsSize = 0; // Tamaño de la lista de colas
		
		if (roads != null) { // Si la lista de carreteras no está vacía
			// Si no hay semáforo verde actual, se elige la carretera con la cola más grande
			if (currGreen == -1) {
				for (List<Vehicle> q : qs) {
					if (q.size() > max) { // Si la cola de esta carretera es mayor que el máximo actual
						max = q.size();
						ind = k; // Actualiza el índice de la carretera con la mayor cola
					}
					k++;
				}
				
				result = ind; // Asigna el índice de la carretera con la mayor cola
			} 
			// Si aún no ha pasado el tiempo asignado para el cambio de semáforo, mantiene el semáforo actual
			else if (currTime - lastSwitchingTime < _timeSlot) {
				result = currGreen; // Mantiene el semáforo actual
			} 
			// Si ha pasado el tiempo, se cambia el semáforo a la carretera con la cola más grande
			else {
				qsSize = qs.size(); // Obtiene el tamaño de la lista de colas
				
				for (int j = 0; j < qsSize; j++) {
					k = (j + currGreen + 1) % qsSize; // Calcula la siguiente carretera a evaluar
					
					if (qs.get(k).size() > max) { // Si la cola de la carretera actual es mayor que el máximo
						max = qs.get(k).size();
						ind = k; // Actualiza el índice de la carretera con la mayor cola
					}
				}
				
				result = ind; // Asigna el índice de la carretera con la mayor cola
			}
		}
		
		return result; // Devuelve el índice de la carretera que tendrá el semáforo verde
	}
}
