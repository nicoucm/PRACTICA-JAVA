package simulator.factories;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 * Implementación de la interfaz Factory<T> basada en una lista de Builders.
 * Cada Builder sabe crear un tipo específico de objeto T.
 * Esta clase recorre todos los builders hasta encontrar el que puede crear la instancia pedida.
 */
public class BuilderBasedFactory<T> implements Factory<T> {

	private List<Builder<T>> builders; // Lista de builders registrados para crear distintos tipos de T

	/**
	 * Constructor. Recibe una lista de builders y hace una copia interna.
	 */
	public BuilderBasedFactory(List<Builder<T>> builders) {
		this.builders = new ArrayList<>(builders); // Copia defensiva para evitar modificaciones externas
	}

	/**
	 * Intenta crear una instancia del objeto T a partir del JSON recibido.
	 * Recorre todos los builders en orden y usa el primero que pueda crear la instancia.
	 *
	 * @param info Objeto JSON con los datos (debe tener campo "type")
	 * @return Objeto creado de tipo T
	 * @throws IllegalArgumentException si ningún builder reconoce el tipo
	 */
	@Override
	public T createInstance(JSONObject info) {
		if (info != null) {
			for (Builder<T> bb : builders) {         // Recorremos todos los builders
				T o = bb.createInstance(info);       // Le pedimos al builder que cree el objeto
				if (o != null)                       // Si el builder lo reconoce y lo crea...
					return o;                        // ...lo devolvemos inmediatamente
			}
		}

		// Si llegamos aquí, ningún builder reconoció el tipo
		throw new IllegalArgumentException("Invalid value for createInstance: " + info);
	}
}
