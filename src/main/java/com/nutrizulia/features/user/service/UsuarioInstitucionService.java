package com.nutrizulia.features.user.service;

import com.nutrizulia.features.user.dto.UsuarioInstitucionDto;
import com.nutrizulia.features.user.mapper.UsuarioInstitucionMapper;
import com.nutrizulia.features.user.repository.UsuarioInstitucionRepository;
import com.nutrizulia.features.user.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UsuarioInstitucionService implements IUsuarioInstitucionService {

    private final UsuarioInstitucionRepository usuarioInstitucionRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioInstitucionMapper usuarioInstitucionMapper;

    @Override
    public List<UsuarioInstitucionDto> getInstitucionesByUsuarioId(Integer usuarioId) {

        if (!usuarioRepository.existsById(usuarioId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Usuario no encontrado con ID: '%s'", usuarioId));
        }

        return usuarioInstitucionRepository.findAllByUsuario_Id(usuarioId)
                .stream()
                .map(usuarioInstitucionMapper::toDto)
                .collect(Collectors.toList());
    }
}
