CREATE TABLE transacoes_recorrentes (
    id             UUID          DEFAULT gen_random_uuid() PRIMARY KEY,
    usuario_id     UUID          NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    categoria_id   UUID          REFERENCES categorias(id) ON DELETE SET NULL,
    carteira_id    UUID          NOT NULL REFERENCES carteiras(id) ON DELETE CASCADE,
    tipo           VARCHAR(20)   NOT NULL,
    valor          NUMERIC(15,2) NOT NULL,
    nome           VARCHAR(255)  NOT NULL,
    ativo          BOOLEAN       DEFAULT true NOT NULL,
    dia_vencimento INTEGER,
    criado_em      TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    atualizado_em  TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);
