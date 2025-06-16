package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewVehicleEvent;

/**
 * Builder específico para la creación de eventos de tipo "NewVehicleEvent".
 * Esta clase es responsable de extraer los datos necesarios del objeto JSON para crear
 * un evento que represente la creación de un nuevo vehículo en el simulador.
 */
public class NewVehicleEventBuilder extends Builder<Event> {

	/**
	 * Constructor de la clase NewVehicleEventBuilder.
	 * Inicializa el builder con un tipo de evento y una descripción del evento.
	 */
	public NewVehicleEventBuilder() {
	    super("new_vehicle", "Creates a new vehicle");
	}

	/**
	 * Método que crea una instancia de un evento de tipo NewVehicleEvent.
	 * Este método extrae los atributos del objeto JSON proporcionado y usa estos valores
	 * para crear un evento que representa la creación de un nuevo vehículo.
	 * 
	 * @param data Objeto JSON que contiene los datos para crear el evento de un nuevo vehículo.
	 * @return Una nueva instancia de NewVehicleEvent creada con los datos extraídos del JSON.
	 */
	@Override
	protected Event create_instance(JSONObject data) {
		// Lista para almacenar las paradas del itinerario del vehículo
		List<String> _itinerary = new ArrayList<String>();
		
		// Extraemos el itinerario desde el JSON y lo convertimos en una lista de cadenas
		for (Object o : data.getJSONArray("itinerary").toList()) {
			_itinerary.add(o.toString());
		}
		
		// Creamos un nuevo evento de tipo NewVehicleEvent con los datos extraídos
		return new NewVehicleEvent(
			data.getInt("time"),        // Tiempo del evento
			data.getString("id"),       // Identificador del vehículo
			data.getInt("maxspeed"),    // Velocidad máxima del vehículo
			data.getInt("class"),       // Clase del vehículo
			_itinerary                  // Itinerario del vehículo
		);
	}
}
