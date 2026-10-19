USE sigitec_db;
-- Consulta con JOIN: incidencias, estado, ubicación y técnico.
SELECT i.id_incidencia, i.titulo, i.prioridad, es.nombre AS estado,
       c.nombre AS casino, e.codigo_interno AS equipo, u.nombre AS tecnico
FROM incidencias i
JOIN estados_incidencia es ON es.id_estado=i.id_estado
JOIN casinos c ON c.id_casino=i.id_casino
LEFT JOIN equipos e ON e.id_equipo=i.id_equipo
LEFT JOIN usuarios u ON u.id_usuario=i.id_usuario_asignado
ORDER BY i.fecha_alta DESC;
-- Búsqueda por título (ejemplo de SELECT con filtro).
SELECT id_incidencia,titulo,fecha_alta FROM incidencias
WHERE titulo LIKE '%servidor%';
-- Conteo de tickets por prioridad.
SELECT prioridad,COUNT(*) AS cantidad FROM incidencias GROUP BY prioridad;
-- Ejemplo de actualización, deshabilitado para preservar los datos de demostración:
-- UPDATE incidencias SET prioridad='CRITICA' WHERE id_incidencia=12345;
-- Ejemplo de borrado controlado, deshabilitado:
-- DELETE FROM comentarios_incidencia WHERE id_comentario=12345;
