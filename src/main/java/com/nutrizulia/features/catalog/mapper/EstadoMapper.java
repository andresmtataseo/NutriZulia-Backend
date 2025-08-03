package com.nutrizulia.features.catalog.mapper;

import com.nutrizulia.features.catalog.dto.EstadoDto;
import com.nutrizulia.features.catalog.model.Estado;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EstadoMapper {

    EstadoDto toDto(Estado estado);

}
