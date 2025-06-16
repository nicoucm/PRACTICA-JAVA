package simulator.factories;

import org.json.JSONObject;

/**
 * Interfaz genérica Factory<T>.
 * Define el contrato que debe cumplir cualquier clase capaz de crear objetos
 * de tipo T a partir de un objeto JSON.
 */
public interface Factory<T> {

    /**
     * Método que debe implementar cualquier factoría concreta.
     * Su propósito es construir un objeto de tipo T utilizando la información proporcionada
     * en formato JSON.
     *
     * @param info Objeto JSON con los datos necesarios para construir el objeto
     * @return Una nueva instancia de tipo T
     */
    public T createInstance(JSONObject info);
}
