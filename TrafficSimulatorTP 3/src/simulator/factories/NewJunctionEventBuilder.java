package simulator.factories;

import org.json.JSONObject;
import org.json.JSONArray;
import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.NewJunctionEvent;

/**
 * Builder para crear instancias de eventos de tipo "NewJunctionEvent".
 * Esta clase es responsable de construir un evento de tipo NewJunctionEvent,
 * que es una implementación de Event. El evento representa la creación de una nueva intersección (junction) en el simulador.
 */
public class NewJunctionEventBuilder extends Builder<Event> {
	
	// Factories para crear las estrategias de cambio de luces y de eliminación de vehículos
	private Factory<LightSwitchingStrategy> _lssFactory;
	private Factory<DequeuingStrategy> _dqsFactory;

	/**
	 * Constructor de la clase NewJunctionEventBuilder.
	 * 
	 * Este constructor inicializa el builder con los factories necesarios para crear
	 * las estrategias de luces (LightSwitchingStrategy) y de eliminación de vehículos (DequeuingStrategy).
	 * 
	 * @param lssFactory Factory para crear estrategias de luces.
	 * @param dqsFactory Factory para crear estrategias de eliminación de vehículos.
	 */
	public NewJunctionEventBuilder(Factory<LightSwitchingStrategy> lssFactory, Factory<DequeuingStrategy> dqsFactory) {
	    // Se llama al constructor de la clase base con el identificador del evento ("new_junction") y su descripción
	    super("new_junction", "Creates a new junction");
	    
	    // Asignamos las fábricas de estrategias de luces y de eliminación de vehículos
	    _lssFactory = lssFactory;
	    _dqsFactory = dqsFactory;
	}

	/**
	 * Método que crea una instancia de Event (en este caso, NewJunctionEvent)
	 * basada en los datos proporcionados en el objeto JSON.
	 * 
	 * Este método extrae los atributos necesarios del JSON, como el tiempo del evento,
	 * el identificador de la intersección, las estrategias de luces y de eliminación de vehículos,
	 * y las coordenadas de la intersección.
	 * 
	 * @param data El objeto JSON que contiene los datos para crear el evento.
	 * @return Una nueva instancia de NewJunctionEvent.
	 */
	@Override
	protected Event create_instance(JSONObject data) {
		// Extraer los atributos básicos del evento
		int time = data.getInt("time"); // El tiempo del evento
		String id = data.getString("id"); // El identificador de la intersección
		
		// Crear las estrategias utilizando las fábricas
		LightSwitchingStrategy lsStrategy = _lssFactory.createInstance(data.getJSONObject("ls_strategy"));
		DequeuingStrategy dqStrategy = _dqsFactory.createInstance(data.getJSONObject("dq_strategy"));
		
		// Extraer las coordenadas de la intersección (si están disponibles), con valores predeterminados (0, 0)
		int x = 0;
		int y = 0;
		try {
			// Si existe el campo "coor", lo extraemos y asignamos a las coordenadas
			if (data.has("coor")) {
				JSONArray coor = data.getJSONArray("coor");
				x = coor.getInt(0);
				y = coor.getInt(1);
			}
		} catch (Exception e) {
			// Si ocurre un error (por ejemplo, si "coor" no es un array válido), no hacemos nada
		}

		// Crear y retornar una nueva instancia de NewJunctionEvent con los atributos extraídos
		return new NewJunctionEvent(time, id, lsStrategy, dqStrategy, x, y);
	}
}
