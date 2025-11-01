# NutriZulia - Backend

<p align="center">
  <img src="logo.png" alt="NutriZulia logo" width="160">
  
</p>

<p align="center">
  <a href="https://www.java.com/"><img src="https://img.shields.io/badge/Java-21-007396?logo=openjdk&logoColor=white" alt="Java 21"></a>
  <a href="https://spring.io/projects/spring-boot"><img src="https://img.shields.io/badge/Spring%20Boot-3.5.0-6DB33F?logo=springboot&logoColor=white" alt="Spring Boot 3.5.0"></a>
  <a href="https://maven.apache.org/"><img src="https://img.shields.io/badge/Maven-3.x-C71A36?logo=apachemaven&logoColor=white" alt="Maven"></a>
  <a href="https://www.postgresql.org/"><img src="https://img.shields.io/badge/PostgreSQL-4169E1?logo=postgresql&logoColor=white" alt="PostgreSQL"></a>
  <a href="https://www.docker.com/"><img src="https://img.shields.io/badge/Docker-Enabled-2496ED?logo=docker&logoColor=white" alt="Docker"></a>
  <a href="https://swagger.io/"><img src="https://img.shields.io/badge/OpenAPI-3.x-85EA2D?logo=swagger&logoColor=black" alt="OpenAPI"></a>
  <a href="https://jwt.io/"><img src="https://img.shields.io/badge/JWT-Auth-000000?logo=jsonwebtokens&logoColor=white" alt="JWT"></a>
</p>

