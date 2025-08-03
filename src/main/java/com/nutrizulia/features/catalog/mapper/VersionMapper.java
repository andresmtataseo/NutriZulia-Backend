package com.nutrizulia.features.catalog.mapper;

import com.nutrizulia.features.catalog.dto.VersionDto;
import com.nutrizulia.features.catalog.model.Version;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VersionMapper {

    @Mapping(source = "nombreTabla", target = "nombre_tabla")
    VersionDto toDto(Version version);

}
