package ar.edu.sigitec.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** Lectura del historial de cambios de estado, con consulta parametrizada. */
public final class ConsultaHistorialMySql {
    private static final String SQL = """
            SELECT h.fecha_cambio, anterior.nombre AS estado_anterior,
                   nuevo.nombre AS estado_nuevo, h.observacion
              FROM historial_estado h
              LEFT JOIN estados_incidencia anterior ON anterior.id_estado=h.id_estado_anterior
              JOIN estados_incidencia nuevo ON nuevo.id_estado=h.id_estado_nuevo
             WHERE h.id_incidencia=?
             ORDER BY h.fecha_cambio, h.id_historial
            """;

    public List<String> listar(int idIncidencia) {
        List<String> eventos = new ArrayList<>();
        try (Connection cx = ConexionMySql.abrir();
             PreparedStatement ps = cx.prepareStatement(SQL)) {
            ps.setInt(1, idIncidencia);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String previo = rs.getString("estado_anterior");
                    eventos.add(rs.getTimestamp("fecha_cambio") + " | " +
                            (previo == null ? "(inicial)" : previo) + " -> " +
                            rs.getString("estado_nuevo") + " | " + rs.getString("observacion"));
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo consultar el historial en MySQL", e);
        }
        return eventos;
    }
}
