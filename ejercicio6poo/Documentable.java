
// Contrato para integraciones que generan documentos (facturas, guías)
public interface Documentable {
    // Genera un documento y lo devuelve como texto
    String generarDocumento();
}
