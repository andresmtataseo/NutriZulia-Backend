package com.nutrizulia.features.catalog.mapper;

import com.nutrizulia.features.catalog.dto.InstitucionDto;
import com.nutrizulia.features.catalog.model.Institucion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InstitucionMapper {

    @Mapping(source = "tipoInstitucion.id", target = "tipo_institucion_id")
    @Mapping(source = "municipioSanitario.id", target = "municipio_sanitario_id")
    InstitucionDto toDto(Institucion institucion);

}
