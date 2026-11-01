
/** Operaciones que el sistema puede pedirle a una integración. */
public enum Operacion {
    CONSULTAR_ESTADO("Consultar estado"),
    COTIZAR("Cotizar envío"),
    COBRAR("Cobrar"),
    REINTEGRAR("Reintegrar"),
    GENERAR_DOCUMENTO("Generar documento"),
    HISTORIAL("Consultar historial de auditoría");

    private final String etiqueta;

    Operacion(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    // Dice si la vista debe pedirle un monto al usuario
    public boolean pideMonto() {
        return this == COBRAR || this == REINTEGRAR;
    }

    // Dice si la vista debe pedirle peso y zona al usuario
    public boolean pideEnvio() {
        return this == COTIZAR;
    }

    public String toString() {
        return etiqueta;
    }
}
