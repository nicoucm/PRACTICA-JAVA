package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class Road extends SimulatedObject {
    
    protected Junction origen;
    protected Junction destino;
    protected int length;
    protected int maxSpeed;
    protected int speedLimit;
    protected int contLimit;
    protected Weather weather;
    protected int totalCont;
    protected List<Vehicle> vehicles;
    protected Comparator<Vehicle> cmp;

    public Road(String id, Junction origen, Junction destino, int maxSpeed, int contLimit, int length, Weather weather) {
        super(id);

        if ((maxSpeed < 1) || (contLimit < 1) || (length < 1) || (origen == null) || (destino == null) || (weather == null)) {
            throw new IllegalArgumentException("illegal argument in Road constructor");
        } else {
            this.origen = origen;
            this.destino = destino;
            this.maxSpeed = maxSpeed;
            this.contLimit = contLimit;
            this.length = length;
            this.weather = weather;
            
            origen.addOutGoingRoad(this);
            destino.addIncomingRoad(this);
            
            vehicles = new ArrayList<Vehicle>();
            
            // Comparador para ordenar los vehículos según su ubicación
            cmp = new Comparator<Vehicle>() {
                @Override
                public int compare(Vehicle v1, Vehicle v2) {
                    if (v1.getLocation() > v2.getLocation()) {
                        return -1;
                    } else if (v1.getLocation() < v2.getLocation()) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            };
        }
    }

    // Método para agregar un vehículo a la carretera
    public void enter(Vehicle v) {
        if ((v.getSpeed() == 0) && (v.getLocation() == 0)) {
            vehicles.add(v);
            vehicles.sort(cmp);
        } else {
            throw new IllegalArgumentException("Vehicle is not valid in enter");
        }
    }

    // Método para eliminar un vehículo de la carretera
    public void exit(Vehicle v) {
        if (!vehicles.remove(v)) {
            throw new IllegalArgumentException("Vehicle not in list");
        }
    }

    // Cambia las condiciones meteorológicas de la carretera
    public void setWeather(Weather w) {
        if (w == null) {
            throw new IllegalArgumentException("Null weather in setWeather()");
        } else {
            weather = w;
        }
    }

    // Añade contaminación a la carretera
    public void addContamination(int c) {
        if (c < 0) {
            throw new IllegalArgumentException("Negative contamination");
        } else {
            totalCont += c;
        }
    }

    // Métodos abstractos que deben ser implementados por clases hijas
    public abstract void reduceTotalContamination();
    public abstract void updateSpeedLimit();
    public abstract int calculateVehicleSpeed(Vehicle v);

    // Avanza la simulación para la carretera
    @Override
    public void advance(int time) {
        reduceTotalContamination();
        updateSpeedLimit();
        
        for (Vehicle v : vehicles) {
            v.setSpeed(calculateVehicleSpeed(v));
            v.advance(0);  // Avanza el vehículo
        }
        
        vehicles.sort(cmp);  // Reordena los vehículos según su ubicación
    }

    // Genera un reporte de la carretera en formato JSON
    @Override
    public JSONObject report() {
        JSONObject jo = new JSONObject();
        JSONArray ja = new JSONArray();
        
        jo.put("speedlimit", speedLimit);
        jo.put("co2", totalCont);
        jo.put("weather", weather.toString());
        
        for (Vehicle v : vehicles) {
            ja.put(v.getId());
        }
        
        jo.put("vehicles", ja);
        jo.put("id", _id);
        
        return jo;
    }

    // Métodos getters
    public int getLength() {
        return length;
    }

    public Junction getDest() {
        return destino;
    }

    public Junction getSrc() {
        return origen;
    }

    public Weather getWeather() {
        return weather;
    }

    public int getContLimit() {
        return contLimit;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public int getTotalCO2() {
        return totalCont;
    }

    public int getSpeedLimit() {
        return speedLimit;
    }

    public List<Vehicle> getVehicles() {
        return Collections.unmodifiableList(new ArrayList<>(vehicles));
    }
}
