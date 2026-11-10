package ar.edu.sigitec.model;

public class Supervisor extends Usuario {
    private String sectorResponsable;

    public Supervisor(int id, String nombre, String email, String sectorResponsable) {
        super(id, nombre, email);
        this.sectorResponsable = sectorResponsable;
    }

    public String getSectorResponsable() {
        return sectorResponsable;
    }

    public void setSectorResponsable(String sectorResponsable) {
        this.sectorResponsable = sectorResponsable;
    }

    @Override
    public String getRol() {
        return "Supervisor";
    }

    @Override
    public String getDescripcion() {
        return super.getDescripcion() + " | Sector: " + sectorResponsable;
    }
}
