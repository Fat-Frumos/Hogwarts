package com.epam.esm.gym.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Permission {

    ADMIN_CREATE("admin:create"),
    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_DELETE("admin:delete"),

    TRAINER_CREATE("trainer:create"),
    TRAINER_READ("trainer:read"),
    TRAINER_UPDATE("trainer:update"),

    TRAINEE_READ("trainee:read");

    private final String authority;
}
