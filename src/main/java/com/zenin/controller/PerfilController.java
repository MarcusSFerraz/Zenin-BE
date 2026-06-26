package com.zenin.controller;

import com.zenin.dto.request.PerfilUpdateRequest;
import com.zenin.dto.response.PerfilResponse;
import com.zenin.model.User;
import com.zenin.service.PerfilService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/perfil")
@RequiredArgsConstructor
@Tag(name = "Perfil", description = "Gerenciamento do perfil do usuário autenticado")
public class PerfilController {

    private final PerfilService perfilService;

    @GetMapping
    @Operation(summary = "Buscar perfil do usuário autenticado")
    public PerfilResponse buscar(@AuthenticationPrincipal User usuario) {
        return PerfilResponse.from(perfilService.buscar(usuario));
    }

    @PutMapping
    @Operation(summary = "Atualizar dados do perfil")
    public PerfilResponse atualizar(@Valid @RequestBody PerfilUpdateRequest request,
                                     @AuthenticationPrincipal User usuario) {
        return PerfilResponse.from(perfilService.atualizar(request, usuario));
    }

    @PostMapping(value = "/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Fazer upload do avatar", description = "Aceita imagens JPEG ou PNG, máximo 5MB")
    public ResponseEntity<Map<String, String>> uploadAvatar(@RequestParam("arquivo") MultipartFile arquivo,
                                                             @AuthenticationPrincipal User usuario) throws IOException {
        String url = perfilService.uploadAvatar(arquivo, usuario);
        return ResponseEntity.ok(Map.of("urlAvatar", url));
    }
}
