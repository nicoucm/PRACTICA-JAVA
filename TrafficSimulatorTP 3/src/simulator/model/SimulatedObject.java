package simulator.model;

import org.json.JSONObject;

public abstract class SimulatedObject {

    protected String _id;

    // Constructor que recibe el ID del objeto simulado
    SimulatedObject(String id) {
        
        // Validación de que el ID no sea nulo ni vacío
        if (id == null || id.length() == 0) {
            throw new IllegalArgumentException("the id must be a nonempty string");
        } else {
            _id = id;
        }
    }

    // Método que retorna el ID del objeto simulado
    public String getId() {
        return _id;
    }

    // Método toString que devuelve el ID como representación textual
    @Override
    public String toString() {
        return _id;
    }

    // Método abstracto que debe ser implementado por las clases que extienden SimulatedObject
    abstract void advance(int time);

    // Método abstracto que debe ser implementado para generar un reporte del objeto
    abstract public JSONObject report();
}
