package com.nutrizulia.catalog.mapper;

import com.nutrizulia.catalog.dto.MunicipioSanitarioDto;
import com.nutrizulia.catalog.model.MunicipioSanitario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MunicipioSanitarioMapper {

    @Mapping(source = "estado.id", target = "estado_id")
    MunicipioSanitarioDto toDto(MunicipioSanitario municipioSanitario);

}
