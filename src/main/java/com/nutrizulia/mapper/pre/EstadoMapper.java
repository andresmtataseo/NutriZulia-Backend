package com.nutrizulia.mapper.pre;

import com.nutrizulia.dto.pre.EstadoDto;
import com.nutrizulia.model.Estado;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EstadoMapper {

    EstadoDto toDto(Estado estado);

}
