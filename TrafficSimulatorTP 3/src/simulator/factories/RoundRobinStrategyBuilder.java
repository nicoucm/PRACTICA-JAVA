package simulator.factories;

import org.json.JSONObject;

import simulator.model.LightSwitchingStrategy;
import simulator.model.RoundRobinStrategy;

/**
 * Builder específico para la creación de eventos de tipo "RoundRobinStrategy".
 * Esta clase es responsable de extraer los datos necesarios del objeto JSON para crear
 * una estrategia de cambio de luces de tipo Round Robin en el simulador.
 */
public class RoundRobinStrategyBuilder extends Builder<LightSwitchingStrategy> {
	
	/**
	 * Constructor de la clase RoundRobinStrategyBuilder.
	 * Inicializa el builder con un tipo de evento y una descripción del evento.
	 */
	public RoundRobinStrategyBuilder() {
	    super("round_robin_lss", "Round robin light switching strategy");
	}

	/**
	 * Método que crea una instancia de una estrategia de cambio de luces de tipo Round Robin.
	 * Este método extrae los atributos del objeto JSON proporcionado y usa estos valores
	 * para crear una estrategia RoundRobinStrategy.
	 * 
	 * @param data Objeto JSON que contiene los datos para crear la estrategia de cambio de luces.
	 * @return Una nueva instancia de RoundRobinStrategy creada con los datos extraídos del JSON.
	 */
	@Override
	protected LightSwitchingStrategy create_instance(JSONObject data) {
		int aux;

		// Si el JSON no contiene el campo "timeslot", se asigna el valor por defecto 1.
		if (data.isNull("timeslot")) {
			aux = 1;
		} else {
			// Si el campo "timeslot" existe, se obtiene su valor como un entero.
			aux = data.getInt("timeslot");
		}
		
		// Se crea y retorna una nueva instancia de RoundRobinStrategy con el valor de "timeslot".
		return new RoundRobinStrategy(aux);
	}
}
