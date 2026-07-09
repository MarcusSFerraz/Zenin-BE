package com.zenin.controller;

import com.zenin.dto.request.FaturaCartaoRequest;
import com.zenin.dto.request.ItemFaturaRequest;
import com.zenin.dto.request.LancamentoFaturaRequest;
import com.zenin.dto.response.FaturaCartaoResponse;
import com.zenin.dto.response.ItemFaturaResponse;
import com.zenin.dto.response.LancamentoFaturaResponse;
import com.zenin.model.User;
import com.zenin.service.FaturaCartaoService;
import com.zenin.service.ItemFaturaService;
import com.zenin.service.LancamentoFaturaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/faturas-cartao")
@RequiredArgsConstructor
@Tag(name = "Faturas de Cartão", description = "Gerenciamento de faturas de cartão de crédito")
public class FaturaCartaoController {

    private final FaturaCartaoService faturaCartaoService;
    private final ItemFaturaService itemFaturaService;
    private final LancamentoFaturaService lancamentoFaturaService;

    @GetMapping
    @Operation(summary = "Listar faturas", description = "Filtrar por ?carteiraId= ou ?pago=true/false")
    public List<FaturaCartaoResponse> listar(
            @RequestParam(required = false) UUID carteiraId,
            @RequestParam(required = false) Boolean pago,
            @AuthenticationPrincipal User usuario) {
        return faturaCartaoService.listar(usuario, carteiraId, pago).stream()
                .map(FaturaCartaoResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar fatura por ID")
    public FaturaCartaoResponse buscar(@PathVariable UUID id, @AuthenticationPrincipal User usuario) {
        return FaturaCartaoResponse.from(faturaCartaoService.buscar(id, usuario));
    }

    @PostMapping
    @Operation(summary = "Criar fatura de cartão")
    public ResponseEntity<FaturaCartaoResponse> criar(@Valid @RequestBody FaturaCartaoRequest request,
                                                       @AuthenticationPrincipal User usuario) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(FaturaCartaoResponse.from(faturaCartaoService.criar(request, usuario)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar fatura")
    public FaturaCartaoResponse atualizar(@PathVariable UUID id,
                                           @Valid @RequestBody FaturaCartaoRequest request,
                                           @AuthenticationPrincipal User usuario) {
        return FaturaCartaoResponse.from(faturaCartaoService.atualizar(id, request, usuario));
    }

    @PatchMapping("/{id}/pagar")
    @Operation(summary = "Marcar fatura como paga e debitar carteira")
    public FaturaCartaoResponse pagar(@PathVariable UUID id,
                                       @RequestParam UUID carteiraId,
                                       @AuthenticationPrincipal User usuario) {
        return FaturaCartaoResponse.from(faturaCartaoService.marcarComoPago(id, carteiraId, usuario));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir fatura")
    public ResponseEntity<Void> deletar(@PathVariable UUID id, @AuthenticationPrincipal User usuario) {
        faturaCartaoService.deletar(id, usuario);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/itens")
    @Operation(summary = "Listar itens da fatura")
    public List<ItemFaturaResponse> listarItens(@PathVariable UUID id, @AuthenticationPrincipal User usuario) {
        return itemFaturaService.listar(id, usuario).stream()
                .map(ItemFaturaResponse::from)
                .toList();
    }

    @PostMapping("/{id}/itens")
    @Operation(summary = "Adicionar item à fatura")
    public ResponseEntity<ItemFaturaResponse> adicionarItem(@PathVariable UUID id,
                                                             @Valid @RequestBody ItemFaturaRequest request,
                                                             @AuthenticationPrincipal User usuario) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ItemFaturaResponse.from(itemFaturaService.criar(id, request, usuario)));
    }

    @PutMapping("/{id}/itens/{itemId}")
    @Operation(summary = "Atualizar item da fatura")
    public ItemFaturaResponse atualizarItem(@PathVariable UUID id,
                                             @PathVariable UUID itemId,
                                             @Valid @RequestBody ItemFaturaRequest request,
                                             @AuthenticationPrincipal User usuario) {
        return ItemFaturaResponse.from(itemFaturaService.atualizar(id, itemId, request, usuario));
    }

    @DeleteMapping("/{id}/itens/{itemId}")
    @Operation(summary = "Remover item da fatura")
    public ResponseEntity<Void> removerItem(@PathVariable UUID id,
                                             @PathVariable UUID itemId,
                                             @AuthenticationPrincipal User usuario) {
        itemFaturaService.deletar(id, itemId, usuario);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/lancamentos")
    @Operation(summary = "Listar lançamentos da fatura")
    public List<LancamentoFaturaResponse> listarLancamentos(@PathVariable UUID id,
                                                             @AuthenticationPrincipal User usuario) {
        return lancamentoFaturaService.listar(id, usuario).stream()
                .map(LancamentoFaturaResponse::from)
                .toList();
    }

    @PostMapping("/{id}/lancamentos")
    @Operation(summary = "Adicionar lançamento à fatura")
    public ResponseEntity<LancamentoFaturaResponse> adicionarLancamento(@PathVariable UUID id,
                                                                         @Valid @RequestBody LancamentoFaturaRequest request,
                                                                         @AuthenticationPrincipal User usuario) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(LancamentoFaturaResponse.from(lancamentoFaturaService.criar(id, request, usuario)));
    }

    @PutMapping("/{id}/lancamentos/{lancamentoId}")
    @Operation(summary = "Atualizar lançamento da fatura")
    public LancamentoFaturaResponse atualizarLancamento(@PathVariable UUID id,
                                                         @PathVariable UUID lancamentoId,
                                                         @Valid @RequestBody LancamentoFaturaRequest request,
                                                         @AuthenticationPrincipal User usuario) {
        return LancamentoFaturaResponse.from(lancamentoFaturaService.atualizar(id, lancamentoId, request, usuario));
    }

    @DeleteMapping("/{id}/lancamentos/{lancamentoId}")
    @Operation(summary = "Remover lançamento da fatura")
    public ResponseEntity<Void> removerLancamento(@PathVariable UUID id,
                                                   @PathVariable UUID lancamentoId,
                                                   @AuthenticationPrincipal User usuario) {
        lancamentoFaturaService.deletar(id, lancamentoId, usuario);
        return ResponseEntity.noContent().build();
    }
}
