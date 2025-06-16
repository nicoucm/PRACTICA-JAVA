package simulator.view;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import simulator.control.Controller;
import simulator.model.*;

public class StatusBar extends JPanel implements TrafficSimObserver {
    private JLabel label; // Etiqueta donde se muestra el tiempo y los eventos

    // Constructor de la clase que recibe el controlador
    public StatusBar(Controller ctrl) {
        initGUI(); // Inicializa la interfaz gráfica
        ctrl.addObserver(this); // Registra a este panel como observador del controlador
    }

    // Inicializa la interfaz gráfica del panel
    private void initGUI() {
        setLayout(new FlowLayout(FlowLayout.LEFT)); // Establece un flujo de disposición de izquierda a derecha
        label = new JLabel("Time: 0"); // Inicializa la etiqueta con el texto "Time: 0"
        add(label); // Añade la etiqueta al panel
    }

    // Este método se llama cuando el simulador avanza (actualiza el tiempo)
    @Override
    public void onAdvance(RoadMap map, Collection<simulator.model.Event> events, int time) {
        label.setText("Time: " + time); // Actualiza la etiqueta con el nuevo tiempo
    }
    
    // Este método se llama cuando se agrega un evento
    @Override
    public void onEventAdded(RoadMap map, Collection<simulator.model.Event> events, simulator.model.Event e, int time) {
        // Actualiza la etiqueta con el tiempo y el evento agregado
        label.setText("Time: " + time + " | Event added: " + e.toString());
    }

    // Este método se llama cuando el simulador se reinicia
    @Override
    public void onReset(RoadMap map, Collection<simulator.model.Event> events, int time) {
        label.setText("Time: 0"); // Restablece la etiqueta a "Time: 0"
    }

    // Este método se llama cuando el simulador se registra
    @Override
    public void onRegister(RoadMap map, Collection<simulator.model.Event> events, int time) {
        // No se necesita realizar ninguna acción en este caso
    }
}
