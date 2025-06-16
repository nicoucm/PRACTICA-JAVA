package simulator.model;

public abstract class NewRoadEvent extends Event {
	
	protected String _id;  // Identificador de la carretera
	protected String _srcJun;  // ID de la intersección de origen
	protected String _destJun;  // ID de la intersección de destino
	protected int _length;  // Longitud de la carretera
	protected int _co2Limit;  // Límite de CO2 de la carretera
	protected int _maxSpeed;  // Velocidad máxima de la carretera
	protected Weather _weather;  // Condiciones meteorológicas en la carretera

	// Constructor: Inicializa el evento de nueva carretera con los parámetros especificados
	NewRoadEvent(int time, String id, String srcJun, String destJun, int length, int co2Limit, int maxSpeed, Weather weather) {
		super(time);  // Llama al constructor de la clase base Event con el tiempo
		_id = id;
		_srcJun = srcJun;
		_destJun = destJun;
		_length = length;
		_co2Limit = co2Limit;
		_maxSpeed = maxSpeed;
		_weather = weather;
	}
	
	// Método toString que genera una representación en cadena del evento
	@Override
	public String toString() {
	    return "New Road '" + _id + "' from '" + _srcJun + "' to '" + _destJun + 
	           "', length: " + _length + ", CO2 limit: " + _co2Limit + 
	           ", max speed: " + _maxSpeed + ", weather: " + _weather;
	}

}
