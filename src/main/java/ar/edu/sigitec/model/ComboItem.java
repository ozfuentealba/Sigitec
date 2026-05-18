package ar.edu.sigitec.model;

public class ComboItem {
    private final int id;
    private final String nombre;

    public ComboItem(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }

    @Override
    public String toString() { return nombre; }
}
