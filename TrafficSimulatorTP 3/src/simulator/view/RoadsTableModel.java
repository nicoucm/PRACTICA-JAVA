package simulator.view;

import javax.swing.table.AbstractTableModel;
import simulator.control.Controller;
import simulator.model.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RoadsTableModel extends AbstractTableModel implements TrafficSimObserver {
    // Atributos
    private Controller _ctrl; // Controlador del simulador
    private List<Road> _roads; // Lista de carreteras a mostrar
    private final String[] _colNames = {"ID", "Length", "Weather", "Max Speed", "Speed Limit", "Total CO2", "CO2 Limit"}; // Nombres de las columnas

    // Constructor de la clase
    public RoadsTableModel(Controller ctrl) {
        _ctrl = ctrl; // Inicializa el controlador
        _roads = new ArrayList<>(); // Inicializa la lista de carreteras vacía
        _ctrl.addObserver(this); // Registra esta clase como observador del simulador
    }

    // Devuelve el número de filas (en este caso, el número de carreteras)
    @Override
    public int getRowCount() {
        return _roads.size(); // El número de filas es igual al número de carreteras
    }

    // Devuelve el número de columnas (en este caso, las 7 columnas definidas)
    @Override
    public int getColumnCount() {
        return _colNames.length; // El número de columnas es igual a la longitud del array _colNames
    }

    // Devuelve el nombre de cada columna
    @Override
    public String getColumnName(int column) {
        return _colNames[column]; // Devuelve el nombre de la columna según el índice
    }

    // Devuelve el valor en una celda específica
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Road r = _roads.get(rowIndex); // Obtiene la carretera correspondiente a la fila
        // Devuelve el valor correspondiente según la columna seleccionada
        switch (columnIndex) {
            case 0: return r.getId(); // Devuelve el ID de la carretera
            case 1: return r.getLength(); // Devuelve la longitud de la carretera
            case 2: return r.getWeather(); // Devuelve el estado del clima en la carretera
            case 3: return r.getMaxSpeed(); // Devuelve la velocidad máxima permitida en la carretera
            case 4: return r.getSpeedLimit(); // Devuelve el límite de velocidad de la carretera
            case 5: return r.getTotalCO2(); // Devuelve la cantidad total de CO2 generada por la carretera
            case 6: return r.getContLimit(); // Devuelve el límite de CO2 permitido para la carretera
            default: return null; // Si el índice no es válido, devuelve null
        }
    }

    // Actualiza la lista de carreteras y notifica a la vista que los datos han cambiado
    private void updateRoads(RoadMap map) {
        _roads = new ArrayList<>(map.getRoads()); // Actualiza la lista de carreteras
        fireTableDataChanged(); // Notifica a la vista que los datos de la tabla han cambiado
    }

    // Método que se llama cuando el simulador avanza (actualiza las carreteras)
    @Override
    public void onAdvance(RoadMap map, Collection<simulator.model.Event> events, int time) {
        updateRoads(map); // Actualiza las carreteras
    }

    // Método que se llama cuando se agrega un evento (actualiza las carreteras)
    @Override
    public void onEventAdded(RoadMap map, Collection<simulator.model.Event> events, simulator.model.Event e, int time) {
        updateRoads(map); // Actualiza las carreteras
    }

    // Método que se llama cuando el simulador se reinicia (actualiza las carreteras)
    @Override
    public void onReset(RoadMap map, Collection<simulator.model.Event> events, int time) {
        updateRoads(map); // Actualiza las carreteras
    }

    // Método que se llama cuando el simulador se registra (actualiza las carreteras)
    @Override
    public void onRegister(RoadMap map, Collection<simulator.model.Event> events, int time) {
        updateRoads(map); // Actualiza las carreteras
    }
}
