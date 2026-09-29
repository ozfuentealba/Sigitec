package ar.edu.sigitec.dao;

import ar.edu.sigitec.db.DatabaseConnection;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class CasinoDAO {
    public void insertar(String nombre, String localidad, String contacto, String telefono) throws SQLException {
        String sql = "INSERT INTO casinos(nombre, localidad, contacto, telefono) VALUES(?,?,?,?)";
        try (Connection cn = DatabaseConnection.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, localidad);
            ps.setString(3, contacto);
            ps.setString(4, telefono);
            ps.executeUpdate();
        }
    }

    public DefaultTableModel listar() throws SQLException {
        DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Nombre", "Localidad", "Contacto", "Teléfono"}, 0);
        String sql = "SELECT id_casino, nombre, localidad, contacto, telefono FROM casinos WHERE activo = TRUE ORDER BY id_casino DESC";
        try (Connection cn = DatabaseConnection.getConnection(); PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                model.addRow(new Object[]{rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)});
            }
        }
        return model;
    }
}
