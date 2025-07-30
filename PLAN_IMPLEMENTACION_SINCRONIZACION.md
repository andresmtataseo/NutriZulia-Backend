# 📋 Plan de Implementación del Sistema de Sincronización - NutriZulia Backend

## 🎯 Objetivo General
Implementar un sistema de sincronización bidireccional completo que permita a la aplicación móvil Android sincronizar datos de manera eficiente y confiable con el servidor backend.

## 🔍 Análisis del Estado Actual

### ✅ Fortalezas Identificadas
- **Arquitectura Spring Boot 3.5.0** con Java 21 establecida
- **Autenticación JWT** completamente implementada y funcional
- **Base de datos PostgreSQL** configurada
- **Entidades JPA** bien estructuradas con relaciones definidas
- **Campo `updated_at`** ya presente en las entidades principales
- **MapStruct** configurado para mapeo de DTOs
- **Swagger/OpenAPI** para documentación de API

### ⚠️ Gaps Identificados
- **Falta campo `created_at`** en las entidades
- **No existen endpoints de sincronización**
- **Falta lógica de UPSERT** para sincronización bidireccional
- **No hay DTOs específicos** para operaciones de sincronización
- **Faltan índices de base de datos** para optimización de consultas
- **No hay triggers** para actualización automática de timestamps
- **Falta sistema de rate limiting** para endpoints de sincronización

---

## 🚀 Plan de Implementación por Fases

### **FASE 1: Preparación de Base de Datos y Entidades** ⏱️ (2-3 días)

#### 1.1 Creación de Clase Base para Entidades
**Objetivo**: Centralizar campos comunes de auditoría

**Archivos a crear:**
- `src/main/java/com/nutrizulia/common/entity/BaseEntity.java`

**Funcionalidades:**
```java
@MappedSuperclass
public abstract class BaseEntity {
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
```

#### 1.2 Modificación del Esquema de Entidades
**Archivos a modificar:**
- `src/main/java/com/nutrizulia/collection/model/Paciente.java`
- `src/main/java/com/nutrizulia/collection/model/Representante.java`
- `src/main/java/com/nutrizulia/collection/model/Consulta.java`
- `src/main/java/com/nutrizulia/collection/model/DetalleAntropometrico.java`
- `src/main/java/com/nutrizulia/collection/model/DetalleVital.java`
- `src/main/java/com/nutrizulia/collection/model/DetalleMetabolico.java`
- `src/main/java/com/nutrizulia/collection/model/DetallePedriatrico.java`
- `src/main/java/com/nutrizulia/collection/model/DetalleObstetricia.java`
- `src/main/java/com/nutrizulia/collection/model/EvaluacionAntropometrica.java`
- `src/main/java/com/nutrizulia/collection/model/Diagnostico.java`
- `src/main/java/com/nutrizulia/collection/model/PacienteRepresentante.java`
- `src/main/java/com/nutrizulia/collection/model/Actividad.java`

**Tareas:**
1. ✅ Extender de `BaseEntity` todas las entidades principales
2. ✅ Remover campos `updatedAt` duplicados
3. ✅ Agregar anotaciones `@PrePersist` y `@PreUpdate` si es necesario
4. ✅ Validar que todas las relaciones mantengan consistencia

#### 1.3 Scripts de Migración de Base de Datos
**Archivos a crear:**
- `src/main/resources/db/migration/V2__add_created_at_fields.sql`
- `src/main/resources/db/migration/V3__add_sync_indexes.sql`
- `src/main/resources/db/migration/V4__add_sync_triggers.sql`

**Contenido de migraciones:**
```sql
-- V2: Agregar campos created_at
ALTER TABLE pacientes ADD COLUMN IF NOT EXISTS created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE representantes ADD COLUMN IF NOT EXISTS created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
-- ... para todas las tablas

-- V3: Crear índices de rendimiento
CREATE INDEX IF NOT EXISTS idx_pacientes_updated_at ON pacientes(updated_at);
CREATE INDEX IF NOT EXISTS idx_pacientes_user_updated ON pacientes(usuario_institucion_id, updated_at);
-- ... para todas las tablas

-- V4: Triggers de actualización automática
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';
```

