package com.zenin.service;

import com.zenin.dto.request.MetaRequest;
import com.zenin.dto.response.MetaResponse;
import com.zenin.model.*;
import com.zenin.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MetaService {

    private final MetaRepository metaRepository;
    private final CategoriaRepository categoriaRepository;
    private final InvestimentoRepository investimentoRepository;
    private final AporteMetaRepository aporteMetaRepository;
    private final CarteiraRepository carteiraRepository;

    public List<MetaResponse> listar(User usuario) {
        List<Meta> metas = metaRepository.findByUsuarioId(usuario.getId());
        if (metas.isEmpty()) return List.of();

        List<UUID> metaIds = metas.stream().map(Meta::getId).toList();
        Map<UUID, BigDecimal> totalAportesMap = aporteMetaRepository.sumByMetaIds(metaIds).stream()
                .collect(Collectors.toMap(
                        row -> (UUID) row[0],
                        row -> (BigDecimal) row[1]
                ));

        return metas.stream()
                .map(m -> {
                    BigDecimal totalAportes = totalAportesMap.getOrDefault(m.getId(), BigDecimal.ZERO);
                    BigDecimal totalInvestimento = m.getInvestimento() != null
                            ? m.getInvestimento().getValorAtual()
                            : BigDecimal.ZERO;
                    return MetaResponse.from(m, totalAportes, totalInvestimento);
                })
                .toList();
    }

    public Meta buscar(UUID id, User usuario) {
        return metaRepository.findByIdAndUsuarioId(id, usuario.getId())
                .orElseThrow(() -> new EntityNotFoundException("Meta não encontrada"));
    }

    public MetaResponse buscarResponse(UUID id, User usuario) {
        Meta meta = buscar(id, usuario);
        return toFullResponse(meta);
    }

    private MetaResponse toFullResponse(Meta meta) {
        BigDecimal totalAportes = aporteMetaRepository.sumByMetaId(meta.getId());
        BigDecimal totalInvestimento = meta.getInvestimento() != null
                ? meta.getInvestimento().getValorAtual()
                : BigDecimal.ZERO;
        return MetaResponse.from(meta, totalAportes, totalInvestimento);
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

        Investimento investimento = null;
        if (request.investimentoId() != null) {
            investimento = investimentoRepository.findByIdAndUsuarioId(request.investimentoId(), usuario.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Investimento não encontrado"));
        }

        BigDecimal valorInicial = request.valorInicial() != null ? request.valorInicial() : BigDecimal.ZERO;

        boolean deduct = !Boolean.FALSE.equals(request.deduzirCarteira());
        if (deduct && request.carteiraId() != null && valorInicial.compareTo(BigDecimal.ZERO) > 0) {
            Carteira carteira = carteiraRepository.findByIdAndUsuarioId(request.carteiraId(), usuario.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Carteira não encontrada"));
            if (carteira.getSaldoAtual().compareTo(valorInicial) < 0) {
                throw new IllegalArgumentException("Saldo insuficiente na carteira");
            }
            carteira.setSaldoAtual(carteira.getSaldoAtual().subtract(valorInicial));
            carteiraRepository.save(carteira);
        }

        Meta meta = Meta.builder()
                .usuario(usuario)
                .categoria(categoria)
                .investimento(investimento)
                .nome(request.nome())
                .valorAlvo(request.valorAlvo())
                .valorInicial(valorInicial)
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

        Investimento investimento = null;
        if (request.investimentoId() != null) {
            investimento = investimentoRepository.findByIdAndUsuarioId(request.investimentoId(), usuario.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Investimento não encontrado"));
        }

        meta.setCategoria(categoria);
        meta.setInvestimento(investimento);
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
