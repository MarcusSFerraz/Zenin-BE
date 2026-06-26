CREATE TABLE taxas_investimento (
    id              UUID    DEFAULT gen_random_uuid() PRIMARY KEY,
    tipo            TEXT    NOT NULL,
    nome            VARCHAR(50),
    taxa_anual      NUMERIC,
    taxa_diaria     NUMERIC NOT NULL,
    pu_venda        NUMERIC,
    pu_base         NUMERIC,
    vencimento      DATE,
    data_referencia DATE    NOT NULL,
    criado_em       TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE (tipo, data_referencia)
);
