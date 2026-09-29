package ar.edu.sigitec.ui;

import ar.edu.sigitec.dao.CasinoDAO;
import javax.swing.*;
import java.awt.*;

public class CasinoPanel extends JPanel {
    private final CasinoDAO dao = new CasinoDAO();
    private final JTextField nombre = new JTextField();
    private final JTextField localidad = new JTextField();
    private final JTextField contacto = new JTextField();
    private final JTextField telefono = new JTextField();
    private final JTable table = new JTable();

    public CasinoPanel() {
        setLayout(new BorderLayout(8, 8));
        JPanel form = new JPanel(new GridLayout(2, 4, 6, 6));
        form.setBorder(BorderFactory.createTitledBorder("Registrar casino / sala"));
        form.add(new JLabel("Nombre")); form.add(new JLabel("Localidad")); form.add(new JLabel("Contacto")); form.add(new JLabel("Teléfono"));
        form.add(nombre); form.add(localidad); form.add(contacto); form.add(telefono);
        add(form, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        JButton guardar = new JButton("Guardar casino");
        guardar.addActionListener(e -> guardar());
        add(guardar, BorderLayout.SOUTH);
        cargar();
    }

    private void guardar() {
        try {
            dao.insertar(nombre.getText(), localidad.getText(), contacto.getText(), telefono.getText());
            nombre.setText(""); localidad.setText(""); contacto.setText(""); telefono.setText("");
            cargar();
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
    }
    private void cargar() {
        try { table.setModel(dao.listar()); } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
    }
}
