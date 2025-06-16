package simulator.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class ViewUtils {

    /*
     * Devuelve la ventana (Frame) a la que pertenece el componente 'c'.
     */
    public static Frame getWindow(Component c) {
        Frame w = null;
        if (c != null) {
            if (c instanceof Frame)
                w = (Frame) c;  // Si el componente es una instancia de Frame, lo asigna a 'w'
            else
                w = (Frame) SwingUtilities.getWindowAncestor(c);  // Si no, busca la ventana contenedora del componente
        }
        return w;
    }

    /*
     * Muestra un cuadro de diálogo con un mensaje de error.
     */
    public static void showErrorMsg(String msg) {
        showErrorMsg(null, msg);  // Llama al otro método, pasando null para no especificar la ventana
    }

    /*
     * Muestra un cuadro de diálogo con un mensaje de error, colocándolo en relación
     * a la ventana a la que pertenece el componente 'c'.
     */
    public static void showErrorMsg(Component c, String msg) {
        JOptionPane.showMessageDialog(getWindow(c), msg, "ERROR", JOptionPane.ERROR_MESSAGE);
        // Muestra un mensaje de error con el texto especificado, en una ventana correspondiente
    }

    /*
     * Pregunta al usuario si está seguro de que desea salir. Si confirma, cierra la
     * aplicación usando System.exit.
     * El cuadro de diálogo se coloca en relación a la ventana de 'c'.
     */
    public static void quit(Component c) {

        int n = JOptionPane.showOptionDialog(getWindow(c), "Are sure you want to quit?", "Quit",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

        if (n == 0) {  // Si el usuario selecciona "Yes"
            System.exit(0);  // Finaliza la aplicación
        }
    }

    /*
     * Convierte el código hash del objeto 's' en un color.
     */
    public static Color get_color(Object s) {
        return new Color(s.hashCode());  // Devuelve un color basado en el código hash del objeto
    }
}

