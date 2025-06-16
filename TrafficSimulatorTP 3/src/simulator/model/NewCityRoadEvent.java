package simulator.model;

public class NewCityRoadEvent extends NewRoadEvent {

	// Constructor: Inicializa el evento con la información de la carretera.
	public NewCityRoadEvent(int time, String id, String srcJun, String destJun, int length, int co2Limit, int maxSpeed,
			Weather weather) {
		// Llama al constructor de la clase base (NewRoadEvent)
		super(time, id, srcJun, destJun, length, co2Limit, maxSpeed, weather);
	}

	// Método que ejecuta el evento: crea una nueva carretera de tipo CityRoad.
	@Override
	public void execute(RoadMap map) {
	    map.addRoad(new CityRoad(getId(), map.getJunction(getSrcJun()), map.getJunction(getDestJun()), getMaxSpeed(), getCo2Limit(), getLength(), getWeather()));
	}
	
	// Método para representar el evento como una cadena de texto.
	@Override
	public String toString() {
	    return "New City Road '" + getId() + "' from '" + getSrcJun() + "' to '" + getDestJun() + 
	           "', length: " + getLength() + ", CO2 limit: " + getCo2Limit() + 
	           ", max speed: " + getMaxSpeed() + ", weather: " + getWeather();
	}

	// Métodos getters para obtener la información de la carretera.
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

