package com.nutrizulia.mapper.pre;

import com.nutrizulia.dto.pre.EnfermedadDto;
import com.nutrizulia.model.pre.Enfermedad;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EnfermedadMapper {

    @Mapping(source = "codigoInternacional", target = "codigo_internacional")
    EnfermedadDto toDto(Enfermedad enfermedad);

}
