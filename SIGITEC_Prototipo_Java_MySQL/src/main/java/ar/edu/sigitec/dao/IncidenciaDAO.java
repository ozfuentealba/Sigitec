package ar.edu.sigitec.dao;

import ar.edu.sigitec.db.DatabaseConnection;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class IncidenciaDAO {
    public void insertar(int idCasino, Integer idEquipo, int idTipo, int idPrioridad, int idEstado, String titulo, String descripcion, String clienteReporta, Integer idTecnico) throws SQLException {
        String codigo = generarCodigo();
        String sql = "INSERT INTO incidencias(codigo, id_casino, id_equipo, id_tipo_incidencia, id_prioridad, id_estado, titulo, descripcion, cliente_reporta, id_tecnico_asignado) " +
                     "VALUES(?,?,?,?,?,?,?,?,?,?)";
        try (Connection cn = DatabaseConnection.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            ps.setInt(2, idCasino);
            if (idEquipo == null) ps.setNull(3, Types.INTEGER); else ps.setInt(3, idEquipo);
            ps.setInt(4, idTipo);
            ps.setInt(5, idPrioridad);
            ps.setInt(6, idEstado);
            ps.setString(7, titulo);
            ps.setString(8, descripcion);
            ps.setString(9, clienteReporta);
            if (idTecnico == null) ps.setNull(10, Types.INTEGER); else ps.setInt(10, idTecnico);
            ps.executeUpdate();
        }
    }

    public void cambiarEstado(int idIncidencia, int idEstadoNuevo, int idUsuario, String observacion) throws SQLException {
        try (Connection cn = DatabaseConnection.getConnection()) {
            cn.setAutoCommit(false);
            int anterior = 0;
            try (PreparedStatement sel = cn.prepareStatement("SELECT id_estado FROM incidencias WHERE id_incidencia=?")) {
                sel.setInt(1, idIncidencia);
                try (ResultSet rs = sel.executeQuery()) { if (rs.next()) anterior = rs.getInt(1); }
            }
            try (PreparedStatement upd = cn.prepareStatement("UPDATE incidencias SET id_estado=?, fecha_cierre = CASE WHEN ? = 5 THEN NOW() ELSE fecha_cierre END WHERE id_incidencia=?")) {
                upd.setInt(1, idEstadoNuevo);
                upd.setInt(2, idEstadoNuevo);
                upd.setInt(3, idIncidencia);
                upd.executeUpdate();
            }
            try (PreparedStatement hist = cn.prepareStatement("INSERT INTO historial_estado(id_incidencia, id_estado_anterior, id_estado_nuevo, id_usuario, observacion) VALUES(?,?,?,?,?)")) {
                hist.setInt(1, idIncidencia);
                if (anterior == 0) hist.setNull(2, Types.INTEGER); else hist.setInt(2, anterior);
                hist.setInt(3, idEstadoNuevo);
                hist.setInt(4, idUsuario);
                hist.setString(5, observacion);
                hist.executeUpdate();
            }
            cn.commit();
        }
    }

    public void comentar(int idIncidencia, int idUsuario, String comentario) throws SQLException {
        String sql = "INSERT INTO comentarios_incidencia(id_incidencia, id_usuario, comentario) VALUES(?,?,?)";
        try (Connection cn = DatabaseConnection.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idIncidencia);
            ps.setInt(2, idUsuario);
            ps.setString(3, comentario);
            ps.executeUpdate();
        }
    }

    public DefaultTableModel listar() throws SQLException {
        DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Código", "Casino", "Equipo", "Tipo", "Prioridad", "Estado", "Título", "Técnico", "Fecha"}, 0);
        String sql = "SELECT i.id_incidencia, i.codigo, c.nombre, COALESCE(e.identificador,'-'), ti.nombre, p.nombre, es.nombre, i.titulo, COALESCE(u.nombre_completo,'Sin asignar'), i.fecha_alta " +
                     "FROM incidencias i JOIN casinos c ON c.id_casino=i.id_casino " +
                     "LEFT JOIN equipos e ON e.id_equipo=i.id_equipo JOIN tipos_incidencia ti ON ti.id_tipo_incidencia=i.id_tipo_incidencia " +
                     "JOIN prioridades p ON p.id_prioridad=i.id_prioridad JOIN estados_incidencia es ON es.id_estado=i.id_estado " +
                     "LEFT JOIN usuarios u ON u.id_usuario=i.id_tecnico_asignado ORDER BY i.id_incidencia DESC";
        try (Connection cn = DatabaseConnection.getConnection(); PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                model.addRow(new Object[]{rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getTimestamp(10)});
            }
        }
        return model;
    }

    public DefaultTableModel reportePorEstado() throws SQLException {
        DefaultTableModel model = new DefaultTableModel(new Object[]{"Estado", "Cantidad"}, 0);
        String sql = "SELECT es.nombre, COUNT(*) FROM incidencias i JOIN estados_incidencia es ON es.id_estado=i.id_estado GROUP BY es.nombre ORDER BY COUNT(*) DESC";
        try (Connection cn = DatabaseConnection.getConnection(); PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) model.addRow(new Object[]{rs.getString(1), rs.getInt(2)});
        }
        return model;
    }

    private String generarCodigo() throws SQLException {
        String sql = "SELECT COALESCE(MAX(id_incidencia),0) + 1 FROM incidencias";
        try (Connection cn = DatabaseConnection.getConnection(); PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            rs.next();
            return String.format("INC-%04d", rs.getInt(1));
        }
    }
}
