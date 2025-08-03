package com.nutrizulia.features.catalog.mapper;

import com.nutrizulia.features.catalog.dto.EspecialidadDto;
import com.nutrizulia.features.catalog.model.Especialidad;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EspecialidadMapper {

    EspecialidadDto toDto(Especialidad especialidad);

}
