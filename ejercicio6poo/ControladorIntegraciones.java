// Importa ArrayList
import java.util.ArrayList;

// El controlador recibe las peticiones de la vista y las delega al catálogo o a la integración correspondiente
public class ControladorIntegraciones {

    // Guarda el catálogo que el controlador va a consultar y modificar.
    private CatalogoIntegraciones catalogo;

    // Constructor: recibe el catálogo y lo guarda en el atributo de esta clase.
    public ControladorIntegraciones(CatalogoIntegraciones catalogo) {
        this.catalogo = catalogo;
    }

    // Le pide al catálogo la lista de integraciones activas devuelve esa lista a quien llamó al método.
    public ArrayList<IntegracionAPI> listarActivas() {
        return catalogo.listarActivas();
    }

    // Busca una integración usando su identificador y devuelve el resultado
    public IntegracionAPI buscarPorId(String id) {
        return catalogo.buscarPorId(id);
    }

    // Le pide al catálogo que ordene sus integraciones por latencia.
    public void ordenarPorLatencia() {
        catalogo.ordenar();
    }

    // Revisa qué capacidades tiene la integración y devuelve una lista con las operaciones que puede realizar
    public ArrayList<Operacion> operacionesDisponibles(IntegracionAPI api) {
    	
        ArrayList<Operacion> ops = new ArrayList<>();

        // instanceof comprueba si api implementa esa interfaz. Si la implementa, se agrega la operación correspondiente.
        
        if (api instanceof Consultable) ops.add(Operacion.CONSULTAR_ESTADO);
        if (api instanceof Cotizable) ops.add(Operacion.COTIZAR);
        if (api instanceof Cobrable) ops.add(Operacion.COBRAR);
        if (api instanceof Reintegrable) ops.add(Operacion.REINTEGRAR);
        if (api instanceof Documentable) ops.add(Operacion.GENERAR_DOCUMENTO);
        if (api instanceof Auditable) ops.add(Operacion.HISTORIAL);

        // Devuelve la lista de operaciones que se encontraron
        return ops;
    }

    // Ejecuta la operación solicitada sobre la integración. Si la integración no puede realizarla devuelve un mensaje indicando que no está disponible. 'valor' puede representar un monto o un peso, según la operación. 'zona' se utiliza para cotizar.
 
    public ResultadoOperacion ejecutar(
            IntegracionAPI api, Operacion op, double valor, String zona) {

        // Según el valor de 'op', intenta ejecutar una operación diferente
        switch (op) {
            case CONSULTAR_ESTADO:
                // Comprueba que api pueda consultar su estado. La variable 'c' representa esa misma integración como Consultable.
                if (api instanceof Consultable c) {
                    return ResultadoOperacion.exitoso(c.consultarEstado());
                }
                break;

            case COTIZAR:
                // Si puede cotizar, usa el valor como peso y pasa también la zona.
                if (api instanceof Cotizable c) {
                    return c.cotizar(valor, zona);
                }
                break;

            case COBRAR:
                // Si puede cobrar, usa valor como el monto a cobrar.
                if (api instanceof Cobrable c) {
                    return c.cobrar(valor);
                }
                break;

            case REINTEGRAR:
                // Si puede reintegrar, usa valor como el monto del reintegro.
                if (api instanceof Reintegrable r) {
                    return r.reintegrar(valor);
                }
                break;

            case GENERAR_DOCUMENTO:
                // Si puede generar documentos, genera uno y devuelve el resultado.
                if (api instanceof Documentable d) {
                    return ResultadoOperacion.exitoso(d.generarDocumento());
                }
                break;

            case HISTORIAL:
                // Si permite consultar auditoría, obtiene y formatea su historial.
                if (api instanceof Auditable a) {
                    return ResultadoOperacion.exitoso(formatearHistorial(a));
                }
                break;
        }

        // Se llega aquí si la operación no se pudo realizar.
        return noDisponible(api, op);
    }

    // Crea un resultado fallido con un mensaje estándar.
    private ResultadoOperacion noDisponible(IntegracionAPI api, Operacion op) {
        return ResultadoOperacion.fallido(
                "La operación '" + op + "' no está disponible para " + api.getNombre() + ". El proceso de compra continúa.");
    }

    // Obtiene los eventos de auditoría y los convierte en un solo texto.
    private String formatearHistorial(Auditable a) {
        ArrayList<EventoAuditoria> eventos = a.consultarHistorialEventos();

        // Si no hay eventos, devuelve un mensaje específico.
        if (eventos.isEmpty()) {
            return "Sin eventos registrados.";
        }

        // StringBuilder permite juntar texto de forma cómoda.
        StringBuilder sb = new StringBuilder();

        // Recorre los eventos y agrega cada uno en una línea.
        for (EventoAuditoria e : eventos) {
            sb.append(e).append("\n");
        }

        // Convierte el contenido acumulado en un String y lo devuelve.
        return sb.toString();
    }
}