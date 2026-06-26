package com.zenin.service;

import com.zenin.dto.request.CategoriaRequest;
import com.zenin.model.Categoria;
import com.zenin.model.TipoTransacao;
import com.zenin.model.User;
import com.zenin.repository.CategoriaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public List<Categoria> listar(User usuario, TipoTransacao tipo) {
        if (tipo != null) {
            return categoriaRepository.findByUsuarioIdAndTipo(usuario.getId(), tipo);
        }
        return categoriaRepository.findByUsuarioId(usuario.getId());
    }

    public Categoria buscar(UUID id, User usuario) {
        return categoriaRepository.findByIdAndUsuarioId(id, usuario.getId())
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));
    }

    @Transactional
    public Categoria criar(CategoriaRequest request, User usuario) {
        Categoria categoria = Categoria.builder()
                .usuario(usuario)
                .nome(request.nome())
                .tipo(request.tipo())
                .cor(request.cor() != null ? request.cor() : "#3B82F6")
                .icone(request.icone())
                .build();

        return categoriaRepository.save(categoria);
    }

    @Transactional
    public Categoria atualizar(UUID id, CategoriaRequest request, User usuario) {
        Categoria categoria = buscar(id, usuario);

        categoria.setNome(request.nome());
        categoria.setTipo(request.tipo());
        if (request.cor() != null) categoria.setCor(request.cor());
        categoria.setIcone(request.icone());

        return categoriaRepository.save(categoria);
    }

    @Transactional
    public void deletar(UUID id, User usuario) {
        Categoria categoria = buscar(id, usuario);
        categoriaRepository.delete(categoria);
    }
}
