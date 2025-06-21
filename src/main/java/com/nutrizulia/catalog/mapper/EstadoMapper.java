package com.nutrizulia.catalog.mapper;

import com.nutrizulia.catalog.dto.EstadoDto;
import com.nutrizulia.catalog.model.Estado;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EstadoMapper {

    EstadoDto toDto(Estado estado);

}
