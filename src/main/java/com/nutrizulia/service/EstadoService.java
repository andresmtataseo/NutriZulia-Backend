package com.nutrizulia.service;

import com.nutrizulia.model.Estado;
import com.nutrizulia.repository.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

@Service
public class EstadoService implements IEstadoService {

    @Autowired
    private EstadoRepository estadoRepository;

    @Override
    public List<Estado> getEstados(Integer idEstado, String nombre) {
        boolean isIdPresent = idEstado != null;
        boolean isNamePresent = StringUtils.hasText(nombre);

        if (isIdPresent && isNamePresent) {
            return estadoRepository.findByIdOrNombreContainingIgnoreCase(idEstado, nombre);
        } else if (isIdPresent) {
            return estadoRepository.findById(idEstado)
                    .map(Collections::singletonList)
                    .orElse(Collections.emptyList());
        } else if (isNamePresent) {
            return estadoRepository.findByNombreContainingIgnoreCase(nombre);
        } else {
            return estadoRepository.findAll();
        }
    }

    @Override
    public Estado getEstadoById(Integer idEstado) {
        return estadoRepository.findById(idEstado).orElse(null);
    }

    @Override
    public Estado saveEstado(Estado estado) {
        return estadoRepository.save(estado);
    }

    @Override
    public void deleteEstado(Estado estado) {
        estadoRepository.delete(estado);
    }
}
