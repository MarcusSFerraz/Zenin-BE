package com.zenin.controller;

import com.zenin.dto.request.TaxaInvestimentoRequest;
import com.zenin.dto.response.TaxaInvestimentoResponse;
import com.zenin.service.TaxaInvestimentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/taxas-investimento")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin — Taxas de Investimento", description = "Gerenciamento administrativo de taxas (Tesouro, CDB, LCI/LCA)")
@SecurityRequirement(name = "bearerAuth")
public class TaxaInvestimentoController {

    private final TaxaInvestimentoService taxaInvestimentoService;

    @GetMapping
    @Operation(summary = "Listar todas as taxas")
    public List<TaxaInvestimentoResponse> listar() {
        return taxaInvestimentoService.listar().stream()
                .map(TaxaInvestimentoResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar taxa por ID")
    public TaxaInvestimentoResponse buscar(@PathVariable UUID id) {
        return TaxaInvestimentoResponse.from(taxaInvestimentoService.buscar(id));
    }

    @PostMapping
    @Operation(summary = "Cadastrar taxa de investimento")
    public ResponseEntity<TaxaInvestimentoResponse> criar(@Valid @RequestBody TaxaInvestimentoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(TaxaInvestimentoResponse.from(taxaInvestimentoService.criar(request)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar taxa de investimento")
    public TaxaInvestimentoResponse atualizar(@PathVariable UUID id,
                                               @Valid @RequestBody TaxaInvestimentoRequest request) {
        return TaxaInvestimentoResponse.from(taxaInvestimentoService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir taxa de investimento")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        taxaInvestimentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
