ALTER TABLE metas
    ADD COLUMN investimento_id UUID REFERENCES investimentos(id) ON DELETE SET NULL;
