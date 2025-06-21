package com.nutrizulia.catalog.mapper;

import com.nutrizulia.catalog.dto.EspecialidadDto;
import com.nutrizulia.catalog.model.Especialidad;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EspecialidadMapper {

    EspecialidadDto toDto(Especialidad especialidad);

}
