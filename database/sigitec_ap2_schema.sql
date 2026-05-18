CREATE DATABASE IF NOT EXISTS sigitec_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE sigitec_db;

CREATE TABLE roles (
  id_rol INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(50) NOT NULL UNIQUE,
  descripcion VARCHAR(150)
) ENGINE=InnoDB;

CREATE TABLE usuarios (
  id_usuario INT AUTO_INCREMENT PRIMARY KEY,
  id_rol INT NOT NULL,
  nombre VARCHAR(100) NOT NULL,
  email VARCHAR(120),
  username VARCHAR(50) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  activo BOOLEAN NOT NULL DEFAULT TRUE,
  fecha_alta DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_usuarios_roles
    FOREIGN KEY (id_rol) REFERENCES roles(id_rol)
) ENGINE=InnoDB;

CREATE TABLE casinos (
  id_casino INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  direccion VARCHAR(150),
  ciudad VARCHAR(80),
  provincia VARCHAR(80),
  telefono VARCHAR(50),
  email VARCHAR(120),
  activo BOOLEAN NOT NULL DEFAULT TRUE
) ENGINE=InnoDB;

CREATE TABLE sectores (
  id_sector INT AUTO_INCREMENT PRIMARY KEY,
  id_casino INT NOT NULL,
  nombre VARCHAR(100) NOT NULL,
  descripcion VARCHAR(200),
  CONSTRAINT fk_sectores_casinos
    FOREIGN KEY (id_casino) REFERENCES casinos(id_casino)
) ENGINE=InnoDB;

CREATE TABLE equipos (
  id_equipo INT AUTO_INCREMENT PRIMARY KEY,
  id_casino INT NOT NULL,
  id_sector INT,
  codigo_interno VARCHAR(50) NOT NULL,
  tipo_equipo VARCHAR(60) NOT NULL,
  marca VARCHAR(60),
  modelo VARCHAR(80),
  nro_serie VARCHAR(80),
  direccion_ip VARCHAR(45),
  activo BOOLEAN NOT NULL DEFAULT TRUE,
  CONSTRAINT uk_equipo_casino_codigo UNIQUE (id_casino, codigo_interno),
  CONSTRAINT fk_equipos_casinos
    FOREIGN KEY (id_casino) REFERENCES casinos(id_casino),
  CONSTRAINT fk_equipos_sectores
    FOREIGN KEY (id_sector) REFERENCES sectores(id_sector)
) ENGINE=InnoDB;

CREATE TABLE tipos_incidencia (
  id_tipo INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(80) NOT NULL UNIQUE,
  descripcion VARCHAR(200),
  prioridad_base ENUM('BAJA','MEDIA','ALTA','CRITICA') NOT NULL DEFAULT 'MEDIA'
) ENGINE=InnoDB;

CREATE TABLE estados_incidencia (
  id_estado INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(60) NOT NULL UNIQUE,
  orden_flujo INT NOT NULL,
  es_final BOOLEAN NOT NULL DEFAULT FALSE
) ENGINE=InnoDB;

CREATE TABLE incidencias (
  id_incidencia INT AUTO_INCREMENT PRIMARY KEY,
  id_casino INT NOT NULL,
  id_equipo INT,
  id_tipo INT NOT NULL,
  id_estado INT NOT NULL,
  id_usuario_reporta INT NOT NULL,
  id_usuario_asignado INT,
  titulo VARCHAR(150) NOT NULL,
  descripcion TEXT NOT NULL,
  prioridad ENUM('BAJA','MEDIA','ALTA','CRITICA') NOT NULL DEFAULT 'MEDIA',
  fecha_alta DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  fecha_cierre DATETIME NULL,
  solucion TEXT NULL,
  CONSTRAINT fk_incidencias_casinos
    FOREIGN KEY (id_casino) REFERENCES casinos(id_casino),
  CONSTRAINT fk_incidencias_equipos
    FOREIGN KEY (id_equipo) REFERENCES equipos(id_equipo),
  CONSTRAINT fk_incidencias_tipos
    FOREIGN KEY (id_tipo) REFERENCES tipos_incidencia(id_tipo),
  CONSTRAINT fk_incidencias_estados
    FOREIGN KEY (id_estado) REFERENCES estados_incidencia(id_estado),
  CONSTRAINT fk_incidencias_usuario_reporta
    FOREIGN KEY (id_usuario_reporta) REFERENCES usuarios(id_usuario),
  CONSTRAINT fk_incidencias_usuario_asignado
    FOREIGN KEY (id_usuario_asignado) REFERENCES usuarios(id_usuario),
  INDEX idx_incidencias_estado (id_estado),
  INDEX idx_incidencias_fecha (fecha_alta),
  INDEX idx_incidencias_casino (id_casino)
) ENGINE=InnoDB;

CREATE TABLE comentarios_incidencia (
  id_comentario INT AUTO_INCREMENT PRIMARY KEY,
  id_incidencia INT NOT NULL,
  id_usuario INT NOT NULL,
  comentario TEXT NOT NULL,
  fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_comentarios_incidencias
    FOREIGN KEY (id_incidencia) REFERENCES incidencias(id_incidencia)
    ON DELETE CASCADE,
  CONSTRAINT fk_comentarios_usuarios
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
) ENGINE=InnoDB;

CREATE TABLE historial_estado (
  id_historial INT AUTO_INCREMENT PRIMARY KEY,
  id_incidencia INT NOT NULL,
  id_estado_anterior INT,
  id_estado_nuevo INT NOT NULL,
  id_usuario INT NOT NULL,
  fecha_cambio DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  observacion VARCHAR(250),
  CONSTRAINT fk_historial_incidencias
    FOREIGN KEY (id_incidencia) REFERENCES incidencias(id_incidencia)
    ON DELETE CASCADE,
  CONSTRAINT fk_historial_estado_anterior
    FOREIGN KEY (id_estado_anterior) REFERENCES estados_incidencia(id_estado),
  CONSTRAINT fk_historial_estado_nuevo
    FOREIGN KEY (id_estado_nuevo) REFERENCES estados_incidencia(id_estado),
  CONSTRAINT fk_historial_usuarios
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
) ENGINE=InnoDB;