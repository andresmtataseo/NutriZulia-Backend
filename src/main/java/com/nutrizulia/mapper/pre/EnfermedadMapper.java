package com.nutrizulia.mapper.pre;

import com.nutrizulia.dto.pre.EnfermedadDto;
import com.nutrizulia.model.Enfermedad;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EnfermedadMapper {

    EnfermedadDto toDto(Enfermedad enfermedad);

}
