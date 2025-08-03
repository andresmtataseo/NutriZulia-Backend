package com.nutrizulia.features.institution.mapper;

import com.nutrizulia.features.institution.dto.InstitucionDto;
import com.nutrizulia.features.institution.model.Institucion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InstitucionMapper {

    @Mapping(source = "tipoInstitucion.id", target = "tipo_institucion_id")
    @Mapping(source = "municipioSanitario.id", target = "municipio_sanitario_id")
    InstitucionDto toDto(Institucion institucion);

}
