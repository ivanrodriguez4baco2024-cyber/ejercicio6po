// importa SwingUtilities para ejecutar la ventana en el hilo adecuado de Swing
import javax.swing.SwingUtilities;

// clase principal: aquí comienza la ejecución del programa.
public class Principal {

    // Método de entrada. Java lo ejecuta al iniciar el programa. Args contiene los argumentos que se escriban al ejecutarlo
    public static void main(String[] args) {

        // Crea el catálogo, que guarda las integraciones disponibles
        CatalogoIntegraciones catalogo = new CatalogoIntegraciones();

        // Carga los datos iniciales en el catálogo
        catalogo.cargarInicial();

        //Crea el controlador y le entrega el catálogo para que pueda gestionarlo.
        ControladorIntegraciones controlador =
                new ControladorIntegraciones(catalogo);

        // Comprueba que se haya escrito al menos un argumento y que el primero sea "consola", sin importar mayúsculas o minúsculas
        if (args.length > 0 && args[0].equalsIgnoreCase("consola")) {

            // Crea la vista de texto y la inicia.
            new VistaConsola(controlador).iniciar();

        } else {

            // Pide a Swing que cree y muestre la ventana en el hilo de eventos.
            SwingUtilities.invokeLater(
                    () -> new VentanaPrincipal(controlador).setVisible(true)
            );
        }
    }
}