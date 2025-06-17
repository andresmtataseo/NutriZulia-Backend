package com.nutrizulia.mapper.pre;

import com.nutrizulia.dto.pre.TipoIndicadorDto;
import com.nutrizulia.model.pre.TipoIndicador;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TipoIndicadorMapper {

    TipoIndicadorDto toDto(TipoIndicador tipoIndicador);

}
