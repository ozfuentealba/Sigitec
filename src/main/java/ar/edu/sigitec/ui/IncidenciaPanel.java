package ar.edu.sigitec.ui;

import ar.edu.sigitec.dao.CommonDAO;
import ar.edu.sigitec.dao.IncidenciaDAO;
import ar.edu.sigitec.model.ComboItem;
import ar.edu.sigitec.model.UserSession;
import javax.swing.*;
import java.awt.*;

public class IncidenciaPanel extends JPanel {
    private final IncidenciaDAO dao = new IncidenciaDAO();
    private final CommonDAO common = new CommonDAO();
    private final UserSession session;
    private final JComboBox<ComboItem> casino = new JComboBox<>();
    private final JComboBox<ComboItem> equipo = new JComboBox<>();
    private final JComboBox<ComboItem> tipo = new JComboBox<>();
    private final JComboBox<ComboItem> prioridad = new JComboBox<>();
    private final JComboBox<ComboItem> estado = new JComboBox<>();
    private final JComboBox<ComboItem> tecnico = new JComboBox<>();
    private final JTextField titulo = new JTextField();
    private final JTextField reporta = new JTextField();
    private final JTextArea descripcion = new JTextArea(3, 40);
    private final JTextField idEstadoIncidencia = new JTextField();
    private final JTextArea comentario = new JTextArea(3, 40);
    private final JTable table = new JTable();

    public IncidenciaPanel(UserSession session) {
        this.session = session;
        setLayout(new BorderLayout(8, 8));
        JPanel form = new JPanel(new GridLayout(5, 4, 6, 6));
        form.setBorder(BorderFactory.createTitledBorder("Registrar incidencia"));
        form.add(new JLabel("Casino")); form.add(casino); form.add(new JLabel("Equipo")); form.add(equipo);
        form.add(new JLabel("Tipo")); form.add(tipo); form.add(new JLabel("Prioridad")); form.add(prioridad);
        form.add(new JLabel("Estado")); form.add(estado); form.add(new JLabel("Técnico")); form.add(tecnico);
        form.add(new JLabel("Título")); form.add(titulo); form.add(new JLabel("Reporta")); form.add(reporta);
        form.add(new JLabel("Descripción")); form.add(new JScrollPane(descripcion));
        JButton guardar = new JButton("Guardar incidencia");
        guardar.addActionListener(e -> guardar());
        form.add(guardar);
        add(form, BorderLayout.NORTH);

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new GridLayout(2, 1));
        JPanel estadoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        estadoPanel.setBorder(BorderFactory.createTitledBorder("Actualizar estado / agregar comentario"));
        estadoPanel.add(new JLabel("ID incidencia:")); estadoPanel.add(idEstadoIncidencia);
        JComboBox<ComboItem> estadoCambio = estado;
        estadoPanel.add(new JLabel("Nuevo estado:")); estadoPanel.add(estadoCambio);
        JButton cambiar = new JButton("Cambiar estado"); cambiar.addActionListener(e -> cambiarEstado());
        estadoPanel.add(cambiar);
        bottom.add(estadoPanel);
        JPanel comentarioPanel = new JPanel(new BorderLayout());
        comentarioPanel.add(new JScrollPane(comentario), BorderLayout.CENTER);
        JButton comentar = new JButton("Agregar comentario"); comentar.addActionListener(e -> comentar());
        comentarioPanel.add(comentar, BorderLayout.EAST);
        bottom.add(comentarioPanel);
        add(bottom, BorderLayout.SOUTH);
        cargarCombos(); cargar();
    }

    private void cargarCombos() {
        try {
            casino.removeAllItems(); equipo.removeAllItems(); tipo.removeAllItems(); prioridad.removeAllItems(); estado.removeAllItems(); tecnico.removeAllItems();
            for (ComboItem item : common.list("SELECT id_casino, nombre FROM casinos WHERE activo=TRUE ORDER BY nombre")) casino.addItem(item);
            for (ComboItem item : common.list("SELECT id_equipo, identificador FROM equipos WHERE activo=TRUE ORDER BY identificador")) equipo.addItem(item);
            for (ComboItem item : common.list("SELECT id_tipo_incidencia, nombre FROM tipos_incidencia ORDER BY nombre")) tipo.addItem(item);
            for (ComboItem item : common.list("SELECT id_prioridad, nombre FROM prioridades ORDER BY nivel")) prioridad.addItem(item);
            for (ComboItem item : common.list("SELECT id_estado, nombre FROM estados_incidencia ORDER BY id_estado")) estado.addItem(item);
            for (ComboItem item : common.list("SELECT id_usuario, nombre_completo FROM usuarios WHERE activo=TRUE ORDER BY nombre_completo")) tecnico.addItem(item);
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
    }

    private void guardar() {
        try {
            dao.insertar(((ComboItem)casino.getSelectedItem()).getId(), ((ComboItem)equipo.getSelectedItem()).getId(),
                    ((ComboItem)tipo.getSelectedItem()).getId(), ((ComboItem)prioridad.getSelectedItem()).getId(),
                    ((ComboItem)estado.getSelectedItem()).getId(), titulo.getText(), descripcion.getText(), reporta.getText(),
                    ((ComboItem)tecnico.getSelectedItem()).getId());
            titulo.setText(""); descripcion.setText(""); reporta.setText(""); cargar();
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
    }

    private void cambiarEstado() {
        try {
            dao.cambiarEstado(Integer.parseInt(idEstadoIncidencia.getText()), ((ComboItem)estado.getSelectedItem()).getId(), session.getIdUsuario(), "Cambio realizado desde prototipo");
            cargar();
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
    }

    private void comentar() {
        try {
            dao.comentar(Integer.parseInt(idEstadoIncidencia.getText()), session.getIdUsuario(), comentario.getText());
            comentario.setText("");
            JOptionPane.showMessageDialog(this, "Comentario agregado.");
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
    }

    private void cargar() {
        try { table.setModel(dao.listar()); } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
    }
}
