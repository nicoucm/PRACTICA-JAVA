package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class RoadMap {
    
    private List<Junction> _junctions;
    private List<Road> _roads;
    private List<Vehicle> _vehicles;
    private Map<String, Junction> _junctionMap;
    private Map<String, Road> _roadMap;
    private Map<String, Vehicle> _vehicleMap;
    
    protected RoadMap() {
        _junctions = new ArrayList<Junction>();
        _roads = new ArrayList<Road>();
        _vehicles = new ArrayList<Vehicle>();
        _junctionMap = new HashMap<String, Junction>();
        _roadMap = new HashMap<String, Road>();
        _vehicleMap = new HashMap<String, Vehicle>();
    }
    
    public void addJunction(Junction j) {
        if (_junctionMap.containsKey(j.getId())) {
            throw new IllegalArgumentException("Junction already in map");
        }
        _junctions.add(j);
        _junctionMap.put(j.getId(), j);
    }
    
    public void addRoad(Road r) {
        if (_roadMap.containsKey(r.getId()) || !_junctionMap.containsValue(r.getDest()) || !_junctionMap.containsValue(r.getSrc())) {
            throw new IllegalArgumentException("Illegal argument in addRoad()");
        }
        _roads.add(r);
        _roadMap.put(r.getId(), r);
    }
    
    public void addVehicle(Vehicle v) {
        List<Junction> aux = v.getItinerary();
        
        if (_vehicleMap.containsKey(v.getId())) {
            throw new IllegalArgumentException("Vehicle already in Map");
        }
        
        for (int i = 0; i < v.getItinerary().size() - 1; i++) {
            if (aux.get(i).roadTo(aux.get(i + 1)) == null) {
                throw new IllegalArgumentException("Null road between junctions of itinerary");
            }
        }
        
        _vehicles.add(v);
        _vehicleMap.put(v.getId(), v);
    }
    
    public Junction getJunction(String id) {
        return _junctionMap.get(id);
    }
    
    public Road getRoad(String id) {
        return _roadMap.get(id);
    }
    
    public Vehicle getVehicle(String id) {
        return _vehicleMap.get(id);
    }
    
    public List<Junction> getJunctions() {
        return Collections.unmodifiableList(_junctions);
    }
    
    public List<Road> getRoads() {
        return Collections.unmodifiableList(_roads);
    }
    
    public List<Vehicle> getVehicles() {
        return Collections.unmodifiableList(_vehicles);
    }
    
    public void reset() {
        _junctions.clear();
        _roads.clear();
        _vehicles.clear();
        
        _junctionMap.clear();
        _roadMap.clear();
        _vehicleMap.clear();
    }
    
    public JSONObject report() {
        JSONObject jo = new JSONObject();
        JSONArray ja = new JSONArray();
        
        for (Junction j : _junctions) {
            ja.put(j.report());
        }
        jo.put("junctions", ja);
        
        ja = new JSONArray();
        for (Road r : _roads) {
            ja.put(r.report());
        }
        jo.put("roads", ja);
        
        ja = new JSONArray();
        for (Vehicle v : _vehicles) {
            ja.put(v.report());
        }
        jo.put("vehicles", ja);
        
        return jo;
    }
}
