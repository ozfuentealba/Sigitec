package ar.edu.sigitec.repository;

import java.util.List;
import java.util.Optional;

/** Contrato abstracto: admite persistencia en memoria o MySQL. */
public interface Repositorio<T> {
    void agregar(T entidad);
    List<T> listar();
    Optional<T> buscarPorId(int id);
    void actualizar(T entidad);
}
