
// Resultado de ejecutar una operación sobre una integración. Sirve para devolver si salió bien, un mensaje y (si aplica) un monto
public class ResultadoOperacion {
    private boolean exito;
    private String mensaje;
    private double monto;

    public ResultadoOperacion(boolean exito, String mensaje, double monto) {
        this.exito = exito;
        this.mensaje = mensaje;
        this.monto = monto;
    }

    // Crea un resultado exitoso que lleva un monto (cobro, cotización, etc.)
    public static ResultadoOperacion exitoso(String mensaje, double monto) {
        return new ResultadoOperacion(true, mensaje, monto);
    }

    // Crea un resultado exitoso que solo lleva texto
    public static ResultadoOperacion exitoso(String mensaje) {
        return new ResultadoOperacion(true, mensaje, 0);
    }

    // Crea un resultado fallido con el motivo del fallo
    public static ResultadoOperacion fallido(String mensaje) {
        return new ResultadoOperacion(false, mensaje, 0);
    }

    public boolean isExito() { return exito; }
    public String getMensaje() { return mensaje; }
    public double getMonto() { return monto; }

    // Texto listo para mostrar en cualquier vista
    public String toString() {
    	if (exito) {
    	    return "[OK] " + mensaje;
    	} else {
    	    return "[NO] " + mensaje;
    	}
    }
}
