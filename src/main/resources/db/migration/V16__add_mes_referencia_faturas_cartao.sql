ALTER TABLE faturas_cartao
    ADD COLUMN mes_referencia VARCHAR(7) CHECK (mes_referencia ~ '^\d{4}-(0[1-9]|1[0-2])$');