## Tabla de contenidos
- [Descripción](#descripción)
- [Objetivo del Proyecto](#objetivo-del-proyecto)
- [Qué hace el sistema](#qué-hace-el-sistema)
- [Módulos y funcionalidades principales](#módulos-y-funcionalidades-principales)
- [Flujos de trabajo clave](#flujos-de-trabajo-clave)
- [Beneficios](#beneficios)
- [Tecnologías Utilizadas](#tecnologías-utilizadas)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Configuración y Perfiles](#configuración-y-perfiles)
- [Ejecución Local](#ejecución-local)
- [Ejecución con Docker](#ejecución-con-docker)
- [Documentación de la API](#documentación-de-la-api)
- [Seguridad](#seguridad)
- [Endpoints Principales](#endpoints-principales)

## Descripción
NutriZulia Backend es el núcleo de servicios que soporta tanto el Sistema Administrativo Web como la aplicación móvil utilizada por nutricionistas de instituciones públicas del Estado Zulia. Es una propuesta tecnológica dirigida a la Coordinación Regional de Nutrición y Dietética del Estado Zulia. Expone una API RESTful segura que centraliza el registro, sincronización, consulta y análisis de datos clínicos y administrativos relacionados con el estado nutricional de la población atendida.

Este backend forma parte del ecosistema NutriZulia junto a:
- Aplicación móvil Android: [NutriZulia-Android](https://github.com/andresmtataseo/NutriZulia-Android)
- Frontend web (Angular): [NutriZulia](https://github.com/andresmtataseo/NutriZulia)

### Objetivo del Proyecto
- Unificar y estandarizar la captura de información nutricional y clínica.
- Facilitar la toma de decisiones con métricas y dashboards confiables.
- Mejorar la trazabilidad de pacientes y la gestión de usuarios e instituciones.
 - Soportar operación multiplataforma (Web y Android) para nutricionistas de instituciones públicas.
 - Alinear la propuesta tecnológica con la Coordinación Regional de Nutrición y Dietética del Estado Zulia.

### Qué hace el sistema
- Recibe y sincroniza datos desde la aplicación móvil (móvil → backend) y puede servir sincronizaciones completas (backend → móvil).
- Administra catálogos y entidades maestras (municipios sanitarios, instituciones, tipos de institución, especialidades, relaciones de parentesco, grupos etarios, etc.).
- Gestiona usuarios y su asignación a instituciones, con control de roles (`ADMINISTRADOR`, `SUPERVISOR`, `NUTRICIONISTA`).
- Ofrece dashboards con métricas clave: consultas por mes, instituciones activas por municipio, usuarios activos por institución, distribución por grupos etarios y estado nutricional por grupo etario.
- Publica recursos para descarga (manuales y APK) y expone un endpoint de salud del servicio.

### Módulos y funcionalidades principales
- Autenticación y Seguridad (JWT): inicio de sesión, verificación del token, recuperación y cambio de contraseña, cierre de sesión.
- Recolección de Datos: pacientes, consultas, diagnósticos, evaluaciones antropométricas, y detalles clínicos (antropométricos, vitales, metabólicos, pediátricos, obstétricos) y actividades.
- Catálogo: consulta de catálogos y datos de referencia normalizados.
- Usuarios e Instituciones: administración de usuarios, validaciones (cédula, correo, teléfono), asignaciones a instituciones y actualización de datos.
- Dashboard: visualización de indicadores y gráficos para análisis.
- Público y Salud: descarga de manuales y APK, verificación de disponibilidad del servicio.

### Flujos de trabajo clave
- Autenticación: emisión y validación de JWT, protección de recursos con sesiones `STATELESS`.
- Sincronización: endpoints de sincronización parcial (listas de registros) y sincronización completa (dataset completo por entidad) entre móvil y backend.
- Consulta/Analítica: endpoints de dashboard parametrizables por rangos de fechas y filtros (municipio/institución).

### Beneficios
- Calidad y consistencia de los datos clínicos y administrativos.
- Indicadores oportunos para la gestión y toma de decisiones.
- Plataforma escalable, segura y con documentación accesible.

## Tecnologías Utilizadas
- **Java 21**: Lenguaje de programación principal
- **Spring Boot 3.5.0**: Framework para el desarrollo de aplicaciones Java
- **Spring Data JPA**: Acceso y manipulación de datos
- **Spring Security**: Autenticación y autorización
- **PostgreSQL**: Sistema de gestión de base de datos relacional
- **JWT (JSON Web Token)**: Autenticación basada en tokens
- **MapStruct**: Mapeo de objetos DTO/entidades
- **Lombok**: Reducción de código repetitivo
- **SpringDoc OpenAPI**: Documentación de la API (Swagger UI)
- **Apache POI / JXLS**: Generación de reportes
- **Flyway**: Migraciones de base de datos
- **Docker**: Contenerización

## Estructura del Proyecto
El proyecto está organizado en los siguientes módulos principales:

- **auth**: Gestión de autenticación y autorización
- **catalog**: Gestión de catálogos y datos de referencia
- **collection**: Gestión de colecciones de datos
- **common**: Componentes comunes utilizados en toda la aplicación
- **institution**: Gestión de instituciones
- **user**: Gestión de usuarios
- **userinstitution**: Gestión de la relación entre usuarios e instituciones
- **dashboard**: Métricas y gráficos del sistema
- **public**: Descarga de manuales y APK

## Configuración y Perfiles

```properties
spring.application.name=nutrizulia
spring.main.banner-mode=off
spring.profiles.active=dev2
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jackson.time-zone=America/Caracas
```

Perfiles disponibles:

- **dev** (`application-dev.properties`): desarrollo local con PostgreSQL.

  ```properties
  server.port=8080
  spring.datasource.url=jdbc:postgresql://localhost:5432/nutrizulia_db
  spring.datasource.username=postgres
  spring.datasource.password=1234
  spring.jpa.hibernate.ddl-auto=update
  spring.flyway.enabled=false
  springdoc.api-docs.path=/v3/api-docs
  springdoc.swagger-ui.path=/swagger-ui.html
  cors.allowed-origin-patterns=*
  cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS,PATCH
  cors.allowed-headers=Authorization,Content-Type,Accept,Origin,Access-Control-Request-Method,Access-Control-Request-Headers
  logging.level.org.springframework.security=DEBUG
  logging.level.com.nutrizulia=DEBUG
  ```

- **dev2** (`application-dev2.properties`): desarrollo apuntando a una base de datos PostgreSQL remota. Útil para pruebas integradas. Configuración similar a `dev` pero con URL remota.

- **prod** (`application-prod.properties`): producción parametrizada por variables de entorno.

  Variables esperadas (ejemplos):

  ```properties
  server.port=${PORT}
  spring.datasource.url=${SPRING_DATASOURCE_URL}
  spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
  spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}
  spring.jpa.hibernate.ddl-auto=${SPRING_JPA_HIBERNATE_DDL_AUTO}
  spring.jpa.properties.hibernate.format_sql=${SPRING_JPA_PROPERTIES_HIBERNATE_FORMAT_SQL}
  spring.jpa.show-sql=${SPRING_JPA_SHOW_SQL}
  spring.flyway.enabled=${SPRING_FLYWAY_ENABLED}
  spring.flyway.locations=${SPRING_FLYWAY_LOCATIONS}
  spring.flyway.baseline-on-migrate=${SPRING_FLYWAY_BASELINE_ON_MIGRATE}
  jwt.expiration=${JWT_EXPIRATION}
  jwt.secret=${JWT_SECRET}
  springdoc.api-docs.enabled=${SPRINGDOC_API_DOCS_ENABLED}
  springdoc.swagger-ui.enabled=${SPRINGDOC_SWAGGER_UI_ENABLED}
  cors.allowed-methods=${CORS_ALLOWED_METHODS}
  cors.allowed-headers=${CORS_ALLOWED_HEADERS}
  cors.allowed-origin-patterns=${CORS_ALLOWED_ORIGIN_PATTERNS}
  logging.level.root=${LOGGING_LEVEL_ROOT}
  logging.level.com.nutrizulia=${LOGGING_LEVEL_COM_NUTRIZULIA}
  logging.level.org.springframework.security=${LOGGING_LEVEL_ORG_SPRINGFRAMEWORK_SECURITY}
  ```

## Ejecución Local
1. Clona el repositorio
2. Navega al directorio del proyecto
3. Compila: `./mvnw clean install` (Linux/macOS) o `.\\mvnw clean install` (Windows)
4. Ejecuta en `dev`: `./mvnw spring-boot:run -Dspring-boot.run.profiles=dev` (Linux/macOS) o `.\\mvnw spring-boot:run -Dspring-boot.run.profiles=dev` (Windows)

Aplicación disponible en `http://localhost:8080`

## Ejecución con Docker

### Dockerfile (imagen única)
- Construye: `docker build -t nutrizulia-backend .`
- Ejecuta (ejemplo prod):
  `docker run --rm -p 8080:8080 -e SPRING_PROFILES_ACTIVE=prod -e SPRING_DATASOURCE_URL="jdbc:postgresql://<host>:5432/<db>" -e SPRING_DATASOURCE_USERNAME=<user> -e SPRING_DATASOURCE_PASSWORD=<pass> nutrizulia-backend`

### Docker Compose
El archivo `docker-compose.yml` define `postgres` y `backend`. Crea un archivo `.env` en la raíz con las variables requeridas:

```env
SERVER_PORT=8080
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/nutrizulia_db
SPRING_DATASOURCE_USERNAME=admin
SPRING_DATASOURCE_PASSWORD=admin_password
SPRING_DATASOURCE_DATABASE=nutrizulia_db
JWT_SECRET=define_un_secreto_seguro
JWT_EXPIRATION=2629800000
CORS_ALLOWED_METHODS=GET,POST,PUT,DELETE,OPTIONS,PATCH
CORS_ALLOWED_HEADERS=Authorization,Content-Type,Accept,Origin,Access-Control-Request-Method,Access-Control-Request-Headers
CORS_ALLOWED_ORIGIN_PATTERNS=*
SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_JPA_PROPERTIES_HIBERNATE_FORMAT_SQL=true
SPRING_JPA_SHOW_SQL=false
SPRING_FLYWAY_ENABLED=false
SPRING_FLYWAY_LOCATIONS=classpath:db/migration
SPRING_FLYWAY_BASELINE_ON_MIGRATE=true
MAILERSEND_FROM=NutriZulia <no-reply@example.com>
MAILERSEND_API_TOKEN=mlsn.xxxxx
MAILERSEND_TRIAL_ENABLED=true
MAILERSEND_TRIAL_ADMIN_EMAIL=nutrizulia@gmail.com
SPRINGDOC_API_DOCS_ENABLED=true
SPRINGDOC_SWAGGER_UI_ENABLED=true
LOGGING_LEVEL_ROOT=info
LOGGING_LEVEL_COM_NUTRIZULIA=info
LOGGING_LEVEL_ORG_SPRINGFRAMEWORK_SECURITY=warn
```

Levanta los servicios: `docker-compose up -d`

La API quedará en `http://localhost:8080`

## Documentación de la API
La documentación de la API está disponible a través de Swagger UI:
- URL: `http://localhost:8080/swagger-ui.html`
- API Docs: `http://localhost:8080/v3/api-docs`

## Seguridad
La aplicación utiliza JWT y sesiones `STATELESS`. Para acceder a los endpoints protegidos, incluye el token JWT en el encabezado de autorización:

```
Authorization: Bearer <token>
```

Acceso abierto (permitAll):
- `POST /api/v1/auth/sign-in`
- `POST /api/v1/auth/forgot-password`
- `GET /health`
- `GET /api/v1/public/manual-app`
- `GET /api/v1/public/manual-web`
- `GET /api/v1/public/apk`
- `GET /swagger-ui.html`, `GET /v3/api-docs/**` y recursos relacionados

El CORS se configura vía propiedades (`CorsConfig`) con `cors.allowed-origin-patterns`, `cors.allowed-methods`, `cors.allowed-headers`.

## Endpoints Principales

### Autenticación (`/api/v1/auth`)
- `POST /sign-in`: iniciar sesión
- `GET /check`: verificar autenticación
- `POST /forgot-password`: recuperación de contraseña
- `POST /change-password`: cambiar contraseña
- `POST /logout`: cerrar sesión

### Catálogo (`/api/v1/catalog`)
- `GET /institutions`: lista de instituciones (requiere rol `ADMINISTRADOR` o `SUPERVISOR`)
- `GET /institutions/{id}`: detalle de institución (roles)
- `GET /institutions/get-by-municipio-sanitario`: instituciones por municipio sanitario (roles)
- Otros catálogos disponibles: estados, municipios, parroquias, especialidades, etnias, relaciones, nacionalidades, grupos etarios, etc.

### Usuarios (`/api/v1/user`)
- `GET /get-all/institutions`: usuarios con instituciones (roles)
- `POST /create`: crear usuario (rol `ADMINISTRADOR`)
- `GET /check-cedula`, `GET /check-email`, `GET /check-phone`
- `PUT /save-phone`, `PUT /save-email`, `PUT /update`
- `GET /institutions-by-user`, `GET /detail`
- `POST /assign-institution`, `PUT /update-institution`

### Recolección de Datos (`/api/v1/collection`)
- Sincronización parcial (móvil → backend):
  - `POST /sync/patients`, `/sync/consultations`, `/sync/representatives`, `/sync/anthropometric-evaluations`, `/sync/anthropometric-details`, `/sync/vital-details`, `/sync/metabolic-details`, `/sync/pediatric-details`, `/sync/obstetric-details`, `/sync/activities`, `/sync/diagnoses`
- Sincronización completa (backend → móvil):
  - `GET /sync/patients/full`, `/sync/consultations/full`, `/sync/representatives/full`, `/sync/anthropometric-evaluations/full`, `/sync/anthropometric-details/full`, `/sync/vital-details/full`, `/sync/metabolic-details/full`, `/sync/pediatric-details/full`, `/sync/obstetric-details/full`, `/sync/activities/full`, `/sync/diagnoses/full`

### Dashboard (`/api/v1/dashboard`)
- `GET /consultations-per-month`: consultas por mes
- `GET /active-institutions-by-municipality`: instituciones activas por municipio
- `GET /active-users-by-institution`: usuarios activos por institución
- `GET /age-group-distribution`: distribución por grupo etario
- `GET /nutritional-status-by-age-group`: estado nutricional por grupo etario

### Público
- `GET /api/v1/public/manual-app`: descarga manual app
- `GET /api/v1/public/manual-web`: descarga manual web
- `GET /api/v1/public/apk`: descarga APK
- `GET /health`: verificación de salud del servicio