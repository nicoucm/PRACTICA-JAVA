package simulator.view;

import javax.swing.table.AbstractTableModel; // Importa la clase AbstractTableModel para crear un modelo de tabla.
import simulator.control.Controller; // Importa el controlador de la simulación.
import simulator.model.*; // Importa las clases del modelo de simulación (Junction, RoadMap, Event, etc.).
import java.util.ArrayList; // Importa ArrayList para almacenar los cruces de tráfico.
import java.util.Collection; // Importa Collection para trabajar con colecciones genéricas.
import java.util.List; // Importa List para definir listas de cruces.
import java.util.stream.Collectors; // Importa Collectors para procesar flujos de datos.

public class JunctionsTableModel extends AbstractTableModel implements TrafficSimObserver {
    private Controller _ctrl; // Controlador de la simulación.
    private List<Junction> _junctions; // Lista de los cruces de tráfico.
    private final String[] _colNames = {"ID", "Green", "Queues"}; // Nombres de las columnas de la tabla.

    // Constructor que inicializa el controlador y la lista de cruces.
    public JunctionsTableModel(Controller ctrl) {
        _ctrl = ctrl; // Asocia el controlador.
        _junctions = new ArrayList<>(); // Inicializa la lista de cruces como una lista vacía.
        _ctrl.addObserver(this); // Se agrega esta clase como observador del controlador.
    }

    // Método que retorna el número de filas de la tabla.
    @Override
    public int getRowCount() {
        return _junctions.size(); // El número de filas es igual al tamaño de la lista de cruces.
    }

    // Método que retorna el número de columnas de la tabla.
    @Override
    public int getColumnCount() {
        return _colNames.length; // El número de columnas es el tamaño del arreglo _colNames.
    }

    // Método que retorna el nombre de la columna en función del índice.
    @Override
    public String getColumnName(int column) {
        return _colNames[column]; // Devuelve el nombre de la columna según su índice.
    }

    // Método que retorna el valor de una celda en la tabla según su fila y columna.
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Junction j = _junctions.get(rowIndex); // Obtiene el cruce de tráfico en la fila indicada.
        switch (columnIndex) {
            case 0: // Si la columna es 0, devuelve el ID del cruce.
                return j.getId();
            case 1: // Si la columna es 1, devuelve la carretera que tiene el semáforo verde, o "NONE" si no hay semáforo verde.
                return j.getGreenLightIndex() != -1 ? j.getInRoads().get(j.getGreenLightIndex()).getId() : "NONE";
            case 2: // Si la columna es 2, devuelve las colas de los vehículos esperando, con sus IDs de carretera y los vehículos.
                return j.getInRoads().stream()
                    .map(r -> r.getId() + ":" + r.getVehicles().stream().map(Vehicle::getId).collect(Collectors.toList()))
                    .collect(Collectors.joining(", "));
            default: // Si no es ninguna de las tres columnas, retorna null.
                return null;
        }
    }

    // Método que actualiza la lista de cruces con la información del mapa de carreteras.
    private void updateJunctions(RoadMap map) {
        _junctions = new ArrayList<>(map.getJunctions()); // Se actualiza la lista de cruces con los nuevos cruces del mapa.
        fireTableDataChanged(); // Notifica a la tabla que los datos han cambiado.
    }

    // Método que es llamado cuando se avanza la simulación. Recibe el mapa de carreteras, los eventos y el tiempo.
    @Override
    public void onAdvance(RoadMap map, Collection<simulator.model.Event> events, int time) {
        updateJunctions(map); // Actualiza los cruces de tráfico con el mapa de carreteras.
    }

    // Método que es llamado cuando se agrega un nuevo evento a la simulación.
    @Override
    public void onEventAdded(RoadMap map, Collection<simulator.model.Event> events, simulator.model.Event e, int time) {
        updateJunctions(map); // Actualiza los cruces de tráfico cuando se agrega un evento.
    }

    // Método que es llamado cuando se reinicia la simulación.
    @Override
    public void onReset(RoadMap map, Collection<simulator.model.Event> events, int time) {
        updateJunctions(map); // Actualiza los cruces de tráfico con el mapa de carreteras al reiniciar.
    }

    // Método que es llamado cuando se registra la simulación.
    @Override
    public void onRegister(RoadMap map, Collection<simulator.model.Event> events, int time) {
        updateJunctions(map); // Actualiza los cruces de tráfico cuando la simulación se registra.
    }
}
