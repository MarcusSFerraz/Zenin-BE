package com.zenin.dto.request;

import com.zenin.model.Role;

public record AdminUsuarioUpdateRequest(
        Role role,
        Boolean ativo
) {}
