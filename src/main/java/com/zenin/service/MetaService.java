package com.zenin.service;

import com.zenin.dto.request.MetaRequest;
import com.zenin.model.Categoria;
import com.zenin.model.Meta;
import com.zenin.model.User;
import com.zenin.repository.CategoriaRepository;
import com.zenin.repository.MetaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MetaService {

    private final MetaRepository metaRepository;
    private final CategoriaRepository categoriaRepository;

    public List<Meta> listar(User usuario) {
        return metaRepository.findByUsuarioId(usuario.getId());
    }

    public Meta buscar(UUID id, User usuario) {
        return metaRepository.findByIdAndUsuarioId(id, usuario.getId())
                .orElseThrow(() -> new EntityNotFoundException("Meta não encontrada"));
    }

    @Transactional
    public Meta criar(MetaRequest request, User usuario) {
        if (request.dataFim().isBefore(request.dataInicio())) {
            throw new IllegalArgumentException("Data de fim deve ser posterior à data de início");
        }

        Categoria categoria = null;
        if (request.categoriaId() != null) {
            categoria = categoriaRepository.findByIdAndUsuarioId(request.categoriaId(), usuario.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));
        }

        Meta meta = Meta.builder()
                .usuario(usuario)
                .categoria(categoria)
                .nome(request.nome())
                .valorAlvo(request.valorAlvo())
                .periodo(request.periodo())
                .dataInicio(request.dataInicio())
                .dataFim(request.dataFim())
                .cor(request.cor() != null ? request.cor() : "#3B82F6")
                .icone(request.icone() != null ? request.icone() : "target")
                .build();

        return metaRepository.save(meta);
    }

    @Transactional
    public Meta atualizar(UUID id, MetaRequest request, User usuario) {
        if (request.dataFim().isBefore(request.dataInicio())) {
            throw new IllegalArgumentException("Data de fim deve ser posterior à data de início");
        }

        Meta meta = buscar(id, usuario);

        Categoria categoria = null;
        if (request.categoriaId() != null) {
            categoria = categoriaRepository.findByIdAndUsuarioId(request.categoriaId(), usuario.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));
        }

        meta.setCategoria(categoria);
        meta.setNome(request.nome());
        meta.setValorAlvo(request.valorAlvo());
        meta.setPeriodo(request.periodo());
        meta.setDataInicio(request.dataInicio());
        meta.setDataFim(request.dataFim());
        if (request.cor() != null) meta.setCor(request.cor());
        if (request.icone() != null) meta.setIcone(request.icone());

        return metaRepository.save(meta);
    }

    @Transactional
    public void deletar(UUID id, User usuario) {
        Meta meta = buscar(id, usuario);
        metaRepository.delete(meta);
    }
}
