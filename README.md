# SIGITEC - TP3 / Actividad Práctica 3
Sistema de Gestión de Incidencias Técnicas y Mantenimiento para Casinos.

## Contenido
- Java 17 (objetos, herencia, abstracción, polimorfismo, constructores, encapsulamiento).
- Menú de consola (`do-while` y `switch`) para registrar, buscar, listar y cambiar incidencias.
- `ArrayList`, `LinkedList` / `Queue`, `Comparator` y búsquedas por texto.
- JDBC y MySQL opcionales, compatibles con **el esquema del TP2**.
- Pruebas autónomas de las reglas de negocio.
- Diagramas del prototipo y ejemplos SQL.

## Modo demostración (sin servidor MySQL)

Requisitos: JDK 17 o superior. Desde IntelliJ, NetBeans o Eclipse, ejecutar
`ar.edu.sigitec.app.Main` sin argumentos. Desde Windows con Maven instalado:

```powershell
mvn clean compile
mvn exec:java
```

Si no dispone de Maven, abra los archivos en una IDE con JDK 17+ y ejecute la clase Main.

## Modo MySQL REAL (JDBC)

1. Iniciar MySQL 8 y ejecutar `database/01_esquema_AP2.sql` sobre una **base nueva**. El archivo no se debe ejecutar dos veces sobre tablas existentes (es el esquema elaborado para AP2).
2. Ejecutar `database/02_datos_demo_AP3.sql`. Este archivo agrega catálogos y casos de ejemplo; puede repetirse sin duplicar los registros de demostración.
3. Abrir el proyecto como Maven para cargar el controlador oficial `mysql-connector-j`.
4. Configurar las credenciales en las variables de entorno (NO subir contraseñas a GitHub):

```powershell
$env:SIGITEC_DB_URL = 'jdbc:mysql://localhost:3306/sigitec_db?serverTimezone=America/Argentina/Buenos_Aires'
$env:SIGITEC_DB_USER = 'root'
$env:SIGITEC_DB_PASSWORD = 'REEMPLAZAR_CON_TU_CLAVE'
mvn clean compile
mvn exec:java -Dexec.args="--mysql"
```

En una IDE, en los argumentos de `Main` agregar `--mysql`.

**Nota de alcance:** el modo MySQL persiste altas de incidencias y cambios de estado, incluyendo historial. Las pantallas de catálogos son consultas (no altas). Los datos de acceso no se implementan en TP3; las contraseñas ficticias de la carga de demostración no son cuentas operativas.

## Pruebas sin Maven ni MySQL

Con JDK 17+ en una terminal bash:

```bash
mkdir -p out
javac -encoding UTF-8 -d out $(find src/main/java src/test/java -name '*.java')
java -cp out ar.edu.sigitec.PruebasSinDependencias
java -cp out ar.edu.sigitec.app.Main
```

En Windows puede ejecutarse la clase `PruebasSinDependencias` directamente en la IDE.

## Estructura

`src/main/java`: dominio (Usuario/Técnico/Supervisor, Casino, Equipo, Incidencia), aplicación, servicios, repositorios y JDBC.
`src/test/java`: casos automáticos sin frameworks externos.
`database`: esquema heredado TP2, datos de ejemplo y consultas.
`docs`: diagramas UML / flujo de menú.

El modo demostración funciona sin MySQL, pero **solo el modo `--mysql` conserva los datos entre ejecuciones**.
