CREATE TABLE aportes_metas (
    id          UUID          DEFAULT gen_random_uuid() PRIMARY KEY,
    meta_id     UUID          NOT NULL REFERENCES metas(id) ON DELETE CASCADE,
    carteira_id UUID          NOT NULL REFERENCES carteiras(id) ON DELETE CASCADE,
    usuario_id  UUID          NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    valor       NUMERIC(15,2) NOT NULL,
    criado_em   TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);
