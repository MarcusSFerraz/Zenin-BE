ALTER TABLE users
    ADD COLUMN url_avatar       VARCHAR(500),
    ADD COLUMN preferencia_tema VARCHAR(20)  DEFAULT 'dark',
    ADD COLUMN dia_pagamento    INTEGER      DEFAULT 5 CHECK (dia_pagamento >= 1 AND dia_pagamento <= 31),
    ADD COLUMN numero_whatsapp  VARCHAR(20),
    ADD COLUMN ativo            BOOLEAN      DEFAULT false,
    ADD COLUMN criado_em        TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    ADD COLUMN atualizado_em    TIMESTAMP WITH TIME ZONE DEFAULT NOW();
