package com.nutrizulia.features.catalog.mapper;

import com.nutrizulia.features.catalog.dto.EnfermedadDto;
import com.nutrizulia.features.catalog.model.Enfermedad;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EnfermedadMapper {

    @Mapping(source = "codigoInternacional", target = "codigo_internacional")
    EnfermedadDto toDto(Enfermedad enfermedad);

}
