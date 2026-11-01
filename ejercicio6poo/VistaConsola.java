

import java.util.ArrayList;
import java.util.Scanner;


// Vista de consola (MVC): muestra menús y pide datos; la lógica la hace el controlador
public class VistaConsola {
    private ControladorIntegraciones controlador;
    private Scanner tecladito = new Scanner(System.in);

    public VistaConsola(ControladorIntegraciones controlador) {
        this.controlador = controlador;
    }

    // Ciclo principal del menú
    public void iniciar() {
        int opcion;

        do {
            System.out.println("\nINTEGRACIONES API");
            System.out.println("1. Listar integraciones activas");
            System.out.println("2. Buscar integración por ID");
            System.out.println("3. Ordenar catálogo por latencia");
            System.out.println("4. Operaciones de una integración");
            System.out.println("5. Solicitar reintegro (checkout)");
            System.out.println("0. Salir");

            opcion = (int) leerNumero("Elija una opción: ");

            switch (opcion) {
                case 1:
                    mostrarLista();
                    break;

                case 2:
                    buscar();
                    break;

                case 3:
                    controlador.ordenarPorLatencia();
                    System.out.println("Catálogo ordenado.");
                    mostrarLista();
                    break;

                case 4:
                    operaciones();
                    break;

                case 5:
                    reintegro();
                    break;

                case 0:
                    System.out.println("Hasta luego.");
                    break;

                default:
                    System.out.println("Opción no válida.");
                    break;
            }

        } while (opcion != 0);
    }

    // Imprime una línea por cada integración activa
    private void mostrarLista() {
        System.out.printf("%-8s %-32s %-28s %8s  %s%n", "ID", "NOMBRE", "TIPO", "LATENCIA", "ESTADO");
        for (IntegracionAPI i : controlador.listarActivas()) {
            System.out.printf("%-8s %-32s %-28s %5d ms  %s%n",
                    i.getId(), i.getNombre(), i.getTipo(), i.getLatenciaPromedio(), i.getEstado());
        }
    }

    // Pide un id y muestra su configuración
    private void buscar() {
        IntegracionAPI api = pedirIntegracion();
        if (api != null) {
            System.out.println("\n" + api.mostrarConfiguracion());
        }
    }

    // Muestra las operaciones de una integración y ejecuta la que el usuario elija
    private void operaciones() {
        IntegracionAPI api = pedirIntegracion();
        if (api == null) return;
        ArrayList<Operacion> ops = controlador.operacionesDisponibles(api);
        System.out.println("\nOperaciones disponibles para " + api.getNombre() + ":");
        for (int i = 0; i < ops.size(); i++) {
            System.out.println((i + 1) + ". " + ops.get(i));
        }
        int n = (int) leerNumero("Elija una operación (0 para cancelar): ");
        if (n < 1 || n > ops.size()) return;
        ejecutarYMostrar(api, ops.get(n - 1));
    }

    // Pide un monto y solicita un reintegro a la integración elegida (aunque no lo ofrezca)
    private void reintegro() {
        IntegracionAPI api = pedirIntegracion();
        if (api != null) {
            ejecutarYMostrar(api, Operacion.REINTEGRAR);
        }
    }

    // Pide los datos que la operación necesite, la ejecuta e imprime el resultado
    private void ejecutarYMostrar(IntegracionAPI api, Operacion op) {
        double valor = 0;
        String zona = "";
        if (op.pideMonto()) {
            valor = leerNumero("Monto: ");
        } else if (op.pideEnvio()) {
            valor = leerNumero("Peso del paquete (kg): ");
            System.out.print("Zona de destino: ");
            zona = tecladito.nextLine().trim();
        }
        ResultadoOperacion r = controlador.ejecutar(api, op, valor, zona);
        System.out.println("\n" + r);
        if (r.isExito() && r.getMonto() > 0) {
            System.out.printf("Monto: %.2f%n", r.getMonto());
        }
    }

    // Pide un id; si no existe avisa y devuelve null
    private IntegracionAPI pedirIntegracion() {
        System.out.print("ID de la integración: ");
        IntegracionAPI api = controlador.buscarPorId(tecladito.nextLine().trim());
        if (api == null) {
            System.out.println("No existe una integración con ese ID.");
        }
        return api;
    }

    // Lee un número validando la entrada; repite hasta que sea válido
    private double leerNumero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Double.parseDouble(tecladito.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un número válido.");
            }
        }
    }
}
