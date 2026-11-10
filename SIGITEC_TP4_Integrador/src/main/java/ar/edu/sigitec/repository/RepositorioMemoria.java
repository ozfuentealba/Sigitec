package ar.edu.sigitec.repository;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación genérica en memoria.
 * Usa una lista para almacenar objetos durante la ejecución del prototipo.
 */
public class RepositorioMemoria<T> implements Repositorio<T> {
    private final List<T> datos = new ArrayList<>();

    @Override
    public void agregar(T entidad) {
        datos.add(entidad);
    }

    @Override
    public void actualizar(T entidad) {
        int id = obtenerId(entidad);
        for (int i = 0; i < datos.size(); i++) {
            if (obtenerId(datos.get(i)) == id) {
                datos.set(i, entidad);
                return;
            }
        }
        throw new IllegalArgumentException("Registro inexistente: " + id);
    }

    @Override
    public List<T> listar() {
        return new ArrayList<>(datos);
    }

    @Override
    public Optional<T> buscarPorId(int id) {
        return datos.stream().filter(entidad -> obtenerId(entidad) == id).findFirst();
    }

    private int obtenerId(T entidad) {
        try {
            Method metodo = entidad.getClass().getMethod("getId");
            Object valor = metodo.invoke(entidad);
            return (int) valor;
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("La entidad no posee método getId(): " + entidad.getClass().getName(), e);
        }
    }
}
