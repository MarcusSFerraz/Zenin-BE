package com.zenin.service;

import com.zenin.dto.request.ItemFaturaRequest;
import com.zenin.model.FaturaCartao;
import com.zenin.model.ItemFatura;
import com.zenin.model.User;
import com.zenin.repository.FaturaCartaoRepository;
import com.zenin.repository.ItemFaturaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemFaturaService {

    private final ItemFaturaRepository itemFaturaRepository;
    private final FaturaCartaoRepository faturaRepository;

    public List<ItemFatura> listar(UUID faturaId, User usuario) {
        faturaRepository.findByIdAndUsuarioId(faturaId, usuario.getId())
                .orElseThrow(() -> new EntityNotFoundException("Fatura não encontrada"));
        return itemFaturaRepository.findByFaturaIdAndUsuarioId(faturaId, usuario.getId());
    }

    @Transactional
    public ItemFatura criar(UUID faturaId, ItemFaturaRequest request, User usuario) {
        FaturaCartao fatura = faturaRepository.findByIdAndUsuarioId(faturaId, usuario.getId())
                .orElseThrow(() -> new EntityNotFoundException("Fatura não encontrada"));

        ItemFatura item = ItemFatura.builder()
                .fatura(fatura)
                .usuario(usuario)
                .descricao(request.descricao())
                .valor(request.valor())
                .dataCompra(request.dataCompra())
                .build();

        return itemFaturaRepository.save(item);
    }

    @Transactional
    public ItemFatura atualizar(UUID faturaId, UUID itemId, ItemFaturaRequest request, User usuario) {
        faturaRepository.findByIdAndUsuarioId(faturaId, usuario.getId())
                .orElseThrow(() -> new EntityNotFoundException("Fatura não encontrada"));

        ItemFatura item = itemFaturaRepository.findByIdAndUsuarioId(itemId, usuario.getId())
                .orElseThrow(() -> new EntityNotFoundException("Item não encontrado"));

        item.setDescricao(request.descricao());
        item.setValor(request.valor());
        item.setDataCompra(request.dataCompra());

        return itemFaturaRepository.save(item);
    }

    @Transactional
    public void deletar(UUID faturaId, UUID itemId, User usuario) {
        faturaRepository.findByIdAndUsuarioId(faturaId, usuario.getId())
                .orElseThrow(() -> new EntityNotFoundException("Fatura não encontrada"));

        ItemFatura item = itemFaturaRepository.findByIdAndUsuarioId(itemId, usuario.getId())
                .orElseThrow(() -> new EntityNotFoundException("Item não encontrado"));

        itemFaturaRepository.delete(item);
    }
}