#### 1.4 Configuración de Flyway
**Archivo a modificar:**
- `src/main/resources/application.properties` o `application.yml`

**Configuración:**
```properties
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
spring.flyway.baseline-on-migrate=true
```

---

### **FASE 2: DTOs y Mappers de Sincronización** ⏱️ (1-2 días)

#### 2.1 DTOs Principales de Sincronización
**Archivos a crear:**
- `src/main/java/com/nutrizulia/sync/dto/SyncPushRequestDto.java`
- `src/main/java/com/nutrizulia/sync/dto/SyncPullResponseDto.java`
- `src/main/java/com/nutrizulia/sync/dto/SyncResultDto.java`
- `src/main/java/com/nutrizulia/sync/dto/SyncStatsDto.java`

**Estructura del SyncPushRequestDto:**
```java
public class SyncPushRequestDto {
    private List<PacienteSyncDto> pacientes;
    private List<RepresentanteSyncDto> representantes;
    private List<ConsultaSyncDto> consultas;
    private List<DetalleAntropometricoSyncDto> detallesAntropometricos;
    // ... más entidades
}
```

#### 2.2 DTOs Específicos por Entidad
**Archivos a crear:**
- `src/main/java/com/nutrizulia/sync/dto/entity/PacienteSyncDto.java`
- `src/main/java/com/nutrizulia/sync/dto/entity/RepresentanteSyncDto.java`
- `src/main/java/com/nutrizulia/sync/dto/entity/ConsultaSyncDto.java`
- `src/main/java/com/nutrizulia/sync/dto/entity/DetalleAntropometricoSyncDto.java`
- `src/main/java/com/nutrizulia/sync/dto/entity/DetalleVitalSyncDto.java`
- `src/main/java/com/nutrizulia/sync/dto/entity/DetalleMetabolicoSyncDto.java`
- `src/main/java/com/nutrizulia/sync/dto/entity/DetallePedriatricoSyncDto.java`
- `src/main/java/com/nutrizulia/sync/dto/entity/DetalleObstetriciaSyncDto.java`
- `src/main/java/com/nutrizulia/sync/dto/entity/EvaluacionAntropometricaSyncDto.java`
- `src/main/java/com/nutrizulia/sync/dto/entity/DiagnosticoSyncDto.java`
- `src/main/java/com/nutrizulia/sync/dto/entity/PacienteRepresentanteSyncDto.java`
- `src/main/java/com/nutrizulia/sync/dto/entity/ActividadSyncDto.java`

#### 2.3 Mappers de Sincronización
**Archivos a crear:**
- `src/main/java/com/nutrizulia/sync/mapper/SyncMapper.java`
- `src/main/java/com/nutrizulia/sync/mapper/PacienteSyncMapper.java`
- `src/main/java/com/nutrizulia/sync/mapper/ConsultaSyncMapper.java`
- Mappers específicos para cada entidad

**Ejemplo de mapper:**
```java
@Mapper(componentModel = "spring")
public interface PacienteSyncMapper {
    PacienteSyncDto toSyncDto(Paciente paciente);
    Paciente toEntity(PacienteSyncDto dto);
    List<PacienteSyncDto> toSyncDtoList(List<Paciente> pacientes);
}
```

---

### **FASE 3: Servicios de Sincronización** ⏱️ (3-4 días)

#### 3.1 Interfaces de Servicio
**Archivos a crear:**
- `src/main/java/com/nutrizulia/sync/service/ISyncService.java`
- `src/main/java/com/nutrizulia/sync/service/ISyncEntityService.java`

**Interface principal:**
```java
public interface ISyncService {
    SyncResultDto pushChanges(SyncPushRequestDto request, UUID usuarioInstitucionId);
    SyncPullResponseDto pullChanges(LocalDateTime since, UUID usuarioInstitucionId);
    void validateSyncData(SyncPushRequestDto request);
    SyncStatsDto getSyncStats(UUID usuarioInstitucionId);
}
```

