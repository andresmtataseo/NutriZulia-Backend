package com.nutrizulia.mapper.pre;

import com.nutrizulia.dto.pre.RolDto;
import com.nutrizulia.model.pre.Rol;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RolMapper {

    RolDto toDto(Rol rol);

}
