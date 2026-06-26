CREATE TABLE categorias (
    id            UUID         DEFAULT gen_random_uuid() PRIMARY KEY,
    usuario_id    UUID         NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    nome          VARCHAR(255) NOT NULL,
    tipo          VARCHAR(20)  NOT NULL,
    cor           VARCHAR(20)  DEFAULT '#3B82F6',
    icone         VARCHAR(100),
    criado_em     TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    atualizado_em TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);
