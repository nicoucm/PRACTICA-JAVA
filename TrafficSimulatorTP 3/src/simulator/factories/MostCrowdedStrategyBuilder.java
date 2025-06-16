package simulator.factories;

import org.json.JSONObject;

import simulator.model.LightSwitchingStrategy;
import simulator.model.MostCrowdedStrategy;

/**
 * Builder para crear instancias de la estrategia de cambio de luces más concurrida.
 * Esta clase extiende la clase Builder y está especializada en la creación de instancias
 * de la clase MostCrowdedStrategy, que es una implementación de LightSwitchingStrategy.
 */
public class MostCrowdedStrategyBuilder extends Builder<LightSwitchingStrategy> {

	/**
	 * Constructor de la clase MostCrowdedStrategyBuilder.
	 * 
	 * El constructor inicializa el builder con un identificador único y una descripción
	 * que permite distinguir esta estrategia de otras en la fábrica.
	 * 
	 * En este caso, el identificador es "most_crowded_lss" y la descripción es "Most crowded light switching strategy".
	 */
	public MostCrowdedStrategyBuilder() {
	    // Se llama al constructor de la clase base (Builder) con los parámetros específicos
	    super("most_crowded_lss", "Most crowded light switching strategy");
	}

	/**
	 * Método que crea una instancia de LightSwitchingStrategy basada en los datos proporcionados.
	 * 
	 * Este método toma un objeto JSON que debe contener información sobre el "timeslot" 
	 * (intervalo de tiempo) para la estrategia. Si no se especifica, se asigna un valor por defecto.
	 * Luego, crea una nueva instancia de MostCrowdedStrategy usando ese valor.
	 * 
	 * @param data El objeto JSON que contiene los datos para crear la instancia.
	 * @return Una nueva instancia de MostCrowdedStrategy.
	 */
	@Override
	protected LightSwitchingStrategy create_instance(JSONObject data) {
		int aux;

		// Verificamos si el campo "timeslot" está presente en el JSON. Si no, asignamos un valor por defecto.
		if (data.isNull("timeslot")) {
			aux = 1; // Valor por defecto si no se encuentra el campo "timeslot"
		} else {
			// Si se encuentra el campo "timeslot", obtenemos su valor como un entero
			aux = data.getInt("timeslot");
		}

		// Creamos y retornamos una instancia de MostCrowdedStrategy con el valor obtenido para timeslot
		return new MostCrowdedStrategy(aux);
	}
}
