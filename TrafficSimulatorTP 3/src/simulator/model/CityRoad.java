package simulator.model;

public class CityRoad extends Road {

    /**
     * Constructor de la clase CityRoad.
     * 
     * @param id Identificador de la carretera.
     * @param srcJunc Junction de origen (inicio de la carretera).
     * @param destJunc Junction de destino (fin de la carretera).
     * @param maxSpeed Velocidad máxima permitida en la carretera.
     * @param contLimit Límite de contaminación permitido en la carretera.
     * @param length Longitud de la carretera.
     * @param weather Condiciones climáticas de la carretera.
     */
    public CityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) {
        super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
        updateSpeedLimit(); // Actualiza el límite de velocidad al crear la carretera.
    }

    /**
     * Método para reducir la contaminación total en la carretera según el clima.
     * 
     * Se reduce la contaminación total dependiendo de las condiciones climáticas.
     * 
     * - Si el clima es tormentoso o ventoso, se reduce en 10 unidades.
     * - En otros casos, se reduce en 2 unidades.
     */
    @Override
    public void reduceTotalContamination() {
        int x = 0;
        
        // Dependiendo del clima, se reduce la contaminación.
        switch(weather) {
            case WINDY:
            case STORM:
                x = 10; // Reducción mayor si el clima es tormentoso o ventoso.
                break;
            default:
                x = 2;  // Reducción menor en otros casos.
                break;
        }
        
        // Se asegura de que la contaminación total no sea negativa.
        totalCont = Math.max(0, (totalCont - x));
    }

    /**
     * Actualiza el límite de velocidad de la carretera.
     * Si la contaminación total supera el límite, se emite una advertencia.
     */
    @Override
    public void updateSpeedLimit() {
        speedLimit = maxSpeed; // La velocidad límite se establece inicialmente como la velocidad máxima.
        
        // Si la contaminación total supera el límite de contaminación, se emite una advertencia.
        if (totalCont > contLimit) {
            System.out.println("Advertencia: Carretera " + _id + " ha superado el límite de CO2 (" + totalCont + " > " + contLimit + ")");
        }
    }

    /**
     * Calcula la velocidad de un vehículo en la carretera según su clase de contaminación.
     * 
     * @param v El vehículo cuya velocidad se va a calcular.
     * @return La velocidad calculada del vehículo.
     */
    @Override
    public int calculateVehicleSpeed(Vehicle v) {
        // La velocidad del vehículo se ajusta según su clase de contaminación.
        v.setSpeed((int)(((11.0 - v.getContClass()) / 11.0) * speedLimit));
        return v.getSpeed();
    }
}
