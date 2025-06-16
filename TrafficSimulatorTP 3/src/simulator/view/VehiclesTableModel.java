package simulator.view;

import javax.swing.table.AbstractTableModel;
import simulator.control.Controller;
import simulator.model.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class VehiclesTableModel extends AbstractTableModel implements TrafficSimObserver {
    private Controller _ctrl;  // Controlador que maneja la simulación
    private List<Vehicle> _vehicles;  // Lista de vehículos a mostrar en la tabla
    private final String[] _colNames = {"ID", "Status", "Itinerary", "CO2 Class", "Max Speed", "Speed", "Total CO2", "Distance"}; // Nombres de las columnas

    // Constructor que recibe el controlador y registra la vista como observador
    public VehiclesTableModel(Controller ctrl) {
        _ctrl = ctrl;
        _vehicles = new ArrayList<>();  // Inicializa la lista de vehículos
        _ctrl.addObserver(this);  // Se registra como observador del controlador
    }

    // Método que devuelve el número de filas en la tabla
    @Override
    public int getRowCount() {
        return _vehicles.size();  // El número de filas es igual al número de vehículos
    }

    // Método que devuelve el número de columnas en la tabla
    @Override
    public int getColumnCount() {
        return _colNames.length;  // El número de columnas es igual al tamaño del array _colNames
    }

    // Método que devuelve el nombre de la columna en una posición específica
    @Override
    public String getColumnName(int column) {
        return _colNames[column];  // Devuelve el nombre de la columna basado en el índice
    }

    // Método que obtiene el valor en una posición específica de la tabla (fila y columna)
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Vehicle v = _vehicles.get(rowIndex);  // Obtiene el vehículo en la fila indicada
        switch (columnIndex) {
            case 0: return v.getId();  // Devuelve el ID del vehículo
            case 1: return getStatusString(v);  // Devuelve el estado del vehículo como texto
            case 2: return v.getItinerary().stream().map(Junction::getId).collect(Collectors.joining(","));  // Devuelve el itinerario del vehículo (IDs de los cruces)
            case 3: return v.getContClass();  // Devuelve la clase de CO2 del vehículo
            case 4: return v.getMaxSpeed();  // Devuelve la velocidad máxima del vehículo
            case 5: return v.getSpeed();  // Devuelve la velocidad actual del vehículo
            case 6: return v.getTotalCO2();  // Devuelve el CO2 total generado por el vehículo
            case 7: return v.getDistance();  // Devuelve la distancia recorrida por el vehículo
            default: return null;  // Si la columna no es válida, devuelve null
        }
    }

    // Método que convierte el estado de un vehículo en una cadena de texto
    private String getStatusString(Vehicle v) {
        switch (v.getStatus()) {
            case PENDING: return "Pending";  // Si el estado es PENDING, devuelve "Pending"
            case TRAVELING: return v.getRoad().getId() + ":" + v.getLocation();  // Si está TRAVELING, devuelve la carretera y la localización
            case WAITING: return "Waiting:" + v.getRoad().getDest().getId();  // Si está WAITING, devuelve la carretera y el destino
            case ARRIVED: return "Arrived";  // Si ha llegado, devuelve "Arrived"
            default: return "";  // Si el estado es desconocido, devuelve una cadena vacía
        }
    }

    // Método que actualiza la lista de vehículos a partir de un RoadMap
    private void updateVehicles(RoadMap map) {
        _vehicles = new ArrayList<>(map.getVehicles());  // Obtiene los vehículos del mapa
        fireTableStructureChanged();  // Notifica a la vista que la estructura de la tabla ha cambiado
    }

    // Método llamado cada vez que el simulador avanza en el tiempo
    @Override
    public void onAdvance(RoadMap map, Collection<Event> events, int time) {
        updateVehicles(map);  // Actualiza la lista de vehículos
    }

    // Método llamado cada vez que se agrega un nuevo evento al simulador
    @Override
    public void onEventAdded(RoadMap map, Collection<Event> events, Event e, int time) {
        updateVehicles(map);  // Actualiza la lista de vehículos
    }

    // Método llamado cuando el simulador es reiniciado
    @Override
    public void onReset(RoadMap map, Collection<Event> events, int time) {
        updateVehicles(map);  // Actualiza la lista de vehículos
    }

    // Método llamado cuando el simulador es registrado
    @Override
    public void onRegister(RoadMap map, Collection<Event> events, int time) {
        updateVehicles(map);  // Actualiza la lista de vehículos
    }
}
