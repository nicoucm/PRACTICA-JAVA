package simulator.view;

import simulator.control.Controller;
import simulator.model.*;
import simulator.model.Event;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;

public class ControlPanel extends JPanel implements TrafficSimObserver {
    private Controller _ctrl;  // Controlador que gestiona la simulación
    private boolean _stopped;  // Bandera que indica si la simulación está detenida
    private JButton _loadButton, _co2Button, _weatherButton, _runButton, _stopButton, _exitButton;  // Botones de control
    private JSpinner _ticksSpinner;  // Spinner para seleccionar el número de ticks

    // Constructor del panel de control
    public ControlPanel(Controller ctrl) {
        _ctrl = ctrl;  // Asigna el controlador
        _stopped = true;  // Inicialmente la simulación está detenida
        initGUI();  // Inicializa la interfaz gráfica
        _ctrl.addObserver(this);  // Se agrega como observador del controlador
    }

    // Método que inicializa la interfaz gráfica
    private void initGUI() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);  // No se puede mover la barra de herramientas
        setLayout(new BorderLayout());  // Usa BorderLayout para el panel principal
        add(toolBar, BorderLayout.PAGE_START);  // Añade la barra de herramientas al panel

        // Botón Load: carga eventos
        _loadButton = new JButton();
        _loadButton.setIcon(loadIcon("open.png"));  // Carga el icono para el botón
        _loadButton.setToolTipText("Load events");  // Texto de ayuda
        _loadButton.addActionListener(e -> loadEvents());  // Acción para cargar eventos
        toolBar.add(_loadButton);  // Añade el botón a la barra de herramientas
        toolBar.addSeparator();  // Separa botones

        // Botón CO2: cambia la clase de CO2
        _co2Button = new JButton();
        _co2Button.setIcon(loadIcon("co2class.png"));
        _co2Button.setToolTipText("Change CO2 class");
        _co2Button.addActionListener(e -> changeCO2Class());
        toolBar.add(_co2Button);

        // Botón Weather: cambia las condiciones meteorológicas
        _weatherButton = new JButton();
        _weatherButton.setIcon(loadIcon("weather.png"));
        _weatherButton.setToolTipText("Change weather");
        _weatherButton.addActionListener(e -> changeWeather());
        toolBar.add(_weatherButton);
        toolBar.addSeparator();  // Separa botones

        // Botón Run: inicia la simulación
        _runButton = new JButton();
        _runButton.setIcon(loadIcon("run.png"));
        _runButton.setToolTipText("Run simulation");
        _runButton.addActionListener(e -> runSimulation());
        toolBar.add(_runButton);

        // Botón Stop: detiene la simulación
        _stopButton = new JButton();
        _stopButton.setIcon(loadIcon("stop.png"));
        _stopButton.setToolTipText("Stop simulation");
        _stopButton.addActionListener(e -> _stopped = true);  // Detiene la simulación
        _stopButton.setEnabled(false);  // Está deshabilitado hasta que la simulación comience
        toolBar.add(_stopButton);

        // Spinner para los ticks (número de pasos de simulación)
        toolBar.add(new JLabel(" Ticks: "));
        _ticksSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 1000, 1));  // Modelo de números de ticks
        _ticksSpinner.setMaximumSize(new Dimension(80, 32));
        toolBar.add(_ticksSpinner);
        toolBar.addSeparator();  // Separa botones
        
        // Botón Exit: cierra la aplicación
        toolBar.add(Box.createHorizontalGlue());
        
        
        _exitButton = new JButton();
        _exitButton.setIcon(loadIcon("exit.png"));
        _exitButton.setToolTipText("Exit");
        _exitButton.addActionListener(e -> ViewUtils.quit(this));  // Acción para salir
        toolBar.add(_exitButton);
    }

    // Método para cargar un icono desde los recursos
    private ImageIcon loadIcon(String name) {
        try {
            return new ImageIcon("resources/icons/" + name);  // Ruta del icono
        } catch (Exception e) {
            return null;  // Si no se puede cargar el icono, se devuelve null
        }
    }

    // Método para cargar los eventos desde un archivo
    private void loadEvents() {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("resources/examples"));  // Directorio por defecto
        int result = chooser.showOpenDialog(this);  // Muestra el cuadro de diálogo de apertura
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                _ctrl.reset();  // Reinicia la simulación
                _ctrl.loadEvents(new FileInputStream(chooser.getSelectedFile()));  // Carga los eventos del archivo seleccionado
            } catch (Exception e) {
                ViewUtils.showErrorMsg(this, "Error loading events: " + e.getMessage());  // Muestra un error si no se pueden cargar los eventos
            }
        }
    }

    // Método para abrir el diálogo de cambio de clase CO2
    private void changeCO2Class() {
        ChangeCO2ClassDialog dialog = new ChangeCO2ClassDialog(_ctrl);
        dialog.open();  // Muestra el diálogo
    }

    // Método para abrir el diálogo de cambio de clima
    private void changeWeather() {
        ChangeWeatherDialog dialog = new ChangeWeatherDialog(_ctrl);
        dialog.open();  // Muestra el diálogo
    }

    // Método para iniciar la simulación
    private void runSimulation() {
        _stopped = false;
        _loadButton.setEnabled(false);  // Deshabilita los botones durante la simulación
        _co2Button.setEnabled(false);
        _weatherButton.setEnabled(false);
        _runButton.setEnabled(false);
        _stopButton.setEnabled(true);  // Habilita el botón de parada
        _exitButton.setEnabled(false);
        int ticks = (Integer) _ticksSpinner.getValue();  // Obtiene el número de ticks seleccionados
        run_sim(ticks);  // Inicia la simulación
    }

    // Método recursivo para ejecutar los pasos de la simulación
    private void run_sim(int n) {
        if (n > 0 && !_stopped) {  // Si la simulación no se ha detenido
            try {
                _ctrl.run(1);  // Avanza un paso de simulación
                SwingUtilities.invokeLater(() -> run_sim(n - 1));  // Llama recursivamente para continuar con el siguiente tick
            } catch (Exception e) {
                ViewUtils.showErrorMsg(this, e.getMessage());  // Muestra un error si ocurre alguna excepción
                _stopped = true;  // Detiene la simulación
                updateButtons();  // Actualiza los botones
            }
        } else {
            _stopped = true;  // Detiene la simulación cuando se ha completado
            updateButtons();  // Actualiza los botones
        }
    }

    // Método para actualizar los botones al detenerse la simulación
    private void updateButtons() {
        _loadButton.setEnabled(true);
        _co2Button.setEnabled(true);
        _weatherButton.setEnabled(true);
        _runButton.setEnabled(true);
        _stopButton.setEnabled(false);  // Deshabilita el botón de parada cuando la simulación está detenida
        _exitButton.setEnabled(true);
    }

    // Métodos de observador, no se necesita implementar nada en este caso
    @Override
    public void onAdvance(RoadMap map, Collection<Event> events, int time) {
        // No se necesita actualizar nada aquí por ahora
    }

    @Override
    public void onEventAdded(RoadMap map, Collection<Event> events, Event e, int time) {
        // No se necesita actualizar nada aquí por ahora
    }

    @Override
    public void onReset(RoadMap map, Collection<Event> events, int time) {
        // No se necesita actualizar nada aquí por ahora
    }

    @Override
    public void onRegister(RoadMap map, Collection<Event> events, int time) {
        // No se necesita actualizar nada aquí por ahora
    }
}
