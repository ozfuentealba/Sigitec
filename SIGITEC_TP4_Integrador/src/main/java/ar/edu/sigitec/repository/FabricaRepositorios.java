package ar.edu.sigitec.repository;

import ar.edu.sigitec.model.Incidencia;

/** Fábrica sencilla que encapsula la elección del origen de datos. */
public final class FabricaRepositorios {
    private FabricaRepositorios() { }

    public static Repositorio<Incidencia> crearIncidencias(boolean usarMySql) {
        if (usarMySql) return new RepositorioIncidenciasMySql();
        return new RepositorioMemoria<>();
    }
}
