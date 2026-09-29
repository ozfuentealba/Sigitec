package ar.edu.sigitec.ui;

import ar.edu.sigitec.dao.IncidenciaDAO;
import javax.swing.*;
import java.awt.*;

public class ReportePanel extends JPanel {
    private final IncidenciaDAO dao = new IncidenciaDAO();
    private final JTable table = new JTable();

    public ReportePanel() {
        setLayout(new BorderLayout(8, 8));
        JButton cargar = new JButton("Generar reporte por estado");
        cargar.addActionListener(e -> cargarReporte());
        add(cargar, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        cargarReporte();
    }

    private void cargarReporte() {
        try { table.setModel(dao.reportePorEstado()); } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
    }
}
