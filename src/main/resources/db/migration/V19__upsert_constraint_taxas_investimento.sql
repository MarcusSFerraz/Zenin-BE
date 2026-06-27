ALTER TABLE taxas_investimento
    DROP CONSTRAINT IF EXISTS taxas_investimento_tipo_data_referencia_key;

CREATE UNIQUE INDEX taxas_investimento_referencia_uidx
    ON taxas_investimento (referencia)
    WHERE referencia IS NOT NULL;
