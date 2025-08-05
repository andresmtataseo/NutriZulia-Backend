package com.nutrizulia.features.collection.mapper;

import com.nutrizulia.features.collection.dto.ConsultaDto;
import com.nutrizulia.features.collection.model.Consulta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ConsultaMapper {
    
    @Mapping(source = "usuarioInstitucion.id", target = "usuario_institucion_id")
    @Mapping(source = "paciente.id", target = "paciente_id")
    @Mapping(source = "tipoActividad.id", target = "tipo_actividad_id")
    @Mapping(source = "especialidad.id", target = "especialidad_remitente_id")
    @Mapping(source = "tipoConsulta", target = "tipo_consulta")
    @Mapping(source = "motivoConsulta", target = "motivo_consulta")
    @Mapping(source = "fechaHoraProgramada", target = "fecha_hora_programada")
    @Mapping(source = "fechaHoraReal", target = "fecha_hora_real")
    @Mapping(source = "observaciones", target = "observaciones")
    @Mapping(source = "planes", target = "planes")
    @Mapping(source = "estado", target = "estado")
    @Mapping(source = "createdAt", target = "created_at")
    @Mapping(source = "updatedAt", target = "updated_at")
    @Mapping(source = "isDeleted", target = "is_deleted")
    ConsultaDto toDto(Consulta consulta);
    
    @Mapping(source = "usuario_institucion_id", target = "usuarioInstitucion.id")
    @Mapping(source = "paciente_id", target = "paciente.id")
    @Mapping(source = "tipo_actividad_id", target = "tipoActividad.id")
    @Mapping(source = "especialidad_remitente_id", target = "especialidad.id")
    @Mapping(source = "tipo_consulta", target = "tipoConsulta")
    @Mapping(source = "motivo_consulta", target = "motivoConsulta")
    @Mapping(source = "fecha_hora_programada", target = "fechaHoraProgramada")
    @Mapping(source = "fecha_hora_real", target = "fechaHoraReal")
    @Mapping(source = "observaciones", target = "observaciones")
    @Mapping(source = "planes", target = "planes")
    @Mapping(source = "estado", target = "estado")
    @Mapping(source = "created_at", target = "createdAt")
    @Mapping(source = "updated_at", target = "updatedAt")
    @Mapping(source = "is_deleted", target = "isDeleted")
    Consulta toEntity(ConsultaDto consultaDto);
}