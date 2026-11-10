package ar.edu.sigitec.service;

import ar.edu.sigitec.model.Incidencia;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/** Exportación local sencilla; no reemplaza la base de datos MySQL. */
public final class ExportadorIncidenciasCsv {
    public Path exportar(List<Incidencia> incidencias, Path destino) throws IOException {
        if (destino == null) throw new IllegalArgumentException("Destino requerido");
        Path carpeta = destino.toAbsolutePath().getParent();
        if (carpeta != null) Files.createDirectories(carpeta);
        try (BufferedWriter writer = Files.newBufferedWriter(destino, StandardCharsets.UTF_8)) {
            writer.write("id;titulo;prioridad;estado;equipo;responsable;fecha_alta");
            writer.newLine();
            for (Incidencia i : incidencias) {
                writer.write(i.getId() + ";" + escapar(i.getTitulo()) + ";" +
                        i.getPrioridad() + ";" + i.getEstado() + ";" +
                        escapar(i.getEquipo().getCodigo()) + ";" +
                        escapar(i.getResponsable().getNombre()) + ";" + i.getFechaCreacion());
                writer.newLine();
            }
        }
        return destino.toAbsolutePath();
    }

    /** Escapa delimitadores, comillas, saltos y caracteres de fórmula para CSV. */
    private String escapar(String texto) {
        if (texto == null) return "\"\"";
        String limpio = texto.replace("\r", " ").replace("\n", " ");
        if (!limpio.isBlank() && "=+-@".indexOf(limpio.charAt(0)) >= 0) limpio = "'" + limpio;
        return "\"" + limpio.replace("\"", "\"\"") + "\"";
    }
}
