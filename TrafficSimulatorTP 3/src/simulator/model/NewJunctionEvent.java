package simulator.model;

public class NewJunctionEvent extends Event {

    private String id;
    private LightSwitchingStrategy lsStrategy;
    private DequeuingStrategy dqStrategy;
    private int x;
    private int y;

    // Constructor: Inicializa el evento con la información de la nueva intersección (junction).
    public NewJunctionEvent(int time, String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor) {
        super(time);
        this.id = id;
        this.lsStrategy = lsStrategy;
        this.dqStrategy = dqStrategy;
        this.x = xCoor;
        this.y = yCoor;
       // System.out.println("Creando NewJunctionEvent para " + id + " con coordenadas (" + xCoor + "," + yCoor + ")");
    }

    // Sobrescribe el método execute para agregar la intersección al mapa de simulación.
    @Override
    public void execute(RoadMap map) {
        map.addJunction(new Junction(getId(), getLsStrategy(), getDqStrategy(), getX(), getY()));
    }
    
    // Sobrescribe el método toString para dar una representación en cadena del evento.
    @Override
    public String toString() {
        return "New Junction '" + getId() + "' at (" + getX() + ", " + getY() + 
               "), Light Strategy: " + getLsStrategy() + ", Dequeue Strategy: " + getDqStrategy();
    }

    // Métodos getters
    public String getId() {
        return id;
    }

    public LightSwitchingStrategy getLsStrategy() {
        return lsStrategy;
    }

    public DequeuingStrategy getDqStrategy() {
        return dqStrategy;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
