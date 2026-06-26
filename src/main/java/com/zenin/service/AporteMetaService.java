package com.zenin.service;

import com.zenin.dto.request.AporteMetaRequest;
import com.zenin.model.AporteMeta;
import com.zenin.model.Carteira;
import com.zenin.model.Meta;
import com.zenin.model.User;
import com.zenin.repository.AporteMetaRepository;
import com.zenin.repository.CarteiraRepository;
import com.zenin.repository.MetaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AporteMetaService {

    private final AporteMetaRepository aporteMetaRepository;
    private final MetaRepository metaRepository;
    private final CarteiraRepository carteiraRepository;

    public List<AporteMeta> listarPorUsuario(User usuario) {
        return aporteMetaRepository.findByUsuarioId(usuario.getId());
    }

    public List<AporteMeta> listarPorMeta(UUID metaId, User usuario) {
        metaRepository.findByIdAndUsuarioId(metaId, usuario.getId())
                .orElseThrow(() -> new EntityNotFoundException("Meta não encontrada"));
        return aporteMetaRepository.findByMetaId(metaId);
    }

    @Transactional
    public AporteMeta criar(AporteMetaRequest request, User usuario) {
        Meta meta = metaRepository.findByIdAndUsuarioId(request.metaId(), usuario.getId())
                .orElseThrow(() -> new EntityNotFoundException("Meta não encontrada"));

        Carteira carteira = carteiraRepository.findByIdAndUsuarioId(request.carteiraId(), usuario.getId())
                .orElseThrow(() -> new EntityNotFoundException("Carteira não encontrada"));

        if (carteira.getSaldoAtual().compareTo(request.valor()) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente na carteira");
        }

        AporteMeta aporte = AporteMeta.builder()
                .meta(meta)
                .carteira(carteira)
                .usuario(usuario)
                .valor(request.valor())
                .build();

        meta.setValorAtual(meta.getValorAtual().add(request.valor()));
        carteira.setSaldoAtual(carteira.getSaldoAtual().subtract(request.valor()));

        metaRepository.save(meta);
        carteiraRepository.save(carteira);

        return aporteMetaRepository.save(aporte);
    }

    @Transactional
    public void deletar(UUID id, User usuario) {
        AporteMeta aporte = aporteMetaRepository.findByIdAndUsuarioId(id, usuario.getId())
                .orElseThrow(() -> new EntityNotFoundException("Aporte não encontrado"));

        // Reverte o impacto do aporte
        Meta meta = aporte.getMeta();
        Carteira carteira = aporte.getCarteira();
        meta.setValorAtual(meta.getValorAtual().subtract(aporte.getValor()));
        carteira.setSaldoAtual(carteira.getSaldoAtual().add(aporte.getValor()));

        metaRepository.save(meta);
        carteiraRepository.save(carteira);
        aporteMetaRepository.delete(aporte);
    }
}
