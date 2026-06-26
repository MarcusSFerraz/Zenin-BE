CREATE TABLE metas (
    id            UUID          DEFAULT gen_random_uuid() PRIMARY KEY,
    usuario_id    UUID          NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    categoria_id  UUID          REFERENCES categorias(id) ON DELETE SET NULL,
    nome          VARCHAR(255)  NOT NULL,
    valor_alvo    NUMERIC(15,2) NOT NULL,
    valor_atual   NUMERIC(15,2) DEFAULT 0,
    periodo       VARCHAR(20)   NOT NULL,
    data_inicio   DATE          NOT NULL,
    data_fim      DATE          NOT NULL,
    cor           VARCHAR(20)   DEFAULT '#3B82F6',
    icone         VARCHAR(100)  DEFAULT 'target',
    criado_em     TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    atualizado_em TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);
