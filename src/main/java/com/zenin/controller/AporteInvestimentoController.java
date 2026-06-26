package com.zenin.controller;

import com.zenin.dto.request.AporteInvestimentoRequest;
import com.zenin.dto.response.AporteInvestimentoResponse;
import com.zenin.model.User;
import com.zenin.service.AporteInvestimentoService;
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
@RequestMapping("/api/aportes-investimentos")
@RequiredArgsConstructor
@Tag(name = "Aportes em Investimentos", description = "Registro de aportes realizados em investimentos")
public class AporteInvestimentoController {

    private final AporteInvestimentoService aporteInvestimentoService;

    @GetMapping
    @Operation(summary = "Listar aportes do usuário em investimentos")
    public List<AporteInvestimentoResponse> listar(@AuthenticationPrincipal User usuario) {
        return aporteInvestimentoService.listarPorUsuario(usuario).stream()
                .map(AporteInvestimentoResponse::from)
                .toList();
    }

    @GetMapping("/investimento/{investimentoId}")
    @Operation(summary = "Listar aportes de um investimento específico")
    public List<AporteInvestimentoResponse> listarPorInvestimento(@PathVariable UUID investimentoId,
                                                                    @AuthenticationPrincipal User usuario) {
        return aporteInvestimentoService.listarPorInvestimento(investimentoId, usuario).stream()
                .map(AporteInvestimentoResponse::from)
                .toList();
    }

    @PostMapping
    @Operation(summary = "Registrar aporte em investimento")
    public ResponseEntity<AporteInvestimentoResponse> criar(@Valid @RequestBody AporteInvestimentoRequest request,
                                                             @AuthenticationPrincipal User usuario) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(AporteInvestimentoResponse.from(aporteInvestimentoService.criar(request, usuario)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Estornar aporte de investimento")
    public ResponseEntity<Void> deletar(@PathVariable UUID id, @AuthenticationPrincipal User usuario) {
        aporteInvestimentoService.deletar(id, usuario);
        return ResponseEntity.noContent().build();
    }
}
