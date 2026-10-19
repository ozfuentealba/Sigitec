package ar.edu.sigitec.service;

import ar.edu.sigitec.exception.DatoInvalidoException;
import ar.edu.sigitec.exception.EntidadNoEncontradaException;
import ar.edu.sigitec.model.*;
import ar.edu.sigitec.repository.Repositorio;

import java.util.*;
import java.util.stream.Collectors;

public class IncidenciaService {
    private final Repositorio<Incidencia> repositorioIncidencias;
    private final Queue<Incidencia> colaPendientes = new LinkedList<>();
    private int ultimoId;

    public IncidenciaService(Repositorio<Incidencia> repositorioIncidencias) {
        this.repositorioIncidencias = repositorioIncidencias;
        List<Incidencia> existentes = repositorioIncidencias.listar();
        this.ultimoId = Math.max(100, existentes.stream().mapToInt(Incidencia::getId).max().orElse(0));
        existentes.stream().filter(i -> i.getEstado() == EstadoIncidencia.PENDIENTE)
                .sorted(Comparator.comparing(Incidencia::getFechaCreacion))
                .forEach(colaPendientes::offer);
    }

    public Incidencia registrar(String titulo, String descripcion, Prioridad prioridad, Equipo equipo, Usuario responsable)
            throws DatoInvalidoException {
        validarTexto(titulo, "El título es obligatorio.");
        validarTexto(descripcion, "La descripción es obligatoria.");
        if (prioridad == null || equipo == null || responsable == null) {
            throw new DatoInvalidoException("Prioridad, equipo y responsable son obligatorios.");
        }

        Incidencia incidencia = new Incidencia(++ultimoId, titulo, descripcion, prioridad, equipo, responsable);
        repositorioIncidencias.agregar(incidencia);
        colaPendientes.offer(incidencia);
        return incidencia;
    }

    public List<Incidencia> listar() {
        return repositorioIncidencias.listar();
    }

    public List<Incidencia> listarOrdenadasPorPrioridad() {
        List<Incidencia> copia = repositorioIncidencias.listar();
        copia.sort(Comparator.comparing((Incidencia i) -> i.getPrioridad().getOrden()).reversed()
                .thenComparing(Incidencia::getFechaCreacion));
        return copia;
    }

    public List<Incidencia> buscarPorTexto(String texto) {
        String criterio = texto.toLowerCase(Locale.ROOT);
        return repositorioIncidencias.listar().stream()
                .filter(i -> i.getTitulo().toLowerCase(Locale.ROOT).contains(criterio)
                        || i.getDescripcion().toLowerCase(Locale.ROOT).contains(criterio)
                        || i.getEquipo().getCodigo().toLowerCase(Locale.ROOT).contains(criterio))
                .collect(Collectors.toList());
    }

    public void cambiarEstado(int idIncidencia, EstadoIncidencia nuevoEstado)
            throws EntidadNoEncontradaException {
        Incidencia incidencia = repositorioIncidencias.buscarPorId(idIncidencia)
                .orElseThrow(() -> new EntidadNoEncontradaException("No existe una incidencia con ID " + idIncidencia));
        incidencia.setEstado(nuevoEstado);
        repositorioIncidencias.actualizar(incidencia);
    }

    public Optional<Incidencia> obtenerSiguientePendiente() {
        while (!colaPendientes.isEmpty()) {
            Incidencia incidencia = colaPendientes.poll();
            if (incidencia.getEstado() == EstadoIncidencia.PENDIENTE) {
                return Optional.of(incidencia);
            }
        }
        return Optional.empty();
    }

    private void validarTexto(String valor, String mensaje) throws DatoInvalidoException {
        if (valor == null || valor.trim().isEmpty()) {
            throw new DatoInvalidoException(mensaje);
        }
    }
}
