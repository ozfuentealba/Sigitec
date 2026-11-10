# Guion de presentación de SIGITEC (aproximadamente 3 minutos)

**Inicio (0:00-0:35) | Mostrar portada y repositorio**

Hola, soy estudiante de Ingeniería en Sistemas y en este trabajo presento SIGITEC, un prototipo de gestión de incidencias técnicas para salas de casino. La idea surgió de un problema operativo bastante común: cuando hay fallas en máquinas, servidores o equipos de red, muchas veces el seguimiento queda repartido entre mensajes y anotaciones. Después cuesta reconstruir quién tomó un caso y en qué estado quedó.

**Problema y solución (0:35-1:10) | Mostrar menú en Eclipse**

Con SIGITEC busqué ordenar ese recorrido. Desde el menú se pueden consultar equipos, registrar una incidencia, asignar un responsable, cambiar el estado y buscar problemas anteriores. No es todavía un sistema comercial completo, sino un prototipo que permite comprobar la parte central del trabajo.

**Arquitectura (1:10-1:45) | Mostrar paquetes de Eclipse y diagrama**

Lo desarrollé en Java y mantuve una separación por responsabilidades: modelo, servicios, repositorios y conexión a base de datos. Elegí el patrón Repository, porque permite que las reglas del sistema no dependan de si los datos vienen de una lista en memoria o de MySQL. Una pequeña fábrica elige el repositorio al iniciar. También usé interfaces, clases derivadas, arreglos para contar prioridades y ArrayList para trabajar con las incidencias.

**Demostración (1:45-2:30) | Mostrar ejecución real**

Ahora voy a usar el modo MySQL. Primero consulto la lista de incidencias. Después registro una nueva, le asigno una prioridad y cambio su estado. La aplicación utiliza JDBC y consultas preparadas para escribir y recuperar los datos. También se puede consultar el historial de estados, obtener un resumen por prioridad y exportar las incidencias a un CSV. Si cierro y vuelvo a abrir SIGITEC, los registros siguen guardados en MySQL.

**Cierre (2:30-3:00) | Mostrar GitHub y conclusiones**

En los trabajos anteriores definí los requisitos y armé el modelo relacional. En esta última etapa integré ese diseño con el código, los controles de errores y las pruebas. Quedan mejoras por hacer, como una interfaz gráfica, autenticación real y trabajo simultáneo de varios usuarios. Para mí, el valor de esta versión es que el circuito principal ya puede probarse de punta a punta. En GitHub queda el código, la base de datos y la documentación para reproducirlo.

**Nota:** Las acciones descritas deben mostrarse realmente. Si alguna no se ejecuta, suprimir esa frase antes de grabar. No publicar contraseñas ni datos reales del casino.
