# SIGITEC - Prototipo Java + MySQL

Sistema de Gestión de Incidencias Técnicas y Mantenimiento para Casinos.

## Requisitos

- Java 17 o superior
- MySQL Server
- Maven
- NetBeans, IntelliJ IDEA o Eclipse

## Crear base de datos

Ejecutar en MySQL Workbench el script:

```sql
database/sigitec_db.sql
```

## Configurar conexión

Editar:

```text
src/main/resources/config.properties
```

Ejemplo:

```properties
db.url=jdbc:mysql://localhost:3306/sigitec_db?useSSL=false&serverTimezone=America/Argentina/Buenos_Aires&allowPublicKeyRetrieval=true
db.user=root
db.password=admin
```

## Usuarios de prueba

| Usuario | Clave | Rol |
|---|---|---|
| admin | admin123 | Administrador |
| supervisor | super123 | Supervisor técnico |
| tecnico | tecnico123 | Técnico |

## Ejecutar

Desde la carpeta del proyecto:

```bash
mvn clean compile
mvn exec:java
```

## Módulos del prototipo

- Inicio de sesión.
- Registro de casinos/salas.
- Registro de máquinas/equipos.
- Registro de incidencias.
- Actualización de estado.
- Comentarios técnicos.
- Reporte básico por estado.

Este prototipo es una versión inicial operativa. No representa el producto final, sino una base funcional para validar requerimientos y demostrar la viabilidad técnica del proyecto.
