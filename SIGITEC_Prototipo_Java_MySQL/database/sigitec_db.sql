CREATE DATABASE IF NOT EXISTS sigitec_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE sigitec_db;

DROP TABLE IF EXISTS historial_estado;
DROP TABLE IF EXISTS comentarios_incidencia;
DROP TABLE IF EXISTS incidencias;
DROP TABLE IF EXISTS equipos;
DROP TABLE IF EXISTS tipos_equipo;
DROP TABLE IF EXISTS estados_incidencia;
DROP TABLE IF EXISTS prioridades;
DROP TABLE IF EXISTS tipos_incidencia;
DROP TABLE IF EXISTS casinos;
DROP TABLE IF EXISTS usuarios;
DROP TABLE IF EXISTS roles;

CREATE TABLE roles (
  id_rol INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE usuarios (
  id_usuario INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(100) NOT NULL,
  nombre_completo VARCHAR(120) NOT NULL,
  id_rol INT NOT NULL,
  activo BOOLEAN NOT NULL DEFAULT TRUE,
  FOREIGN KEY (id_rol) REFERENCES roles(id_rol)
);

CREATE TABLE casinos (
  id_casino INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(120) NOT NULL,
  localidad VARCHAR(100),
  direccion VARCHAR(150),
  contacto VARCHAR(120),
  telefono VARCHAR(60),
  email VARCHAR(120),
  activo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE tipos_equipo (
  id_tipo_equipo INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(80) NOT NULL UNIQUE
);

CREATE TABLE equipos (
  id_equipo INT AUTO_INCREMENT PRIMARY KEY,
  id_casino INT NOT NULL,
  id_tipo_equipo INT NOT NULL,
  identificador VARCHAR(80) NOT NULL,
  marca VARCHAR(80),
  modelo VARCHAR(80),
  ubicacion VARCHAR(120),
  ip VARCHAR(45),
  activo BOOLEAN NOT NULL DEFAULT TRUE,
  FOREIGN KEY (id_casino) REFERENCES casinos(id_casino),
  FOREIGN KEY (id_tipo_equipo) REFERENCES tipos_equipo(id_tipo_equipo)
);

CREATE TABLE tipos_incidencia (
  id_tipo_incidencia INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(80) NOT NULL UNIQUE,
  descripcion VARCHAR(250)
);

CREATE TABLE prioridades (
  id_prioridad INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(50) NOT NULL UNIQUE,
  nivel INT NOT NULL
);

CREATE TABLE estados_incidencia (
  id_estado INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE incidencias (
  id_incidencia INT AUTO_INCREMENT PRIMARY KEY,
  codigo VARCHAR(30) NOT NULL UNIQUE,
  id_casino INT NOT NULL,
  id_equipo INT NULL,
  id_tipo_incidencia INT NOT NULL,
  id_prioridad INT NOT NULL,
  id_estado INT NOT NULL,
  titulo VARCHAR(160) NOT NULL,
  descripcion TEXT NOT NULL,
  fecha_alta DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  cliente_reporta VARCHAR(120),
  id_tecnico_asignado INT NULL,
  fecha_cierre DATETIME NULL,
  FOREIGN KEY (id_casino) REFERENCES casinos(id_casino),
  FOREIGN KEY (id_equipo) REFERENCES equipos(id_equipo),
  FOREIGN KEY (id_tipo_incidencia) REFERENCES tipos_incidencia(id_tipo_incidencia),
  FOREIGN KEY (id_prioridad) REFERENCES prioridades(id_prioridad),
  FOREIGN KEY (id_estado) REFERENCES estados_incidencia(id_estado),
  FOREIGN KEY (id_tecnico_asignado) REFERENCES usuarios(id_usuario)
);

CREATE TABLE comentarios_incidencia (
  id_comentario INT AUTO_INCREMENT PRIMARY KEY,
  id_incidencia INT NOT NULL,
  id_usuario INT NOT NULL,
  comentario TEXT NOT NULL,
  fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (id_incidencia) REFERENCES incidencias(id_incidencia),
  FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);

CREATE TABLE historial_estado (
  id_historial INT AUTO_INCREMENT PRIMARY KEY,
  id_incidencia INT NOT NULL,
  id_estado_anterior INT NULL,
  id_estado_nuevo INT NOT NULL,
  id_usuario INT NOT NULL,
  fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  observacion VARCHAR(250),
  FOREIGN KEY (id_incidencia) REFERENCES incidencias(id_incidencia),
  FOREIGN KEY (id_estado_anterior) REFERENCES estados_incidencia(id_estado),
  FOREIGN KEY (id_estado_nuevo) REFERENCES estados_incidencia(id_estado),
  FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);

INSERT INTO roles(nombre) VALUES ('Administrador'), ('Supervisor técnico'), ('Técnico');
INSERT INTO usuarios(username, password, nombre_completo, id_rol) VALUES
('admin', 'admin123', 'Administrador SIGITEC', 1),
('supervisor', 'super123', 'Supervisor Técnico', 2),
('tecnico', 'tecnico123', 'Técnico de Soporte', 3);

INSERT INTO casinos(nombre, localidad, direccion, contacto, telefono, email) VALUES
('Casino Demo Centro', 'Neuquén', 'Av. Principal 123', 'Operador de sala', '299-0000000', 'contacto@casinodemo.com'),
('Sala Demo Sur', 'Río Negro', 'Calle 45 Nº 800', 'Responsable técnico', '298-0000000', 'soporte@salademo.com');

INSERT INTO tipos_equipo(nombre) VALUES ('Máquina tragamonedas'), ('Servidor'), ('Terminal cashless'), ('Impresora'), ('Dispositivo de red');
INSERT INTO tipos_incidencia(nombre, descripcion) VALUES
('Comunicación', 'Fallas de conexión, red o comunicación con equipos'),
('Base de datos', 'Errores o inconsistencias en la información'),
('Aplicación', 'Errores funcionales del sistema'),
('Hardware', 'Fallas físicas de equipos o periféricos'),
('Operación', 'Consultas o problemas de uso');
INSERT INTO prioridades(nombre, nivel) VALUES ('Baja', 1), ('Media', 2), ('Alta', 3), ('Crítica', 4);
INSERT INTO estados_incidencia(nombre) VALUES ('Pendiente'), ('En análisis'), ('Asignada'), ('Resuelta'), ('Cerrada');

INSERT INTO equipos(id_casino, id_tipo_equipo, identificador, marca, modelo, ubicacion, ip) VALUES
(1, 1, 'Maq-001', 'IGT', 'I-Game', 'Sector A', '192.168.1.20'),
(1, 2, 'SRV-SIAC', 'Dell', 'PowerEdge', 'Rack técnico', '192.168.1.10'),
(2, 3, 'ATV-015', 'GST', 'AtView', 'Sector B', '192.168.2.15');

INSERT INTO incidencias(codigo, id_casino, id_equipo, id_tipo_incidencia, id_prioridad, id_estado, titulo, descripcion, cliente_reporta, id_tecnico_asignado)
VALUES ('INC-0001', 1, 1, 1, 3, 2, 'Máquina sin comunicación', 'La máquina Maq-001 no reporta eventos al sistema.', 'Operador de sala', 3);
