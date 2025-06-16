package simulator.factories;

import org.json.JSONObject;
import simulator.model.Event;
import simulator.model.Weather;

/**
 * Clase abstracta que sirve como base para la creación de eventos de tipo "NewRoadEvent".
 * Esta clase proporciona la lógica común para extraer los atributos necesarios de un objeto JSON
 * para crear eventos relacionados con la creación de nuevas carreteras en el simulador.
 */
public abstract class NewRoadEventBuilder extends Builder<Event> {
	
	// Atributos comunes para todos los eventos de tipo "NewRoadEvent"
	protected String id;      // Identificador de la carretera
	protected String origen;  // Origen de la carretera
	protected String destino; // Destino de la carretera
	protected int length;     // Longitud de la carretera
	protected int co2Limit;   // Límite de CO2 de la carretera
	protected int maxSpeed;   // Velocidad máxima permitida en la carretera
	protected int time;       // Tiempo en el que ocurre el evento
	protected Weather weather; // Clima en la carretera (puede ser soleado, lluvioso, etc.)

	/**
	 * Constructor de la clase NewRoadEventBuilder.
	 * 
	 * Este constructor inicializa el builder con un tipo de evento y una descripción general.
	 * 
	 * @param type El tipo de evento que este builder puede crear (especificado por las subclases).
	 */
	public NewRoadEventBuilder(String type) {
	    // Se llama al constructor de la clase base con el tipo de evento y una descripción del evento
	    super(type, "Creates a new road event");
	}

	/**
	 * Método para extraer los atributos necesarios para la creación del evento a partir de un objeto JSON.
	 * Este método es llamado por las subclases para obtener los datos que se utilizarán para crear el evento.
	 * 
	 * @param data El objeto JSON que contiene los datos necesarios para crear el evento.
	 */
	protected void getAttributes(JSONObject data) {
		// Extraemos los atributos del objeto JSON
		id = data.getString("id");         // Identificador de la carretera
		time = data.getInt("time");        // Tiempo del evento
		origen = data.getString("src");    // Origen de la carretera
		destino = data.getString("dest");  // Destino de la carretera
		length = data.getInt("length");    // Longitud de la carretera
		co2Limit = data.getInt("co2limit"); // Límite de CO2 de la carretera
		maxSpeed = data.getInt("maxspeed"); // Velocidad máxima permitida
		weather = Weather.valueOf(data.getString("weather")); // Clima en la carretera
	}

	/**
	 * Método abstracto para crear una instancia de Event basada en los atributos extraídos.
	 * Este método debe ser implementado por las subclases, que decidirán cómo crear el evento específico.
	 * 
	 * @param data El objeto JSON que contiene los datos para crear el evento.
	 * @return Una nueva instancia del tipo de evento correspondiente.
	 */
	@Override
	protected abstract Event create_instance(JSONObject data);
}
