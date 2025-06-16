package simulator.model;

public class InterCityRoad extends Road {

    /**
     * Constructor de la clase InterCityRoad.
     * Inicializa una carretera interurbana con los parámetros dados, invocando el constructor de la clase base.
     * Además, llama al método `updateSpeedLimit` para establecer el límite de velocidad de la carretera.
     * 
     * @param id Identificador de la carretera.
     * @param srcJunc Intersección de origen.
     * @param destJunc Intersección de destino.
     * @param maxSpeed Velocidad máxima de la carretera.
     * @param contLimit Límite de contaminación aceptable.
     * @param length Longitud de la carretera.
     * @param weather Condiciones meteorológicas actuales.
     */
    public InterCityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) {
        // Llama al constructor de la clase base (Road) para inicializar los atributos comunes
        super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
        // Llama al método que ajusta el límite de velocidad según las condiciones actuales
        updateSpeedLimit();
    }

    /**
     * Método para reducir la contaminación total en la carretera según las condiciones meteorológicas.
     * Dependiendo del tipo de clima, la contaminación total se ajusta en un porcentaje específico.
     */
    @Override
    public void reduceTotalContamination() {
        int x = 0;
        
        // Ajusta el valor de 'x' dependiendo del clima actual.
        switch(weather) {
            case SUNNY:
                x = 2;
                break;
            case CLOUDY:
                x = 3;
                break;
            case RAINY:
                x = 10;
                break;
            case WINDY:
                x = 15;
                break;
            case STORM:
                x = 20;
                break;
        }
        
        // Reducción de la contaminación total en función del porcentaje 'x'
        totalCont = (int)(((100.0 - x) / 100.0) * totalCont);
    }

    /**
     * Actualiza el límite de velocidad de la carretera en función de la contaminación.
     * Si la contaminación total excede el límite permitido, se reduce el límite de velocidad.
     */
    @Override
    public void updateSpeedLimit() {
        double x = 1; // Valor predeterminado para el factor de reducción de la velocidad (sin reducción)

        // Si la contaminación total supera el límite, se reduce el límite de velocidad al 50%
        if (totalCont > contLimit) {
            x = 0.5;
        }
        
        // Actualiza el límite de velocidad en función del factor 'x'
        speedLimit = (int)(maxSpeed * x);
    }

    /**
     * Calcula la velocidad del vehículo en función del clima y el límite de velocidad de la carretera.
     * Si el clima es tormentoso, se reduce la velocidad del vehículo a un 80% de la velocidad máxima.
     * 
     * @param v El vehículo cuyo límite de velocidad se calcula.
     * @return La velocidad calculada para el vehículo.
     */
    @Override
    public int calculateVehicleSpeed(Vehicle v) {
        double x = 1; // Valor predeterminado para el factor de reducción de velocidad (sin reducción)

        // Si el clima es tormentoso, se reduce la velocidad al 80% del límite
        if (weather == Weather.STORM) {
            x = 0.8;
        }
        
        // Calcula la velocidad del vehículo en función del límite de velocidad de la carretera
        v.setSpeed((int)(speedLimit * x));
        
        return v.getSpeed();
    }
}