#### 3.2 Servicio Principal de Sincronización
**Archivo a crear:**
- `src/main/java/com/nutrizulia/sync/service/SyncService.java`

**Funcionalidades principales:**
- Coordinación de operaciones PUSH/PULL
- Manejo de transacciones atómicas
- Validación de datos de entrada
- Generación de estadísticas de sincronización
- Manejo de errores y rollback

#### 3.3 Servicios Específicos por Entidad
**Archivos a crear:**
- `src/main/java/com/nutrizulia/sync/service/entity/PacienteSyncService.java`
- `src/main/java/com/nutrizulia/sync/service/entity/RepresentanteSyncService.java`
- `src/main/java/com/nutrizulia/sync/service/entity/ConsultaSyncService.java`
- Servicios para cada entidad con lógica UPSERT específica

**Funcionalidades por servicio:**
```java
public interface ISyncEntityService<T, D> {
    List<T> upsertEntities(List<D> dtos, UUID usuarioInstitucionId);
    List<T> findModifiedSince(LocalDateTime since, UUID usuarioInstitucionId);
    void validateEntityData(List<D> dtos);
}
```

#### 3.4 Repositorios Extendidos
**Archivos a modificar:**
- Todos los repositorios en `src/main/java/com/nutrizulia/collection/repository/`

**Métodos a agregar:**
```java
@Query("SELECT e FROM EntityName e WHERE e.usuarioInstitucion.id = :usuarioInstitucionId AND e.updatedAt > :since ORDER BY e.updatedAt ASC")
List<EntityName> findModifiedSince(@Param("usuarioInstitucionId") UUID usuarioInstitucionId, 
                                  @Param("since") LocalDateTime since);

@Modifying
@Query("INSERT INTO EntityName (...) VALUES (...) ON CONFLICT (id) DO UPDATE SET ...")
void upsertEntity(@Param("entity") EntityName entity);
```

---

### **FASE 4: Controladores de Sincronización** ⏱️ (1-2 días)

#### 4.1 Controlador Principal
**Archivo a crear:**
- `src/main/java/com/nutrizulia/sync/controller/SyncController.java`

**Endpoints a implementar:**
```java
@RestController
@RequestMapping("/api/sync")
@Validated
@SecurityRequirement(name = "bearerAuth")
public class SyncController {
    
    @PostMapping("/push")
    @Operation(summary = "Enviar cambios al servidor")
    public ResponseEntity<SyncResultDto> pushChanges(
        @Valid @RequestBody SyncPushRequestDto request,
        Authentication authentication
    );
    
    @GetMapping("/pull")
    @Operation(summary = "Obtener cambios del servidor")
    public ResponseEntity<SyncPullResponseDto> pullChanges(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime since,
        @RequestParam(defaultValue = "1000") @Min(1) @Max(5000) int limit,
        Authentication authentication
    );
    
    @GetMapping("/stats")
    @Operation(summary = "Obtener estadísticas de sincronización")
    public ResponseEntity<SyncStatsDto> getSyncStats(Authentication authentication);
}
```

#### 4.2 Manejo de Errores Específicos
**Archivo a crear:**
- `src/main/java/com/nutrizulia/sync/exception/SyncExceptionHandler.java`

**Excepciones a manejar:**
- `SyncValidationException`
- `SyncConflictException`
- `SyncTimeoutException`
- `SyncDataIntegrityException`

---

### **FASE 5: Seguridad y Validaciones** ⏱️ (1-2 días)

#### 5.1 Configuración de Seguridad
**Archivo a modificar:**
- `src/main/java/com/nutrizulia/common/config/SecurityConfig.java`

**Modificaciones:**
```java
.requestMatchers("/api/sync/**").authenticated()
.requestMatchers(HttpMethod.POST, "/api/sync/push").hasRole("SYNC_WRITE")
.requestMatchers(HttpMethod.GET, "/api/sync/pull").hasRole("SYNC_READ")
```

#### 5.2 Rate Limiting
**Archivo a crear:**
- `src/main/java/com/nutrizulia/sync/config/RateLimitConfig.java`
- `src/main/java/com/nutrizulia/sync/aspect/RateLimitAspect.java`

