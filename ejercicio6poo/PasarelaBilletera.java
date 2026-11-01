
// Pasarela de billeteras digitales. Cobra y reintegra, con un saldo máximo por billetera
public class PasarelaBilletera extends PasarelaPago implements Reintegrable {
    private String nombreBilletera;
    private String telefonoAsociado;
    private double saldoMaximo;

    public PasarelaBilletera(String id, String nombre, String proveedor, int latenciaPromedio, EstadoConexion estado, String urlBase, String version, double comisionPorcentaje, String monedaSoportada, double limiteTransaccion, String nombreBilletera, String telefonoAsociado, double saldoMaximo) {
        super(id, nombre, proveedor, latenciaPromedio, estado, urlBase, version, comisionPorcentaje, monedaSoportada, limiteTransaccion);
        this.nombreBilletera = nombreBilletera;
        this.telefonoAsociado = telefonoAsociado;
        this.saldoMaximo = saldoMaximo;
    }

    public String getNombreBilletera() { 
    	return nombreBilletera; 
    	}
    public String getTelefonoAsociado() { 
    	return telefonoAsociado; 
    	}
    public double getSaldoMaximo() { 
    	return saldoMaximo; 
    	}

    // Además de las reglas base, el monto no puede pasar del saldo máximo de la billetera
    protected String validarCobro(double monto) {
        String error = super.validarCobro(monto);
        if (error != null) {
            return error;
        }
        if (monto > saldoMaximo) {
            return "El monto excede el saldo máximo de la billetera (" + String.format("%.2f", saldoMaximo) + ").";
        }
        return null;
    }

    // Devuelve dinero a la billetera del cliente

    public ResultadoOperacion reintegrar(double monto) {
        return procesarReintegro(monto);
    }


    public String getTipo() { return "Pasarela - Billetera digital"; }

    @Override
    public String getDetallesEspecificos() {
        return super.getDetallesEspecificos() + "\nBilletera: " + nombreBilletera + "\nTeléfono asociado: " + telefonoAsociado + "\nSaldo máximo: " + String.format("%.2f", saldoMaximo);
    }
}
