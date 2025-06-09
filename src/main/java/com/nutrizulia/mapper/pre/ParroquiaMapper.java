package com.nutrizulia.mapper.pre;

import com.nutrizulia.dto.pre.ParroquiaDto;
import com.nutrizulia.model.pre.Parroquia;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ParroquiaMapper {

    @Mapping(source = "municipio.id", target = "municipio_id")
    ParroquiaDto toDto(Parroquia parroquia);

}
