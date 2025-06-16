package simulator.view;

import javax.swing.table.AbstractTableModel;
import simulator.control.Controller;
import simulator.model.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EventsTableModel extends AbstractTableModel implements TrafficSimObserver {
    private Controller _ctrl;  // Controlador que gestiona la simulación
    private List<Event> _events;  // Lista de eventos a mostrar en la tabla
    private final String[] _colNames = {"Time", "Description"};  // Nombres de las columnas de la tabla

    // Constructor del modelo de tabla
    public EventsTableModel(Controller ctrl) {
        _ctrl = ctrl;  // Asigna el controlador
        _events = new ArrayList<>();  // Inicializa la lista de eventos vacía
        _ctrl.addObserver(this);  // Se agrega como observador del controlador
    }

    // Método para obtener el número de filas (eventos)
    @Override
    public int getRowCount() {
        return _events.size();  // El número de filas es el tamaño de la lista de eventos
    }

    // Método para obtener el número de columnas
    @Override
    public int getColumnCount() {
        return _colNames.length;  // El número de columnas es igual al tamaño del array _colNames
    }

    // Método para obtener el nombre de una columna
    @Override
    public String getColumnName(int column) {
        return _colNames[column];  // Devuelve el nombre de la columna correspondiente
    }

    // Método para obtener el valor de una celda en la tabla
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Event e = _events.get(rowIndex);  // Obtiene el evento en la fila indicada
        switch (columnIndex) {
            case 0: return e.getTime();  // Devuelve el tiempo del evento
            case 1: return e.toString();  // Devuelve la descripción del evento
            default: return null;  // Si el índice de la columna no es válido, devuelve null
        }
    }

    // Método privado para actualizar la lista de eventos
    private void updateEvents(Collection<Event> events) {
        _events = new ArrayList<>(events);  // Convierte la colección de eventos en una lista
        fireTableStructureChanged();  // Notifica a la tabla que los datos han cambiado y que debe ser actualizada
    }

    // Métodos de observador para reaccionar a los eventos de la simulación

    @Override
    public void onAdvance(RoadMap map, Collection<Event> events, int time) {
        updateEvents(events);  // Actualiza los eventos cuando la simulación avanza
    }

    @Override
    public void onEventAdded(RoadMap map, Collection<Event> events, Event e, int time) {
        updateEvents(events);  // Actualiza los eventos cuando se añade un evento
    }

    @Override
    public void onReset(RoadMap map, Collection<Event> events, int time) {
        updateEvents(events);  // Actualiza los eventos cuando la simulación se reinicia
    }

    @Override
    public void onRegister(RoadMap map, Collection<Event> events, int time) {
        updateEvents(events);  // Actualiza los eventos cuando se registran nuevos eventos
    }
}
