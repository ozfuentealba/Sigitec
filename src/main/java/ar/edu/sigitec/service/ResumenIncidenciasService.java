package ar.edu.sigitec.service;

import ar.edu.sigitec.model.Incidencia;
import ar.edu.sigitec.model.Prioridad;
import java.util.ArrayList;
import java.util.List;

/** Conjuga un arreglo de contadores con un ArrayList de incidencias variables. */
public final class ResumenIncidenciasService {
    public List<String> resumir(List<Incidencia> incidencias) {
        // Los niveles de prioridad son un catálogo pequeño y fijo: arreglo.
        Prioridad[] prioridades = Prioridad.values();
        int[] cantidades = new int[prioridades.length];

        // La cantidad de tickets puede cambiar durante la ejecución: ArrayList.
        ArrayList<Incidencia> registros = new ArrayList<>(incidencias);
        for (Incidencia incidencia : registros) {
            cantidades[incidencia.getPrioridad().ordinal()]++;
        }

        List<String> lineas = new ArrayList<>();
        lineas.add("Total de incidencias: " + registros.size());
        for (int i = prioridades.length - 1; i >= 0; i--) {
            lineas.add(prioridades[i] + ": " + cantidades[i]);
        }
        return lineas;
    }
}
