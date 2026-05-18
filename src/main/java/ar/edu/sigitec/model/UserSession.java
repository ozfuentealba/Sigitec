package ar.edu.sigitec.model;

public class UserSession {
    private final int idUsuario;
    private final String username;
    private final String nombreCompleto;
    private final String rol;

    public UserSession(int idUsuario, String username, String nombreCompleto, String rol) {
        this.idUsuario = idUsuario;
        this.username = username;
        this.nombreCompleto = nombreCompleto;
        this.rol = rol;
    }

    public int getIdUsuario() { return idUsuario; }
    public String getUsername() { return username; }
    public String getNombreCompleto() { return nombreCompleto; }
    public String getRol() { return rol; }
}
