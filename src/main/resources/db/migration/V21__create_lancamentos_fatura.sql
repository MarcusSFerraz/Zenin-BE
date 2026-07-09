CREATE TABLE lancamentos_fatura (
    id           UUID         NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    fatura_id    UUID         NOT NULL REFERENCES faturas_cartao(id) ON DELETE CASCADE,
    usuario_id   UUID         NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    descricao    VARCHAR(255) NOT NULL,
    valor        NUMERIC(15, 2) NOT NULL CHECK (valor > 0),
    tipo         VARCHAR(10)  NOT NULL CHECK (tipo IN ('cobranca', 'credito')),
    criado_em    TIMESTAMPTZ  NOT NULL DEFAULT now(),
    atualizado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_lancamentos_fatura_fatura_id ON lancamentos_fatura(fatura_id);
CREATE INDEX idx_lancamentos_fatura_usuario_id ON lancamentos_fatura(usuario_id);
