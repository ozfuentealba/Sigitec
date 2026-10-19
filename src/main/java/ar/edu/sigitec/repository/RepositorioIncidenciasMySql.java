package ar.edu.sigitec.repository;

import ar.edu.sigitec.db.ConexionMySql;
import ar.edu.sigitec.model.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

/** Implementación JDBC para el esquema de AP2 (sin recrear tablas). */
public final class RepositorioIncidenciasMySql implements Repositorio<Incidencia> {
    private static final String LISTAR = """
        SELECT i.id_incidencia, i.titulo, i.descripcion, i.prioridad, i.fecha_alta,
               est.nombre AS estado, e.id_equipo, e.codigo_interno, e.tipo_equipo,
               c.id_casino, c.nombre AS casino, c.ciudad,
               u.id_usuario, u.nombre AS responsable, u.email, rol.nombre AS rol
          FROM incidencias i
          JOIN estados_incidencia est ON est.id_estado=i.id_estado
          JOIN equipos e ON e.id_equipo=i.id_equipo
          JOIN casinos c ON c.id_casino=e.id_casino
          JOIN usuarios u ON u.id_usuario=i.id_usuario_asignado
          JOIN roles rol ON rol.id_rol=u.id_rol
         ORDER BY i.id_incidencia
        """;

    public RepositorioIncidenciasMySql() {
        try (Connection cx = ConexionMySql.abrir()) {
            if (!cx.isValid(3)) throw new SQLException("Conexión no válida");
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo conectar a MySQL. Verifique servicio, esquema y variables SIGITEC_DB_*.", e);
        }
    }

    @Override public List<Incidencia> listar() {
        List<Incidencia> lista = new ArrayList<>();
        try (Connection cx=ConexionMySql.abrir();
             PreparedStatement ps=cx.prepareStatement(LISTAR);
             ResultSet rs=ps.executeQuery()) {
            while (rs.next()) {
                Casino casino = new Casino(rs.getInt("id_casino"), rs.getString("casino"), rs.getString("ciudad"));
                Equipo equipo = new Equipo(rs.getInt("id_equipo"), rs.getString("codigo_interno"), rs.getString("tipo_equipo"), casino);
                int idUser = rs.getInt("id_usuario");
                String nombre = rs.getString("responsable");
                String email = rs.getString("email");
                String rol = rs.getString("rol").toUpperCase(Locale.ROOT);
                Usuario user = switch (rol) {
                    case "TECNICO" -> new Tecnico(idUser,nombre,email,"Soporte técnico");
                    case "SUPERVISOR" -> new Supervisor(idUser,nombre,email,"Operaciones");
                    default -> new Administrador(idUser,nombre,email);
                };
                String estado = rs.getString("estado");
                // PUD AP2: los cinco estados incluyen EN_PROCESO (se resume como EN_ANALISIS en el prototipo).
                if ("EN_PROCESO".equals(estado)) estado = "EN_ANALISIS";
                if ("ABIERTA".equals(estado)) estado = "PENDIENTE";
                Timestamp fecha = rs.getTimestamp("fecha_alta");
                lista.add(new Incidencia(rs.getInt("id_incidencia"),rs.getString("titulo"),
                        rs.getString("descripcion"),Prioridad.valueOf(rs.getString("prioridad")),
                        equipo,user,EstadoIncidencia.valueOf(estado),
                        fecha==null ? LocalDateTime.now() : fecha.toLocalDateTime()));
            }
            return lista;
        } catch (SQLException e) {
            throw new IllegalStateException("Error leyendo incidencias de MySQL",e);
        }
    }

    @Override public Optional<Incidencia> buscarPorId(int id) {
        return listar().stream().filter(i -> i.getId()==id).findFirst();
    }

    @Override public void agregar(Incidencia i) {
        String q = """
            INSERT INTO incidencias
            (id_incidencia,id_casino,id_equipo,id_tipo,id_estado,
             id_usuario_reporta,id_usuario_asignado,titulo,descripcion,prioridad,fecha_alta)
            VALUES (?,?,?,(SELECT MIN(id_tipo) FROM tipos_incidencia),
                    (SELECT id_estado FROM estados_incidencia WHERE nombre=? LIMIT 1),?,?,?,?,?,?)
            """;
        try (Connection cx=ConexionMySql.abrir(); PreparedStatement ps=cx.prepareStatement(q)) {
            ps.setInt(1,i.getId());
            ps.setInt(2,i.getEquipo().getCasino().getId());
            ps.setInt(3,i.getEquipo().getId());
            ps.setString(4,i.getEstado().name());
            ps.setInt(5,i.getResponsable().getId());
            ps.setInt(6,i.getResponsable().getId());
            ps.setString(7,i.getTitulo());
            ps.setString(8,i.getDescripcion());
            ps.setString(9,i.getPrioridad().name());
            ps.setTimestamp(10,Timestamp.valueOf(i.getFechaCreacion()));
            if (ps.executeUpdate()!=1) throw new SQLException("No se insertó ninguna fila");
        } catch (SQLException e) {
            throw new IllegalStateException("Error guardando incidencia en MySQL (revise catálogos y claves foráneas)",e);
        }
    }

    @Override public void actualizar(Incidencia i) {
        try (Connection cx = ConexionMySql.abrir()) {
            cx.setAutoCommit(false);
            try {
                int anterior;
                try (PreparedStatement ps = cx.prepareStatement(
                        "SELECT id_estado FROM incidencias WHERE id_incidencia=? FOR UPDATE")) {
                    ps.setInt(1,i.getId());
                    try (ResultSet rs=ps.executeQuery()) {
                        if (!rs.next()) throw new SQLException("Incidencia inexistente " + i.getId());
                        anterior=rs.getInt(1);
                    }
                }
                int nuevo;
                try (PreparedStatement ps=cx.prepareStatement(
                        "SELECT id_estado FROM estados_incidencia WHERE nombre=?")) {
                    ps.setString(1,i.getEstado().name());
                    try (ResultSet rs=ps.executeQuery()) {
                        if (!rs.next()) throw new SQLException("Falta estado " + i.getEstado().name());
                        nuevo=rs.getInt(1);
                    }
                }
                try (PreparedStatement ps=cx.prepareStatement(
                        "UPDATE incidencias SET id_estado=?,fecha_cierre=? WHERE id_incidencia=?")) {
                    ps.setInt(1,nuevo);
                    if (i.getEstado()==EstadoIncidencia.RESUELTA || i.getEstado()==EstadoIncidencia.CERRADA)
                        ps.setTimestamp(2,Timestamp.valueOf(LocalDateTime.now()));
                    else ps.setNull(2,Types.TIMESTAMP);
                    ps.setInt(3,i.getId());
                    ps.executeUpdate();
                }
                if (nuevo!=anterior) {
                    try (PreparedStatement ps=cx.prepareStatement(
                            "INSERT INTO historial_estado (id_incidencia,id_estado_anterior,id_estado_nuevo,id_usuario,observacion) VALUES (?,?,?,?,?)")) {
                        ps.setInt(1,i.getId()); ps.setInt(2,anterior); ps.setInt(3,nuevo);
                        ps.setInt(4,i.getResponsable().getId()); ps.setString(5,"Cambio desde consola TP3");
                        ps.executeUpdate();
                    }
                }
                cx.commit();
            } catch (SQLException e) {
                cx.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error al actualizar estado de incidencia",e);
        }
    }
}
