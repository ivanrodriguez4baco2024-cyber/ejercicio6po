

import java.util.ArrayList;
import java.util.UUID;

// Certificador de Factura Electrónica en Línea. Genera documentos, informa su estado y audita
public class CertificadorFEL extends IntegracionAPI implements Consultable, Documentable, Auditable {
    private String nitEmisor;
    private String nombreEmisor;
    private String regimen;
    private double porcentajeIva;
    private String serieActual;
    private int contadorDocumentos;
    private ArrayList<String> documentosEmitidos;
    private ArrayList<EventoAuditoria> historialEventos;

    public CertificadorFEL(String id, String nombre, String proveedor, int latenciaPromedio, EstadoConexion estado, String urlBase, String version, String nitEmisor, String nombreEmisor, String regimen, double porcentajeIva, String serieActual) {
        super(id, nombre, proveedor, latenciaPromedio, estado, urlBase, version);
        this.nitEmisor = nitEmisor;
        this.nombreEmisor = nombreEmisor;
        this.regimen = regimen;
        this.porcentajeIva = porcentajeIva;
        this.serieActual = serieActual;
        this.contadorDocumentos = 0;
        this.documentosEmitidos = new ArrayList<>();
        this.historialEventos = new ArrayList<>();
    }

    public String getNitEmisor() { 
    	return nitEmisor; 
    	}
    public String getNombreEmisor() { 
    	return nombreEmisor; 
    	}
    public String getRegimen() { 
    	return regimen; 
    	}
    public double getPorcentajeIva() { 
    	return porcentajeIva; 
    	}
    public String getSerieActual() { 
    	return serieActual; 
    	}
    public int getContadorDocumentos() { 
    	return contadorDocumentos; 
    }

    // Informa el estado del certificador y cuántos documentos ha emitido

    public String consultarEstado() {
        return getNombre() + " está " + getEstado() + ". Documentos emitidos: " + contadorDocumentos;
    }

    // Emite una factura de prueba a Consumidor Final y devuelve su XML simulado

    public String generarDocumento() {
        if (!estaActiva()) {
            registrarEvento("Intento de emisión con el certificador desconectado");
            return "No se pudo generar el documento: el certificador está desconectado.";
        }
        String uuid = generarUuid();
        double monto = 100.00; // monto de ejemplo, no real
        contadorDocumentos++;
        documentosEmitidos.add(uuid);
        registrarEvento("Factura emitida " + serieActual + "-" + contadorDocumentos + " (" + uuid + ")");
        return construirXml("CF", "Consumidor Final", monto, uuid);
    }

    // Calcula cuánto IVA contiene un monto que ya lo trae incluido
    public double calcularIva(double montoTotal) {
        return montoTotal / (1 + porcentajeIva) * porcentajeIva;
    }

    // Revisa si un UUID ya fue emitido por este certificador
    public boolean documentoExiste(String uuid) {
        return documentosEmitidos.contains(uuid);
    }

    // Guarda un evento al final del historial
    public void registrarEvento(String descripcion) {
        historialEventos.add(new EventoAuditoria(descripcion));
    }

    // Devuelve todos los eventos guardados
    public ArrayList<EventoAuditoria> consultarHistorialEventos() {
        return historialEventos;
    }

    // Crea un identificador único para cada documento
    private String generarUuid() {
        return UUID.randomUUID().toString().toUpperCase();
    }

    // Arma el texto XML simulado de la factura
    private String construirXml(String nitReceptor, String nombreReceptor, double monto, String uuid) {
        return "<DTE>\n"
             + "  <Emisor nit=\"" + nitEmisor + "\" nombre=\"" + nombreEmisor + "\" regimen=\"" + regimen + "\"/>\n"
             + "  <Receptor nit=\"" + nitReceptor + "\" nombre=\"" + nombreReceptor + "\"/>\n"
             + "  <Serie>" + serieActual + "</Serie><Numero>" + contadorDocumentos + "</Numero>\n"
             + "  <Total>" + String.format("%.2f", monto) + "</Total>\n"
             + "  <Iva>" + String.format("%.2f", calcularIva(monto)) + "</Iva>\n"
             + "  <UUID>" + uuid + "</UUID>\n"
             + "</DTE>";
    }

    public String getTipo() { return "Certificador FEL"; }

    public String getDetallesEspecificos() {
        return "NIT emisor: " + nitEmisor + "\nEmisor: " + nombreEmisor + "\nRégimen: " + regimen + "\nIVA: " + (porcentajeIva * 100) + "%" + "\nSerie actual: " + serieActual;
    }
}
