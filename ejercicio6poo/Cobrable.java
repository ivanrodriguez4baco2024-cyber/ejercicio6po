
// Contrato para integraciones que pueden cobrar dinero
public interface Cobrable {
    // Intenta cobrar el monto indicado. */
    ResultadoOperacion cobrar(double monto);
}
