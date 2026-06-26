package com.zenin.controller;

import com.zenin.dto.request.TransacaoRequest;
import com.zenin.dto.response.TransacaoResponse;
import com.zenin.model.User;
import com.zenin.service.TransacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transacoes")
@RequiredArgsConstructor
@Tag(name = "Transações", description = "Gerenciamento de transações financeiras")
public class TransacaoController {

    private final TransacaoService transacaoService;

    @GetMapping
    @Operation(summary = "Listar transações", description = "Filtre por ?carteiraId=, ?inicio=&fim= (yyyy-MM-dd)")
    public List<TransacaoResponse> listar(
            @RequestParam(required = false) UUID carteiraId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
            @AuthenticationPrincipal User usuario) {
        return transacaoService.listar(usuario, carteiraId, inicio, fim).stream()
                .map(TransacaoResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar transação por ID")
    public TransacaoResponse buscar(@PathVariable UUID id, @AuthenticationPrincipal User usuario) {
        return TransacaoResponse.from(transacaoService.buscar(id, usuario));
    }

    @PostMapping
    @Operation(summary = "Criar transação")
    public ResponseEntity<TransacaoResponse> criar(@Valid @RequestBody TransacaoRequest request,
                                                    @AuthenticationPrincipal User usuario) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(TransacaoResponse.from(transacaoService.criar(request, usuario)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar transação")
    public TransacaoResponse atualizar(@PathVariable UUID id,
                                        @Valid @RequestBody TransacaoRequest request,
                                        @AuthenticationPrincipal User usuario) {
        return TransacaoResponse.from(transacaoService.atualizar(id, request, usuario));
    }

    @PatchMapping("/{id}/pagar")
    @Operation(summary = "Marcar transação como paga")
    public TransacaoResponse pagar(@PathVariable UUID id, @AuthenticationPrincipal User usuario) {
        return TransacaoResponse.from(transacaoService.marcarComoPago(id, usuario));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir transação")
    public ResponseEntity<Void> deletar(@PathVariable UUID id, @AuthenticationPrincipal User usuario) {
        transacaoService.deletar(id, usuario);
        return ResponseEntity.noContent().build();
    }
}
