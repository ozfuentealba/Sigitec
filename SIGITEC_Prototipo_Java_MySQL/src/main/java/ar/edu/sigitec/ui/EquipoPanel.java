package ar.edu.sigitec.ui;

import ar.edu.sigitec.dao.CommonDAO;
import ar.edu.sigitec.dao.EquipoDAO;
import ar.edu.sigitec.model.ComboItem;
import javax.swing.*;
import java.awt.*;

public class EquipoPanel extends JPanel {
    private final EquipoDAO dao = new EquipoDAO();
    private final CommonDAO common = new CommonDAO();
    private final JComboBox<ComboItem> casino = new JComboBox<>();
    private final JComboBox<ComboItem> tipo = new JComboBox<>();
    private final JTextField identificador = new JTextField();
    private final JTextField marca = new JTextField();
    private final JTextField modelo = new JTextField();
    private final JTextField ubicacion = new JTextField();
    private final JTextField ip = new JTextField();
    private final JTable table = new JTable();

    public EquipoPanel() {
        setLayout(new BorderLayout(8, 8));
        JPanel form = new JPanel(new GridLayout(4, 4, 6, 6));
        form.setBorder(BorderFactory.createTitledBorder("Registrar máquina / equipo"));
        form.add(new JLabel("Casino")); form.add(casino); form.add(new JLabel("Tipo")); form.add(tipo);
        form.add(new JLabel("Identificador")); form.add(identificador); form.add(new JLabel("Marca")); form.add(marca);
        form.add(new JLabel("Modelo")); form.add(modelo); form.add(new JLabel("Ubicación")); form.add(ubicacion);
        form.add(new JLabel("IP")); form.add(ip);
        add(form, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        JButton guardar = new JButton("Guardar equipo");
        guardar.addActionListener(e -> guardar());
        add(guardar, BorderLayout.SOUTH);
        cargarCombos(); cargar();
    }

    private void cargarCombos() {
        try {
            casino.removeAllItems(); tipo.removeAllItems();
            for (ComboItem item : common.list("SELECT id_casino, nombre FROM casinos WHERE activo=TRUE ORDER BY nombre")) casino.addItem(item);
            for (ComboItem item : common.list("SELECT id_tipo_equipo, nombre FROM tipos_equipo ORDER BY nombre")) tipo.addItem(item);
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
    }

    private void guardar() {
        try {
            ComboItem c = (ComboItem) casino.getSelectedItem(); ComboItem t = (ComboItem) tipo.getSelectedItem();
            dao.insertar(c.getId(), t.getId(), identificador.getText(), marca.getText(), modelo.getText(), ubicacion.getText(), ip.getText());
            identificador.setText(""); marca.setText(""); modelo.setText(""); ubicacion.setText(""); ip.setText("");
            cargar();
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
    }
    private void cargar() {
        try { table.setModel(dao.listar()); } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
    }
}
