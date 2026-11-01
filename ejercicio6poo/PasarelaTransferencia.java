
// Pasarela de transferencias bancarias. Solo cobra. No implementa Reintegrable, así que nunca se le puede pedir un reintegro.
public class PasarelaTransferencia extends PasarelaPago {
    private String bancoDestino;
    private String numeroCuenta;
    private int tiempoAcreditacionHoras;

    public PasarelaTransferencia(String id, String nombre, String proveedor, int latenciaPromedio, EstadoConexion estado, String urlBase, String version, double comisionPorcentaje, String monedaSoportada, double limiteTransaccion, String bancoDestino, String numeroCuenta, int tiempoAcreditacionHoras) {
        super(id, nombre, proveedor, latenciaPromedio, estado, urlBase, version, comisionPorcentaje, monedaSoportada, limiteTransaccion);
        this.bancoDestino = bancoDestino;
        this.numeroCuenta = numeroCuenta;
        this.tiempoAcreditacionHoras = tiempoAcreditacionHoras;
    }

    public String getBancoDestino() { 
    	return bancoDestino; 
    	}
    public String getNumeroCuenta() { 
    	return numeroCuenta; 
    	}
    public int getTiempoAcreditacionHoras() { 
    	return tiempoAcreditacionHoras; 
    	}

    public String getTipo() { 
    	return "Pasarela - Transferencia"; 
    	}

    public String getDetallesEspecificos() {
        return super.getDetallesEspecificos() + "\nBanco destino: " + bancoDestino + "\nCuenta: " + numeroCuenta + "\nAcreditación: " + tiempoAcreditacionHoras + " horas";
    }
}
