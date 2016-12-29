BPulse master data manager

1. Introducción
- BPUlse master data manager es una herramienta con el fin de realizar la inserción de datos maestros en el aplicativo bpulse

2. Requisitos previos
- Tener descargado el jar, o usando maven, el driver jdbc con la base de datos a utilizar
- Crear en la aplicación el mastertype y el masterdef a utilizar

3. Configuración
- La configuración posible esta situada en el fichero application.properties. En este fichero, se debe definir:
  * dbdriver - Driver jdbc
  * dbconnection - Cadena de conexión jdbc
  * dbuser - Usuario de conexión a la base de datos
  * dbpassword - Contraseña de conexión a la base de datos
  * bp.endpoint - URL de la aplicación app.model. Ejemplo http://bpulse:30003/app.model/
  * bp.masterTypeId - Tipo maestro. Ejemplo Client (Este campo es obligatorio)
  * bp.masterDefId - Definicion de maestro(Este campo es obligatorio). Ejemplo ClientDef
  * bp.login - Usuario de acceso a bpulse. Este usuario debe tener asignado el grupo API
  * bp.password - Password del usuario
  * bp.query - petición SQL a lanzar. El primer campo es el codigo de maestro, el siguiente es su binding, y los siguientes son atributos de masterdef. Ejemplo: select ID_POBLACION, NOMBRE, ID_ZONA from SYSDBA.POBLACION
  
4. Ejecución
- Para su uso, es necesario arrancar desde la clase App.java