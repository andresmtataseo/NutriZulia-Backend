package com.nutrizulia.controller;

import com.nutrizulia.model.Municipio;
import com.nutrizulia.service.IMunicipioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
@CrossOrigin(value = "http://localhost:3000")
public class MunicipioController {

    @Autowired
    private IMunicipioService municipioService;

    @GetMapping("municipios")
    public ResponseEntity<List<Municipio>> getMunicipios(
            @RequestParam(required = true) Integer idEstado,
            @RequestParam(required = false) Integer idMunicipio,
            @RequestParam(required = false) String nombre) {

        List<Municipio> municipios = municipioService.getMunicipios(idEstado, idMunicipio, nombre);
        return new ResponseEntity<>(municipios, HttpStatus.OK);
    }

}
