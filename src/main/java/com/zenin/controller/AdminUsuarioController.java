package com.zenin.controller;

import com.zenin.dto.request.AdminUsuarioUpdateRequest;
import com.zenin.dto.response.UsuarioAdminResponse;
import com.zenin.service.AdminUsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/usuarios")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin - Usuários", description = "Gestão de usuários (restrito a ADMIN)")
public class AdminUsuarioController {

    private final AdminUsuarioService adminUsuarioService;

    @GetMapping
    @Operation(summary = "Listar todos os usuários")
    public List<UsuarioAdminResponse> listar() {
        return adminUsuarioService.listarTodos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuário por ID")
    public UsuarioAdminResponse buscar(@PathVariable UUID id) {
        return adminUsuarioService.buscarPorId(id);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Atualizar role e/ou status ativo do usuário")
    public UsuarioAdminResponse atualizar(@PathVariable UUID id,
                                          @RequestBody AdminUsuarioUpdateRequest request) {
        return adminUsuarioService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar usuário")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        adminUsuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
