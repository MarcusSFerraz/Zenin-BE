package com.zenin.controller;

import com.zenin.dto.request.CategoriaRequest;
import com.zenin.dto.response.CategoriaResponse;
import com.zenin.model.TipoTransacao;
import com.zenin.model.User;
import com.zenin.service.CategoriaService;
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
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
@Tag(name = "Categorias", description = "Gerenciamento de categorias de transações")
public class CategoriaController {

    private final CategoriaService categoriaService;

    @GetMapping
    @Operation(summary = "Listar categorias do usuário", description = "Filtrar por tipo usando ?tipo=RECEITA ou ?tipo=DESPESA")
    public List<CategoriaResponse> listar(@RequestParam(required = false) TipoTransacao tipo,
                                           @AuthenticationPrincipal User usuario) {
        return categoriaService.listar(usuario, tipo).stream()
                .map(CategoriaResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar categoria por ID")
    public CategoriaResponse buscar(@PathVariable UUID id, @AuthenticationPrincipal User usuario) {
        return CategoriaResponse.from(categoriaService.buscar(id, usuario));
    }

    @PostMapping
    @Operation(summary = "Criar categoria")
    public ResponseEntity<CategoriaResponse> criar(@Valid @RequestBody CategoriaRequest request,
                                                    @AuthenticationPrincipal User usuario) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CategoriaResponse.from(categoriaService.criar(request, usuario)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar categoria")
    public CategoriaResponse atualizar(@PathVariable UUID id,
                                        @Valid @RequestBody CategoriaRequest request,
                                        @AuthenticationPrincipal User usuario) {
        return CategoriaResponse.from(categoriaService.atualizar(id, request, usuario));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir categoria")
    public ResponseEntity<Void> deletar(@PathVariable UUID id, @AuthenticationPrincipal User usuario) {
        categoriaService.deletar(id, usuario);
        return ResponseEntity.noContent().build();
    }
}
