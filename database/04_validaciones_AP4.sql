USE sigitec_db;
-- Consultas de control sin cambios en los registros.
SELECT COUNT(*) AS cantidad_incidencias FROM incidencias;
SELECT id_incidencia,titulo,prioridad,fecha_alta FROM incidencias ORDER BY fecha_alta DESC;
SELECT es.nombre AS estado,COUNT(*) AS total FROM incidencias i
JOIN estados_incidencia es ON es.id_estado=i.id_estado GROUP BY es.nombre ORDER BY total DESC;
SELECT h.id_incidencia, anterior.nombre AS anterior, nuevo.nombre AS nuevo,
       h.fecha_cambio, h.observacion
FROM historial_estado h
LEFT JOIN estados_incidencia anterior ON anterior.id_estado=h.id_estado_anterior
JOIN estados_incidencia nuevo ON nuevo.id_estado=h.id_estado_nuevo
ORDER BY h.fecha_cambio DESC LIMIT 20;
