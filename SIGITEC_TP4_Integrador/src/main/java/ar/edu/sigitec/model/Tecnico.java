package ar.edu.sigitec.model;

public class Tecnico extends Usuario {
    private String especialidad;

    public Tecnico(int id, String nombre, String email, String especialidad) {
        super(id, nombre, email);
        this.especialidad = especialidad;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    @Override
    public String getRol() {
        return "Técnico";
    }

    @Override
    public String getDescripcion() {
        return super.getDescripcion() + " | Especialidad: " + especialidad;
    }
}
