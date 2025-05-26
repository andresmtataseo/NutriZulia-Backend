package com.nutrizulia.controller;

import com.nutrizulia.model.Estado;
import com.nutrizulia.service.IEstadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api")
@CrossOrigin(value = "http://localhost:3000")
public class EstadoController {

    @Autowired
    private IEstadoService estadoService;

    @GetMapping("estados")
    public List<Estado> getEstados(Integer idEstado, String nombreEstado) {
        return estadoService.getEstados(idEstado, nombreEstado);
    }

}
