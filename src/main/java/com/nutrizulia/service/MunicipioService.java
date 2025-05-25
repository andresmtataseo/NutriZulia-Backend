package com.nutrizulia.service;

import com.nutrizulia.model.Municipio;
import com.nutrizulia.repository.MunicipioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

@Service
public class MunicipioService implements IMunicipioService {

    @Autowired
    private MunicipioRepository municipioRepository;

    @Override
    public List<Municipio> getMunicipios(Integer idEstado, Integer idMunicipio, String nombre) {

        boolean isIdMunicipioPresent = idMunicipio != null;
        boolean isNamePresent = StringUtils.hasText(nombre);

        if (isIdMunicipioPresent && isNamePresent) {
            return municipioRepository.findByEstadoIdAndIdAndNombreContainingIgnoreCase(idEstado, idMunicipio, nombre);
        } else if (isIdMunicipioPresent) {
            return municipioRepository.findByEstadoIdAndId(idEstado, idMunicipio)
                    .map(Collections::singletonList)
                    .orElse(Collections.emptyList());
        } else if (isNamePresent) {
            return municipioRepository.findByEstadoIdAndNombreContainingIgnoreCase(idEstado, nombre);
        } else {
            return municipioRepository.findByEstadoId(idEstado);
        }
    }

    @Override
    public Municipio saveMunicipio(Municipio municipio) {
        return municipioRepository.save(municipio);
    }

    @Override
    public void deleteMunicipio(Municipio municipio) {
        municipioRepository.delete(municipio);
    }
}
