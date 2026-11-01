
// Contrato para integraciones que pueden devolver dinero. Está separado de Cobrable porque no todas las pasarelas reintegran. (ej. las transferencias bancarias no).

public interface Reintegrable {
    // Intenta devolver el monto indicado al cliente
    ResultadoOperacion reintegrar(double monto);
}
