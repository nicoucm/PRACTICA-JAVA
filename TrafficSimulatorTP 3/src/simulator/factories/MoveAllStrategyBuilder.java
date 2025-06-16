package simulator.factories;

import org.json.JSONObject;

import simulator.model.DequeuingStrategy;
import simulator.model.MoveAllStrategy;

/**
 * Builder para crear instancias de la estrategia de desencolar "MoveAll".
 * Esta clase extiende la clase Builder y está especializada en la creación de instancias
 * de la clase MoveAllStrategy, que es una implementación de DequeuingStrategy.
 */
public class MoveAllStrategyBuilder extends Builder<DequeuingStrategy> {

	/**
	 * Constructor de la clase MoveAllStrategyBuilder.
	 * 
	 * El constructor inicializa el builder con un identificador único y una descripción
	 * que permite distinguir esta estrategia de otras en la fábrica.
	 * 
	 * En este caso, el identificador es "move_all_dqs" y la descripción es "Move all dequeuing strategy".
	 */
	public MoveAllStrategyBuilder() {
	    // Se llama al constructor de la clase base (Builder) con los parámetros específicos
	    super("move_all_dqs", "Move all dequeuing strategy");
	}

	/**
	 * Método que crea una instancia de DequeuingStrategy basada en los datos proporcionados.
	 * 
	 * En este caso, la estrategia "MoveAllStrategy" no requiere parámetros adicionales
	 * de entrada, por lo que simplemente se crea una nueva instancia de MoveAllStrategy.
	 * 
	 * @param data El objeto JSON que contiene los datos para crear la instancia (no se utiliza en este caso).
	 * @return Una nueva instancia de MoveAllStrategy.
	 */
	@Override
	protected DequeuingStrategy create_instance(JSONObject data) {
		// Creamos y retornamos una nueva instancia de MoveAllStrategy
		return new MoveAllStrategy();
	}
}
