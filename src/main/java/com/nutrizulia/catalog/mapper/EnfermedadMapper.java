package com.nutrizulia.catalog.mapper;

import com.nutrizulia.catalog.dto.EnfermedadDto;
import com.nutrizulia.catalog.model.Enfermedad;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EnfermedadMapper {

    @Mapping(source = "codigoInternacional", target = "codigo_internacional")
    EnfermedadDto toDto(Enfermedad enfermedad);

}
