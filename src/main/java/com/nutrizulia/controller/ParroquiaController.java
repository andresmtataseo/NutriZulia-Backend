package com.nutrizulia.controller;

import com.nutrizulia.model.Parroquia;
import com.nutrizulia.service.IParroquiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
@CrossOrigin(value = "http://localhost:3000")
public class ParroquiaController {

    @Autowired
    private IParroquiaService parroquiaService;

    @GetMapping("parroquias")
    public ResponseEntity<List<Parroquia>> getMunicipios(
            @RequestParam(required = true) Integer idMunicipio,
            @RequestParam(required = false) Integer idParroquia,
            @RequestParam(required = false) String nombre) {

        List<Parroquia> parroquias = parroquiaService.getParroquias(idMunicipio, idParroquia, nombre);
        return new ResponseEntity<>(parroquias, HttpStatus.OK);
    }

}
