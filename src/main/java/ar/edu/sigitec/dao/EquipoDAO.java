package ar.edu.sigitec.dao;

import ar.edu.sigitec.db.DatabaseConnection;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class EquipoDAO {
    public void insertar(int idCasino, int idTipo, String identificador, String marca, String modelo, String ubicacion, String ip) throws SQLException {
        String sql = "INSERT INTO equipos(id_casino, id_tipo_equipo, identificador, marca, modelo, ubicacion, ip) VALUES(?,?,?,?,?,?,?)";
        try (Connection cn = DatabaseConnection.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idCasino);
            ps.setInt(2, idTipo);
            ps.setString(3, identificador);
            ps.setString(4, marca);
            ps.setString(5, modelo);
            ps.setString(6, ubicacion);
            ps.setString(7, ip);
            ps.executeUpdate();
        }
    }

    public DefaultTableModel listar() throws SQLException {
        DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Casino", "Tipo", "Identificador", "Marca", "Modelo", "Ubicación", "IP"}, 0);
        String sql = "SELECT e.id_equipo, c.nombre, te.nombre, e.identificador, e.marca, e.modelo, e.ubicacion, e.ip " +
                     "FROM equipos e JOIN casinos c ON c.id_casino=e.id_casino JOIN tipos_equipo te ON te.id_tipo_equipo=e.id_tipo_equipo " +
                     "WHERE e.activo = TRUE ORDER BY e.id_equipo DESC";
        try (Connection cn = DatabaseConnection.getConnection(); PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                model.addRow(new Object[]{rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8)});
            }
        }
        return model;
    }
}
