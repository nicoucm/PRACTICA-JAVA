package simulator.view;

import javax.swing.*; // Importa las clases necesarias de Swing para la creación de la interfaz gráfica.
import java.awt.*; // Importa las clases necesarias para el diseño y el layout.
import simulator.control.Controller; // Importa el controlador de la simulación.

public class MainWindow extends JFrame {
    private Controller _ctrl; // Controlador de la simulación, responsable de gestionar la lógica de la simulación.

    // Constructor que inicializa la ventana principal de la simulación.
    public MainWindow(Controller ctrl) {
        super("Traffic Simulator"); // Define el título de la ventana.
        _ctrl = ctrl; // Asocia el controlador pasado como parámetro.
        initGUI(); // Inicializa la interfaz gráfica de usuario.
    }

    // Método que configura la interfaz gráfica.
    private void initGUI() {
        // Crea un panel principal con un diseño de BorderLayout.
        JPanel mainPanel = new JPanel(new BorderLayout());
        this.setContentPane(mainPanel); // Establece el panel principal como el contenido de la ventana.
        
        // Añade el panel de control en la parte superior (PAGE_START).
        mainPanel.add(new ControlPanel(_ctrl), BorderLayout.PAGE_START);
        
        // Añade la barra de estado en la parte inferior (PAGE_END).
        mainPanel.add(new StatusBar(_ctrl), BorderLayout.PAGE_END);

        // Crea un panel para las vistas que ocupará la parte central de la ventana.
        JPanel viewsPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.add(viewsPanel, BorderLayout.CENTER);

        // Crea un panel para las tablas y lo organiza en un layout vertical.
        JPanel tablesPanel = new JPanel();
        tablesPanel.setLayout(new BoxLayout(tablesPanel, BoxLayout.Y_AXIS));
        viewsPanel.add(tablesPanel);

        // Crea un panel para los mapas y lo organiza también en un layout vertical.
        JPanel mapsPanel = new JPanel();
        mapsPanel.setLayout(new BoxLayout(mapsPanel, BoxLayout.Y_AXIS));
        viewsPanel.add(mapsPanel);

        // Crea y añade las vistas de las tablas dentro del panel de tablas.
        // Cada vista tiene su propio modelo de datos y su tamaño preferido.
        
        // Vista de Eventos
        JPanel eventsView = createViewPanel(new JTable(new EventsTableModel(_ctrl)), "Events");
        eventsView.setPreferredSize(new Dimension(500, 200)); // Define el tamaño preferido para la vista.
        tablesPanel.add(eventsView);

        // Vista de Vehículos
        JPanel vehiclesView = createViewPanel(new JTable(new VehiclesTableModel(_ctrl)), "Vehicles");
        vehiclesView.setPreferredSize(new Dimension(500, 200));
        tablesPanel.add(vehiclesView);

        // Vista de Carreteras
        JPanel roadsView = createViewPanel(new JTable(new RoadsTableModel(_ctrl)), "Roads");
        roadsView.setPreferredSize(new Dimension(500, 200));
        tablesPanel.add(roadsView);

        // Vista de Cruces
        JPanel junctionsView = createViewPanel(new JTable(new JunctionsTableModel(_ctrl)), "Junctions");
        junctionsView.setPreferredSize(new Dimension(500, 200));
        tablesPanel.add(junctionsView);

        // Crea y añade las vistas de los mapas dentro del panel de mapas.
        
        // Vista del Mapa
        JPanel mapView = createViewPanel(new MapComponent(_ctrl), "Map");
        mapView.setPreferredSize(new Dimension(500, 400)); // Define el tamaño preferido para la vista del mapa.
        mapsPanel.add(mapView);

        // Vista del Mapa por Carretera
        JPanel mapByRoadView = createViewPanel(new MapByRoadComponent(_ctrl), "Map by Road");
        mapByRoadView.setPreferredSize(new Dimension(500, 400));
        mapsPanel.add(mapByRoadView);

        // Configura la operación de cierre de la ventana (NO hacer nada al cerrar la ventana).
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        
        // Ajusta el tamaño de la ventana según el contenido.
        this.pack();
        
        // Hace visible la ventana.
        this.setVisible(true);
    }

    // Método auxiliar para crear un panel de vista con un componente (como una tabla o un mapa) y un título.
    private JPanel createViewPanel(JComponent c, String title) {
        JPanel p = new JPanel(new BorderLayout()); // Crea un nuevo panel con un diseño BorderLayout.
        p.setBorder(BorderFactory.createTitledBorder(title));
        p.add(new JScrollPane(c)); // Añade el componente dentro de un JScrollPane (para permitir el desplazamiento).
        return p; // Devuelve el panel creado.
    }
}
