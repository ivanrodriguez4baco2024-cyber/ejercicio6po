
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;


// Vista gráfica (MVC) hecha con Swing. Solo muestra datos y delega todo al controlador.
public class VentanaPrincipal extends JFrame {
    private ControladorIntegraciones controlador;
    private DefaultTableModel modeloTabla;
    private JTable tabla;
    private JTextField txtBuscar;
    private JComboBox<Operacion> cmbOperaciones;
    private JTextArea txtSalida;

    // Construye la ventana con sus tres zonas: botones arriba, tabla al centro, operaciones abajo
    public VentanaPrincipal(ControladorIntegraciones controlador) {
        this.controlador = controlador;
        setTitle("Integraciones API - Checkout");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 960, 640);

        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(8, 8, 8, 8));
        contentPane.setLayout(new BorderLayout(8, 8));
        setContentPane(contentPane);

        // Zona superior: listar, ordenar y buscar
        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 4));
        contentPane.add(panelSuperior, BorderLayout.NORTH);

        JButton btnListar = new JButton("Listar activas");
        btnListar.addActionListener(e -> cargarTabla());
        panelSuperior.add(btnListar);

        JButton btnOrdenar = new JButton("Ordenar por latencia");
        btnOrdenar.addActionListener(e -> ordenar());
        panelSuperior.add(btnOrdenar);

        panelSuperior.add(new JLabel("Buscar ID:"));
        txtBuscar = new JTextField();
        txtBuscar.setColumns(10);
        panelSuperior.add(txtBuscar);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> buscar());
        panelSuperior.add(btnBuscar);

        // Zona central: tabla de integraciones
        modeloTabla = new DefaultTableModel(
        		new Object[] { "ID", "Nombre", "Tipo", "Latencia (ms)", "Estado" }, 0);
        tabla = new JTable(modeloTabla);
        tabla.setDefaultEditor(Object.class, null);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                actualizarOperaciones();
            }
        });
        JScrollPane scrollTabla = new JScrollPane(tabla);
        contentPane.add(scrollTabla, BorderLayout.CENTER);

        //Zona inferior: operaciones y salida de texto
        JPanel panelInferior = new JPanel();
        panelInferior.setLayout(new BorderLayout(4, 4));
        panelInferior.setPreferredSize(new Dimension(0, 240));
        contentPane.add(panelInferior, BorderLayout.SOUTH);

        JPanel panelOperaciones = new JPanel();
        panelOperaciones.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 4));
        panelInferior.add(panelOperaciones, BorderLayout.NORTH);

        panelOperaciones.add(new JLabel("Operación:"));
        cmbOperaciones = new JComboBox<>();
        cmbOperaciones.setPreferredSize(new Dimension(240, 26));
        panelOperaciones.add(cmbOperaciones);

        JButton btnEjecutar = new JButton("Ejecutar");
        btnEjecutar.addActionListener(e -> ejecutar((Operacion) cmbOperaciones.getSelectedItem()));
        panelOperaciones.add(btnEjecutar);

        JButton btnConfig = new JButton("Ver configuración");
        btnConfig.addActionListener(e -> verConfiguracion());
        panelOperaciones.add(btnConfig);

        JButton btnReintegro = new JButton("Solicitar reintegro (checkout)");
        btnReintegro.addActionListener(e -> ejecutar(Operacion.REINTEGRAR));
        panelOperaciones.add(btnReintegro);

        txtSalida = new JTextArea();
        txtSalida.setEditable(false);
        txtSalida.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        JScrollPane scrollSalida = new JScrollPane(txtSalida);
        panelInferior.add(scrollSalida, BorderLayout.CENTER);

        cargarTabla();
    }

    // Vuelve a llenar la tabla con las integraciones activas, en el orden actual del catálogo
    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        for (IntegracionAPI i : controlador.listarActivas()) {
            modeloTabla.addRow(new Object[] { i.getId(), i.getNombre(), i.getTipo(),
                    i.getLatenciaPromedio(), i.getEstado() });
        }
    }

    // Pide al controlador ordenar el catálogo y refresca la tabla
    private void ordenar() {
        controlador.ordenarPorLatencia();
        cargarTabla();
        txtSalida.setText("Catálogo ordenado por latencia (empates por ID).");
    }

    // Busca por id: si existe, la selecciona en la tabla y muestra su configuración
    private void buscar() {
        IntegracionAPI api = controlador.buscarPorId(txtBuscar.getText().trim());
        if (api == null) {
            JOptionPane.showMessageDialog(this, "No existe una integración con ese ID.");
            return;
        }
        for (int fila = 0; fila < modeloTabla.getRowCount(); fila++) {
            if (modeloTabla.getValueAt(fila, 0).equals(api.getId())) {
                tabla.setRowSelectionInterval(fila, fila);
            }
        }
        txtSalida.setText(api.mostrarConfiguracion());
    }

    // Devuelve la integración seleccionada en la tabla, o null si no hay ninguna
    private IntegracionAPI getSeleccionada() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            return null;
        }
        return controlador.buscarPorId((String) modeloTabla.getValueAt(fila, 0));
    }

    // Llena el combo solo con las operaciones que la integración seleccionada ofrece
    private void actualizarOperaciones() {
        cmbOperaciones.removeAllItems();
        IntegracionAPI api = getSeleccionada();
        if (api != null) {
            for (Operacion op : controlador.operacionesDisponibles(api)) {
                cmbOperaciones.addItem(op);
            }
        }
    }

    // Muestra la configuración de la integración seleccionada
    private void verConfiguracion() {
        IntegracionAPI api = getSeleccionada();
        if (api == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una integración de la tabla.");
            return;
        }
        txtSalida.setText(api.mostrarConfiguracion());
    }

    // Pide los datos que la operación necesite, la ejecuta y muestra el resultado
    private void ejecutar(Operacion op) {
        IntegracionAPI api = getSeleccionada();
        if (api == null || op == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una integración y una operación.");
            return;
        }
        double valor = 0;
        String zona = "";
        try {
            if (op.pideMonto()) {
                String t = JOptionPane.showInputDialog(this, "Monto:");
                if (t == null) return;
                valor = Double.parseDouble(t.trim());
            } else if (op.pideEnvio()) {
                String t = JOptionPane.showInputDialog(this, "Peso del paquete (kg):");
                if (t == null) return;
                valor = Double.parseDouble(t.trim());
                zona = JOptionPane.showInputDialog(this, "Zona de destino:");
                if (zona == null) return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un número válido.");
            return;
        }
        ResultadoOperacion r = controlador.ejecutar(api, op, valor, zona.trim());
        String texto = api.getNombre() + " - " + op + "\n\n" + r;
        if (r.isExito() && r.getMonto() > 0) {
            texto += String.format("\nMonto: %.2f", r.getMonto());
        }
        txtSalida.setText(texto);
    }
}
