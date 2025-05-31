package com.nutrizulia.mapper;

import com.nutrizulia.dto.pre.ParroquiaDto;
import com.nutrizulia.model.Parroquia;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ParroquiaMapper {

    @Mapping(source = "municipio.id", target = "municipio_id")
    ParroquiaDto toDto(Parroquia parroquia);

    Parroquia toEntity(ParroquiaDto parroquiaDto);

}
