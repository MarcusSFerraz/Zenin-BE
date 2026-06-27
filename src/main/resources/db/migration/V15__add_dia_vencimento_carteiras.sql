ALTER TABLE carteiras
    ADD COLUMN dia_vencimento INTEGER CHECK (dia_vencimento BETWEEN 1 AND 28);
