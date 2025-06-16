package simulator.factories;

import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewInterCityRoadEvent;

/**
 * Builder para crear instancias de eventos de tipo "NewInterCityRoadEvent".
 * Esta clase extiende de NewRoadEventBuilder y está especializada en la creación
 * de instancias de NewInterCityRoadEvent, que es una implementación de Event.
 */
public class NewInterCityRoadEventBuilder extends NewRoadEventBuilder {

	/**
	 * Constructor de la clase NewInterCityRoadEventBuilder.
	 * 
	 * El constructor inicializa el builder con un identificador único que se utiliza
	 * para reconocer el tipo de evento que este builder puede crear. En este caso,
	 * el identificador es "new_inter_city_road".
	 */
	public NewInterCityRoadEventBuilder() {
		// Se llama al constructor de la clase base (NewRoadEventBuilder) con el identificador "new_inter_city_road"
		super("new_inter_city_road");
	}

	/**
	 * Método que crea una instancia de Event (en este caso, NewInterCityRoadEvent)
	 * basada en los datos proporcionados en el objeto JSON.
	 * 
	 * Este método utiliza los datos extraídos del JSON para crear un nuevo evento
	 * de tipo NewInterCityRoadEvent con los atributos necesarios como el tiempo, el id,
	 * origen, destino, longitud, límite de CO2, velocidad máxima y condiciones climáticas.
	 * 
	 * @param data El objeto JSON que contiene los datos para crear el evento.
	 * @return Una nueva instancia de NewInterCityRoadEvent.
	 */
	@Override
	protected Event create_instance(JSONObject data) {
		// Se extraen los atributos del JSON. Este método puede ser definido en la clase base.
		getAttributes(data);

		// Se crea y retorna una nueva instancia de NewInterCityRoadEvent con los atributos extraídos.
		return new NewInterCityRoadEvent(time, id, origen, destino, length, co2Limit, maxSpeed, weather);
	}
}
