package com.nutrizulia.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioInstitucionDto {

    private Integer institucionId;
    private String institucionNombre;
    private Integer rolId;
    private String rolNombre;

}