
import java.util.ArrayList;

// Contrato para integraciones que deben dejar rastro de lo que hacen
public interface Auditable {
    // Guarda un evento en el historial
    void registrarEvento(String descripcion);

    // Devuelve todos los eventos guardados
    ArrayList<EventoAuditoria> consultarHistorialEventos();
}
