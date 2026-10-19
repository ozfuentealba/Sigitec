package ar.edu.sigitec;

import ar.edu.sigitec.exception.*;
import ar.edu.sigitec.model.*;
import ar.edu.sigitec.repository.*;
import ar.edu.sigitec.service.*;
import java.util.*;

/** Pruebas de lógica POO independientes de MySQL y de frameworks externos. */
public class PruebasSinDependencias {
    private static int verificaciones=0;
    private static void verificar(boolean condicion,String mensaje) {
        verificaciones++;
        if (!condicion) throw new AssertionError(mensaje);
    }
    public static void main(String[] args) throws Exception {
        Repositorio<Incidencia> repo=new RepositorioMemoria<>();
        IncidenciaService servicio=new IncidenciaService(repo);
        Casino c=new Casino(1,"Casino Demo","Neuquén");
        Equipo e=new Equipo(1,"EQ-01","Servidor",c);
        Usuario t=new Tecnico(1,"Técnico","tecnico@example.com","Redes");
        Usuario s=new Supervisor(2,"Supervisor","supervisor@example.com","Área técnica");
        verificar(t.getRol().equals("Técnico")&&!t.getRol().equals(s.getRol()),"Herencia/polimorfismo");
        Incidencia una=servicio.registrar("Error de red","Timeout en terminal",Prioridad.ALTA,e,t);
        Incidencia dos=servicio.registrar("Cable suelto","Sin conexión física",Prioridad.BAJA,e,t);
        verificar(servicio.listar().size()==2,"Registro de incidencias");
        verificar(servicio.buscarPorTexto("timeout").size()==1,"Búsqueda por descripción");
        verificar(servicio.listarOrdenadasPorPrioridad().get(0).getId()==una.getId(),"Ordenamiento por prioridad");
        verificar(servicio.obtenerSiguientePendiente().orElseThrow().getId()==una.getId(),"Cola FIFO");
        servicio.cambiarEstado(una.getId(),EstadoIncidencia.EN_ANALISIS);
        verificar(repo.buscarPorId(una.getId()).orElseThrow().getEstado()==EstadoIncidencia.EN_ANALISIS,"Cambio de estado");
        boolean erro = false;
        try { servicio.registrar("  ","detalle",Prioridad.MEDIA,e,t); }
        catch (DatoInvalidoException ex) { erro=true; }
        verificar(erro,"Validación y excepciones");
        boolean noExiste=false;
        try { servicio.cambiarEstado(999999,EstadoIncidencia.RESUELTA); }
        catch (EntidadNoEncontradaException ex) { noExiste=true; }
        verificar(noExiste,"Entidad no encontrada");
        System.out.println("PRUEBAS CORRECTAS: " + verificaciones + "/" + verificaciones);
    }
}
