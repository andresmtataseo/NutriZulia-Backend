# NutriZulia - Backend

## Descripción
Sistema Administrativo WEB para NutriZulia. Esta aplicación backend proporciona una API RESTful para gestionar información relacionada con nutrición y salud, incluyendo usuarios, instituciones, catálogos y colecciones de datos.

## Tecnologías Utilizadas
- **Java 21**: Lenguaje de programación principal
- **Spring Boot 3.5.0**: Framework para el desarrollo de aplicaciones Java
- **Spring Data JPA**: Para el acceso y manipulación de datos
- **Spring Security**: Para la autenticación y autorización
- **MySQL**: Sistema de gestión de base de datos relacional
- **JWT (JSON Web Token)**: Para la autenticación basada en tokens
- **MapStruct**: Para el mapeo de objetos
- **Lombok**: Para reducir código repetitivo
- **SpringDoc OpenAPI**: Para la documentación de la API
- **Docker**: Para la contenerización de la aplicación

## Estructura del Proyecto
El proyecto está organizado en los siguientes módulos principales:

- **auth**: Gestión de autenticación y autorización
- **catalog**: Gestión de catálogos y datos de referencia
- **collection**: Gestión de colecciones de datos
- **common**: Componentes comunes utilizados en toda la aplicación
- **institution**: Gestión de instituciones
- **user**: Gestión de usuarios
- **userinstitution**: Gestión de la relación entre usuarios e instituciones

## Requisitos Previos
- Java 21
- Maven
- MySQL
- Docker (opcional)

## Configuración y Ejecución

### Configuración de Base de Datos
La aplicación está configurada para conectarse a una base de datos MySQL. Puedes modificar la configuración en el archivo `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/nutrizulia_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=
```

### Ejecución Local
1. Clona el repositorio
2. Navega al directorio del proyecto
3. Ejecuta `./mvnw clean install`
4. Ejecuta `./mvnw spring-boot:run`

La aplicación estará disponible en `http://localhost:8080`

### Ejecución con Docker
1. Construye la imagen Docker: `docker build -t nutrizulia-backend .`
2. Ejecuta el contenedor: `docker run -p 8080:8080 nutrizulia-backend`

## Documentación de la API
La documentación de la API está disponible a través de Swagger UI:
- URL: `http://localhost:8080/swagger-ui.html`
- API Docs: `http://localhost:8080/v3/api-docs`

## Seguridad
La aplicación utiliza JWT para la autenticación. Para acceder a los endpoints protegidos, es necesario incluir el token JWT en el encabezado de autorización:

```
Authorization: Bearer <token>
```

## Endpoints Principales
- `/api/auth`: Endpoints de autenticación
- `/api/catalog`: Endpoints para gestión de catálogos
- `/api/institution`: Endpoints para gestión de instituciones
- `/api/user`: Endpoints para gestión de usuarios

## En desarrollo
Este proyecto aún sigue en desarrollo y forma parte de mi trabajo especial de grado.