package com.nutrizulia.catalog.mapper;

import com.nutrizulia.catalog.dto.ParroquiaDto;
import com.nutrizulia.catalog.model.Parroquia;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ParroquiaMapper {

    @Mapping(source = "municipio.id", target = "municipio_id")
    ParroquiaDto toDto(Parroquia parroquia);

}
