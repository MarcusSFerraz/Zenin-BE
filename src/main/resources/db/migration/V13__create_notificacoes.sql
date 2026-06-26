CREATE TABLE notificacoes (
    id         UUID    DEFAULT gen_random_uuid() PRIMARY KEY,
    usuario_id UUID    NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    titulo     TEXT    NOT NULL,
    mensagem   TEXT    NOT NULL,
    lida       BOOLEAN DEFAULT false,
    criado_em  TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);
