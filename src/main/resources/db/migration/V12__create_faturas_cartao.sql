CREATE TABLE faturas_cartao (
    id              UUID          DEFAULT gen_random_uuid() PRIMARY KEY,
    usuario_id      UUID          NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    carteira_id     UUID          NOT NULL REFERENCES carteiras(id) ON DELETE CASCADE,
    data_vencimento DATE          NOT NULL,
    valor           NUMERIC(15,2) DEFAULT 0 NOT NULL,
    pago            BOOLEAN       DEFAULT false NOT NULL,
    criado_em       TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    atualizado_em   TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);
