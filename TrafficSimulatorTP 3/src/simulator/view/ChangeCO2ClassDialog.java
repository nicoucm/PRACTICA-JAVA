package simulator.view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import simulator.control.Controller;
import simulator.misc.Pair;
import simulator.model.*;

public class ChangeCO2ClassDialog extends JDialog implements TrafficSimObserver {
    private JComboBox<String> _vehiclesCombo;  // ComboBox para seleccionar un vehículo
    private JComboBox<Integer> _co2Combo;      // ComboBox para seleccionar la clase de CO2
    private JSpinner _ticksSpinner;             // Spinner para seleccionar el número de ticks
    private JButton _okButton, _cancelButton;   // Botones de OK y Cancelar
    private Controller _ctrl;                  // Controlador de la simulación
    private List<Vehicle> _vehicles;           // Lista de vehículos disponibles

    // Constructor de la clase
    public ChangeCO2ClassDialog(Controller ctrl) {
        super((Frame) null, "Change CO2 Class", true);  // Configura el JDialog
        _ctrl = ctrl;  // Asigna el controlador
        _vehicles = new ArrayList<>();  // Inicializa la lista de vehículos
        initGUI();  // Inicializa la interfaz gráfica
        _ctrl.addObserver(this);  // Añade el observador para recibir actualizaciones
    }

    // Método que inicializa la interfaz gráfica
    private void initGUI() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));  // Layout en columna
        setContentPane(mainPanel);

        // Etiqueta informativa sobre la acción a realizar
        JLabel infoLabel = new JLabel("<html>Select a vehicle, a CO2 class, and the number of ticks<br>to schedule a CO2 class change event.</html>");
        infoLabel.setAlignmentX(CENTER_ALIGNMENT);
        mainPanel.add(infoLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));  // Espaciado entre componentes

        // Panel que contiene los campos para la entrada de datos
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new GridLayout(3, 2, 5, 5));  // Layout en 3 filas y 2 columnas
        mainPanel.add(fieldsPanel);

        // Etiqueta y campo para seleccionar el vehículo
        JLabel vehicleLabel = new JLabel("Vehicle:");
        fieldsPanel.add(vehicleLabel);
        _vehiclesCombo = new JComboBox<>();  // ComboBox para los vehículos
        fieldsPanel.add(_vehiclesCombo);

        // Etiqueta y campo para seleccionar la clase de CO2
        JLabel co2Label = new JLabel("CO2 Class:");
        fieldsPanel.add(co2Label);
        _co2Combo = new JComboBox<>();  // ComboBox para la clase de CO2
        for (int i = 0; i <= 10; i++) {
            _co2Combo.addItem(i);  // Añadimos las clases de CO2 de 0 a 10
        }
        fieldsPanel.add(_co2Combo);

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
                String vehicle = (String) _vehiclesCombo.getSelectedItem();  // Obtener el vehículo seleccionado
                int co2 = (Integer) _co2Combo.getSelectedItem();  // Obtener la clase de CO2 seleccionada
                int ticks = (Integer) _ticksSpinner.getValue();  // Obtener el número de ticks seleccionado
                List<Pair<String, Integer>> cs = new ArrayList<>();
                cs.add(new Pair<>(vehicle, co2));  // Creamos una pareja de vehículo y clase de CO2
                _ctrl.addEvent(new SetContClassEvent(ticks, cs));  // Añadimos el evento al controlador
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

    // Método para abrir el diálogo y cargar los vehículos disponibles
    public void open() {
        _vehiclesCombo.removeAllItems();  // Limpiamos el combo de vehículos
        for (Vehicle v : _vehicles) {
            _vehiclesCombo.addItem(v.getId());  // Añadimos cada vehículo al combo
        }
        if (_vehicles.isEmpty()) {
            ViewUtils.showErrorMsg(this, "No vehicles available");  // Si no hay vehículos disponibles, mostramos un mensaje de error
            return;
        }
        setVisible(true);  // Mostramos el diálogo
    }

    // Método que actualiza la lista de vehículos cuando se recibe una actualización
    private void updateVehicles(RoadMap map) {
        _vehicles = new ArrayList<>(map.getVehicles());  // Actualizamos la lista de vehículos
    }

    // Métodos del observador para recibir eventos de la simulación
    @Override
    public void onAdvance(RoadMap map, Collection<simulator.model.Event> events, int time) {
        updateVehicles(map);  // Actualizamos los vehículos al avanzar
    }

    @Override
    public void onEventAdded(RoadMap map, Collection<simulator.model.Event> events, simulator.model.Event e, int time) {
        updateVehicles(map);  // Actualizamos los vehículos al añadir un evento
    }

    @Override
    public void onReset(RoadMap map, Collection<simulator.model.Event> events, int time) {
        updateVehicles(map);  // Actualizamos los vehículos al reiniciar
    }

    @Override
    public void onRegister(RoadMap map, Collection<simulator.model.Event> events, int time) {
        updateVehicles(map);  // Actualizamos los vehículos al registrarse
    }
}
