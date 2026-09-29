package ar.edu.sigitec.ui;

import ar.edu.sigitec.dao.AuthDAO;
import ar.edu.sigitec.model.UserSession;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private final JTextField txtUser = new JTextField("admin", 15);
    private final JPasswordField txtPass = new JPasswordField("admin123", 15);

    public LoginFrame() {
        setTitle("SIGITEC - Inicio de sesión");
        setSize(380, 220);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("SIGITEC", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(2, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        form.add(new JLabel("Usuario:"));
        form.add(txtUser);
        form.add(new JLabel("Clave:"));
        form.add(txtPass);
        add(form, BorderLayout.CENTER);

        JButton btnLogin = new JButton("Ingresar");
        btnLogin.addActionListener(e -> login());
        add(btnLogin, BorderLayout.SOUTH);
    }

    private void login() {
        try {
            UserSession session = new AuthDAO().login(txtUser.getText().trim(), new String(txtPass.getPassword()));
            if (session == null) {
                JOptionPane.showMessageDialog(this, "Usuario o clave incorrectos.");
                return;
            }
            new MainFrame(session).setVisible(true);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error de conexión: " + ex.getMessage());
        }
    }
}
