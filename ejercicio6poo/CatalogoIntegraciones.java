
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

//Registro central de integraciones (Modelo en MVC). Guarda TODAS las integraciones en una única lista
public class CatalogoIntegraciones {
    private ArrayList<IntegracionAPI> integraciones;

    public CatalogoIntegraciones() {
        this.integraciones = new ArrayList<>();
    }

    // Agrega una integración nueva al catálogo
    public void agregar(IntegracionAPI integracion) {
        integraciones.add(integracion);
    }

    public ArrayList<IntegracionAPI> getIntegraciones() { 
    	return integraciones; 
    	}

    // Busca una integración por su id (ignora mayúsculas), devuelve null si no existe
    public IntegracionAPI buscarPorId(String id) {
        for (IntegracionAPI i : integraciones) {
            if (i.getId().equalsIgnoreCase(id)) {
                return i;
            }
        }
        return null;
    }

    // Devuelve solo las integraciones activas, respetando el orden actual de la lista
    public ArrayList<IntegracionAPI> listarActivas() {
        ArrayList<IntegracionAPI> activas = new ArrayList<>();
        for (IntegracionAPI i : integraciones) {
            if (i.estaActiva()) {
                activas.add(i);
            }
        }
        return activas;
    }

    // Ordena el catálogo usando el orden natural definido en compareTo
    public void ordenar() {
        Collections.sort(integraciones);
    }

    // Convierte varios textos en una lista
    private ArrayList<String> lista(String[] items) {
        return new ArrayList<>(Arrays.asList(items));
    }

    // Carga inicial: crea las 11 integraciones de ejemplo
    public void cargarInicial() {
        // Ejemplo de pago con tarjeta
        agregar(new PasarelaTarjeta(
                "PAY-67", "Tarjeta de ejemplo", "Banco de prueba", 100,
                EstadoConexion.CONECTADO,
                "https://PasarelaTrarjeta.com", "1.0",
                2.0, "GTQ", 1000,
                lista(new String[]{"Visa"})
        ));

        // Ejemplo de transferencia bancaria
        agregar(new PasarelaTransferencia(
                "PAY-68", "Transferencia de ejemplo", "Banco de prueba", 200,
                EstadoConexion.CONECTADO,
                "https://PasarelaTransferencia.com", "1.0",
                1.0, "GTQ", 5000,
                "Banco de prueba", "123456", 24
        ));

        // Ejemplo de billetera digital
        agregar(new PasarelaBilletera(
                "PAY-69", "Billetera de ejemplo", "Proveedor de prueba", 100,
                EstadoConexion.CONECTADO,
                "https://Vegetta.777", "1.0",
                1.0, "GTQ", 500,
                "Billetera de ejemplo", "5555-0000", 1000
        ));

        // Ejemplo de proveedor de envíos
        agregar(new ProveedorLogistico(
                "LOG-67", "Envío de ejemplo", "Empresa de envíos", 100,
                EstadoConexion.CONECTADO,
                "https://67.67", "1.0",
                "Local", 1, 1.0, 10, 24,
                lista(new String[]{"Zona 18"})
        ));

        // Ejemplo de certificador de factura electrónica
        agregar(new CertificadorFEL(
                "FEL-67", "Certificador de ejemplo", "Empresa de prueba", 100,
                EstadoConexion.CONECTADO,
                "https://minecraft.xd", "1.0",
                "0000000-0", "Empresa de ejemplo",
                "General", 0.12, "A"
        ));
    }
}
