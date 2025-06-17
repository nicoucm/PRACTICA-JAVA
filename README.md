# Simulador de Tráfico - Interfaz Gráfica (Práctica 2 TP2)

Este proyecto implementa una interfaz gráfica de usuario (GUI) para un simulador de tráfico, desarrollada siguiendo el patrón de diseño Modelo-Vista-Controlador (MVC). Forma parte de la **Práctica 2 de la asignatura TP2**.



---

## 📁 Estructura del proyecto

- `src/`: Código fuente del simulador.
  - `simulator.model`: Clases del modelo, incluyendo `TrafficSimulator` y los eventos.
  - `simulator.control`: Clase `Controller` que conecta la vista y el modelo.
  - `simulator.view`: Clases de la vista (GUI), incluyendo:
    - `MainWindow`: Ventana principal de la aplicación.
    - `ControlPanel`: Panel superior con botones de control.
    - `StatusBar`: Barra inferior de estado.
    - `MapComponent` y `MapByRoadComponent`: Vistas gráficas del mapa.
    - `EventsTableModel`, `VehiclesTableModel`, `RoadsTableModel`, `JunctionsTableModel`: Tablas de información.
    - `ChangeCO2ClassDialog` y `ChangeWeatherDialog`: Ventanas de diálogo para eventos personalizados.
- `resources/`: Recursos gráficos e iconos utilizados por la interfaz.
- `examples/`: Ficheros de ejemplo para cargar eventos.

---

## ⚙️ Requisitos

- Java 17 o superior
- No se permite el uso de herramientas de generación automática de GUIs (como WindowBuilder)
- Seguir exactamente la estructura de paquetes y nombres indicados en el enunciado

---

## 🚀 Ejecución

```bash
# Modo gráfico (GUI)
java -jar simulator.jar -m gui [-i path/to/events.ini]

# Modo consola (batch)
java -jar simulator.jar -m console -i path/to/events.ini -o path/to/output.txt
