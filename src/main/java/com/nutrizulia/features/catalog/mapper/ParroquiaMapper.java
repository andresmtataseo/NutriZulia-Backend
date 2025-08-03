package com.nutrizulia.features.catalog.mapper;

import com.nutrizulia.features.catalog.dto.ParroquiaDto;
import com.nutrizulia.features.catalog.model.Parroquia;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ParroquiaMapper {

    @Mapping(source = "municipio.id", target = "municipio_id")
    ParroquiaDto toDto(Parroquia parroquia);

}
