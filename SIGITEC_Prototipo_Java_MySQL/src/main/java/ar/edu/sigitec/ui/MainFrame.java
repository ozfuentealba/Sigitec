package ar.edu.sigitec.ui;

import ar.edu.sigitec.model.UserSession;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame(UserSession session) {
        setTitle("SIGITEC - Gestión de Incidencias Técnicas");
        setSize(1050, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel header = new JLabel("Usuario: " + session.getNombreCompleto() + " | Rol: " + session.getRol());
        header.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        add(header, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Casinos", new CasinoPanel());
        tabs.addTab("Equipos", new EquipoPanel());
        tabs.addTab("Incidencias", new IncidenciaPanel(session));
        tabs.addTab("Reportes", new ReportePanel());
        add(tabs, BorderLayout.CENTER);
    }
}
