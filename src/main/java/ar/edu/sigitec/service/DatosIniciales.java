package ar.edu.sigitec.service;

import ar.edu.sigitec.exception.DatoInvalidoException;
import ar.edu.sigitec.model.*;
import ar.edu.sigitec.repository.Repositorio;

import java.util.List;

public class DatosIniciales {
    private DatosIniciales() {
    }

    public static void cargar(Repositorio<Usuario> usuarios, Repositorio<Casino> casinos,
                              Repositorio<Equipo> equipos, IncidenciaService incidenciaService) {
        Administrador admin = new Administrador(1, "Oscar Fuentealba", "oscar@example.com");
        Supervisor supervisor = new Supervisor(2, "Supervisor Técnico", "supervisor@example.com", "Operaciones");
        Tecnico tecnicoCashless = new Tecnico(3, "Técnico Cashless", "cashless@example.com", "Cashless / AFT");
        Tecnico tecnicoRedes = new Tecnico(4, "Técnico Redes", "redes@example.com", "Redes / Servidores");

        usuarios.agregar(admin);
        usuarios.agregar(supervisor);
        usuarios.agregar(tecnicoCashless);
        usuarios.agregar(tecnicoRedes);

        Casino magic = new Casino(1, "Casino Magic", "Neuquén");
        Casino allen = new Casino(2, "Sala Allen", "Allen");
        casinos.agregar(magic);
        casinos.agregar(allen);

        Equipo maquina231 = new Equipo(1, "EGM-231", "Máquina de juego", magic);
        Equipo servidor = new Equipo(2, "SRV-CASHLESS-01", "Servidor", magic);
        Equipo atm = new Equipo(3, "ATM-02", "Kiosco / Cajero", allen);
        equipos.agregar(maquina231);
        equipos.agregar(servidor);
        equipos.agregar(atm);

        try {
            incidenciaService.registrar("Falla de ticket en máquina", "La máquina no imprime ticket al finalizar la operación.",
                    Prioridad.ALTA, maquina231, tecnicoCashless);
            incidenciaService.registrar("Servidor sin respuesta", "El servidor no responde consultas desde la red interna.",
                    Prioridad.CRITICA, servidor, tecnicoRedes);
            incidenciaService.registrar("ATM con error de conexión", "El cajero no permite validar operaciones contra la base de datos.",
                    Prioridad.MEDIA, atm, tecnicoCashless);
        } catch (DatoInvalidoException e) {
            throw new IllegalStateException("Error cargando datos iniciales", e);
        }
    }

    public static Usuario seleccionarPrimerTecnico(List<Usuario> usuarios) {
        return usuarios.stream()
                .filter(u -> u instanceof Tecnico)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Debe existir al menos un técnico."));
    }
}
