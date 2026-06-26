CREATE TABLE aportes_investimentos (
    id              UUID          DEFAULT gen_random_uuid() PRIMARY KEY,
    investimento_id UUID          NOT NULL REFERENCES investimentos(id) ON DELETE CASCADE,
    carteira_id     UUID          NOT NULL REFERENCES carteiras(id),
    usuario_id      UUID          NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    valor           NUMERIC(15,2) NOT NULL,
    criado_em       TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);
