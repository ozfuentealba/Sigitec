package ar.edu.sigitec.model;

import java.time.LocalDateTime;

public class Incidencia {
    private final int id;
    private String titulo;
    private String descripcion;
    private Prioridad prioridad;
    private EstadoIncidencia estado;
    private Equipo equipo;
    private Usuario responsable;
    private LocalDateTime fechaCreacion;

    public Incidencia(int id, String titulo, String descripcion, Prioridad prioridad, Equipo equipo, Usuario responsable) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.prioridad = prioridad;
        this.equipo = equipo;
        this.responsable = responsable;
        this.estado = EstadoIncidencia.PENDIENTE;
        this.fechaCreacion = LocalDateTime.now();
    }

    /** Reconstruye una incidencia recuperada de la base de datos. */
    public Incidencia(int id, String titulo, String descripcion, Prioridad prioridad,
                      Equipo equipo, Usuario responsable, EstadoIncidencia estado,
                      LocalDateTime fechaCreacion) {
        this(id, titulo, descripcion, prioridad, equipo, responsable);
        this.estado = java.util.Objects.requireNonNull(estado, "Estado obligatorio");
        this.fechaCreacion = java.util.Objects.requireNonNull(fechaCreacion, "Fecha obligatoria");
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Prioridad getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Prioridad prioridad) {
        this.prioridad = prioridad;
    }

    public EstadoIncidencia getEstado() {
        return estado;
    }

    public void setEstado(EstadoIncidencia estado) {
        this.estado = estado;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public Usuario getResponsable() {
        return responsable;
    }

    public void setResponsable(Usuario responsable) {
        this.responsable = responsable;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    @Override
    public String toString() {
        return "#" + id + " [" + prioridad + "] " + titulo +
                " | Estado: " + estado +
                " | Equipo: " + equipo.getCodigo() +
                " | Responsable: " + responsable.getNombre();
    }
}
