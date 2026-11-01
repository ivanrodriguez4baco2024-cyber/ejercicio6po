
// Contrato para integraciones que calculan cotizaciones (ej. envíos)
public interface Cotizable {
    // Calcula cuánto costaría enviar un paquete de cierto peso a una zona.
    ResultadoOperacion cotizar(double pesoKg, String zona);
}
