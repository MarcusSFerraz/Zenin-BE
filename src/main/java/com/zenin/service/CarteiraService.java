package com.zenin.service;

import com.zenin.dto.request.CarteiraRequest;
import com.zenin.model.Carteira;
import com.zenin.model.User;
import com.zenin.repository.CarteiraRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CarteiraService {

    private final CarteiraRepository carteiraRepository;

    public List<Carteira> listar(User usuario) {
        return carteiraRepository.findByUsuarioId(usuario.getId());
    }

    public Carteira buscar(UUID id, User usuario) {
        return carteiraRepository.findByIdAndUsuarioId(id, usuario.getId())
                .orElseThrow(() -> new EntityNotFoundException("Carteira não encontrada"));
    }

    @Transactional
    public Carteira criar(CarteiraRequest request, User usuario) {
        BigDecimal saldoInicial = request.saldoInicial() != null ? request.saldoInicial() : BigDecimal.ZERO;

        Carteira carteira = Carteira.builder()
                .usuario(usuario)
                .nome(request.nome())
                .cor(request.cor() != null ? request.cor() : "#3B82F6")
                .saldoInicial(saldoInicial)
                .saldoAtual(saldoInicial)
                .cartaoCredito(request.cartaoCredito())
                .build();

        return carteiraRepository.save(carteira);
    }

    @Transactional
    public Carteira atualizar(UUID id, CarteiraRequest request, User usuario) {
        Carteira carteira = buscar(id, usuario);

        carteira.setNome(request.nome());
        if (request.cor() != null) carteira.setCor(request.cor());
        carteira.setCartaoCredito(request.cartaoCredito());

        return carteiraRepository.save(carteira);
    }

    @Transactional
    public void deletar(UUID id, User usuario) {
        Carteira carteira = buscar(id, usuario);
        carteiraRepository.delete(carteira);
    }
}
