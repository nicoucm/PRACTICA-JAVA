package simulator.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;

public class Junction extends SimulatedObject {
    // Atributos
    private List<Road> incomingRoads; // Lista de carreteras entrantes
    private Map<Junction, Road> outgoingRoads; // Mapa de carreteras salientes (Junction destino -> Road)
    private List<List<Vehicle>> queues; // Listas de vehículos en las colas de cada carretera
    private Map<Road, List<Vehicle>> roadQueues; // Mapa de colas de vehículos por carretera
    private int greenLightIndex; // Índice de la carretera con el semáforo verde
    private int lastSwitchTime; // Última vez que se cambió el semáforo
    private LightSwitchingStrategy lsStrategy; // Estrategia para cambiar el semáforo
    private DequeuingStrategy dqStrategy; // Estrategia para sacar vehículos de las colas
    private int x; // Coordenada X del cruce
    private int y; // Coordenada Y del cruce

    /**
     * Constructor para crear un cruce con las estrategias de semáforo y dequeuing, 
     * y las coordenadas del cruce.
     * 
     * @param id El identificador del cruce.
     * @param lsStrategy Estrategia para cambiar el semáforo.
     * @param dqStrategy Estrategia para sacar vehículos de las colas.
     * @param xCoor Coordenada X del cruce.
     * @param yCoor Coordenada Y del cruce.
     */
    public Junction(String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor) {
        super(id);
        
        // Validaciones de entrada
        if (lsStrategy == null || dqStrategy == null || xCoor < 0 || yCoor < 0) {
            throw new IllegalArgumentException("Argumentos inválidos en el constructor de Junction");
        }

        // Inicialización de los atributos
        this.lsStrategy = lsStrategy;
        this.dqStrategy = dqStrategy;
        this.x = xCoor;
        this.y = yCoor;
        this.lastSwitchTime = 0;
        this.greenLightIndex = -1;
        this.incomingRoads = new LinkedList<>();
        this.outgoingRoads = new HashMap<>();
        this.queues = new ArrayList<>();
        this.roadQueues = new HashMap<>();
    }

    // Métodos getter
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getGreenLightIndex() {
        return greenLightIndex;
    }

    public List<Road> getInRoads() {
        return incomingRoads;
    }

    /**
     * Añade una carretera entrante al cruce y crea su cola de vehículos asociada.
     * 
     * @param r La carretera entrante.
     */
    public void addIncomingRoad(Road r) {
        if (r.getDest() != this) {
            throw new IllegalArgumentException("La carretera " + r.getId() + " no tiene este cruce como destino");
        }
        incomingRoads.add(r);
        List<Vehicle> queueForRoad = new LinkedList<>();
        queues.add(queueForRoad);
        roadQueues.put(r, queueForRoad);
    }

    /**
     * Añade una carretera saliente al cruce.
     * 
     * @param r La carretera saliente.
     */
    public void addOutGoingRoad(Road r) {
        if (r.getSrc() != this) {
            throw new IllegalArgumentException("La carretera " + r.getId() + " no tiene este cruce como origen");
        }
        if (outgoingRoads.containsKey(r.getDest())) {
            throw new IllegalArgumentException("Ya existe una carretera saliente hacia " + r.getDest().getId());
        }
        outgoingRoads.put(r.getDest(), r);
    }

    /**
     * Añade un vehículo a la cola de la carretera correspondiente.
     * 
     * @param v El vehículo que entra en el cruce.
     */
    public void enter(Vehicle v) {
        Road currentRoad = v.getRoad();
        List<Vehicle> queue = roadQueues.get(currentRoad);
        if (queue == null) {
            throw new IllegalArgumentException("No se encontró cola para la carretera " + currentRoad.getId());
        }
        queue.add(v);
    }

    /**
     * Obtiene la carretera que conecta el cruce actual con otro cruce.
     * 
     * @param j El cruce de destino.
     * @return La carretera que conecta este cruce con el destino.
     */
    public Road roadTo(Junction j) {
        return outgoingRoads.get(j);
    }

    /**
     * Avanza el cruce según el tiempo actual.
     * Si hay vehículos en la cola con semáforo verde, los mueve a la siguiente carretera.
     * Luego, se evalúa cuál es la siguiente carretera que debe tener el semáforo verde.
     * 
     * @param currTime El tiempo actual del simulador.
     */
    @Override
    public void advance(int currTime) {
        // Mueve los vehículos si hay semáforo verde y vehículos en la cola
        if (greenLightIndex != -1 && !queues.get(greenLightIndex).isEmpty()) {
            List<Vehicle> vehiclesToAdvance = dqStrategy.dequeue(queues.get(greenLightIndex));
            for (Vehicle v : vehiclesToAdvance) {
                v.moveToNextRoad();
                queues.get(greenLightIndex).remove(v);
            }
        }
        
        // Evalúa cuál será el siguiente semáforo verde
        int nextGreen = -1;
        if (!queues.isEmpty()) {
            nextGreen = lsStrategy.chooseNextGreen(incomingRoads, queues, greenLightIndex, lastSwitchTime, currTime);
        }
        
        // Cambia el semáforo si es necesario
        if (nextGreen != greenLightIndex) {
            greenLightIndex = nextGreen;
            lastSwitchTime = currTime;
        }
    }

    /**
     * Genera un informe del estado actual del cruce.
     * 
     * @return Un objeto JSON con el estado del cruce.
     */
    @Override
    public JSONObject report() {
        JSONObject report = new JSONObject();
        report.put("id", _id);

        // Determina qué carretera tiene el semáforo verde
        String green = "none";
        if (greenLightIndex != -1 && !incomingRoads.isEmpty()) {
            green = incomingRoads.get(greenLightIndex).getId();
        }
        report.put("green", green);

        // Prepara el informe de las colas de vehículos
        JSONArray queuesArray = new JSONArray();
        for (Road r : incomingRoads) {
            JSONObject queueReport = new JSONObject();
            queueReport.put("road", r.getId());
            JSONArray vehiclesArray = new JSONArray();
            for (Vehicle v : roadQueues.get(r)) {
                vehiclesArray.put(v.getId());
            }
            queueReport.put("vehicles", vehiclesArray);
            queuesArray.put(queueReport);
        }
        report.put("queues", queuesArray);

        return report;
    }
}
