
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Un registro del historial de auditoría: cuándo pasó algo y qué fue
public class EventoAuditoria {
    private LocalDateTime fecha;
    private String descripcion;

    // La fecha se toma automáticamente al crear el evento
    public EventoAuditoria(String descripcion) {
        this.fecha = LocalDateTime.now();
        this.descripcion = descripcion;
    }

    public LocalDateTime getFecha() { 
    	return fecha; 
    	}
    public String getDescripcion() { 
    	return descripcion; 
    	}

    // Devuelve el evento como una línea de texto con la fecha adelante
    public String toString() {
        return "[" + fecha.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "] " + descripcion;
    }
}