**Configuración:**
- 10 requests por minuto para endpoints de sincronización
- Límites diferenciados por tipo de operación (PUSH vs PULL)
- Bloqueo temporal por abuso

#### 5.3 Validadores Personalizados
**Archivos a crear:**
- `src/main/java/com/nutrizulia/sync/validator/SyncDataValidator.java`
- `src/main/java/com/nutrizulia/sync/validator/TimestampValidator.java`
- `src/main/java/com/nutrizulia/sync/validator/UUIDValidator.java`
- `src/main/java/com/nutrizulia/sync/validator/RelationshipValidator.java`

**Validaciones a implementar:**
- Formato de UUIDs
- Consistencia de timestamps
- Integridad referencial
- Límites de tamaño de payload
- Validación de permisos por institución

---

### **FASE 6: Optimizaciones y Monitoreo** ⏱️ (2-3 días)

#### 6.1 Configuración de Performance
**Archivos a crear:**
- `src/main/java/com/nutrizulia/sync/config/SyncConfig.java`
- `src/main/java/com/nutrizulia/sync/config/CacheConfig.java`

**Optimizaciones:**
```java
@Configuration
public class SyncConfig {
    @Bean
    @ConfigurationProperties("sync.performance")
    public SyncPerformanceProperties syncProperties() {
        return new SyncPerformanceProperties();
    }
    
    @Bean
    public TaskExecutor syncTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        return executor;
    }
}
```

#### 6.2 Sistema de Cache
**Implementación:**
- Cache de consultas frecuentes con Redis/Caffeine
- Cache de metadatos de sincronización
- Invalidación inteligente de cache

#### 6.3 Logging y Métricas
**Archivos a crear:**
- `src/main/java/com/nutrizulia/sync/aspect/SyncLoggingAspect.java`
- `src/main/java/com/nutrizulia/sync/metrics/SyncMetrics.java`

**Métricas a capturar:**
- Tiempo de respuesta por endpoint
- Volumen de datos sincronizados
- Frecuencia de sincronización por usuario
- Tasa de errores y tipos de error
- Uso de recursos (CPU, memoria, DB connections)

#### 6.4 Health Checks
**Archivo a crear:**
- `src/main/java/com/nutrizulia/sync/health/SyncHealthIndicator.java`

**Verificaciones:**
- Conectividad a base de datos
- Estado de servicios de sincronización
- Latencia de operaciones críticas

---

### **FASE 7: Testing y Documentación** ⏱️ (2-3 días)

#### 7.1 Tests Unitarios
**Archivos a crear:**
- `src/test/java/com/nutrizulia/sync/service/SyncServiceTest.java`
- `src/test/java/com/nutrizulia/sync/service/PacienteSyncServiceTest.java`
- `src/test/java/com/nutrizulia/sync/controller/SyncControllerTest.java`
- `src/test/java/com/nutrizulia/sync/mapper/SyncMapperTest.java`
- `src/test/java/com/nutrizulia/sync/validator/SyncValidatorTest.java`

**Cobertura objetivo:** >80%

#### 7.2 Tests de Integración
**Archivos a crear:**
- `src/test/java/com/nutrizulia/sync/integration/SyncIntegrationTest.java`
- `src/test/java/com/nutrizulia/sync/integration/SyncPerformanceTest.java`
- `src/test/java/com/nutrizulia/sync/integration/SyncSecurityTest.java`

**Escenarios a probar:**
- Sincronización completa end-to-end
- Manejo de conflictos de datos
- Comportamiento bajo carga
- Seguridad y autenticación
- Recuperación ante fallos

#### 7.3 Tests de Carga
**Herramientas:** JMeter o Gatling
**Escenarios:**
- 100 usuarios concurrentes sincronizando
- Payloads de diferentes tamaños
- Sincronización de 10,000+ registros

#### 7.4 Documentación
**Archivos a crear:**
- `docs/SYNC_API_GUIDE.md` - Guía de uso de la API
- `docs/SYNC_TROUBLESHOOTING.md` - Guía de resolución de problemas
- `docs/SYNC_PERFORMANCE_TUNING.md` - Guía de optimización
- `docs/SYNC_DEPLOYMENT.md` - Guía de despliegue

