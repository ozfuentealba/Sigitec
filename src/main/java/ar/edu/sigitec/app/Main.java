package ar.edu.sigitec.app;

import ar.edu.sigitec.db.CatalogoMySql;
import ar.edu.sigitec.db.ConsultaHistorialMySql;
import ar.edu.sigitec.exception.*;
import ar.edu.sigitec.model.*;
import ar.edu.sigitec.repository.*;
import ar.edu.sigitec.service.*;
import ar.edu.sigitec.util.LectorConsola;
import java.sql.SQLException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

/** Aplicación Java con menú y dos modos: demostración en memoria, y persistencia JDBC/MySQL. */
public class Main {
    private final LectorConsola lector = new LectorConsola();
    private final Repositorio<Usuario> usuarios = new RepositorioMemoria<>();
    private final Repositorio<Casino> casinos = new RepositorioMemoria<>();
    private final Repositorio<Equipo> equipos = new RepositorioMemoria<>();
    private final IncidenciaService servicio;
    private final boolean mysql;

    public Main(boolean mysql) {
        this.mysql=mysql;
        Repositorio<Incidencia> repo = FabricaRepositorios.crearIncidencias(mysql);
        if (mysql) {
            try { CatalogoMySql.cargar(usuarios,casinos,equipos); }
            catch (SQLException e) { throw new IllegalStateException("No se pudieron cargar catálogos MySQL",e); }
        }
        this.servicio=new IncidenciaService(repo);
        if (!mysql) DatosIniciales.cargar(usuarios,casinos,equipos,servicio);
    }

    public static void main(String[] args) {
        boolean mysql=Arrays.asList(args).contains("--mysql");
        try {
            System.out.println("SIGITEC | Modo " + (mysql?"MySQL JDBC":"demostración en memoria"));
            new Main(mysql).iniciar();
        } catch (IllegalStateException e) {
            System.err.println("No se pudo iniciar SIGITEC: " + e.getMessage());
            if (e.getCause()!=null) System.err.println("Detalle técnico: " + e.getCause().getMessage());
            System.exit(1);
        }
    }

    private void iniciar() {
        int opcion;
        do {
            mostrarMenu();
            opcion=lector.leerEntero("Seleccione una opción: ");
            ejecutarOpcion(opcion);
        } while (opcion!=0);
    }

    private void mostrarMenu() {
        System.out.println("\n==== SIGITEC - Gestión de Incidencias Técnicas ====");
        System.out.println("1. Listar usuarios");
        System.out.println("2. Listar casinos");
        System.out.println("3. Listar equipos");
        System.out.println("4. Listar incidencias");
        System.out.println("5. Registrar incidencia");
        System.out.println("6. Cambiar estado");
        System.out.println("7. Buscar incidencia por texto");
        System.out.println("8. Ordenar por prioridad");
        System.out.println("9. Atender siguiente pendiente (cola FIFO)");
        System.out.println("10. Resumen de incidencias por prioridad");
        System.out.println("11. Exportar incidencias a CSV");
        System.out.println("12. Ver historial de estados (MySQL)");
        System.out.println("0. Salir");
    }

    private void ejecutarOpcion(int opcion) {
        try {
            switch (opcion) {
                case 1 -> listar("Usuarios",usuarios.listar());
                case 2 -> listar("Casinos",casinos.listar());
                case 3 -> listar("Equipos",equipos.listar());
                case 4 -> listar("Incidencias",servicio.listar());
                case 5 -> registrarIncidencia();
                case 6 -> cambiarEstado();
                case 7 -> buscarIncidencias();
                case 8 -> listar("Incidencias por prioridad",servicio.listarOrdenadasPorPrioridad());
                case 9 -> atenderPendiente();
                case 10 -> listar("Resumen por prioridad", new ResumenIncidenciasService().resumir(servicio.listar()));
                case 11 -> exportarCsv();
                case 12 -> consultarHistorial();
                case 0 -> System.out.println("Fin del prototipo SIGITEC.");
                default -> System.out.println("Opción inválida. Vuelva a intentar.");
            }
        } catch (DatoInvalidoException | EntidadNoEncontradaException | IllegalArgumentException e) {
            System.out.println("Dato incorrecto: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error al escribir el archivo CSV: " + e.getMessage());
        } catch (IllegalStateException e) {
            System.out.println("Error técnico: " + e.getMessage());
        }
    }

    private <T> void listar(String titulo, List<T> datos) {
        System.out.println("\n--- " + titulo + " ---");
        if (datos.isEmpty()) { System.out.println("No hay registros."); return; }
        for (T d:datos) System.out.println(d);
    }

    private void registrarIncidencia() throws DatoInvalidoException,EntidadNoEncontradaException {
        listar("Equipos",equipos.listar());
        int idEquipo=lector.leerEntero("ID de equipo: ");
        Equipo equipo=equipos.buscarPorId(idEquipo).orElseThrow(
                ()->new EntidadNoEncontradaException("No existe el equipo " + idEquipo));
        String titulo=lector.leerTexto("Título: ");
        String desc=lector.leerTexto("Descripción: ");
        Prioridad prioridad=seleccionarPrioridad();
        Usuario responsable=DatosIniciales.seleccionarPrimerTecnico(usuarios.listar());
        Incidencia i=servicio.registrar(titulo,desc,prioridad,equipo,responsable);
        System.out.println("Incidencia registrada: " + i);
    }

    private Prioridad seleccionarPrioridad() {
        Prioridad[] valores=Prioridad.values();
        for (int i=0;i<valores.length;i++) System.out.println((i+1)+". "+valores[i]);
        int opcion=lector.leerEntero("Prioridad: ");
        if (opcion<1||opcion>valores.length) throw new IllegalArgumentException("Prioridad inválida");
        return valores[opcion-1];
    }

    private EstadoIncidencia seleccionarEstado() {
        EstadoIncidencia[] valores=EstadoIncidencia.values();
        for (int i=0;i<valores.length;i++) System.out.println((i+1)+". "+valores[i]);
        int opcion=lector.leerEntero("Estado: ");
        if (opcion<1||opcion>valores.length) throw new IllegalArgumentException("Estado inválido");
        return valores[opcion-1];
    }

    private void cambiarEstado() throws EntidadNoEncontradaException {
        listar("Incidencias",servicio.listar());
        int id=lector.leerEntero("ID de incidencia: ");
        servicio.cambiarEstado(id,seleccionarEstado());
        System.out.println("Estado actualizado.");
    }

    private void buscarIncidencias() {
        String texto=lector.leerTexto("Texto a buscar: ");
        listar("Búsqueda",servicio.buscarPorTexto(texto));
    }

    private void exportarCsv() throws IOException {
        var archivo = new ExportadorIncidenciasCsv().exportar(
                servicio.listar(), Paths.get("reportes", "incidencias_tp4.csv"));
        System.out.println("Archivo exportado correctamente: " + archivo);
    }

    private void consultarHistorial() {
        if (!mysql) {
            System.out.println("El historial persistente se consulta en modo --mysql.");
            return;
        }
        int id = lector.leerEntero("ID de incidencia: ");
        listar("Historial de la incidencia " + id, new ConsultaHistorialMySql().listar(id));
    }

    private void atenderPendiente() throws EntidadNoEncontradaException {
        Optional<Incidencia> siguiente=servicio.obtenerSiguientePendiente();
        if (siguiente.isEmpty()) { System.out.println("No hay incidencias pendientes."); return; }
        servicio.cambiarEstado(siguiente.get().getId(),EstadoIncidencia.EN_ANALISIS);
        System.out.println("Se tomó la incidencia " + siguiente.get().getId());
    }
}
