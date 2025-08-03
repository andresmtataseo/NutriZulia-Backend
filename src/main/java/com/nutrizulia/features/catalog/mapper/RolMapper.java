package com.nutrizulia.features.catalog.mapper;

import com.nutrizulia.features.catalog.dto.RolDto;
import com.nutrizulia.features.catalog.model.Rol;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RolMapper {

    RolDto toDto(Rol rol);

}