**Documentación Swagger:**
- Ejemplos de requests/responses
- Códigos de error detallados
- Guías de autenticación

---

## 🛠️ Estructura Final de Archivos

```
src/main/java/com/nutrizulia/
├── sync/
│   ├── controller/
│   │   └── SyncController.java
│   ├── dto/
│   │   ├── SyncPushRequestDto.java
│   │   ├── SyncPullResponseDto.java
│   │   ├── SyncResultDto.java
│   │   ├── SyncStatsDto.java
│   │   └── entity/
│   │       ├── PacienteSyncDto.java
│   │       ├── RepresentanteSyncDto.java
│   │       ├── ConsultaSyncDto.java
│   │       └── ... (más DTOs por entidad)
│   ├── mapper/
│   │   ├── SyncMapper.java
│   │   ├── PacienteSyncMapper.java
│   │   └── ... (más mappers)
│   ├── service/
│   │   ├── ISyncService.java
│   │   ├── SyncService.java
│   │   ├── ISyncEntityService.java
│   │   └── entity/
│   │       ├── PacienteSyncService.java
│   │       ├── RepresentanteSyncService.java
│   │       └── ... (más servicios)
│   ├── validator/
│   │   ├── SyncDataValidator.java
│   │   ├── TimestampValidator.java
│   │   ├── UUIDValidator.java
│   │   └── RelationshipValidator.java
│   ├── config/
│   │   ├── SyncConfig.java
│   │   ├── RateLimitConfig.java
│   │   └── CacheConfig.java
│   ├── aspect/
│   │   ├── SyncLoggingAspect.java
│   │   └── RateLimitAspect.java
│   ├── exception/
│   │   ├── SyncException.java
│   │   ├── SyncValidationException.java
│   │   ├── SyncConflictException.java
│   │   └── SyncExceptionHandler.java
│   ├── metrics/
│   │   └── SyncMetrics.java
│   └── health/
│       └── SyncHealthIndicator.java
├── common/
│   ├── entity/
│   │   └── BaseEntity.java
│   └── config/
│       └── SecurityConfig.java (modificado)
└── collection/
    ├── model/ (entidades modificadas)
    └── repository/ (repositorios extendidos)

src/main/resources/
├── db/migration/
│   ├── V2__add_created_at_fields.sql
│   ├── V3__add_sync_indexes.sql
│   └── V4__add_sync_triggers.sql
└── application.yml (configuración actualizada)

docs/
├── SYNC_API_GUIDE.md
├── SYNC_TROUBLESHOOTING.md
├── SYNC_PERFORMANCE_TUNING.md
└── SYNC_DEPLOYMENT.md
```

---

## 📊 Dependencias Adicionales Requeridas

### Agregar al `pom.xml`:
```xml
<!-- Rate Limiting -->
<dependency>
    <groupId>com.github.vladimir-bukhtoyarov</groupId>
    <artifactId>bucket4j-core</artifactId>
    <version>7.6.0</version>
</dependency>

<!-- Métricas y Monitoreo -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>

<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>

<!-- Migraciones de Base de Datos -->
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
</dependency>

<!-- Cache -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>

<dependency>
    <groupId>com.github.ben-manes.caffeine</groupId>
    <artifactId>caffeine</artifactId>
</dependency>

<!-- Testing -->
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>postgresql</artifactId>
    <scope>test</scope>
</dependency>

<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>junit-jupiter</artifactId>
    <scope>test</scope>
</dependency>
```

---

## ⚡ Consideraciones Técnicas Críticas

### 1. **Estrategia de UPSERT**
```sql
INSERT INTO pacientes (id, nombres, apellidos, ..., updated_at) 
VALUES (?, ?, ?, ..., ?) 
ON CONFLICT (id) 
DO UPDATE SET 
    nombres = EXCLUDED.nombres,
    apellidos = EXCLUDED.apellidos,
    updated_at = EXCLUDED.updated_at
WHERE pacientes.updated_at < EXCLUDED.updated_at;
```

