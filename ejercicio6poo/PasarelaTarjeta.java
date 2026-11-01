
import java.util.ArrayList;

// Pasarela de tarjetas de crédito/débito. Cobra y reintegra
public class PasarelaTarjeta extends PasarelaPago implements Reintegrable {
    private ArrayList<String> marcasSoportadas;

    public PasarelaTarjeta(String id, String nombre, String proveedor, int latenciaPromedio, EstadoConexion estado, String urlBase, String version, double comisionPorcentaje, String monedaSoportada, double limiteTransaccion, ArrayList<String> marcasSoportadas) {
        super(id, nombre, proveedor, latenciaPromedio, estado, urlBase, version, comisionPorcentaje, monedaSoportada, limiteTransaccion);
        this.marcasSoportadas = marcasSoportadas;
    }

    public ArrayList<String> getMarcasSoportadas() { 
    	return marcasSoportadas; 
    	}

    // Cobra usando la lógica base y agrega las marcas aceptadas al mensaje
    public ResultadoOperacion cobrar(double monto) {
        ResultadoOperacion r = super.cobrar(monto);
        if (r.isExito()) {
            return ResultadoOperacion.exitoso(r.getMensaje() + " Marcas aceptadas: " + marcasSoportadas, monto);
        }
        return r;
    }

    // Devuelve dinero a la tarjeta del cliente
    public ResultadoOperacion reintegrar(double monto) {
        return procesarReintegro(monto);
    }

    public String getTipo() { return "Pasarela - Tarjeta"; 
    }

    public String getDetallesEspecificos() {
        return super.getDetallesEspecificos() + "\nMarcas soportadas: " + marcasSoportadas;
    }
}
