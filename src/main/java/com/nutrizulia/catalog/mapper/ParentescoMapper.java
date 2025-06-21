package com.nutrizulia.catalog.mapper;

import com.nutrizulia.catalog.dto.ParentescoDto;
import com.nutrizulia.catalog.model.Parentesco;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ParentescoMapper {

    ParentescoDto toDto(Parentesco parentesco);

}
