
import java.util.ArrayList;

// Proveedor de envíos. Consulta estado, cotiza y genera guías. No es Cobrable ni Reintegrable: no maneja dinero del cliente
public class ProveedorLogistico extends IntegracionAPI implements Consultable, Cotizable, Documentable {
    private String tipoServicio;
    private double tarifaBase;
    private double tarifaPorKg;
    private double pesoMaximoKg;
    private int tiempoEntregaHoras;
    private ArrayList<String> zonasCobertura;
    private ArrayList<String> guias;
    private ArrayList<String> estadosPaquetes;

    public ProveedorLogistico(String id, String nombre, String proveedor, int latenciaPromedio,  EstadoConexion estado, String urlBase, String version, String tipoServicio, double tarifaBase, double tarifaPorKg, double pesoMaximoKg, int tiempoEntregaHoras, ArrayList<String> zonasCobertura) {
        super(id, nombre, proveedor, latenciaPromedio, estado, urlBase, version);
        this.tipoServicio = tipoServicio;
        this.tarifaBase = tarifaBase;
        this.tarifaPorKg = tarifaPorKg;
        this.pesoMaximoKg = pesoMaximoKg;
        this.tiempoEntregaHoras = tiempoEntregaHoras;
        this.zonasCobertura = zonasCobertura;
        this.guias = new ArrayList<>();
        this.estadosPaquetes = new ArrayList<>();
    }

    public String getTipoServicio() { 
    	return tipoServicio; 
    	}
    public double getTarifaBase() { 
    	return tarifaBase; 
    	}
    public double getTarifaPorKg() { 
    	return tarifaPorKg; 
    	}
    public double getPesoMaximoKg() { 
    	return pesoMaximoKg; 
    	}
    public int getTiempoEntregaHoras() { 
    	return tiempoEntregaHoras; 
    	}
    public ArrayList<String> getZonasCobertura() { 
    	return zonasCobertura; 
    	}

    // Informa el estado del proveedor y cuántos paquetes tiene registrados
    public String consultarEstado() {
        return getNombre() + " está " + getEstado() + ". Paquetes registrados: " + guias.size();
    }

    // Calcula el costo de un envío si el peso y la zona son válidos

    public ResultadoOperacion cotizar(double pesoKg, String zona) {
        if (!estaActiva()) {
            return ResultadoOperacion.fallido("El proveedor está desconectado.");
        }
        if (pesoKg <= 0 || pesoKg > pesoMaximoKg) {
            return ResultadoOperacion.fallido("El peso debe estar entre 0 y " + pesoMaximoKg + " kg.");
        }
        if (!cubreZona(zona)) {
            return ResultadoOperacion.fallido("No hay cobertura en la zona '" + zona + "'. Zonas: " + zonasCobertura);
        }
        double costo = tarifaBase + tarifaPorKg * pesoKg;
        return ResultadoOperacion.exitoso("Cotización para " + zona + ": entrega en aprox. "
                + tiempoEntregaHoras + " horas.", costo);
    }

    // Crea una guía de envío nueva, la registra como "Creado" y devuelve el texto de la guía
    public String generarDocumento() {
        String guia = "GUIA-" + getId() + "-" + String.format("%04d", guias.size() + 1);
        registrarPaquete(guia, "Creado");
        return "=== GUÍA DE ENVÍO ===\nNúmero: " + guia
             + "\nTransportista: " + getNombre()
             + "\nServicio: " + tipoServicio
             + "\nEstado: Creado";
    }

    // Guarda un paquete nuevo con su estado inicial
    public boolean registrarPaquete(String guia, String estado) {
        if (buscarIndiceGuia(guia) != -1) {
            return false; // la guía ya existía
        }
        guias.add(guia);
        estadosPaquetes.add(estado);
        return true;
    }

    // Devuelve el estado de un paquete a partir de su número de guía
    public String rastrearPaquete(String guia) {
        int i = buscarIndiceGuia(guia);

        if (i == -1) {
            return "Guía no encontrada";
        } else {
            return estadosPaquetes.get(i);
        }
    }

    // Dice si el proveedor entrega en esa zona (sin importar mayúsculas)
    public boolean cubreZona(String zona) {
        for (String z : zonasCobertura) {
            if (z.equalsIgnoreCase(zona)) {
                return true;
            }
        }
        return false;
    }

    // Busca la posición de una guía en la lista, -1 si no está
    private int buscarIndiceGuia(String guia) {
        for (int i = 0; i < guias.size(); i++) {
            if (guias.get(i).equals(guia)) {
                return i;
            }
        }
        return -1;
    }

   
    public String getTipo() { return "Proveedor logístico"; }

    public String getDetallesEspecificos() {
        return "Servicio: " + tipoServicio + "\nTarifa base: " + String.format("%.2f", tarifaBase) + "\nTarifa por kg: " + String.format("%.2f", tarifaPorKg)+ "\nPeso máximo: " + pesoMaximoKg + " kg" + "\nEntrega estimada: " + tiempoEntregaHoras + " horas"+ "\nZonas: " + zonasCobertura;
    }
}
