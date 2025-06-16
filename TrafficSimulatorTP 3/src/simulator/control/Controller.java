package simulator.control; // Este archivo pertenece al paquete "simulator.control"

import java.io.InputStream;
import java.io.OutputStream;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.Event;
import simulator.model.TrafficSimObserver;
import simulator.model.TrafficSimulator;

public class Controller {

    // === Atributos privados ===

    private TrafficSimulator simulator;         // El modelo: se encarga de simular el tráfico
    private Factory<Event> eventsFactory;       // Fábrica para crear objetos Event desde JSON

    // === Constructor ===

    public Controller(TrafficSimulator sim, Factory<Event> factory) {
        // Validamos que no se pasen argumentos nulos
        if (sim == null || factory == null)
            throw new IllegalArgumentException("Arguments cannot be null");

        // Inicializamos atributos
        this.simulator = sim;
        this.eventsFactory = factory;
    }

    // === Carga de eventos desde archivo JSON ===

    public void loadEvents(InputStream input) {
        // Leemos todo el contenido del InputStream como un JSON
        JSONObject root = new JSONObject(new JSONTokener(input));

        // Validamos que exista un array llamado "events"
        if (!root.has("events") || !(root.get("events") instanceof JSONArray))
            throw new IllegalArgumentException("Invalid JSON format: missing 'events'");

        // Obtenemos el array de eventos
        JSONArray eventsArray = root.getJSONArray("events");

        // Iteramos por cada evento JSON
        for (int i = 0; i < eventsArray.length(); i++) {
            JSONObject evt = eventsArray.getJSONObject(i);                    // Obtenemos el objeto JSON del evento
            simulator.addEvent(eventsFactory.createInstance(evt));            // Lo convertimos a un Event y lo añadimos al simulador
        }
    }

    // === Ejecutar simulación y guardar resultados en OutputStream ===

    public void run(int steps, OutputStream output) {
        JSONArray states = new JSONArray();  // Aquí guardamos los estados tras cada tick

        for (int i = 0; i < steps; i++) {
            simulator.advance();                     // Avanzamos un paso en el simulador
            states.put(simulator.report());          // Guardamos el estado actual
        }

        JSONObject result = new JSONObject();        // Creamos el objeto final
        result.put("states", states);                // Lo rellenamos con todos los estados

        try {
            output.write(result.toString(3).getBytes()); // Escribimos el resultado en formato JSON indentado
        } catch (Exception e) {
            e.printStackTrace();  // Mostramos el error si algo sale mal al escribir
        }
    }

    // === Reinicia el simulador ===

    public void reset() {
        simulator.reset();   // Llama al método reset del simulador
    }

    // === Añadir un observador (vista) ===

    public void addObserver(TrafficSimObserver o) {
        simulator.addObserver(o);  // Lo registra en el modelo
    }

    // === Eliminar un observador (vista) ===

    public void removeObserver(TrafficSimObserver o) {
        simulator.removeObserver(o);  // Lo quita del registro
    }

    // === Añadir un evento manualmente (por ejemplo, desde un diálogo) ===

    public void addEvent(Event e) {
        simulator.addEvent(e);  // Añade un evento al simulador
    }

    // === Ejecutar simulación sin guardar salida (para GUI) ===

    public void run(int n) {
        int i = 0;
        while (i < n) {
            simulator.advance();  // Avanza paso a paso
            i++;
        }
    }
}

