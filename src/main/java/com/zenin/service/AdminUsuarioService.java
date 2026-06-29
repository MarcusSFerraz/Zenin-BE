package com.zenin.service;

import com.zenin.dto.request.AdminUsuarioUpdateRequest;
import com.zenin.dto.response.UsuarioAdminResponse;
import com.zenin.model.User;
import com.zenin.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminUsuarioService {

    private final UserRepository userRepository;

    public List<UsuarioAdminResponse> listarTodos() {
        return userRepository.findAll().stream()
                .map(UsuarioAdminResponse::from)
                .toList();
    }

    public UsuarioAdminResponse buscarPorWhatsapp(String numeroWhatsapp) {
        User user = userRepository.findByNumeroWhatsapp(numeroWhatsapp)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com WhatsApp: " + numeroWhatsapp));
        return UsuarioAdminResponse.from(user);
    }

    public UsuarioAdminResponse buscarPorId(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado: " + id));
        return UsuarioAdminResponse.from(user);
    }

    public UsuarioAdminResponse atualizar(UUID id, AdminUsuarioUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado: " + id));

        if (request.role() != null) {
            user.setRole(request.role());
        }
        if (request.ativo() != null) {
            user.setAtivo(request.ativo());
        }

        return UsuarioAdminResponse.from(userRepository.save(user));
    }

    public void deletar(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("Usuário não encontrado: " + id);
        }
        userRepository.deleteById(id);
    }
}
