package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetContClassEvent;

/**
 * Builder específico para la creación de eventos de tipo "SetContClassEvent".
 * Esta clase es responsable de extraer los datos necesarios del objeto JSON para crear
 * un evento que establece la clase de contaminación de los vehículos en el simulador.
 */
public class SetContClassEventBuilder extends Builder<Event> {

	/**
	 * Constructor de la clase SetContClassEventBuilder.
	 * Inicializa el builder con un tipo de evento y una descripción del evento.
	 */
	public SetContClassEventBuilder() {
	    super("set_cont_class", "Sets contamination class of vehicles");
	}

	/**
	 * Método que crea una instancia de un evento "SetContClassEvent".
	 * Este método extrae los atributos del objeto JSON proporcionado y usa estos valores
	 * para crear un evento que establece la clase de contaminación de los vehículos.
	 * 
	 * @param data Objeto JSON que contiene los datos para crear el evento.
	 * @return Una nueva instancia de SetContClassEvent creada con los datos extraídos del JSON.
	 */
	@Override
	protected Event create_instance(JSONObject data) {
		// Lista para almacenar los pares de vehículos y su respectiva clase de contaminación.
		List<Pair<String, Integer>> _cs = new ArrayList<Pair<String, Integer>>();

		// Iteramos sobre el array "info" en el JSON para extraer los datos de vehículos y clases.
		for (int i = 0; i < data.getJSONArray("info").length(); i++) {
			// Para cada objeto dentro de "info", obtenemos el nombre del vehículo y su clase.
			_cs.add(new Pair<String, Integer>(data.getJSONArray("info").getJSONObject(i).getString("vehicle"), 
											  data.getJSONArray("info").getJSONObject(i).getInt("class")));
		}
		
		// Se crea y retorna una nueva instancia de SetContClassEvent utilizando los datos extraídos.
		return new SetContClassEvent(data.getInt("time"), _cs);
	}
}
