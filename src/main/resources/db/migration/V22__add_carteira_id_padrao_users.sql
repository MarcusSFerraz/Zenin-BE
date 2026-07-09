ALTER TABLE users
    ADD COLUMN carteira_id_padrao UUID REFERENCES carteiras(id) ON DELETE SET NULL;
