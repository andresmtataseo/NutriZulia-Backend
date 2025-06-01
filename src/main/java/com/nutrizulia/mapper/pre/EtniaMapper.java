package com.nutrizulia.mapper.pre;

import com.nutrizulia.model.Etnia;
import com.nutrizulia.dto.pre.EtniaDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EtniaMapper {

    EtniaDto toDto(Etnia etnia);

}
