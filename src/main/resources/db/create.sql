CREATE TABLE "user"
(
    id         SERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    username   VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    is_active  BOOLEAN      NOT NULL
);

CREATE TABLE "trainee"
(
    id            SERIAL PRIMARY KEY,
    date_of_birth DATE,
    address       VARCHAR(255),
    user_id       BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES "user" (id) ON DELETE CASCADE
);

CREATE TABLE "training_type"
(
    id                 SERIAL PRIMARY KEY,
    training_type_name VARCHAR(50) NOT NULL
);

CREATE TABLE "trainer"
(
    id                SERIAL PRIMARY KEY,
    specialization_id BIGINT NOT NULL,
    user_id           BIGINT NOT NULL,
    FOREIGN KEY (specialization_id) REFERENCES training_type (id),
    FOREIGN KEY (user_id) REFERENCES "user" (id) ON DELETE CASCADE
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
