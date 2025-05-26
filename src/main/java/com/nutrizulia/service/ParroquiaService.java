package com.nutrizulia.service;

import com.nutrizulia.model.Parroquia;
import com.nutrizulia.repository.ParroquiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

@Service
public class ParroquiaService implements IParroquiaService {

    @Autowired
    private ParroquiaRepository parroquiaRepository;

    @Override
    public List<Parroquia> getParroquias(Integer idMunicipio, Integer idParroquia, String nombre) {

        boolean isIdParroquiaPresent = idParroquia != null;
        boolean isNamePresent = StringUtils.hasText(nombre);

        if (isIdParroquiaPresent && isNamePresent) {
            return parroquiaRepository.findByMunicipioIdAndIdAndNombreContainingIgnoreCase(idMunicipio, idParroquia, nombre);
        } else if (isIdParroquiaPresent) {
            return parroquiaRepository.findByMunicipioIdAndId(idMunicipio, idParroquia)
                    .map(Collections::singletonList)
                    .orElse(Collections.emptyList());
        } else if (isNamePresent) {
            return parroquiaRepository.findByMunicipioIdAndNombreContainingIgnoreCase(idMunicipio, nombre);
        } else {
            return parroquiaRepository.findByMunicipioId(idMunicipio);
        }
    }

    @Override
    public Parroquia getParroquiaById(Integer idParroquia) {
        return parroquiaRepository.findById(idParroquia).orElse(null);
    }

    @Override
    public Parroquia saveParroquia(Parroquia parroquia) {
        return parroquiaRepository.save(parroquia);
    }

    @Override
    public void deleteParroquia(Parroquia parroquia) {
        parroquiaRepository.delete(parroquia);
    }
}
