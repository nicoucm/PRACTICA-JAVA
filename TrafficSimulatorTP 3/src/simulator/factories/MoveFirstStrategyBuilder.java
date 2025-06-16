package simulator.factories;

import org.json.JSONObject;

import simulator.model.DequeuingStrategy;
import simulator.model.MoveFirstStrategy;

/**
 * Builder para crear instancias de la estrategia de desencolar "MoveFirst".
 * Esta clase extiende la clase Builder y está especializada en la creación de instancias
 * de la clase MoveFirstStrategy, que es una implementación de DequeuingStrategy.
 */
public class MoveFirstStrategyBuilder extends Builder<DequeuingStrategy> {

	/**
	 * Constructor de la clase MoveFirstStrategyBuilder.
	 * 
	 * El constructor inicializa el builder con un identificador único y una descripción
	 * que permite distinguir esta estrategia de otras en la fábrica.
	 * 
	 * En este caso, el identificador es "move_first_dqs" y la descripción es "Move first dequeuing strategy".
	 */
	public MoveFirstStrategyBuilder() {
	    // Se llama al constructor de la clase base (Builder) con los parámetros específicos
	    super("move_first_dqs", "Move first dequeuing strategy");
	}

	/**
	 * Método que crea una instancia de DequeuingStrategy basada en los datos proporcionados.
	 * 
	 * En este caso, la estrategia "MoveFirstStrategy" no requiere parámetros adicionales
	 * de entrada, por lo que simplemente se crea una nueva instancia de MoveFirstStrategy.
	 * 
	 * @param data El objeto JSON que contiene los datos para crear la instancia (no se utiliza en este caso).
	 * @return Una nueva instancia de MoveFirstStrategy.
	 */
	@Override
	protected DequeuingStrategy create_instance(JSONObject data) {
		// Creamos y retornamos una nueva instancia de MoveFirstStrategy
		return new MoveFirstStrategy();
	}
}
