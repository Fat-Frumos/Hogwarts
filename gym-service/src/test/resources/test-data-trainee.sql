INSERT INTO users (id, first_name, last_name, username, password, is_active, permission)
VALUES (1, 'Draco', 'Malfoy', 'draco.malfoy.unique', 'password123', TRUE, 'ROLE_TRAINEE'),
       (2, 'Harry', 'Potter', 'harry.potter.unique', 'password456', TRUE, 'ROLE_TRAINEE');

INSERT INTO trainee (id, date_of_birth, address, user_id)
VALUES (1, '2000-01-01', 'Malfoy Manor', 1),
       (2, '1999-07-31', '4 Privet Drive', 2);
