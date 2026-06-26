package com.zenin.controller;

import com.zenin.dto.response.NotificacaoResponse;
import com.zenin.model.User;
import com.zenin.service.NotificacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/notificacoes")
@RequiredArgsConstructor
@Tag(name = "Notificações", description = "Gerenciamento de notificações do usuário")
public class NotificacaoController {

    private final NotificacaoService notificacaoService;

    @GetMapping
    @Operation(summary = "Listar notificações do usuário")
    public List<NotificacaoResponse> listar(@AuthenticationPrincipal User usuario) {
        return notificacaoService.listar(usuario).stream()
                .map(NotificacaoResponse::from)
                .toList();
    }

    @GetMapping("/nao-lidas")
    @Operation(summary = "Contar notificações não lidas")
    public Map<String, Long> contarNaoLidas(@AuthenticationPrincipal User usuario) {
        return Map.of("total", notificacaoService.contarNaoLidas(usuario));
    }

    @PatchMapping("/{id}/ler")
    @Operation(summary = "Marcar notificação como lida")
    public NotificacaoResponse marcarComoLida(@PathVariable UUID id, @AuthenticationPrincipal User usuario) {
        return NotificacaoResponse.from(notificacaoService.marcarComoLida(id, usuario));
    }

    @PatchMapping("/ler-todas")
    @Operation(summary = "Marcar todas as notificações como lidas")
    public ResponseEntity<Void> lerTodas(@AuthenticationPrincipal User usuario) {
        notificacaoService.marcarTodasComoLidas(usuario);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir notificação")
    public ResponseEntity<Void> deletar(@PathVariable UUID id, @AuthenticationPrincipal User usuario) {
        notificacaoService.deletar(id, usuario);
        return ResponseEntity.noContent().build();
    }
}
