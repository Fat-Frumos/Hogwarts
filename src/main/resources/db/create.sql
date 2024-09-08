DROP TABLE IF EXISTS "trainee_trainer" CASCADE;
DROP TABLE IF EXISTS "training" CASCADE;
DROP TABLE IF EXISTS "trainer" CASCADE;
DROP TABLE IF EXISTS "training_type" CASCADE;
DROP TABLE IF EXISTS "trainee" CASCADE;
DROP TABLE IF EXISTS "users" CASCADE;
DROP TABLE IF EXISTS "role_authorities" CASCADE;
DROP TABLE IF EXISTS "roles" CASCADE;
DROP TABLE IF EXISTS "tokens" CASCADE;

CREATE TABLE "roles"
(
    id         SERIAL PRIMARY KEY,
    permission VARCHAR(255) NOT NULL
);

CREATE TABLE "role_authorities"
(
    role_id   BIGINT       NOT NULL,
    authority VARCHAR(255) NOT NULL,
    FOREIGN KEY (role_id) REFERENCES "roles" (id) ON DELETE CASCADE
);

CREATE TABLE "users"
(
    id         SERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    username   VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    is_active  BOOLEAN      NOT NULL,
    permission VARCHAR(255),
    role_id    BIGINT,
    FOREIGN KEY (role_id) REFERENCES "roles" (id) ON DELETE SET NULL
);

CREATE TABLE "trainee"
(
    id            SERIAL PRIMARY KEY,
    date_of_birth DATE,
    address       VARCHAR(255),
    user_id       BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES "users" (id) ON DELETE CASCADE
);

CREATE TABLE "training_type"
(
    id                 SERIAL PRIMARY KEY,
    training_type_name VARCHAR(50) NOT NULL
);

CREATE TABLE "trainer"
(
    id                SERIAL PRIMARY KEY,
    training_type_id BIGINT NOT NULL,
    user_id           BIGINT NOT NULL,
    FOREIGN KEY (training_type_id) REFERENCES training_type (id),
    FOREIGN KEY (user_id) REFERENCES "users" (id) ON DELETE CASCADE
);

CREATE TABLE "training"
(
    id                SERIAL PRIMARY KEY,
    trainee_id        BIGINT       NOT NULL,
    trainer_id        BIGINT       NOT NULL,
    training_name     VARCHAR(255) NOT NULL,
    training_type_id  BIGINT       NOT NULL,
    training_date     DATE         NOT NULL,
    training_duration INTEGER      NOT NULL,
    FOREIGN KEY (trainee_id) REFERENCES "trainee" (id) ON DELETE CASCADE,
    FOREIGN KEY (trainer_id) REFERENCES "trainer" (id) ON DELETE CASCADE,
    FOREIGN KEY (training_type_id) REFERENCES "training_type" (id)
);

CREATE TABLE "trainee_trainer"
(
    trainee_id BIGINT NOT NULL,
    trainer_id BIGINT NOT NULL,
    PRIMARY KEY (trainee_id, trainer_id),
    FOREIGN KEY (trainee_id) REFERENCES "trainee" (id) ON DELETE CASCADE,
    FOREIGN KEY (trainer_id) REFERENCES "trainer" (id) ON DELETE CASCADE
);

CREATE TABLE "tokens"
(
    id               SERIAL PRIMARY KEY,
    token_type       VARCHAR(255) DEFAULT 'BEARER',
    access_token     VARCHAR(255) UNIQUE NOT NULL,
    access_token_ttl BIGINT,
    revoked          BOOLEAN      DEFAULT FALSE,
    expired          BOOLEAN      DEFAULT FALSE,
    user_id          INTEGER,
    FOREIGN KEY (user_id) REFERENCES "users" (id) ON DELETE SET NULL
);
