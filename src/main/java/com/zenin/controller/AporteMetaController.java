package com.zenin.controller;

import com.zenin.dto.request.AporteMetaRequest;
import com.zenin.dto.response.AporteMetaResponse;
import com.zenin.model.User;
import com.zenin.service.AporteMetaService;
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
@RequestMapping("/api/aportes-metas")
@RequiredArgsConstructor
@Tag(name = "Aportes em Metas", description = "Registro de aportes realizados em metas")
public class AporteMetaController {

    private final AporteMetaService aporteMetaService;

    @GetMapping
    @Operation(summary = "Listar aportes do usuário em metas")
    public List<AporteMetaResponse> listar(@AuthenticationPrincipal User usuario) {
        return aporteMetaService.listarPorUsuario(usuario).stream()
                .map(AporteMetaResponse::from)
                .toList();
    }

    @GetMapping("/meta/{metaId}")
    @Operation(summary = "Listar aportes de uma meta específica")
    public List<AporteMetaResponse> listarPorMeta(@PathVariable UUID metaId, @AuthenticationPrincipal User usuario) {
        return aporteMetaService.listarPorMeta(metaId, usuario).stream()
                .map(AporteMetaResponse::from)
                .toList();
    }

    @PostMapping
    @Operation(summary = "Registrar aporte em meta")
    public ResponseEntity<AporteMetaResponse> criar(@Valid @RequestBody AporteMetaRequest request,
                                                     @AuthenticationPrincipal User usuario) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(AporteMetaResponse.from(aporteMetaService.criar(request, usuario)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Estornar aporte de meta")
    public ResponseEntity<Void> deletar(@PathVariable UUID id, @AuthenticationPrincipal User usuario) {
        aporteMetaService.deletar(id, usuario);
        return ResponseEntity.noContent().build();
    }
}
