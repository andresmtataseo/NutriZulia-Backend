package com.nutrizulia.catalog.mapper;

import com.nutrizulia.catalog.dto.RolDto;
import com.nutrizulia.catalog.model.Rol;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RolMapper {

    RolDto toDto(Rol rol);

}
