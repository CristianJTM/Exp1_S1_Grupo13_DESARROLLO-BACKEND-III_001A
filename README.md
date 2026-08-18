# Banco XYZ - Procesamiento Batch

## 1. Descripción del proyecto

El proyecto **Banco XYZ Batch** corresponde a una aplicación desarrollada con **Spring Boot** y **Spring Batch**, cuyo objetivo es procesar información bancaria mediante Jobs batch independientes.

La aplicación permite estructurar y ejecutar procesos de tratamiento de información relacionados con:

- Estados de cuenta anuales.
- Cálculo y procesamiento de intereses.
- Validación y procesamiento de transacciones.

Cada proceso se encuentra separado mediante configuraciones de Jobs, processors y writers, permitiendo mantener una estructura modular y facilitar el mantenimiento de la aplicación. Adicionalmente, se implementó un **Job orquestador** (`procesoBatchCompleto`) que permite ejecutar los tres procesos de forma secuencial en una sola corrida.

## 2. Objetivo

El objetivo principal es implementar un sistema de procesamiento batch capaz de leer información bancaria, validarla y transformarla mediante procesos independientes, almacenando posteriormente los resultados procesados en una base de datos MySQL.

La solución utiliza el modelo de procesamiento de Spring Batch:

```
Entrada de datos
      │
      ▼
   Reader
      │
      ▼
  Processor
      │
      ▼
    Writer
      │
      ▼
Base de datos
```

Los procesos implementados corresponden a:

- Estados Anuales
- Intereses
- Transacciones

Estos tres procesos pueden ejecutarse de forma individual o encadenados mediante el Job orquestador `procesoBatchCompleto`.

## 3. Tecnologías utilizadas

| Tecnología | Uso |
|---|---|
| Java 21 | Lenguaje de programación |
| Spring Boot 4.1.0 | Framework principal |
| Spring Batch 6.0.4 | Procesamiento batch |
| Spring Data JPA | Persistencia de datos |
| Hibernate | ORM |
| MySQL 8.4 | Base de datos |
| Maven | Gestión y construcción del proyecto |
| Git / GitHub | Control de versiones y entrega |

> **Nota sobre versiones:** Spring Boot 4.1.0 gestiona directamente la versión de Spring Batch (6.0.4) a través de su BOM. En el `pom.xml` **no se deben declarar versiones manuales** para `spring-batch-core` ni `spring-batch-infrastructure`; basta con incluir `spring-boot-starter-batch` y dejar que Spring Boot resuelva la versión compatible. Fijar una versión distinta (por ejemplo, una de la línea 5.x) genera conflictos de classpath y errores de arranque de la aplicación.

## 4. Estructura del proyecto

La estructura principal del proyecto es:

```
src/
└── main/
    ├── java/
    │   └── com/
    │       └── bancoxyz/
    │           └── batch/
    │               │
    │               ├── BancoXyzBatchApplication.java
    │               │
    │               ├── config/
    │               │   ├── BatchDataConfig.java
    │               │   ├── EstadosAnualesJobConfig.java
    │               │   ├── InteresesJobConfig.java
    │               │   ├── TransaccionesJobConfig.java
    │               │   └── ProcesoBatchCompletoJobConfig.java
    │               │
    │               ├── listener/
    │               │   └── BatchJobListener.java
    │               │
    │               ├── model/
    │               │   ├── CuentaAnual.java
    │               │   ├── Interes.java
    │               │   └── Transaccion.java
    │               │
    │               ├── processor/
    │               │   ├── CuentaAnualProcessor.java
    │               │   ├── InteresProcessor.java
    │               │   └── TransaccionProcessor.java
    │               │
    │               ├── repository/
    │               │   ├── CuentaRepository.java
    │               │   ├── EstadoCuentaRepository.java
    │               │   └── TransaccionRepository.java
    │               │
    │               └── writer/
    │                   ├── CuentaAnualWriter.java
    │                   ├── InteresWriter.java
    │                   └── TransaccionWriter.java
    │
    └── resources/
        └── application.properties

data/
└── semana_1/
    ├── transacciones.csv
    ├── intereses.csv
    └── cuentas_anuales.csv
```

> **Nota:** la carpeta `data/semana_1/` se ubica en la **raíz del proyecto** (al mismo nivel que `pom.xml` y `src`), ya que los readers (`BatchDataConfig.java`) resuelven las rutas de los CSV como rutas relativas al directorio de trabajo del proceso Java. Si el proyecto se ejecuta desde una ubicación distinta, los archivos deben existir en `<directorio_de_ejecución>/data/semana_1/`.

## 5. Descripción de los componentes

