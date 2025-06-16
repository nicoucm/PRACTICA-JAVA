package simulator.launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.control.Controller;
import simulator.factories.*;
import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.TrafficSimulator;
import simulator.view.MainWindow;

public class Main {

	// Valor por defecto para el límite de tiempo (número de ciclos o "ticks")
	private final static Integer _timeLimitDefaultValue = 10;
	// Valor por defecto para el modo de ejecución (interfaz gráfica)
	private final static String _modeDefaultValue = "gui";
	// Variables para almacenar los archivos de entrada y salida
	private static String _inFile = null;
	private static String _outFile = null;
	// Fábrica para generar eventos
	private static Factory<Event> _eventsFactory = null;
	// Número de ciclos a ejecutar
	private static Integer _ticks = null;
	// Modo de ejecución (puede ser "gui" o "console")
	private static String _mode = null;

	// Método para parsear los argumentos de la línea de comandos
	private static void parseArgs(String[] args) {
		// Definir las opciones válidas de la línea de comandos
		Options cmdLineOptions = buildOptions();

		// Crear el parser y parsear los argumentos
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);
			// Parsear cada opción individualmente
			parseMode(line);
			parseHelpOption(line, cmdLineOptions);
			parseInFileOption(line);
			parseOutFileOption(line);
			parseTicks(line);

			// Si hay argumentos restantes, lanzar un error
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}
	}

	// Método para definir las opciones válidas de la línea de comandos
	private static Options buildOptions() {
		Options cmdLineOptions = new Options();

		// Opción para el archivo de entrada de eventos
		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Events input file").build());
		// Opción para el archivo de salida
		cmdLineOptions.addOption(
				Option.builder("o").longOpt("output").hasArg().desc("Output file, where reports are written.").build());
		// Opción para mostrar la ayuda
		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message").build());
		// Opción para especificar el número de ciclos
		cmdLineOptions.addOption(Option.builder("t").longOpt("time").hasArg().desc("Number of cycles to run").build());
		// Opción para especificar el modo de ejecución
		cmdLineOptions.addOption(Option.builder("m").longOpt("mode").hasArg().desc("Mode to run").build());

		return cmdLineOptions;
	}

	// Método para parsear la opción de ayuda
	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			// Mostrar la ayuda si se especifica la opción
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}

	// Método para parsear el archivo de entrada
	private static void parseInFileOption(CommandLine line) throws ParseException {
		_inFile = line.getOptionValue("i");
		// Si el archivo de entrada no es proporcionado y el modo no es "gui", lanzar un error
		if (_inFile == null && !_mode.equals("gui")) {
			throw new ParseException("An events file is missing");
		}
	}

	// Método para parsear el archivo de salida
	private static void parseOutFileOption(CommandLine line) throws ParseException {
		if (_mode == "console") {
			_outFile = line.getOptionValue("o");
		}
	}

	// Método para parsear el número de ciclos a ejecutar
	private static void parseTicks(CommandLine line) {
		if (_mode == "console") {
			String s = line.getOptionValue("t");
			// Si no se especifica, se usa el valor por defecto
			if (s == null) {
				_ticks = _timeLimitDefaultValue;
			} else {
				_ticks = Integer.valueOf(s);
			}
		}
	}
	
	// Método para parsear el modo de ejecución
	private static void parseMode(CommandLine line) {
		String s = line.getOptionValue("m");
		// Si no se especifica, se usa el valor por defecto "gui"
		if (s == null) {
			_mode = _modeDefaultValue;
		} else {
			_mode = s;
		}
	}

	// Método para inicializar las fábricas de eventos y estrategias
	private static void initFactories() {
		ArrayList<Builder<Event>> ebs = new ArrayList<>();
		List<Builder<LightSwitchingStrategy>> lss = new ArrayList<>();
		List<Builder<DequeuingStrategy>> dqs = new ArrayList<>();
		
		// Añadir estrategias de control de luces
		lss.add(new RoundRobinStrategyBuilder());
		lss.add(new MostCrowdedStrategyBuilder());
		
		// Añadir estrategias de desalojo de vehículos
		dqs.add(new MoveAllStrategyBuilder());
		dqs.add(new MoveFirstStrategyBuilder());
		
		// Añadir diferentes tipos de eventos
		ebs.add(new NewJunctionEventBuilder(new BuilderBasedFactory<>(lss), new BuilderBasedFactory<>(dqs)));
		ebs.add(new NewCityRoadEventBuilder());
		ebs.add(new NewInterCityRoadEventBuilder());
		ebs.add(new SetContClassEventBuilder());
		ebs.add(new SetWeatherEventBuilder());
		ebs.add(new NewVehicleEventBuilder());
		
		// Crear la fábrica de eventos basada en las fábricas de los eventos
		_eventsFactory = new BuilderBasedFactory<>(ebs);
	}

	// Método para iniciar el modo por lotes (consola)
	private static void startBatchMode() throws JSONException, Exception {		
		TrafficSimulator ts = new TrafficSimulator();
		Controller cont = new Controller(ts, _eventsFactory);
		OutputStream out = null;
		
		// Cargar los eventos desde el archivo de entrada
		cont.loadEvents(new FileInputStream(new File(_inFile)));
		
		// Si no se especifica un archivo de salida, utilizar la salida estándar
		if (_outFile == null) {
			out = System.out;
		} else {
			out = new PrintStream(_outFile);
		}
		
		// Ejecutar el simulador
		cont.run(_ticks, out);
	}

	// Método para iniciar el modo gráfico (GUI)
	private static void startGUIMode() throws JSONException, Exception {
		TrafficSimulator ts = new TrafficSimulator();
		Controller cont = new Controller(ts, _eventsFactory);
		OutputStream out = null;
		
		// Si hay un archivo de entrada, cargar los eventos
		if (_inFile != null) {
			cont.loadEvents(new FileInputStream(new File(_inFile)));
		}
		
		// Ejecutar la interfaz gráfica en un hilo separado
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new MainWindow(cont);
			}
		});
		
		// Si no se especifica un archivo de salida, utilizar la salida estándar
		if (_outFile == null) {
			out = System.out;
		} else {
			out = new PrintStream(_outFile);
		}
		
		// No hay límite de ciclos en el modo gráfico, por lo que se pasa 0
		_ticks = 0;
		cont.run(_ticks, out);
	}

	// Método principal que arranca el simulador según los argumentos
	private static void start(String[] args) throws JSONException, Exception {
		// Inicializar las fábricas de eventos y estrategias
		initFactories();
		// Parsear los argumentos de la línea de comandos
		parseArgs(args);
		
		// Dependiendo del modo, se ejecuta el simulador en consola o con interfaz gráfica
		if (_mode.equals("gui")) {
			startGUIMode();
		} else {
			startBatchMode();
		}
	}

	// Punto de entrada principal para el programa
	public static void main(String[] args) {
		try {
			// Llamar al método start con los argumentos proporcionados
			start(args);
		} catch (Exception e) {
			// Capturar cualquier excepción y mostrarla en la salida estándar
			e.printStackTrace();
		}
	}
}

