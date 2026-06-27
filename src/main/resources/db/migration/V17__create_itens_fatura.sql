CREATE TABLE itens_fatura (
    id           UUID          DEFAULT gen_random_uuid() PRIMARY KEY,
    fatura_id    UUID          NOT NULL REFERENCES faturas_cartao(id) ON DELETE CASCADE,
    usuario_id   UUID          NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    descricao    VARCHAR(255)  NOT NULL,
    valor        NUMERIC(15,2) NOT NULL,
    data_compra  DATE,
    criado_em    TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    atualizado_em TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);
