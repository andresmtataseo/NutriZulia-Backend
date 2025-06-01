package com.nutrizulia.mapper.pre;

import com.nutrizulia.dto.pre.ParentescoDto;
import com.nutrizulia.model.Parentesco;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ParentescoMapper {

    ParentescoDto toDto(Parentesco parentesco);

}
