package com.zenin.controller;

import com.zenin.dto.request.MetaRequest;
import com.zenin.dto.response.MetaResponse;
import com.zenin.model.User;
import com.zenin.service.MetaService;
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
@RequestMapping("/api/metas")
@RequiredArgsConstructor
@Tag(name = "Metas", description = "Gerenciamento de metas financeiras")
public class MetaController {

    private final MetaService metaService;

    @GetMapping
    @Operation(summary = "Listar metas do usuário")
    public List<MetaResponse> listar(@AuthenticationPrincipal User usuario) {
        return metaService.listar(usuario);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar meta por ID")
    public MetaResponse buscar(@PathVariable UUID id, @AuthenticationPrincipal User usuario) {
        return metaService.buscarResponse(id, usuario);
    }

    @PostMapping
    @Operation(summary = "Criar meta")
    public ResponseEntity<MetaResponse> criar(@Valid @RequestBody MetaRequest request,
                                               @AuthenticationPrincipal User usuario) {
        var meta = metaService.criar(request, usuario);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(metaService.buscarResponse(meta.getId(), usuario));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar meta")
    public MetaResponse atualizar(@PathVariable UUID id,
                                   @Valid @RequestBody MetaRequest request,
                                   @AuthenticationPrincipal User usuario) {
        var meta = metaService.atualizar(id, request, usuario);
        return metaService.buscarResponse(meta.getId(), usuario);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir meta")
    public ResponseEntity<Void> deletar(@PathVariable UUID id, @AuthenticationPrincipal User usuario) {
        metaService.deletar(id, usuario);
        return ResponseEntity.noContent().build();
    }
}
