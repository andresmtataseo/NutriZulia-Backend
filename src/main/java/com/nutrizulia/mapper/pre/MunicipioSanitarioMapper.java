package com.nutrizulia.mapper.pre;

import com.nutrizulia.dto.pre.MunicipioSanitarioDto;
import com.nutrizulia.model.pre.MunicipioSanitario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MunicipioSanitarioMapper {

    @Mapping(source = "estado.id", target = "estado_id")
    MunicipioSanitarioDto toDto(MunicipioSanitario municipioSanitario);

}
