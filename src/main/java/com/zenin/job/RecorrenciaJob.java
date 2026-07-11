package com.zenin.job;

import com.zenin.service.TransacaoRecorrenteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecorrenciaJob {

    private final TransacaoRecorrenteService transacaoRecorrenteService;

    @Scheduled(cron = "0 0 0 1 * *", zone = "America/Sao_Paulo")
    public void gerarTransacoesRecorrentes() {
        LocalDate mes = LocalDate.now().withDayOfMonth(1);
        log.info("Iniciando geração de transações recorrentes para {}/{}", mes.getMonthValue(), mes.getYear());

        int geradas = transacaoRecorrenteService.processarTodasRecorrencias();

        log.info("Geração concluída: {} transação(ões) criada(s)", geradas);
    }
}
