CREATE TABLE investimentos (
    id               UUID          DEFAULT gen_random_uuid() PRIMARY KEY,
    usuario_id       UUID          NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    carteira_id      UUID          NOT NULL REFERENCES carteiras(id),
    taxa_id          UUID          REFERENCES taxas_investimento(id),
    tipo             TEXT          DEFAULT 'tesouro_selic' NOT NULL,
    valor_inicial    NUMERIC(15,2) NOT NULL,
    valor_atual      NUMERIC(15,2) NOT NULL,
    quantidade_cotas NUMERIC(20,8) DEFAULT 0,
    data_inicio      DATE          DEFAULT CURRENT_DATE NOT NULL,
    data_vencimento  DATE          NOT NULL,
    ativo            BOOLEAN       DEFAULT true NOT NULL,
    criado_em        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    atualizado_em    TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);