### 5.1 Configuración

La carpeta `config` contiene la configuración de los procesos Spring Batch.

**BatchDataConfig.java**

Contiene la configuración relacionada con la lectura de los datos de entrada utilizados por los Jobs.

Define los lectores (Reader) utilizados por los distintos procesos batch.

**EstadosAnualesJobConfig.java**

Define el Job encargado del procesamiento de estados de cuenta anuales.

El flujo general corresponde a:

```
Reader
  ↓
CuentaAnualProcessor
  ↓
CuentaAnualWriter
```

**InteresesJobConfig.java**

Define el Job encargado del procesamiento de información relacionada con intereses.

```
Reader
  ↓
InteresProcessor
  ↓
InteresWriter
```

**TransaccionesJobConfig.java**

Define el Job encargado de procesar las transacciones bancarias.

```
Reader
  ↓
TransaccionProcessor
  ↓
TransaccionWriter
```

**ProcesoBatchCompletoJobConfig.java**

Define el Job orquestador `procesoBatchCompleto`, que ejecuta de forma secuencial los tres `Step` definidos en los `JobConfig` individuales (`transaccionesStep`, `interesesStep`, `estadosAnualesStep`), reutilizándolos como beans de Spring sin duplicar su configuración:

```
procesoBatchCompleto
        │
        ▼
transaccionesStep
        │
        ▼
interesesStep
        │
        ▼
estadosAnualesStep
```

Este Job existe porque, a partir de Spring Boot 4.1.0, el `JobLauncherApplicationRunner` **exige indicar explícitamente el nombre del Job a ejecutar** cuando hay más de un `Job` bean en el contexto (falla con `IllegalStateException: Job name must be specified in case of multiple jobs` si no se especifica). El orquestador permite lanzar los tres procesos en una sola ejecución sin tener que invocar cada Job por separado.

## 6. Models

La carpeta `model` contiene las estructuras utilizadas para representar los resultados procesados.

**CuentaAnual.java**

Representa la información procesada correspondiente a los movimientos y estados anuales de una cuenta.

**Interes.java**

Representa la información resultante del procesamiento de intereses asociados a una cuenta.

**Transaccion.java**

Representa una transacción procesada, incluyendo información como fecha, monto, tipo, anomalías y observaciones.

## 7. Processors

Los processors contienen la lógica de transformación y validación de los datos leídos.

**CuentaAnualProcessor.java**

Realiza validaciones sobre los datos de las cuentas y sus movimientos.

Entre las validaciones implementadas se encuentran:

- Existencia del identificador de cuenta.
- Existencia de la fecha.
- Existencia del monto.
- Validación de montos diferentes de cero.
- Validación de la descripción.
- Validación del tipo de transacción.
- Clasificación entre depósitos y retiros.
- Cálculo del saldo correspondiente al movimiento.

Los registros que no cumplen las validaciones son descartados mediante el procesamiento batch.

**InteresProcessor.java**

Procesa la información necesaria para determinar los valores asociados al cálculo de intereses.

**TransaccionProcessor.java**

Realiza la validación y clasificación de las transacciones bancarias, identificando aquellas que presentan condiciones anómalas.

## 8. Writers

Los writers son responsables de almacenar los resultados procesados.

**CuentaAnualWriter.java**

Almacena los resultados correspondientes a los estados anuales.

**InteresWriter.java**

Almacena los resultados del procesamiento de intereses.

**TransaccionWriter.java**

Almacena los resultados del procesamiento de transacciones.

Las tablas utilizadas para almacenar estos resultados son:

- `estados_anuales`
- `intereses_procesados`
- `transacciones_procesadas`

## 9. Repositories

La carpeta `repository` contiene las interfaces de acceso a datos mediante Spring Data JPA.

**CuentaRepository.java**

Permite acceder a la información de cuentas utilizada por los procesos.

**EstadoCuentaRepository.java**

Permite almacenar y consultar los resultados generados por el procesamiento de estados anuales.

**TransaccionRepository.java**

Permite almacenar y consultar las transacciones procesadas.

## 10. Listener

**BatchJobListener.java**

El listener permite controlar eventos asociados a la ejecución de los Jobs batch.

Se utiliza para registrar información relacionada con:

- Inicio del Job.
- Finalización del Job.
- Estado de ejecución.
- Resultados generales del procesamiento.

Esto facilita la generación de evidencia durante la ejecución de los procesos. El listener está asociado tanto a los Jobs individuales como al Job orquestador `procesoBatchCompleto`.

## 11. Base de datos

La aplicación utiliza MySQL como sistema gestor de base de datos.

La configuración actual se encuentra en:

