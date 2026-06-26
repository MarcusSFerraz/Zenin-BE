package com.zenin.service;

import com.zenin.dto.request.NotificacaoRequest;
import com.zenin.model.Notificacao;
import com.zenin.model.User;
import com.zenin.repository.NotificacaoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificacaoService {

    private final NotificacaoRepository notificacaoRepository;

    public List<Notificacao> listar(User usuario) {
        return notificacaoRepository.findByUsuarioId(usuario.getId());
    }

    public List<Notificacao> listarNaoLidas(User usuario) {
        return notificacaoRepository.findByUsuarioIdAndLida(usuario.getId(), false);
    }

    public long contarNaoLidas(User usuario) {
        return notificacaoRepository.countByUsuarioIdAndLida(usuario.getId(), false);
    }

    @Transactional
    public Notificacao criar(NotificacaoRequest request, User usuario) {
        Notificacao notificacao = Notificacao.builder()
                .usuario(usuario)
                .titulo(request.titulo())
                .mensagem(request.mensagem())
                .build();

        return notificacaoRepository.save(notificacao);
    }

    @Transactional
    public Notificacao marcarComoLida(UUID id, User usuario) {
        Notificacao notificacao = notificacaoRepository.findByIdAndUsuarioId(id, usuario.getId())
                .orElseThrow(() -> new EntityNotFoundException("Notificação não encontrada"));

        notificacao.setLida(true);
        return notificacaoRepository.save(notificacao);
    }

    @Transactional
    public void marcarTodasComoLidas(User usuario) {
        List<Notificacao> pendentes = notificacaoRepository.findByUsuarioIdAndLida(usuario.getId(), false);
        pendentes.forEach(n -> n.setLida(true));
        notificacaoRepository.saveAll(pendentes);
    }

    @Transactional
    public void deletar(UUID id, User usuario) {
        Notificacao notificacao = notificacaoRepository.findByIdAndUsuarioId(id, usuario.getId())
                .orElseThrow(() -> new EntityNotFoundException("Notificação não encontrada"));
        notificacaoRepository.delete(notificacao);
    }
}
