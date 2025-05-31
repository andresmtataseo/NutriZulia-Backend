package com.nutrizulia.mapper;

import com.nutrizulia.model.Etnia;
import com.nutrizulia.dto.pre.EtniaDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EtniaMapper {

    EtniaDto toDto(Etnia etnia);

    Etnia toEntity(EtniaDto etniaDto);
}
