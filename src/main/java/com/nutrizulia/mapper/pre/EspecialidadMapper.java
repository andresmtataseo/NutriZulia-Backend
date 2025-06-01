package com.nutrizulia.mapper.pre;

import com.nutrizulia.dto.pre.EspecialidadDto;
import com.nutrizulia.model.Especialidad;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EspecialidadMapper {

    EspecialidadDto toDto(Especialidad especialidad);

}
