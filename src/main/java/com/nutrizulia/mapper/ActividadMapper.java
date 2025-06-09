package com.nutrizulia.mapper;

import com.nutrizulia.dto.ActividadDto;
import com.nutrizulia.model.Actividad;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ActividadMapper {

    @Mapping(source = "usuarioInstitucion.id", target = "usuario_institucion_id")
    @Mapping(source = "tipoActividad.id", target = "tipo_actividad_id")
    @Mapping(source = "descripcionGeneral", target = "descripcion_general")
    @Mapping(source = "cantidadParticipantes", target = "cantidad_participantes")
    @Mapping(source = "cantidadSesionesRealizadas", target = "cantidad_sesiones_realizadas")
    @Mapping(source = "duracionMinutos", target = "duracion_minutos")
    @Mapping(source = "temaPrincipal", target = "tema_principal")
    @Mapping(source = "programasImplementados", target = "programas_implementados")
    @Mapping(source = "urlEvidencia", target = "url_evidencia")
    ActividadDto toDto(Actividad actividad);

    @Mapping(source = "id", target = "id", ignore = true) // Generalmente el ID se genera en la DB
    @Mapping(target = "usuarioInstitucion", ignore = true) // Debe ser cargado por el servicio
    @Mapping(target = "tipoActividad", ignore = true) // Debe ser cargado por el servicio
    @Mapping(source = "descripcion_general", target = "descripcionGeneral")
    @Mapping(source = "cantidad_participantes", target = "cantidadParticipantes")
    @Mapping(source = "cantidad_sesiones_realizadas", target = "cantidadSesionesRealizadas")
    @Mapping(source = "duracion_minutos", target = "duracionMinutos")
    @Mapping(source = "tema_principal", target = "temaPrincipal")
    @Mapping(source = "programas_implementados", target = "programasImplementados")
    @Mapping(source = "url_evidencia", target = "urlEvidencia")
    Actividad toEntity(ActividadDto actividadDto);

}