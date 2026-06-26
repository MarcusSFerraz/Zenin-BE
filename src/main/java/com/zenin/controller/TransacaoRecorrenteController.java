package com.zenin.controller;

import com.zenin.dto.request.TransacaoRecorrenteRequest;
import com.zenin.dto.response.TransacaoRecorrenteResponse;
import com.zenin.dto.response.TransacaoResponse;
import com.zenin.model.User;
import com.zenin.service.TransacaoRecorrenteService;
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
@RequestMapping("/api/transacoes-recorrentes")
@RequiredArgsConstructor
@Tag(name = "Transações Recorrentes", description = "Gerenciamento de transações recorrentes")
public class TransacaoRecorrenteController {

    private final TransacaoRecorrenteService service;

    @GetMapping
    @Operation(summary = "Listar transações recorrentes")
    public List<TransacaoRecorrenteResponse> listar(@AuthenticationPrincipal User usuario) {
        return service.listar(usuario).stream()
                .map(TransacaoRecorrenteResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar transação recorrente por ID")
    public TransacaoRecorrenteResponse buscar(@PathVariable UUID id, @AuthenticationPrincipal User usuario) {
        return TransacaoRecorrenteResponse.from(service.buscar(id, usuario));
    }

    @PostMapping
    @Operation(summary = "Criar transação recorrente")
    public ResponseEntity<TransacaoRecorrenteResponse> criar(@Valid @RequestBody TransacaoRecorrenteRequest request,
                                                              @AuthenticationPrincipal User usuario) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(TransacaoRecorrenteResponse.from(service.criar(request, usuario)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar transação recorrente")
    public TransacaoRecorrenteResponse atualizar(@PathVariable UUID id,
                                                  @Valid @RequestBody TransacaoRecorrenteRequest request,
                                                  @AuthenticationPrincipal User usuario) {
        return TransacaoRecorrenteResponse.from(service.atualizar(id, request, usuario));
    }

    @PatchMapping("/{id}/alternar-ativo")
    @Operation(summary = "Ativar ou desativar transação recorrente")
    public TransacaoRecorrenteResponse alternarAtivo(@PathVariable UUID id, @AuthenticationPrincipal User usuario) {
        return TransacaoRecorrenteResponse.from(service.alternarAtivo(id, usuario));
    }

    @PostMapping("/processar")
    @Operation(summary = "Processar transações recorrentes do mês atual",
               description = "Gera transações para recorrências que ainda não foram processadas no mês corrente")
    public List<TransacaoResponse> processar(@AuthenticationPrincipal User usuario) {
        return service.processar(usuario).stream()
                .map(TransacaoResponse::from)
                .toList();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir transação recorrente")
    public ResponseEntity<Void> deletar(@PathVariable UUID id, @AuthenticationPrincipal User usuario) {
        service.deletar(id, usuario);
        return ResponseEntity.noContent().build();
    }
}
