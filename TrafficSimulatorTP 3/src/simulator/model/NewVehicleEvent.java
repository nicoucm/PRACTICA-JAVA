package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class NewVehicleEvent extends Event {
	
	private String id;  // Identificador del vehículo
	private int maxSpeed;  // Velocidad máxima del vehículo
	private int contClass;  // Clase de contaminación del vehículo
	private List<String> itinerary;  // Lista de IDs de intersecciones en el itinerario del vehículo

	// Constructor: Inicializa el evento de creación de un nuevo vehículo
	public NewVehicleEvent(int time, String id, int maxSpeed, int contClass, List<String> itinerary) {
		super(time);  // Llama al constructor de la clase base Event con el tiempo
		this.id = id;
		this.maxSpeed = maxSpeed;
		this.contClass = contClass;
		this.itinerary = new ArrayList<String>(itinerary);  // Copia de la lista de itinerarios
	}

	// Método execute que ejecuta el evento en el mapa de simulación
	@Override
	public void execute(RoadMap map) {
		List<Junction> aux = new ArrayList<Junction>();  // Lista auxiliar para almacenar las intersecciones
		Vehicle v;
		
		// Convierte los IDs de las intersecciones en objetos Junction
		for (String j : itinerary) {
			aux.add(map.getJunction(j));  // Agrega cada intersección al itinerario
		}
		
		// Crea el vehículo con los datos proporcionados
		v = new Vehicle(id, maxSpeed, contClass, aux);
		
		// Agrega el vehículo al mapa de simulación
		map.addVehicle(v);
		
		// Mueve el vehículo a la siguiente carretera en su itinerario
		v.moveToNextRoad();
	}
	
	// Método toString que genera una representación en cadena del evento
	@Override
	public String toString() {
	    return "New Vehicle '" + id + "'";  // Representa el vehículo por su ID
	}
}
