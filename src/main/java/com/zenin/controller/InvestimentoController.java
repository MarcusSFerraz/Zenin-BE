package com.zenin.controller;

import com.zenin.dto.request.InvestimentoRequest;
import com.zenin.dto.response.InvestimentoResponse;
import com.zenin.model.User;
import com.zenin.service.InvestimentoService;
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
@RequestMapping("/api/investimentos")
@RequiredArgsConstructor
@Tag(name = "Investimentos", description = "Gerenciamento de investimentos")
public class InvestimentoController {

    private final InvestimentoService investimentoService;

    @GetMapping
    @Operation(summary = "Listar investimentos do usuário")
    public List<InvestimentoResponse> listar(@RequestParam(required = false) Boolean ativo,
                                              @AuthenticationPrincipal User usuario) {
        return investimentoService.listar(usuario, ativo).stream()
                .map(InvestimentoResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar investimento por ID")
    public InvestimentoResponse buscar(@PathVariable UUID id, @AuthenticationPrincipal User usuario) {
        return InvestimentoResponse.from(investimentoService.buscar(id, usuario));
    }

    @PostMapping
    @Operation(summary = "Criar investimento")
    public ResponseEntity<InvestimentoResponse> criar(@Valid @RequestBody InvestimentoRequest request,
                                                       @AuthenticationPrincipal User usuario) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(InvestimentoResponse.from(investimentoService.criar(request, usuario)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar investimento")
    public InvestimentoResponse atualizar(@PathVariable UUID id,
                                           @Valid @RequestBody InvestimentoRequest request,
                                           @AuthenticationPrincipal User usuario) {
        return InvestimentoResponse.from(investimentoService.atualizar(id, request, usuario));
    }

    @PatchMapping("/{id}/encerrar")
    @Operation(summary = "Encerrar investimento")
    public InvestimentoResponse encerrar(@PathVariable UUID id, @AuthenticationPrincipal User usuario) {
        return InvestimentoResponse.from(investimentoService.encerrar(id, usuario));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir investimento")
    public ResponseEntity<Void> deletar(@PathVariable UUID id, @AuthenticationPrincipal User usuario) {
        investimentoService.deletar(id, usuario);
        return ResponseEntity.noContent().build();
    }
}
