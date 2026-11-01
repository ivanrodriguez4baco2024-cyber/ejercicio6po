
// Clase abstracta de todas las integraciones con APIs externas. Implementa Comparable<IntegracionAPI> para que integraciones de clases distintas se puedan ordenar juntas en la misma lista
public abstract class IntegracionAPI implements Comparable<IntegracionAPI> {
    private String id;
    private String nombre;
    private String proveedor;
    private int latenciaPromedio;
    private EstadoConexion estado;
    private String urlBase;
    private String version;

    public IntegracionAPI(String id, String nombre, String proveedor, int latenciaPromedio, EstadoConexion estado, String urlBase, String version) {
        this.id = id;
        this.nombre = nombre;
        this.proveedor = proveedor;
        this.latenciaPromedio = latenciaPromedio;
        this.estado = estado;
        this.urlBase = urlBase;
        this.version = version;
    }

    public String getId() { 
    	return id; 
    	}
    public String getNombre() { 
    	return nombre; 
    	}
    public String getProveedor() { 
    	return proveedor; 
    	}
    public int getLatenciaPromedio() { 
    	return latenciaPromedio; 
    	}
    public void setLatenciaPromedio(int latenciaPromedio) { 
    	this.latenciaPromedio = latenciaPromedio; 
    	}
    public EstadoConexion getEstado() { 
    	return estado; 
    	}
    public void setEstado(EstadoConexion estado) { 
    	this.estado = estado; 
    	}
    public String getUrlBase() { 
    	return urlBase; 
    	}
    public String getVersion() { 
    	return version; 
    	}

    // Una integración está activa mientras no esté desconectada
    public boolean estaActiva() {
        return estado != EstadoConexion.DESCONECTADO;
    }

    // Arma un texto con los parámetros de configuración (comunes + los propios de cada subclase)
    public String mostrarConfiguracion() {
        return "ID: " + id+ "\nNombre: " + nombre+ "\nProveedor: " + proveedor + "\nTipo: " + getTipo()  + "\nEstado: " + estado+ "\nLatencia promedio: " + latenciaPromedio + " ms"+ "\nURL base: " + urlBase + "\nVersión: " + version + "\n" + getDetallesEspecificos();
    }

    // Orden natural: primero menor latencia; si empatan, por id ascendente
    public int compareTo(IntegracionAPI otra) {
        int porLatencia = Integer.compare(this.latenciaPromedio, otra.latenciaPromedio);
        if (porLatencia != 0) {
            return porLatencia;
        }
        return this.id.compareTo(otra.id);
    }

    // Texto corto para listados
    public String toString() {
        return id + " - " + nombre + " (" + getTipo() + ") " + latenciaPromedio + " ms [" + estado + "]";
    }

    // Nombre legible del tipo de integración, metodo abstracto
    public abstract String getTipo();

    // Datos de configuración propios de cada subclase, metodo abstracto
    public abstract String getDetallesEspecificos();
}