### 2. **Manejo de Transacciones**
- Usar `@Transactional(propagation = Propagation.REQUIRED)`
- Implementar savepoints para rollback parcial
- Timeout de 30 segundos para operaciones de sincronización

### 3. **Optimización de Consultas**
- Índices compuestos: `(usuario_institucion_id, updated_at)`
- Paginación con cursor-based pagination
- Uso de `@EntityGraph` para cargar relaciones eficientemente

### 4. **Seguridad**
- Validación de ownership: datos pertenecen al usuario autenticado
- Sanitización de inputs con OWASP Java Encoder
- Rate limiting: 10 requests/minuto por usuario
- Logging de todas las operaciones de sincronización

### 5. **Manejo de Errores**
- Retry automático para errores transitorios
- Circuit breaker para proteger la base de datos
- Fallback a modo offline en caso de fallos críticos

---

## 🎯 Criterios de Aceptación

### Funcionales
- ✅ Endpoint PUSH procesa correctamente datos del cliente móvil
- ✅ Endpoint PULL retorna cambios incrementales desde timestamp
- ✅ Resolución de conflictos basada en timestamp del servidor
- ✅ Manejo de relaciones entre entidades (foreign keys)
- ✅ Validación de integridad de datos
- ✅ Autenticación JWT integrada y funcional

### No Funcionales
- ✅ Tiempo de respuesta < 2 segundos para 1000 registros
- ✅ Throughput > 100 requests/segundo
- ✅ Cobertura de tests > 80%
- ✅ Zero downtime durante deployment
- ✅ Logs estructurados para monitoreo
- ✅ Métricas de performance disponibles
- ✅ Documentación completa de API

### Seguridad
- ✅ Rate limiting implementado y funcional
- ✅ Validación de permisos por institución
- ✅ Sanitización de inputs
- ✅ Logging de operaciones sensibles
- ✅ Manejo seguro de errores (sin exposición de datos)

---

## 📅 Timeline Detallado

| Fase | Duración | Recursos | Dependencias | Entregables |
|------|----------|----------|--------------|-------------|
| **Fase 1** | 2-3 días | 1 Dev Backend | - | BaseEntity, Migraciones DB, Entidades actualizadas |
| **Fase 2** | 1-2 días | 1 Dev Backend | Fase 1 | DTOs de sincronización, Mappers |
| **Fase 3** | 3-4 días | 1-2 Dev Backend | Fase 2 | Servicios de sincronización, Lógica UPSERT |
| **Fase 4** | 1-2 días | 1 Dev Backend | Fase 3 | Controladores REST, Endpoints funcionales |
| **Fase 5** | 1-2 días | 1 Dev Backend + 1 DevOps | Fase 4 | Seguridad, Rate limiting, Validaciones |
| **Fase 6** | 2-3 días | 1 Dev Backend + 1 DevOps | Fase 5 | Optimizaciones, Métricas, Monitoreo |
| **Fase 7** | 2-3 días | 1 Dev Backend + 1 QA | Fase 6 | Tests, Documentación, Validación final |

**Total estimado: 12-19 días de desarrollo**

---

## 🚦 Hitos y Checkpoints

### Checkpoint 1 (Final Fase 3)
- ✅ Servicios de sincronización funcionando
- ✅ Tests unitarios básicos pasando
- ✅ Validación con datos de prueba

### Checkpoint 2 (Final Fase 5)
- ✅ Endpoints REST funcionales
- ✅ Seguridad implementada
- ✅ Tests de integración básicos

### Checkpoint 3 (Final Fase 7)
- ✅ Sistema completo funcional
- ✅ Performance validada
- ✅ Documentación completa
- ✅ Ready for production

---

## 📞 Contacto y Soporte

Para dudas o consultas durante la implementación:
- **Arquitecto de Software**: [Nombre]
- **Lead Backend**: [Nombre]
- **DevOps**: [Nombre]
- **QA Lead**: [Nombre]

---

*Documento creado el: [Fecha]*  
*Última actualización: [Fecha]*  
*Versión: 1.0*