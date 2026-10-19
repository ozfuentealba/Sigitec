package ar.edu.sigitec.model;

public enum Prioridad {
    BAJA(1), MEDIA(2), ALTA(3), CRITICA(4);

    private final int orden;

    Prioridad(int orden) {
        this.orden = orden;
    }

    public int getOrden() {
        return orden;
    }
}
