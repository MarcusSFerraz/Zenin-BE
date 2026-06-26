package com.zenin.controller;

import com.zenin.dto.request.CarteiraRequest;
import com.zenin.dto.response.CarteiraResponse;
import com.zenin.model.User;
import com.zenin.service.CarteiraService;
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
@RequestMapping("/api/carteiras")
@RequiredArgsConstructor
@Tag(name = "Carteiras", description = "Gerenciamento de carteiras financeiras")
public class CarteiraController {

    private final CarteiraService carteiraService;

    @GetMapping
    @Operation(summary = "Listar carteiras do usuário")
    public List<CarteiraResponse> listar(@AuthenticationPrincipal User usuario) {
        return carteiraService.listar(usuario).stream()
                .map(CarteiraResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar carteira por ID")
    public CarteiraResponse buscar(@PathVariable UUID id, @AuthenticationPrincipal User usuario) {
        return CarteiraResponse.from(carteiraService.buscar(id, usuario));
    }

    @PostMapping
    @Operation(summary = "Criar carteira")
    public ResponseEntity<CarteiraResponse> criar(@Valid @RequestBody CarteiraRequest request,
                                                   @AuthenticationPrincipal User usuario) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CarteiraResponse.from(carteiraService.criar(request, usuario)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar carteira")
    public CarteiraResponse atualizar(@PathVariable UUID id,
                                       @Valid @RequestBody CarteiraRequest request,
                                       @AuthenticationPrincipal User usuario) {
        return CarteiraResponse.from(carteiraService.atualizar(id, request, usuario));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir carteira")
    public ResponseEntity<Void> deletar(@PathVariable UUID id, @AuthenticationPrincipal User usuario) {
        carteiraService.deletar(id, usuario);
        return ResponseEntity.noContent().build();
    }
}
