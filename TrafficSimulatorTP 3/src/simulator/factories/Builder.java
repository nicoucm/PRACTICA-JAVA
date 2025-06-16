package simulator.factories;

import org.json.JSONObject;

/**
 * Clase abstracta genérica para construir objetos de tipo T a partir de JSON.
 * Se utiliza dentro del patrón factoría para generar instancias concretas
 * como eventos, carreteras, vehículos, etc.
 */
public abstract class Builder<T> {

    private String _type_tag; // Identificador del tipo que construye este builder (ej. "new_vehicle")
    private String _desc;     // Descripción textual del tipo (útil para mostrar en la interfaz gráfica)

    // Constructor: recibe el tipo y la descripción del builder
    public Builder(String type_tag, String desc) {
        if (type_tag == null || desc == null || type_tag.isBlank() || desc.isBlank())
            throw new IllegalArgumentException("Invalid type/desc");
        this._type_tag = type_tag;
        this._desc = desc;
    }

    // Devuelve el tipo de builder (usado para comparar con el campo "type" del JSON)
    public String get_type_tag() {
        return _type_tag;
    }

    // Devuelve un objeto JSON con información del builder (tipo, descripción y datos por defecto)
    public JSONObject get_info() {
        JSONObject info = new JSONObject();
        info.put("type", _type_tag); // tipo del evento que construye este builder
        info.put("desc", _desc);     // descripción textual

        JSONObject data = new JSONObject(); // datos por defecto del builder
        fill_in_data(data);                 // método que puede rellenar datos adicionales
        info.put("data", data);             // se añaden al JSON

        return info;
    }

    /**
     * Método opcional que las subclases pueden sobrescribir para añadir información extra
     * al JSON de información. Por ejemplo: campos editables por el usuario en un diálogo.
     */
    protected void fill_in_data(JSONObject o) {
        // Por defecto no hace nada
    }

    // Representación textual del builder (se usa al mostrarlo en listas o combos en la GUI)
    @Override
    public String toString() {
        return _desc;
    }

    /**
     * Método principal que recibe un objeto JSON y, si el campo "type" coincide,
     * crea la instancia concreta correspondiente usando el método abstracto.
     */
    public T createInstance(JSONObject info) {
        if (!info.has("type"))
            throw new IllegalArgumentException("El JSON debe incluir el campo 'type'.");

        if (_type_tag.equals(info.getString("type"))) {
            JSONObject data = info.has("data") ? info.getJSONObject("data") : new JSONObject();
            return create_instance(data); // llama al método concreto que debe implementar la subclase
        }

        return null; // si el tipo no coincide, este builder no se hace cargo
    }

    /**
     * Método abstracto que implementan las subclases.
     * Aquí se define cómo se construye el objeto de tipo T a partir del JSON "data".
     */
    protected abstract T create_instance(JSONObject data);
}

