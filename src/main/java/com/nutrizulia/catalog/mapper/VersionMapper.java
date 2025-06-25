package com.nutrizulia.catalog.mapper;

import com.nutrizulia.catalog.dto.VersionDto;
import com.nutrizulia.catalog.model.Version;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VersionMapper {

    @Mapping(source = "nombreTabla", target = "nombre_tabla")
    VersionDto toDto(Version version);

}
