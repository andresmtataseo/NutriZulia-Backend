package com.nutrizulia.features.catalog.mapper;

import com.nutrizulia.features.catalog.dto.ParentescoDto;
import com.nutrizulia.features.catalog.model.Parentesco;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ParentescoMapper {

    ParentescoDto toDto(Parentesco parentesco);

}
