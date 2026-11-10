USE sigitec_db;
-- Datos ilustrativos; se ejecutan después de 01_esquema_AP2.sql.
INSERT INTO roles(nombre,descripcion) VALUES
 ('ADMINISTRADOR','Gestión'),('SUPERVISOR','Seguimiento'),('TECNICO','Soporte')
 ON DUPLICATE KEY UPDATE descripcion=VALUES(descripcion);
INSERT INTO estados_incidencia(nombre,orden_flujo,es_final) VALUES
 ('PENDIENTE',1,FALSE),('EN_ANALISIS',2,FALSE),('EN_PROCESO',3,FALSE),('RESUELTA',4,TRUE),('CERRADA',5,TRUE)
 ON DUPLICATE KEY UPDATE orden_flujo=VALUES(orden_flujo),es_final=VALUES(es_final);
INSERT INTO tipos_incidencia(nombre,descripcion,prioridad_base) VALUES
 ('Aplicacion','Fallas del sistema','MEDIA'),('Red','Fallas de comunicación','ALTA'),('Hardware','Fallas físicas','MEDIA')
 ON DUPLICATE KEY UPDATE descripcion=VALUES(descripcion);
-- No se usa autenticación en consola: hash ficticio no habilita login.
INSERT INTO usuarios(id_rol,nombre,email,username,password_hash)
SELECT id_rol,'Técnico Demo','tecnico@sigitec.local','tecnico_demo','PROTOTIPO_SIN_LOGIN'
FROM roles WHERE nombre='TECNICO'
ON DUPLICATE KEY UPDATE nombre=VALUES(nombre);
INSERT INTO usuarios(id_rol,nombre,email,username,password_hash)
SELECT id_rol,'Supervisor Demo','supervisor@sigitec.local','supervisor_demo','PROTOTIPO_SIN_LOGIN'
FROM roles WHERE nombre='SUPERVISOR'
ON DUPLICATE KEY UPDATE nombre=VALUES(nombre);
INSERT INTO casinos(nombre,direccion,ciudad,provincia)
SELECT 'Casino Ejemplo AP3','Dirección ficticia','Neuquén','Neuquén'
WHERE NOT EXISTS (SELECT 1 FROM casinos WHERE nombre='Casino Ejemplo AP3');
INSERT INTO equipos(id_casino,codigo_interno,tipo_equipo,marca,modelo)
SELECT id_casino,'M-AP3-001','Máquina de juego','Marca Demo','Modelo Demo'
FROM casinos WHERE nombre='Casino Ejemplo AP3'
ON DUPLICATE KEY UPDATE modelo=VALUES(modelo);
INSERT INTO equipos(id_casino,codigo_interno,tipo_equipo,marca,modelo)
SELECT id_casino,'SRV-AP3-001','Servidor','Marca Demo','Modelo Demo'
FROM casinos WHERE nombre='Casino Ejemplo AP3'
ON DUPLICATE KEY UPDATE modelo=VALUES(modelo);
-- Incidencia demostrativa única; la creación normal se hace desde el menú Java.
INSERT INTO incidencias (id_casino,id_equipo,id_tipo,id_estado,id_usuario_reporta,
                         id_usuario_asignado,titulo,descripcion,prioridad)
SELECT c.id_casino, e.id_equipo, ti.id_tipo, es.id_estado, u.id_usuario,
       u.id_usuario,'Servidor sin respuesta (demo)','No responde una consulta interna.','ALTA'
FROM casinos c JOIN equipos e ON e.id_casino=c.id_casino AND e.codigo_interno='SRV-AP3-001'
JOIN tipos_incidencia ti ON ti.nombre='Aplicacion'
JOIN estados_incidencia es ON es.nombre='PENDIENTE'
JOIN usuarios u ON u.username='tecnico_demo'
WHERE c.nombre='Casino Ejemplo AP3'
AND NOT EXISTS (SELECT 1 FROM incidencias WHERE titulo='Servidor sin respuesta (demo)');
