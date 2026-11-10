package ar.edu.sigitec.model;

public class Equipo {
    private final int id;
    private String codigo;
    private String tipo;
    private Casino casino;

    public Equipo(int id, String codigo, String tipo, Casino casino) {
        this.id = id;
        this.codigo = codigo;
        this.tipo = tipo;
        this.casino = casino;
    }

    public int getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Casino getCasino() {
        return casino;
    }

    public void setCasino(Casino casino) {
        this.casino = casino;
    }

    @Override
    public String toString() {
        return "#" + id + " " + codigo + " - " + tipo + " | " + casino.getNombre();
    }
}
