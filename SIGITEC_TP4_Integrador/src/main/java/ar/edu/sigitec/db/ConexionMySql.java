package ar.edu.sigitec.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Credenciales fuera del repositorio: SIGITEC_DB_URL, SIGITEC_DB_USER y SIGITEC_DB_PASSWORD. */
public final class ConexionMySql {
    private ConexionMySql() {}

    private static String entorno(String clave, String defecto) {
        String valor = System.getenv(clave);
        return valor == null || valor.isBlank() ? defecto : valor;
    }

    public static Connection abrir() throws SQLException {
        String url = entorno("SIGITEC_DB_URL", "jdbc:mysql://localhost:3306/sigitec_db?serverTimezone=America/Argentina/Buenos_Aires");
        String user = entorno("SIGITEC_DB_USER", "root");
        String pass = System.getenv("SIGITEC_DB_PASSWORD");
        return DriverManager.getConnection(url, user, pass == null ? "" : pass);
    }
}
