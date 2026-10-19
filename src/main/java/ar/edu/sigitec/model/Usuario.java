package ar.edu.sigitec.model;

/**
 * Clase abstracta: representa el concepto general de usuario del sistema.
 * Se utiliza para aplicar abstracción, herencia y polimorfismo.
 */
public abstract class Usuario {
    private final int id;
    private String nombre;
    private String email;

    protected Usuario(int id, String nombre, String email) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("El email debe ser válido.");
        }
        this.email = email;
    }

    public abstract String getRol();

    public String getDescripcion() {
        return getRol() + " - " + nombre + " <" + email + ">";
    }

    @Override
    public String toString() {
        return "#" + id + " " + getDescripcion();
    }
}
