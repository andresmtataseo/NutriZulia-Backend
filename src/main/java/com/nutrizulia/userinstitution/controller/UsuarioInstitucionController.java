package com.nutrizulia.userinstitution.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.nutrizulia.common.util.ApiConstants.USER_INSTITUTION_API_BASE_URL;

@RestController
@RequiredArgsConstructor
@RequestMapping(USER_INSTITUTION_API_BASE_URL)
@Tag(
        name = "Gestión Usuario-Institución",
        description = "Gestión de asignaciones entre usuarios del sistema e instituciones registradas."
)
public class UsuarioInstitucionController {
}
