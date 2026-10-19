package ar.edu.sigitec.db;

import ar.edu.sigitec.model.*;
import ar.edu.sigitec.repository.Repositorio;
import java.sql.*;
import java.util.*;

/** Recupera los catálogos de la AP2. No inventa equipos ni usuarios en modo MySQL. */
public final class CatalogoMySql {
    private CatalogoMySql() {}

    public static void cargar(Repositorio<Usuario> usuarios, Repositorio<Casino> casinos,
                              Repositorio<Equipo> equipos) throws SQLException {
        Map<Integer, Casino> porId = new HashMap<>();
        try (Connection cx = ConexionMySql.abrir()) {
            try (PreparedStatement ps = cx.prepareStatement(
                    "SELECT u.id_usuario, u.nombre, u.email, r.nombre AS rol " +
                    "FROM usuarios u JOIN roles r ON r.id_rol=u.id_rol ORDER BY u.id_usuario");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id_usuario");
                    String nombre = rs.getString("nombre");
                    String email = rs.getString("email");
                    String rol = rs.getString("rol").toUpperCase(Locale.ROOT);
                    Usuario u = switch (rol) {
                        case "TECNICO" -> new Tecnico(id, nombre, email, "Soporte técnico");
                        case "SUPERVISOR" -> new Supervisor(id, nombre, email, "Operaciones");
                        default -> new Administrador(id, nombre, email);
                    };
                    usuarios.agregar(u);
                }
            }
            try (PreparedStatement ps = cx.prepareStatement(
                    "SELECT id_casino, nombre, ciudad FROM casinos WHERE activo=TRUE ORDER BY id_casino");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Casino c = new Casino(rs.getInt("id_casino"), rs.getString("nombre"), rs.getString("ciudad"));
                    casinos.agregar(c);
                    porId.put(c.getId(), c);
                }
            }
            try (PreparedStatement ps = cx.prepareStatement(
                    "SELECT id_equipo, id_casino, codigo_interno, tipo_equipo FROM equipos WHERE activo=TRUE ORDER BY id_equipo");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Casino c = porId.get(rs.getInt("id_casino"));
                    if (c != null) equipos.agregar(new Equipo(rs.getInt("id_equipo"),
                            rs.getString("codigo_interno"), rs.getString("tipo_equipo"), c));
                }
            }
        }
    }
}
