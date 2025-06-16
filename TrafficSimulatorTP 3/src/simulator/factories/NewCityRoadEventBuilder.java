package simulator.factories;

import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewCityRoadEvent;

/**
 * Builder para crear instancias de eventos de tipo "NewCityRoadEvent".
 * Esta clase extiende de NewRoadEventBuilder y está especializada en la creación
 * de instancias de NewCityRoadEvent, que es una implementación de Event.
 */
public class NewCityRoadEventBuilder extends NewRoadEventBuilder {

	/**
	 * Constructor de la clase NewCityRoadEventBuilder.
	 * 
	 * El constructor inicializa el builder con un identificador único que se utiliza
	 * para reconocer el tipo de evento que este builder puede crear. En este caso,
	 * el identificador es "new_city_road".
	 */
	public NewCityRoadEventBuilder() {
		// Se llama al constructor de la clase base (NewRoadEventBuilder) con el identificador "new_city_road"
		super("new_city_road");
	}

	/**
	 * Método que crea una instancia de Event (en este caso, NewCityRoadEvent)
	 * basada en los datos proporcionados en el objeto JSON.
	 * 
	 * Este método utiliza los datos extraídos del JSON para crear un nuevo evento
	 * de tipo NewCityRoadEvent con los atributos necesarios como el tiempo, el id,
	 * origen, destino, longitud, límite de CO2, velocidad máxima y condiciones climáticas.
	 * 
	 * @param data El objeto JSON que contiene los datos para crear el evento.
	 * @return Una nueva instancia de NewCityRoadEvent.
	 */
	@Override
	protected Event create_instance(JSONObject data) {
		// Se extraen los atributos del JSON. Este método puede ser definido en la clase base.
		getAttributes(data);

		// Se crea y retorna una nueva instancia de NewCityRoadEvent con los atributos extraídos.
		return new NewCityRoadEvent(time, id, origen, destino, length, co2Limit, maxSpeed, weather);
	}
}
