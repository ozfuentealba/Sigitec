package ar.edu.sigitec.model;

public class Administrador extends Usuario {
    public Administrador(int id, String nombre, String email) {
        super(id, nombre, email);
    }

    @Override
    public String getRol() {
        return "Administrador";
    }
}
