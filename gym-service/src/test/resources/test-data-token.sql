INSERT INTO users (id, first_name, last_name, username, password, is_active, permission)
VALUES (1, 'Harry', 'Potter', 'Harry.Potter', 'password123', TRUE, 'ROLE_TRAINEE'),
       (2, 'Hermione', 'Granger', 'Hermione.Granger', 'password456', TRUE, 'ROLE_TRAINEE');

INSERT INTO tokens (id, token_type, access_token, access_token_ttl, revoked, expired, user_id)
VALUES (1, 'Bearer', 'validJwtToken1', 3600, FALSE, FALSE, 1),
       (2, 'Bearer', 'validJwtToken2', 3600, FALSE, FALSE, 2),
       (3, 'Bearer', 'invalidJwtToken', 3600, TRUE, TRUE, 1);
