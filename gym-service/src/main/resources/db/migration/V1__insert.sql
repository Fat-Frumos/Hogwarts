INSERT INTO "roles" (id, permission)
VALUES (1, 'ROLE_TRAINEE'),
       (2, 'ROLE_TRAINER'),
       (3, 'ROLE_ADMIN');

INSERT INTO "role_authorities" (role_id, authority)
VALUES (1, 'trainee:read'),
       (2, 'trainer:create'),
       (2, 'trainer:read'),
       (2, 'trainer:update'),
       (3, 'admin:create'),
       (3, 'admin:read'),
       (3, 'admin:update'),
       (3, 'admin:delete');

INSERT INTO "users" (first_name, last_name, username, password, is_active, permission)
VALUES ('Ginevra', 'Granger', 'Ginevra.Granger', '$2a$10$Uz2nz5i1Ez27py80kBQo/OrdGyi972CjukUj4sQGgtPPxT7atQOU6', TRUE,
        'ROLE_TRAINER'),
       ('Theodore', 'Nott', 'Theodore.Nott', '$2a$10$Uz2nz5i1Ez27py80kBQo/OrdGyi972CjukUj4sQGgtPPxT7atQOU6', TRUE,
        'ROLE_TRAINER'),
       ('Daphne', 'Greengrass', 'Daphne.Greengrass', '$2a$10$Uz2nz5i1Ez27py80kBQo/OrdGyi972CjukUj4sQGgtPPxT7atQOU6',
        TRUE, 'ROLE_TRAINER'),
       ('Pansy', 'Parkinson', 'Pansy.Parkinson', '$2a$10$Uz2nz5i1Ez27py80kBQo/OrdGyi972CjukUj4sQGgtPPxT7atQOU6', TRUE,
        'ROLE_TRAINEE'),
       ('Tracey', 'Davis', 'Tracey.Davis', '$2a$10$Uz2nz5i1Ez27py80kBQo/OrdGyi972CjukUj4sQGgtPPxT7atQOU6', TRUE,
        'ROLE_TRAINEE'),
       ('Millicent', 'Bulstrode', 'Millicent.Bulstrode', '$2a$10$Uz2nz5i1Ez27py80kBQo/OrdGyi972CjukUj4sQGgtPPxT7atQOU6',
        TRUE, 'ROLE_TRAINEE'),
       ('Gregory', 'Goyle', 'Gregory.Goyle', '$2a$10$Uz2nz5i1Ez27py80kBQo/OrdGyi972CjukUj4sQGgtPPxT7atQOU6', TRUE,
        'ROLE_TRAINEE'),
       ('Vincent', 'Crabbe', 'Vincent.Crabbe', '$2a$10$Uz2nz5i1Ez27py80kBQo/OrdGyi972CjukUj4sQGgtPPxT7atQOU6', TRUE,
        'ROLE_TRAINEE'),
       ('Padma', 'Patil', 'Padma.Patil', '$2a$10$Uz2nz5i1Ez27py80kBQo/OrdGyi972CjukUj4sQGgtPPxT7atQOU6', TRUE,
        'ROLE_TRAINEE'),
       ('Parvati', 'Patil', 'Parvati.Patil', '$2a$10$Uz2nz5i1Ez27py80kBQo/OrdGyi972CjukUj4sQGgtPPxT7atQOU6', TRUE,
        'ROLE_TRAINEE'),
       ('Harry', 'Potter', 'Harry.Potter', '$2a$10$AMs1YehIdq.7OBLM09E4ZuzUYvvB.VsvQVX6hfBxZN1N9qOPOAERe', TRUE,
        'ROLE_TRAINEE'),
       ('Hermione', 'Granger', 'Hermione.Granger', '$2a$10$0Vn9yPlu7/QJAzaC/xX/h.i6KmuKdpA9bcMtmvHYTE6fZmXbL.dJ6', TRUE,
        'ROLE_TRAINEE'),
       ('Ron', 'Weasley', 'Ron.Weasley', '$2a$10$Uz2nz5i1Ez27py80kBQo/OrdGyi972CjukUj4sQGgtPPxT7atQOU6', TRUE,
        'ROLE_TRAINEE'),
       ('Luna', 'Lovegood', 'Luna.Lovegood', '$2a$10$Uz2nz5i1Ez27py80kBQo/OrdGyi972CjukUj4sQGgtPPxT7atQOU6', TRUE,
        'ROLE_TRAINEE'),
       ('Ginny', 'Weasley', 'Ginny.Weasley', '$2a$10$Uz2nz5i1Ez27py80kBQo/OrdGyi972CjukUj4sQGgtPPxT7atQOU6', TRUE,
        'ROLE_TRAINEE'),
       ('Albus', 'Dumbledore', 'Albus.Dumbledore', '$2a$10$Uz2nz5i1Ez27py80kBQo/OrdGyi972CjukUj4sQGgtPPxT7atQOU6', TRUE,
        'ROLE_TRAINER'),
       ('Severus', 'Snape', 'Severus.Snape', '$2a$10$Uz2nz5i1Ez27py80kBQo/OrdGyi972CjukUj4sQGgtPPxT7atQOU6', TRUE,
        'ROLE_TRAINER'),
       ('Minerva', 'McGonagall', 'Minerva.McGonagall', '$2a$10$Uz2nz5i1Ez27py80kBQo/OrdGyi972CjukUj4sQGgtPPxT7atQOU6',
        TRUE, 'ROLE_TRAINER'),
       ('Rubeus', 'Hagrid', 'Rubeus.Hagrid', '$2a$10$Uz2nz5i1Ez27py80kBQo/OrdGyi972CjukUj4sQGgtPPxT7atQOU6', TRUE,
        'ROLE_TRAINER'),
       ('Sybill', 'Trelawney', 'Sybill.Trelawney', '$2a$10$Uz2nz5i1Ez27py80kBQo/OrdGyi972CjukUj4sQGgtPPxT7atQOU6',
        FALSE, 'ROLE_TRAINER'),
       ('Sirius', 'Black', 'Sirius.Black', '$2a$10$Uz2nz5i1Ez27py80kBQo/OrdGyi972CjukUj4sQGgtPPxT7atQOU6', TRUE,
        'ROLE_TRAINER'),
       ('Argus', 'Filch', 'Argus.Filch', '$2a$10$Uz2nz5i1Ez27py80kBQo/OrdGyi972CjukUj4sQGgtPPxT7atQOU6', TRUE,
        'ROLE_TRAINER'),
       ('Filius', 'Flitwick', 'Filius.Flitwick', '$2a$10$Uz2nz5i1Ez27py80kBQo/OrdGyi972CjukUj4sQGgtPPxT7atQOU6', TRUE,
        'ROLE_TRAINER'),
       ('Gilderoy', 'Lockhart', 'Gilderoy.Lockhart', '$2a$10$Uz2nz5i1Ez27py80kBQo/OrdGyi972CjukUj4sQGgtPPxT7atQOU6',
        TRUE, 'ROLE_TRAINER'),
       ('Poppy', 'Pomfrey', 'Poppy.Pomfrey', '$2a$10$Uz2nz5i1Ez27py80kBQo/OrdGyi972CjukUj4sQGgtPPxT7atQOU6', TRUE,
        'ROLE_TRAINER'),
       ('Horace', 'Slughorn', 'Horace.Slughorn', '$2a$10$Uz2nz5i1Ez27py80kBQo/OrdGyi972CjukUj4sQGgtPPxT7atQOU6', TRUE,
        'ROLE_TRAINER'),
       ('Godric', 'Gryffindor', 'Godric.Gryffindor', '$2a$10$Uz2nz5i1Ez27py80kBQo/OrdGyi972CjukUj4sQGgtPPxT7atQOU6',
        TRUE, 'ROLE_ADMIN'),
       ('Helga', 'Hufflepuff', 'Helga.Hufflepuff', '$2a$10$Uz2nz5i1Ez27py80kBQo/OrdGyi972CjukUj4sQGgtPPxT7atQOU6', TRUE,
        'ROLE_ADMIN'),
       ('Rowena', 'Ravenclaw', 'Rowena.Ravenclaw', '$2a$10$Uz2nz5i1Ez27py80kBQo/OrdGyi972CjukUj4sQGgtPPxT7atQOU6', TRUE,
        'ROLE_ADMIN'),
       ('Salazar', 'Slytherin', 'Salazar.Slytherin', '$2a$10$Uz2nz5i1Ez27py80kBQo/OrdGyi972CjukUj4sQGgtPPxT7atQOU6',
        TRUE, 'ROLE_ADMIN'),
       ('Lord', 'Voldemort', 'Lord.Voldemort', '$2a$12$EcEravLOsg78oTEjy9Y4v.BbcHAfMyUCqmi7QdVihKC2F9GtWttz6',
        TRUE, 'ROLE_ADMIN'); -- You-Know-Who
