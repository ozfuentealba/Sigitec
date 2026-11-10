package ar.edu.sigitec;

import ar.edu.sigitec.exception.*;
import ar.edu.sigitec.model.*;
import ar.edu.sigitec.repository.*;
import ar.edu.sigitec.service.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/** Pruebas reproducibles sin conexión a MySQL. Para JDBC se requiere servidor local. */
public final class PruebasIntegradorasTP4 {
    private static int ok = 0;
    private static void validar(boolean condicion, String descripcion) {
        if (!condicion) throw new AssertionError("Falló: " + descripcion);
        System.out.println("OK - " + descripcion);
        ok++;
    }
    public static void main(String[] args) throws Exception {
        Repositorio<Incidencia> repositorio = FabricaRepositorios.crearIncidencias(false);
        validar(repositorio instanceof RepositorioMemoria, "Factory elige repositorio en memoria");
        IncidenciaService service = new IncidenciaService(repositorio);
        Casino casino = new Casino(1, "Sala de prueba", "Neuquén");
        Equipo equipo = new Equipo(1, "SRV-01", "Servidor", casino);
        Usuario tecnico = new Tecnico(1, "Técnico", "tecnico@example.com", "Redes");
        Usuario supervisor = new Supervisor(2, "Supervisor", "supervisor@example.com", "Soporte");
        validar(!tecnico.getRol().equals(supervisor.getRol()), "Herencia y polimorfismo de usuarios");
        Incidencia primera = service.registrar("Error de red", "Timeout en servidor", Prioridad.ALTA, equipo, tecnico);
        Incidencia segunda = service.registrar("Ticket atascado", "Falla en impresora", Prioridad.MEDIA, equipo, tecnico);
        Incidencia tercera = service.registrar("Interrupción", "Sin enlace", Prioridad.CRITICA, equipo, tecnico);
        validar(service.listar().size() == 3, "Registro con ArrayList");
        validar(service.buscarPorTexto("timeout").size() == 1, "Búsqueda por descripción");
        validar(service.listarOrdenadasPorPrioridad().get(0).getId() == tercera.getId(), "Ordenamiento por prioridad");
        validar(service.obtenerSiguientePendiente().orElseThrow().getId() == primera.getId(), "Cola FIFO");
        service.cambiarEstado(primera.getId(), EstadoIncidencia.EN_ANALISIS);
        validar(repositorio.buscarPorId(primera.getId()).orElseThrow().getEstado() == EstadoIncidencia.EN_ANALISIS,
                "Actualización mediante repositorio");
        service.cambiarEstado(primera.getId(), EstadoIncidencia.EN_PROCESO);
        validar(service.listar().stream().anyMatch(i -> i.getEstado() == EstadoIncidencia.EN_PROCESO),
                "Estado EN_PROCESO alineado al esquema AP2");
        List<String> resumen = new ResumenIncidenciasService().resumir(service.listar());
        validar(resumen.contains("CRITICA: 1") && resumen.contains("MEDIA: 1") && resumen.contains("ALTA: 1"),
                "Arreglo de contadores y ArrayList en resumen");
        Path archivo = Files.createTempFile("sigitec-tp4-", ".csv");
        new ExportadorIncidenciasCsv().exportar(service.listar(), archivo);
        String csv = Files.readString(archivo, StandardCharsets.UTF_8);
        validar(csv.contains("Error de red") && csv.split("\\R").length == 4, "Exportación CSV con 3 incidencias");
        Files.deleteIfExists(archivo);
        boolean invalido = false;
        try { service.registrar(" ", "detalle", Prioridad.ALTA, equipo, tecnico); }
        catch (DatoInvalidoException e) { invalido = true; }
        validar(invalido, "Excepción por título vacío");
        boolean ausente = false;
        try { service.cambiarEstado(99999, EstadoIncidencia.CERRADA); }
        catch (EntidadNoEncontradaException e) { ausente = true; }
        validar(ausente, "Excepción por ID inexistente");
        validar(Prioridad.values().length == 4, "Uso de arreglo fijo de prioridades");
        validar(repositorio.buscarPorId(primera.getId()).isPresent(), "Consulta por ID del patrón Repository");
        validar(resumen.get(0).equals("Total de incidencias: 3"), "Informe total correcto");
        System.out.println("PRUEBAS TP4 CORRECTAS: " + ok + "/" + ok);
    }
}
