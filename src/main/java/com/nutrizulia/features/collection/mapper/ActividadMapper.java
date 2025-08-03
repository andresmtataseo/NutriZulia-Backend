package com.nutrizulia.features.collection.mapper;

import com.nutrizulia.features.collection.dto.ActividadDto;
import com.nutrizulia.features.collection.model.Actividad;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ActividadMapper {

    @Mapping(source = "usuarioInstitucion.id", target = "usuario_institucion_id")
    @Mapping(source = "tipoActividad.id", target = "tipo_actividad_id")
    @Mapping(source = "descripcionGeneral", target = "descripcion_general")
    @Mapping(source = "cantidadParticipantes", target = "cantidad_participantes")
    @Mapping(source = "cantidadSesiones", target = "cantidad_sesiones")
    @Mapping(source = "duracionMinutos", target = "duracion_minutos")
    @Mapping(source = "temaPrincipal", target = "tema_principal")
    @Mapping(source = "programasImplementados", target = "programas_implementados")
    @Mapping(source = "urlEvidencia", target = "url_evidencia")
    @Mapping(source = "fecha", target = "fecha")
    @Mapping(source = "direccion", target = "direccion")
    @Mapping(source = "createdAt", target = "created_at")
    @Mapping(source = "updatedAt", target = "updated_at")
    @Mapping(source = "isDeleted", target = "is_deleted")
    ActividadDto toDto(Actividad actividad);

    @Mapping(source = "usuario_institucion_id", target = "usuarioInstitucion.id")
    @Mapping(source = "tipo_actividad_id", target = "tipoActividad.id")
    @Mapping(source = "descripcion_general", target = "descripcionGeneral")
    @Mapping(source = "cantidad_participantes", target = "cantidadParticipantes")
    @Mapping(source = "cantidad_sesiones", target = "cantidadSesiones")
    @Mapping(source = "duracion_minutos", target = "duracionMinutos")
    @Mapping(source = "tema_principal", target = "temaPrincipal")
    @Mapping(source = "programas_implementados", target = "programasImplementados")
    @Mapping(source = "url_evidencia", target = "urlEvidencia")
    @Mapping(source = "fecha", target = "fecha")
    @Mapping(source = "direccion", target = "direccion")
    @Mapping(source = "created_at", target = "createdAt")
    @Mapping(source = "updated_at", target = "updatedAt")
    @Mapping(source = "is_deleted", target = "isDeleted")
    Actividad toEntity(ActividadDto actividadDto);

}