```
src/main/resources/application.properties
```

Configuración utilizada:

```properties
spring.datasource.url=jdbc:mysql://localhost:3307/banco_xyz?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=banco_user
spring.datasource.password=banco_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

Hibernate se encuentra configurado para actualizar automáticamente las estructuras correspondientes a las entidades:

```properties
spring.jpa.hibernate.ddl-auto=update
```

Las tablas de resultados generadas actualmente son:

- `estados_anuales`
- `intereses_procesados`
- `transacciones_procesadas`

## 12. Instalación y ejecución

### Requisitos

Para ejecutar el proyecto se requiere:

- Java 21.
- Maven o Maven Wrapper.
- MySQL 8.x.
- Una base de datos denominada `banco_xyz`.
- Los archivos `transacciones.csv`, `intereses.csv` y `cuentas_anuales.csv` ubicados en `data/semana_1/` en la raíz del proyecto (ver sección 4).

### Compilación

Desde la carpeta raíz del proyecto ejecutar:

```
.\mvnw.cmd clean compile
```

La compilación correcta debe finalizar mostrando:

```
BUILD SUCCESS
```

### Ejecución

Para iniciar la aplicación y ejecutar el proceso batch completo:

```
.\mvnw.cmd spring-boot:run
```

Con la configuración actual (`spring.batch.job.name=procesoBatchCompleto`), al iniciar la aplicación se ejecuta automáticamente el Job orquestador, que corre en secuencia los tres procesos (transacciones, intereses y estados anuales).

La aplicación debe mostrar información indicando que Spring Boot fue iniciado correctamente y, en el log, la ejecución de cada `Step` seguida del estado final `COMPLETED` del Job `procesoBatchCompleto`.

### Ejecutar un Job individual

Si se desea ejecutar únicamente uno de los procesos en lugar del orquestador, se puede sobrescribir el nombre del Job por línea de comandos sin modificar `application.properties`:

```
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.arguments=--spring.batch.job.name=transaccionesJob"
```

Los nombres de Job disponibles son: `transaccionesJob`, `interesesJob`, `estadosAnualesJob` y `procesoBatchCompleto`.

## 13. Configuración de Spring Batch

La aplicación utiliza Spring Batch para administrar los procesos batch.

La configuración contempla:

```properties
spring.batch.jdbc.initialize-schema=always
spring.batch.job.enabled=true
spring.batch.job.name=procesoBatchCompleto
```

A partir de Spring Boot 4.1.0, cuando existe más de un `Job` bean en el contexto de la aplicación, es **obligatorio** indicar mediante `spring.batch.job.name` cuál debe ejecutarse al iniciar; de lo contrario, la aplicación falla al arrancar con `IllegalStateException: Job name must be specified in case of multiple jobs`.

Por defecto, esta propiedad apunta al Job orquestador `procesoBatchCompleto`, de modo que al levantar la aplicación se ejecutan los tres procesos batch de forma secuencial. Para ejecutar un Job individual durante pruebas, ver la sección 12.

## 14. Jobs implementados

### Job de Estados Anuales

```
estadosAnualesJob
        │
        ▼
estadosAnualesStep
        │
        ▼
cuentasAnualesReader
        │
        ▼
CuentaAnualProcessor
        │
        ▼
CuentaAnualWriter
        │
        ▼
estados_anuales
```

### Job de Intereses

```
interesesJob
        │
        ▼
interesesStep
        │
        ▼
interesesReader
        │
        ▼
InteresProcessor
        │
        ▼
InteresWriter
        │
        ▼
intereses_procesados
```

### Job de Transacciones

```
transaccionesJob
        │
        ▼
transaccionesStep
        │
        ▼
transaccionesReader
        │
        ▼
TransaccionProcessor
        │
        ▼
TransaccionWriter
        │
        ▼
transacciones_procesadas
```

### Job Orquestador (procesoBatchCompleto)

Ejecuta en secuencia los tres `Step` anteriores, reutilizando exactamente la misma configuración de reader, processor y writer de cada uno. Es el Job que se ejecuta por defecto al iniciar la aplicación.

```
procesoBatchCompleto
        │
        ▼
transaccionesStep  ──▶  transacciones_procesadas
        │
        ▼
interesesStep      ──▶  intereses_procesados
        │
        ▼
estadosAnualesStep ──▶  estados_anuales
```

Cada `Step` conserva su propia tolerancia a fallos (`faultTolerant()`, `skip(Exception.class)`, `skipLimit(20)`), por lo que un registro individual con error dentro de un CSV no detiene la ejecución del Step ni del Job completo, siempre que no se supere el límite de omisiones configurado.
