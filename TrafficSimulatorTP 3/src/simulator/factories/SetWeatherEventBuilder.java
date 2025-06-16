package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetWeatherEvent;
import simulator.model.Weather;

/**
 * Builder específico para la creación de eventos de tipo "SetWeatherEvent".
 * Esta clase es responsable de extraer los datos necesarios del objeto JSON para crear
 * un evento que establece las condiciones climáticas de las carreteras en el simulador.
 */
public class SetWeatherEventBuilder extends Builder<Event> {

	/**
	 * Constructor de la clase SetWeatherEventBuilder.
	 * Inicializa el builder con un tipo de evento y una descripción del evento.
	 */
	public SetWeatherEventBuilder() {
	    super("set_weather", "Sets the weather conditions for roads");
	}

	/**
	 * Método que crea una instancia de un evento "SetWeatherEvent".
	 * Este método extrae los atributos del objeto JSON proporcionado y usa estos valores
	 * para crear un evento que establece las condiciones meteorológicas de las carreteras.
	 * 
	 * @param data Objeto JSON que contiene los datos para crear el evento.
	 * @return Una nueva instancia de SetWeatherEvent creada con los datos extraídos del JSON.
	 */
	@Override
	protected Event create_instance(JSONObject data) {
		// Lista para almacenar los pares de carreteras y sus respectivas condiciones climáticas.
		List<Pair<String, Weather>> _ws = new ArrayList<Pair<String, Weather>>();

		// Iteramos sobre el array "info" en el JSON para extraer los datos de las carreteras y su clima.
		for (int i = 0; i < data.getJSONArray("info").length(); i++) {
			// Para cada objeto dentro de "info", obtenemos el nombre de la carretera y el clima correspondiente.
			_ws.add(new Pair<String, Weather>(data.getJSONArray("info").getJSONObject(i).getString("road"), 
											  Weather.valueOf(data.getJSONArray("info").getJSONObject(i).getString("weather").toUpperCase())));
		}
		
		// Se crea y retorna una nueva instancia de SetWeatherEvent utilizando los datos extraídos.
		return new SetWeatherEvent(data.getInt("time"), _ws);
	}
}
