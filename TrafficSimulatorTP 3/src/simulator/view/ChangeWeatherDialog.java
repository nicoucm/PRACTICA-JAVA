package simulator.view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import simulator.control.Controller;
import simulator.misc.Pair;
import simulator.model.*;

public class ChangeWeatherDialog extends JDialog implements TrafficSimObserver {
    private JComboBox<String> _roadsCombo;  // ComboBox para seleccionar la carretera
    private JComboBox<Weather> _weatherCombo;  // ComboBox para seleccionar las condiciones meteorológicas
    private JSpinner _ticksSpinner;  // Spinner para seleccionar el número de ticks
    private JButton _okButton, _cancelButton;  // Botones OK y Cancelar
    private Controller _ctrl;  // Controlador de la simulación
    private List<Road> _roads;  // Lista de carreteras disponibles

    // Constructor de la clase
    public ChangeWeatherDialog(Controller ctrl) {
        super((Frame) null, "Change Weather", true);  // Configura el JDialog
        _ctrl = ctrl;  // Asigna el controlador
        _roads = new ArrayList<>();  // Inicializa la lista de carreteras
        initGUI();  // Inicializa la interfaz gráfica
        _ctrl.addObserver(this);  // Añade el observador para recibir actualizaciones
    }

    // Método que inicializa la interfaz gráfica
    private void initGUI() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));  // Layout en columna
        setContentPane(mainPanel);

        // Etiqueta informativa sobre la acción a realizar
        JLabel infoLabel = new JLabel("<html>Select a road, a weather condition, and the number of ticks<br>to schedule a weather change event.</html>");
        infoLabel.setAlignmentX(CENTER_ALIGNMENT);
        mainPanel.add(infoLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));  // Espaciado entre componentes

        // Panel que contiene los campos para la entrada de datos
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new GridLayout(3, 2, 5, 5));  // Layout en 3 filas y 2 columnas
        mainPanel.add(fieldsPanel);

        // Etiqueta y campo para seleccionar la carretera
        JLabel roadLabel = new JLabel("Road:");
        fieldsPanel.add(roadLabel);
        _roadsCombo = new JComboBox<>();  // ComboBox para las carreteras
        fieldsPanel.add(_roadsCombo);

        // Etiqueta y campo para seleccionar el clima
        JLabel weatherLabel = new JLabel("Weather:");
        fieldsPanel.add(weatherLabel);
        _weatherCombo = new JComboBox<>(Weather.values());  // ComboBox para las condiciones meteorológicas
        fieldsPanel.add(_weatherCombo);

        // Etiqueta y campo para seleccionar el número de ticks
        JLabel ticksLabel = new JLabel("Ticks:");
        fieldsPanel.add(ticksLabel);
        _ticksSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));  // Spinner para los ticks
        _ticksSpinner.setMaximumSize(new Dimension(80, 32));
        fieldsPanel.add(_ticksSpinner);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));  // Espaciado

        // Panel para los botones de acción (OK y Cancelar)
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setAlignmentX(CENTER_ALIGNMENT);
        mainPanel.add(buttonsPanel);

        // Botón Cancelar
        _cancelButton = new JButton("Cancel");
        _cancelButton.addActionListener(e -> dispose());  // Cierra el diálogo al hacer clic
        buttonsPanel.add(_cancelButton);

        // Botón OK
        _okButton = new JButton("OK");
        _okButton.addActionListener(e -> {
            try {
                String road = (String) _roadsCombo.getSelectedItem();  // Obtener la carretera seleccionada
                Weather weather = (Weather) _weatherCombo.getSelectedItem();  // Obtener el clima seleccionado
                int ticks = (Integer) _ticksSpinner.getValue();  // Obtener el número de ticks seleccionado
                List<Pair<String, Weather>> ws = new ArrayList<>();
                ws.add(new Pair<>(road, weather));  // Creamos una pareja de carretera y clima
                _ctrl.addEvent(new SetWeatherEvent(ticks, ws));  // Añadimos el evento al controlador
                dispose();  // Cerramos el diálogo
            } catch (Exception ex) {
                ViewUtils.showErrorMsg(this, "Error: " + ex.getMessage());  // Mostramos un mensaje de error si algo sale mal
            }
        });
        buttonsPanel.add(_okButton);

        setPreferredSize(new Dimension(300, 200));  // Tamaño preferido del diálogo
        pack();  // Ajusta el tamaño según el contenido
        setLocationRelativeTo(null);  // Centra el diálogo en la pantalla
    }

    // Método para abrir el diálogo y cargar las carreteras disponibles
    public void open() {
        _roadsCombo.removeAllItems();  // Limpiamos el combo de carreteras
        for (Road r : _roads) {
            _roadsCombo.addItem(r.getId());  // Añadimos cada carretera al combo
        }
        if (_roads.isEmpty()) {
            ViewUtils.showErrorMsg(this, "No roads available");  // Si no hay carreteras disponibles, mostramos un mensaje de error
            return;
        }
        setVisible(true);  // Mostramos el diálogo
    }

    // Método que actualiza la lista de carreteras cuando se recibe una actualización
    private void updateRoads(RoadMap map) {
        _roads = new ArrayList<>(map.getRoads());  // Actualizamos la lista de carreteras
    }

    // Métodos del observador para recibir eventos de la simulación
    @Override
    public void onAdvance(RoadMap map, Collection<simulator.model.Event> events, int time) {
        updateRoads(map);  // Actualizamos las carreteras al avanzar
    }

    @Override
    public void onEventAdded(RoadMap map, Collection<simulator.model.Event> events, simulator.model.Event e, int time) {
        updateRoads(map);  // Actualizamos las carreteras al añadir un evento
    }

    @Override
    public void onReset(RoadMap map, Collection<simulator.model.Event> events, int time) {
        updateRoads(map);  // Actualizamos las carreteras al reiniciar
    }

    @Override
    public void onRegister(RoadMap map, Collection<simulator.model.Event> events, int time) {
        updateRoads(map);  // Actualizamos las carreteras al registrarse
    }
}
