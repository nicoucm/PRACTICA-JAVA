package simulator.model;

public class NewInterCityRoadEvent extends NewRoadEvent {

    // Constructor: Inicializa el evento con la información de la carretera interurbana.
    public NewInterCityRoadEvent(int time, String id, String srcJun, String destJun, int length, int co2Limit, int maxSpeed,
            Weather weather) {
        super(time, id, srcJun, destJun, length, co2Limit, maxSpeed, weather);
    }

    // Sobrescribe el método execute para agregar la carretera interurbana al mapa de simulación.
    @Override
    public void execute(RoadMap map) {
        map.addRoad(new InterCityRoad(getId(), map.getJunction(getSrcJun()), map.getJunction(getDestJun()), getMaxSpeed(), getCo2Limit(), getLength(), getWeather()));
    }
    
    // Sobrescribe el método toString para dar una representación en cadena del evento.
    @Override
    public String toString() {
        return "New InterCity Road '" + getId() + "' from '" + getSrcJun() + "' to '" + getDestJun() + 
               "', length: " + getLength() + ", CO2 limit: " + getCo2Limit() + 
               ", max speed: " + getMaxSpeed() + ", weather: " + getWeather();
    }

    // Métodos getters
    public String getId() {
        return _id;
    }

    public String getSrcJun() {
        return _srcJun;
    }

    public String getDestJun() {
        return _destJun;
    }

    public int getLength() {
        return _length;
    }

    public int getCo2Limit() {
        return _co2Limit;
    }

    public int getMaxSpeed() {
        return _maxSpeed;
    }

    public Weather getWeather() {
        return _weather;
    }
}

