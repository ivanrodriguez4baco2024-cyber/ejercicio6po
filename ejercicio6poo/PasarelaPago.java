
import java.util.ArrayList;

// Base de todas las pasarelas de pago. Puede cobrar, consultar su estado y auditar sus eventos. No implementa Reintegrable: eso lo deciden las hijas
public abstract class PasarelaPago extends IntegracionAPI implements Consultable, Cobrable, Auditable {
    private double comisionPorcentaje;
    private String monedaSoportada;
    private double limiteTransaccion;
    private ArrayList<EventoAuditoria> historialEventos;

    public PasarelaPago(String id, String nombre, String proveedor, int latenciaPromedio, EstadoConexion estado, String urlBase, String version, double comisionPorcentaje, String monedaSoportada, double limiteTransaccion) {
        super(id, nombre, proveedor, latenciaPromedio, estado, urlBase, version);
        this.comisionPorcentaje = comisionPorcentaje;
        this.monedaSoportada = monedaSoportada;
        this.limiteTransaccion = limiteTransaccion;
        this.historialEventos = new ArrayList<>();
    }

    public double getComisionPorcentaje() { 
    	return comisionPorcentaje; 
    	}
    public String getMonedaSoportada() { 
    	return monedaSoportada; 
    	}
    public double getLimiteTransaccion() { 
    	return limiteTransaccion; 
    	}

    // Devuelve una frase con el estado y la latencia de la pasarela
   
    public String consultarEstado() {
        return getNombre() + " está " + getEstado() + " (latencia " + getLatenciaPromedio() + " ms)";
    }

    // Guarda un evento nuevo al final del historial
    public void registrarEvento(String descripcion) {
        historialEventos.add(new EventoAuditoria(descripcion));
    }

    // Devuelve la lista completa de eventos guardados
    public ArrayList<EventoAuditoria> consultarHistorialEventos() {
        return historialEventos;
    }

    // Calcula cuánto se queda la pasarela de comisión sobre un monto
    public double calcularComision(double monto) {
        return monto * comisionPorcentaje / 100.0;
    }

    // Revisa si un monto se puede procesar. Devuelve null si todo está bien, o el motivo del rechazo
    protected String validarCobro(double monto) {
        if (!estaActiva()) {
            return "La pasarela está desconectada.";
        }
        if (monto <= 0) {
            return "El monto debe ser mayor que cero.";
        }
        if (monto > limiteTransaccion) {
            return "El monto excede el límite de " + String.format("%.2f", limiteTransaccion) + " " + monedaSoportada + ".";
        }
        return null;
    }

    // Cobra un monto: valida, registra el evento y devuelve el resultado
    public ResultadoOperacion cobrar(double monto) {
        String error = validarCobro(monto);
        if (error != null) {
            registrarEvento("Cobro rechazado: " + error);
            return ResultadoOperacion.fallido(error);
        }
        
        registrarEvento("Cobro aprobado por " + String.format("%.2f", monto) + " " + monedaSoportada);
        return ResultadoOperacion.exitoso("Cobro aprobado. Comisión: " + String.format("%.2f", calcularComision(monto)) + " " + monedaSoportada + ".", monto);
    }

    // Lógica de reintegro, la usan las hijas que sí implementan Reintegrable
    protected ResultadoOperacion procesarReintegro(double monto) {
        String error = validarCobro(monto);
        if (error != null) {
            registrarEvento("Reintegro rechazado: " + error);
            return ResultadoOperacion.fallido(error);
        }
        registrarEvento("Reintegro aprobado por " + String.format("%.2f", monto) + " " + monedaSoportada);
        return ResultadoOperacion.exitoso("Reintegro aprobado.", monto);
    }

    public String getTipo() {
        return "Pasarela de pago";
    }

  
    public String getDetallesEspecificos() {
        return "Comisión: " + comisionPorcentaje + "%" + "\nMoneda: " + monedaSoportada + "\nLímite por transacción: " + String.format("%.2f", limiteTransaccion);
    }
}
