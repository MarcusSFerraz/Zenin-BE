CREATE TABLE transacoes (
    id                      UUID          DEFAULT gen_random_uuid() PRIMARY KEY,
    usuario_id              UUID          NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    categoria_id            UUID          REFERENCES categorias(id) ON DELETE SET NULL,
    carteira_id             UUID          NOT NULL REFERENCES carteiras(id) ON DELETE CASCADE,
    transacao_recorrente_id UUID          REFERENCES transacoes_recorrentes(id) ON DELETE SET NULL,
    tipo                    VARCHAR(20)   NOT NULL,
    valor                   NUMERIC(15,2) NOT NULL,
    descricao               TEXT,
    data                    DATE          NOT NULL DEFAULT CURRENT_DATE,
    data_vencimento         DATE,
    pago                    BOOLEAN       DEFAULT false NOT NULL,
    pago_em                 TIMESTAMP WITH TIME ZONE,
    criado_em               TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    atualizado_em           TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);
