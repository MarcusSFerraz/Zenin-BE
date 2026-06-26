package com.zenin.controller;

import com.zenin.dto.request.FaturaCartaoRequest;
import com.zenin.dto.response.FaturaCartaoResponse;
import com.zenin.model.User;
import com.zenin.service.FaturaCartaoService;
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
}
