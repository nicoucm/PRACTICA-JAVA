package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

public class Vehicle extends SimulatedObject {
    
    private int maxSpeed;                 // Velocidad máxima del vehículo
    private int currentSpeed;             // Velocidad actual del vehículo
    private int contClass;                // Clase de contaminación del vehículo
    private VehicleStatus status;         // Estado del vehículo (pendiente, viajando, esperando, llegado)
    private List<Junction> itinerary;     // Itinerario del vehículo (lista de cruces que recorrerá)
    private Road road;                    // Carretera en la que se encuentra el vehículo
    private int location;                 // Ubicación actual del vehículo en la carretera
    private int totalCont;                // Total de contaminación generada por el vehículo
    private int travelledDistance;        // Distancia total recorrida por el vehículo

    // Constructor de la clase Vehicle
    public Vehicle(String id, int maxSpeed, int contClass, List<Junction> itinerary) {
        super(id); // Llamada al constructor de la clase base SimulatedObject

        // Validación de los parámetros del constructor
        if ((maxSpeed < 1) || ((contClass < 0) || (contClass > 10)) || (itinerary.size() < 2)) {
            throw new IllegalArgumentException("illegal argument in Vehicle");
        } else {
            this.maxSpeed = maxSpeed;
            this.contClass = contClass;
            this.itinerary = Collections.unmodifiableList(new ArrayList<>(itinerary));  // Hacemos que el itinerario sea inmutable
            this.currentSpeed = 0;           // Inicializamos la velocidad a 0
            this.status = VehicleStatus.PENDING;  // Estado inicial: PENDIENTE (esperando a ser asignado a una carretera)
            this.road = null;                // Inicialmente no está en ninguna carretera
            this.location = 0;               // Ubicación inicial en la carretera (0)
            this.totalCont = 0;              // Inicializamos la contaminación total a 0
            this.travelledDistance = 0;      // Inicializamos la distancia recorrida a 0
        }
    }

    // Métodos getter
    public int getLocation() {
        return location;
    }
    
    public int getSpeed() {
        return currentSpeed;
    }
    
    public int getMaxSpeed() {
        return maxSpeed;
    }
    
    public int getContClass() {
        return contClass;
    }
    
    public VehicleStatus getStatus() {
        return status;
    }
    
    public int getTotalCO2() {
        return totalCont;
    }
    
    public List<Junction> getItinerary() {
        return itinerary;
    }
    
    public Road getRoad() {
        return road;
    }
    
    public int getDistance() {
        return travelledDistance;
    }
    
    // Métodos setter
    public void setSpeed(int s) {
        if (s < 0) {
            throw new IllegalArgumentException("negative speed in Vehicle");
        } else {
            if (status == VehicleStatus.TRAVELING) {
                currentSpeed = Math.min(s, maxSpeed);  // La velocidad no puede ser mayor que la máxima
            }
        }
    }
    
    public void setContClass(int c) {
        if ((c < 0) || (c > 10)) {
            throw new IllegalArgumentException("illegal contClass in Vehicle");
        } else {
            contClass = c;
        }
    }
    
    // Método para avanzar el vehículo
    @Override
    public void advance(int time) {
        int newLoc = 0, c = 0;
        int length = road.getLength();      // Longitud de la carretera en la que se encuentra el vehículo
        int diff = 0;
        
        if (status == VehicleStatus.TRAVELING) {
            newLoc = Math.min(location + currentSpeed, length); // Calculamos la nueva ubicación
            diff = (newLoc - location); // Diferencia de distancia recorrida
            travelledDistance += diff;  // Sumamos la distancia recorrida al total
            c = diff * contClass;       // Contaminación generada en este avance
            location = newLoc;          // Actualizamos la ubicación
            totalCont += c;             // Sumamos la contaminación generada
            road.addContamination(c);   // Añadimos la contaminación a la carretera

            // Si el vehículo ha llegado al final de la carretera
            if (location == length) {
                road.getDest().enter(this);  // El vehículo entra en el siguiente cruce
                status = VehicleStatus.WAITING;  // El vehículo cambia a estado "esperando"
                currentSpeed = 0;  // La velocidad del vehículo se pone a 0
            }
        } else {
            currentSpeed = 0;  // Si no está viajando, la velocidad se mantiene a 0
        }
    }
    
    // Método para mover el vehículo a la siguiente carretera
    public void moveToNextRoad() {
        int lastInd = -1;
        if (status == VehicleStatus.PENDING) {
            // Si el vehículo está pendiente, lo asignamos a la primera carretera de su itinerario
            road = itinerary.get(0).roadTo(itinerary.get(1));  // Obtener la carretera entre los dos primeros cruces
            location = 0;  // La ubicación inicial es 0
            road.enter(this);  // El vehículo entra en la carretera
            status = VehicleStatus.TRAVELING;  // El vehículo comienza a viajar
        } else if (status == VehicleStatus.WAITING) {
            // Si el vehículo está esperando, lo movemos a la siguiente carretera de su itinerario
            lastInd = itinerary.indexOf(road.getDest());  // Encontramos el índice del cruce de destino actual
            road.exit(this);  // El vehículo sale de la carretera actual
            location = 0;  // El vehículo comienza de nuevo desde el inicio de la nueva carretera
            if (lastInd == (itinerary.size() - 1)) {
                road = null;  // Si ya ha llegado al último cruce, el vehículo ha llegado a su destino
                status = VehicleStatus.ARRIVED;
            } else {
                road = itinerary.get(lastInd).roadTo(itinerary.get(lastInd + 1));  // Obtener la siguiente carretera
                road.enter(this);  // El vehículo entra en la nueva carretera
                status = VehicleStatus.TRAVELING;  // El vehículo sigue viajando
            }
        } else {
            throw new IllegalArgumentException("illegal moveToNextRoad()");
        }
    }

    // Método para generar el informe del estado del vehículo
    @Override
    public JSONObject report() {
        JSONObject jo = new JSONObject();
        jo.put("id", _id);             // ID del vehículo
        jo.put("speed", currentSpeed); // Velocidad actual del vehículo
        jo.put("distance", travelledDistance);  // Distancia total recorrida
        jo.put("co2", totalCont);      // Contaminación total generada
        jo.put("class", contClass);    // Clase de contaminación del vehículo
        jo.put("status", status.toString()); // Estado del vehículo
        if ((status != VehicleStatus.ARRIVED) && (status != VehicleStatus.PENDING)) {
            jo.put("road", road.getId());  // Si no ha llegado a su destino ni está pendiente, añadimos la carretera en la que está
            jo.put("location", location);  // Añadimos la ubicación del vehículo en la carretera
        }
        return jo;  // Devolvemos el objeto JSON con el reporte del vehículo
    }
}
