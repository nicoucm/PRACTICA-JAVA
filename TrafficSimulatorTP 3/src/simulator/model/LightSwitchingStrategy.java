package simulator.model;

import java.util.List;

public interface LightSwitchingStrategy {

    /**
     * Método para elegir cuál será el próximo semáforo verde.
     * 
     * @param roads Lista de carreteras entrantes al cruce.
     * @param qs Lista de colas de vehículos correspondientes a cada carretera.
     * @param currGreen El índice de la carretera que tiene el semáforo verde actualmente.
     * @param lastSwitchingTime El último tiempo en el que se cambió el semáforo.
     * @param currTime El tiempo actual de la simulación.
     * @return El índice de la carretera que debe tener el semáforo verde a continuación.
     */
    public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime, int currTime);
}
