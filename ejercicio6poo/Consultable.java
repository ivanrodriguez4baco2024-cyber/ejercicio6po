// Contrato para integraciones que pueden informar su estado actual.
public interface Consultable {
    // Devuelve un texto con el estado actual de la integración
    String consultarEstado();
}
