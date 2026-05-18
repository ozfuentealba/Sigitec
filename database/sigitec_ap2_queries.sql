USE sigitec_db;

-- Insercion de registros base
INSERT INTO roles (nombre, descripcion) VALUES
('ADMINISTRADOR', 'Gestiona usuarios, parametros y reportes'),
('SUPERVISOR', 'Asigna responsables y controla el avance'),
('TECNICO', 'Registra y actualiza incidencias');

INSERT INTO estados_incidencia (nombre, orden_flujo, es_final) VALUES
('PENDIENTE', 1, FALSE),
('EN_ANALISIS', 2, FALSE),
('EN_PROCESO', 3, FALSE),
('RESUELTA', 4, TRUE),
('CERRADA', 5, TRUE);

INSERT INTO tipos_incidencia (nombre, descripcion, prioridad_base) VALUES
('Base de datos', 'Errores de conexion, consultas o integridad de datos', 'ALTA'),
('Red', 'Problemas de conectividad o comunicacion', 'ALTA'),
('Maquina', 'Fallas asociadas a maquina o dispositivo de sala', 'MEDIA'),
('Aplicacion', 'Errores funcionales del sistema', 'MEDIA');

INSERT INTO usuarios (id_rol, nombre, email, username, password_hash) VALUES
(1, 'Administrador SIGITEC', 'admin@sigitec.local', 'admin', 'admin123'),
(2, 'Supervisor Tecnico', 'supervisor@sigitec.local', 'supervisor', 'supervisor123'),
(3, 'Tecnico de Soporte', 'tecnico@sigitec.local', 'tecnico', 'tecnico123');

INSERT INTO casinos (nombre, direccion, ciudad, provincia, telefono, email) VALUES
('Casino Demo', 'Av. Principal 123', 'Neuquen', 'Neuquen', '299-0000000', 'soporte@casinodemo.local');

INSERT INTO sectores (id_casino, nombre, descripcion) VALUES
(1, 'Sala principal', 'Sector de maquinas de la sala principal');

INSERT INTO equipos (id_casino, id_sector, codigo_interno, tipo_equipo, marca, modelo, nro_serie, direccion_ip) VALUES
(1, 1, 'M-001', 'Maquina de juego', 'IGT', 'I-Game', 'SN001', '192.168.10.21'),
(1, 1, 'SRV-DB', 'Servidor', 'Dell', 'PowerEdge', 'SRV001', '192.168.10.10');

-- Insercion de una incidencia
INSERT INTO incidencias
(id_casino, id_equipo, id_tipo, id_estado, id_usuario_reporta, id_usuario_asignado, titulo, descripcion, prioridad)
VALUES
(1, 1, 3, 1, 3, 3, 'Maquina sin comunicacion', 'La maquina M-001 no reporta eventos al sistema.', 'ALTA');

INSERT INTO comentarios_incidencia (id_incidencia, id_usuario, comentario) VALUES
(1, 3, 'Se verifica conectividad fisica y estado del equipo.');

INSERT INTO historial_estado (id_incidencia, id_estado_anterior, id_estado_nuevo, id_usuario, observacion) VALUES
(1, NULL, 1, 3, 'Alta inicial de la incidencia');

-- Consulta general de incidencias con relaciones
SELECT
  i.id_incidencia,
  c.nombre AS casino,
  e.codigo_interno AS equipo,
  ti.nombre AS tipo,
  ei.nombre AS estado,
  u.nombre AS tecnico_asignado,
  i.titulo,
  i.prioridad,
  i.fecha_alta
FROM incidencias i
JOIN casinos c ON c.id_casino = i.id_casino
LEFT JOIN equipos e ON e.id_equipo = i.id_equipo
JOIN tipos_incidencia ti ON ti.id_tipo = i.id_tipo
JOIN estados_incidencia ei ON ei.id_estado = i.id_estado
LEFT JOIN usuarios u ON u.id_usuario = i.id_usuario_asignado
ORDER BY i.fecha_alta DESC;

-- Consulta de incidencias pendientes o en proceso
SELECT i.id_incidencia, i.titulo, ei.nombre AS estado, i.prioridad
FROM incidencias i
JOIN estados_incidencia ei ON ei.id_estado = i.id_estado
WHERE ei.es_final = FALSE;

-- Actualizacion de estado de una incidencia
UPDATE incidencias
SET id_estado = 3
WHERE id_incidencia = 1;

INSERT INTO historial_estado (id_incidencia, id_estado_anterior, id_estado_nuevo, id_usuario, observacion)
VALUES (1, 1, 3, 3, 'Se inicia revision tecnica en sitio');

-- Borrado controlado de un comentario de prueba
DELETE FROM comentarios_incidencia
WHERE id_comentario = 1;

-- Borrado de una incidencia de prueba (comentarios e historial se eliminan por cascada)
DELETE FROM incidencias
WHERE id_incidencia = 1;