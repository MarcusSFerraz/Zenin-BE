CREATE TABLE carteiras (
    id             UUID          DEFAULT gen_random_uuid() PRIMARY KEY,
    usuario_id     UUID          NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    nome           VARCHAR(255)  NOT NULL,
    cor            VARCHAR(20)   DEFAULT '#3B82F6',
    saldo_inicial  NUMERIC(15,2) DEFAULT 0,
    saldo_atual    NUMERIC(15,2) DEFAULT 0,
    cartao_credito BOOLEAN       DEFAULT false NOT NULL,
    criado_em      TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    atualizado_em  TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);
