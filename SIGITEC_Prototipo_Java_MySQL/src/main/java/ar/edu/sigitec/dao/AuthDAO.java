package ar.edu.sigitec.dao;

import ar.edu.sigitec.db.DatabaseConnection;
import ar.edu.sigitec.model.UserSession;
import java.sql.*;

public class AuthDAO {
    public UserSession login(String username, String password) throws SQLException {
        String sql = "SELECT u.id_usuario, u.username, u.nombre_completo, r.nombre AS rol " +
                     "FROM usuarios u JOIN roles r ON r.id_rol = u.id_rol " +
                     "WHERE u.username = ? AND u.password = ? AND u.activo = TRUE";
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new UserSession(
                            rs.getInt("id_usuario"),
                            rs.getString("username"),
                            rs.getString("nombre_completo"),
                            rs.getString("rol")
                    );
                }
            }
        }
        return null;
    }
}
