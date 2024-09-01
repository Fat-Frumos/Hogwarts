INSERT INTO "roles" (id, permission)
VALUES (1, 'ROLE_TRAINEE'),
       (2, 'ROLE_TRAINER'),
       (3, 'ROLE_ADMIN');

INSERT INTO "users" (first_name, last_name, username, password, is_active, role_id, permission)
VALUES ('Ginevra', 'Granger', 'Ginevra.Granger', '$2a$10$Uz2nz5i1Ez27py80kBQo/OrdGyi972CjukUj4sQGgtPPxT7atQOU6', TRUE,
        (SELECT id FROM "roles" WHERE permission = 'ROLE_TRAINER'), 'ROLE_TRAINER'),
       ('Theodore', 'Nott', 'Theodore.Nott', '$2a$10$Uz2nz5i1Ez27py80kBQo/OrdGyi972CjukUj4sQGgtPPxT7atQOU6', TRUE,
        (SELECT id FROM "roles" WHERE permission = 'ROLE_TRAINER'), 'ROLE_TRAINER'),
       ('Daphne', 'Greengrass', 'Daphne.Greengrass', '$2a$10$Uz2nz5i1Ez27py80kBQo/OrdGyi972CjukUj4sQGgtPPxT7atQOU6',
        TRUE, (SELECT id FROM "roles" WHERE permission = 'ROLE_TRAINER'), 'ROLE_TRAINER'),
       ('Pansy', 'Parkinson', 'Pansy.Parkinson', '$2a$10$Uz2nz5i1Ez27py80kBQo/OrdGyi972CjukUj4sQGgtPPxT7atQOU6', TRUE,
        (SELECT id FROM "roles" WHERE permission = 'ROLE_TRAINEE'), 'ROLE_TRAINEE'),
       ('Tracey', 'Davis', 'Tracey.Davis', '$2a$10$Uz2nz5i1Ez27py80kBQo/OrdGyi972CjukUj4sQGgtPPxT7atQOU6', TRUE,
        (SELECT id FROM "roles" WHERE permission = 'ROLE_TRAINEE'), 'ROLE_TRAINEE'),
       ('Millicent', 'Bulstrode', 'Millicent.Bulstrode', '$2a$10$Uz2nz5i1Ez27py80kBQo/OrdGyi972CjukUj4sQGgtPPxT7atQOU6',
        TRUE, (SELECT id FROM "roles" WHERE permission = 'ROLE_TRAINEE'), 'ROLE_TRAINEE'),
       ('Gregory', 'Goyle', 'Gregory.Goyle', '$2a$10$Uz2nz5i1Ez27py80kBQo/OrdGyi972CjukUj4sQGgtPPxT7atQOU6', TRUE,
        (SELECT id FROM "roles" WHERE permission = 'ROLE_TRAINEE'), 'ROLE_TRAINEE'),
       ('Vincent', 'Crabbe', 'Vincent.Crabbe', '$2a$10$Uz2nz5i1Ez27py80kBQo/OrdGyi972CjukUj4sQGgtPPxT7atQOU6', TRUE,
        (SELECT id FROM "roles" WHERE permission = 'ROLE_TRAINEE'), 'ROLE_TRAINEE'),
       ('Padma', 'Patil', 'Padma.Patil', '$2a$10$Uz2nz5i1Ez27py80kBQo/OrdGyi972CjukUj4sQGgtPPxT7atQOU6', TRUE,
        (SELECT id FROM "roles" WHERE permission = 'ROLE_TRAINEE'), 'ROLE_TRAINEE'),
       ('Parvati', 'Patil', 'Parvati.Patil', '$2a$10$Uz2nz5i1Ez27py80kBQo/OrdGyi972CjukUj4sQGgtPPxT7atQOU6', TRUE,
        (SELECT id FROM "roles" WHERE permission = 'ROLE_TRAINEE'), 'ROLE_TRAINEE'),
       ('Harry', 'Potter', 'Harry.Potter', '$2a$10$AMs1YehIdq.7OBLM09E4ZuzUYvvB.VsvQVX6hfBxZN1N9qOPOAERe', TRUE,
        (SELECT id FROM "roles" WHERE permission = 'ROLE_TRAINEE'), 'ROLE_TRAINEE'),
       ('Hermione', 'Granger', 'Hermione.Granger', '$2a$10$0Vn9yPlu7/QJAzaC/xX/h.i6KmuKdpA9bcMtmvHYTE6fZmXbL.dJ6', TRUE,
        (SELECT id FROM "roles" WHERE permission = 'ROLE_TRAINEE'), 'ROLE_TRAINEE'),
       ('Ron', 'Weasley', 'Ron.Weasley', '$2a$10$Uz2nz5i1Ez27py80kBQo/OrdGyi972CjukUj4sQGgtPPxT7atQOU6', TRUE,
        (SELECT id FROM "roles" WHERE permission = 'ROLE_TRAINEE'), 'ROLE_TRAINEE'),
       ('Luna', 'Lovegood', 'Luna.Lovegood', '$2a$10$Uz2nz5i1Ez27py80kBQo/OrdGyi972CjukUj4sQGgtPPxT7atQOU6', TRUE,
        (SELECT id FROM "roles" WHERE permission = 'ROLE_TRAINEE'), 'ROLE_TRAINEE'),
       ('Ginny', 'Weasley', 'Ginny.Weasley', '$2a$10$Uz2nz5i1Ez27py80kBQo/OrdGyi972CjukUj4sQGgtPPxT7atQOU6', TRUE,
        (SELECT id FROM "roles" WHERE permission = 'ROLE_TRAINEE'), 'ROLE_TRAINEE'),
       ('Albus', 'Dumbledore', 'Albus.Dumbledore', '$2a$10$Uz2nz5i1Ez27py80kBQo/OrdGyi972CjukUj4sQGgtPPxT7atQOU6', TRUE,
        (SELECT id FROM "roles" WHERE permission = 'ROLE_TRAINER'), 'ROLE_TRAINER'),
       ('Severus', 'Snape', 'Severus.Snape', '$2a$10$Uz2nz5i1Ez27py80kBQo/OrdGyi972CjukUj4sQGgtPPxT7atQOU6', TRUE,
        (SELECT id FROM "roles" WHERE permission = 'ROLE_TRAINER'), 'ROLE_TRAINER'),
       ('Minerva', 'McGonagall', 'Minerva.McGonagall', '$2a$10$Uz2nz5i1Ez27py80kBQo/OrdGyi972CjukUj4sQGgtPPxT7atQOU6',
        TRUE, (SELECT id FROM "roles" WHERE permission = 'ROLE_TRAINER'), 'ROLE_TRAINER'),
       ('Rubeus', 'Hagrid', 'Rubeus.Hagrid', '$2a$10$Uz2nz5i1Ez27py80kBQo/OrdGyi972CjukUj4sQGgtPPxT7atQOU6', TRUE,
        (SELECT id FROM "roles" WHERE permission = 'ROLE_TRAINER'), 'ROLE_TRAINER'),
       ('Sybill', 'Trelawney', 'Sybill.Trelawney', '$2a$10$Uz2nz5i1Ez27py80kBQo/OrdGyi972CjukUj4sQGgtPPxT7atQOU6',
        FALSE, (SELECT id FROM "roles" WHERE permission = 'ROLE_TRAINER'), 'ROLE_TRAINER'),
       ('Sirius', 'Black', 'Sirius.Black', '$2a$10$Uz2nz5i1Ez27py80kBQo/OrdGyi972CjukUj4sQGgtPPxT7atQOU6', TRUE,
        (SELECT id FROM "roles" WHERE permission = 'ROLE_TRAINER'), 'ROLE_TRAINER'),
       ('Argus', 'Filch', 'Argus.Filch', '$2a$10$Uz2nz5i1Ez27py80kBQo/OrdGyi972CjukUj4sQGgtPPxT7atQOU6', TRUE,
        (SELECT id FROM "roles" WHERE permission = 'ROLE_TRAINER'), 'ROLE_TRAINER'),
       ('Filius', 'Flitwick', 'Filius.Flitwick', '$2a$10$Uz2nz5i1Ez27py80kBQo/OrdGyi972CjukUj4sQGgtPPxT7atQOU6', TRUE,
        (SELECT id FROM "roles" WHERE permission = 'ROLE_TRAINER'), 'ROLE_TRAINER'),
       ('Gilderoy', 'Lockhart', 'Gilderoy.Lockhart', '$2a$10$Uz2nz5i1Ez27py80kBQo/OrdGyi972CjukUj4sQGgtPPxT7atQOU6',
        TRUE, (SELECT id FROM "roles" WHERE permission = 'ROLE_TRAINER'), 'ROLE_TRAINER'),
       ('Poppy', 'Pomfrey', 'Poppy.Pomfrey', '$2a$10$Uz2nz5i1Ez27py80kBQo/OrdGyi972CjukUj4sQGgtPPxT7atQOU6', TRUE,
        (SELECT id FROM "roles" WHERE permission = 'ROLE_TRAINER'), 'ROLE_TRAINER'),
       ('Horace', 'Slughorn', 'Horace.Slughorn', '$2a$10$Uz2nz5i1Ez27py80kBQo/OrdGyi972CjukUj4sQGgtPPxT7atQOU6', TRUE,
        (SELECT id FROM "roles" WHERE permission = 'ROLE_TRAINER'), 'ROLE_TRAINER'),
       ('Godric', 'Gryffindor', 'Godric.Gryffindor', '$2a$10$Uz2nz5i1Ez27py80kBQo/OrdGyi972CjukUj4sQGgtPPxT7atQOU6',
        TRUE, (SELECT id FROM "roles" WHERE permission = 'ROLE_ADMIN'), 'ROLE_ADMIN'),
       ('Helga', 'Hufflepuff', 'Helga.Hufflepuff', '$2a$10$Uz2nz5i1Ez27py80kBQo/OrdGyi972CjukUj4sQGgtPPxT7atQOU6', TRUE,
        (SELECT id FROM "roles" WHERE permission = 'ROLE_ADMIN'), 'ROLE_ADMIN'),
       ('Rowena', 'Ravenclaw', 'Rowena.Ravenclaw', '$2a$10$Uz2nz5i1Ez27py80kBQo/OrdGyi972CjukUj4sQGgtPPxT7atQOU6', TRUE,
        (SELECT id FROM "roles" WHERE permission = 'ROLE_ADMIN'), 'ROLE_ADMIN'),
       ('Salazar', 'Slytherin', 'Salazar.Slytherin', '$2a$10$Uz2nz5i1Ez27py80kBQo/OrdGyi972CjukUj4sQGgtPPxT7atQOU6',
        TRUE, (SELECT id FROM "roles" WHERE permission = 'ROLE_ADMIN'), 'ROLE_ADMIN');


INSERT INTO "training_type" (training_type_name)
VALUES ('CARDIO'),
       ('STRENGTH'),
       ('FLEXIBILITY'),
       ('BALANCE'),
       ('COORDINATION'),
       ('CARE'),
       ('POTIONS'),
       ('DEFENSE'),
       ('QUIDDITCH'),
       ('HERBOLOGY'),
       ('DIVINATION'),
       ('TRANSFIGURATION');


INSERT INTO "trainer" (specialization_id, user_id)
VALUES ((SELECT id FROM "training_type" WHERE training_type_name = 'POTIONS'),
        (SELECT id FROM "users" WHERE username = 'Severus.Snape')),
       ((SELECT id FROM "training_type" WHERE training_type_name = 'CARE'),
        (SELECT id FROM "users" WHERE username = 'Rubeus.Hagrid')),
       ((SELECT id FROM "training_type" WHERE training_type_name = 'TRANSFIGURATION'),
        (SELECT id FROM "users" WHERE username = 'Minerva.McGonagall')),
       ((SELECT id FROM "training_type" WHERE training_type_name = 'QUIDDITCH'),
        (SELECT id FROM "users" WHERE username = 'Albus.Dumbledore')),
       ((SELECT id FROM "training_type" WHERE training_type_name = 'CARDIO'),
        (SELECT id FROM "users" WHERE username = 'Argus.Filch')),
       ((SELECT id FROM "training_type" WHERE training_type_name = 'STRENGTH'),
        (SELECT id FROM "users" WHERE username = 'Filius.Flitwick')),
       ((SELECT id FROM "training_type" WHERE training_type_name = 'FLEXIBILITY'),
        (SELECT id FROM "users" WHERE username = 'Gilderoy.Lockhart')),
       ((SELECT id FROM "training_type" WHERE training_type_name = 'BALANCE'),
        (SELECT id FROM "users" WHERE username = 'Poppy.Pomfrey')),
       ((SELECT id FROM "training_type" WHERE training_type_name = 'COORDINATION'),
        (SELECT id FROM "users" WHERE username = 'Horace.Slughorn')),
       ((SELECT id FROM "training_type" WHERE training_type_name = 'HERBOLOGY'),
        (SELECT id FROM "users" WHERE username = 'Godric.Gryffindor')),
       ((SELECT id FROM "training_type" WHERE training_type_name = 'DIVINATION'),
        (SELECT id FROM "users" WHERE username = 'Salazar.Slytherin'));


INSERT INTO "trainee" (date_of_birth, address, user_id)
VALUES ('1980-07-31', '4 Privet Drive, Little Whinging', (SELECT id FROM "users" WHERE username = 'Harry.Potter')),
       ('1979-09-19', 'Hogwarts, Scotland', (SELECT id FROM "users" WHERE username = 'Hermione.Granger')),
       ('1980-03-01', 'The Burrow, Ottery St Catchpole', (SELECT id FROM "users" WHERE username = 'Ron.Weasley')),
       ('1981-02-13', 'The Lovegood House', (SELECT id FROM "users" WHERE username = 'Luna.Lovegood')),
       ('1981-08-11', 'The Burrow, Ottery St Catchpole', (SELECT id FROM "users" WHERE username = 'Ginny.Weasley')),
       ('1981-08-11', 'Hogwarts, Scotland', (SELECT id FROM "users" WHERE username = 'Ginevra.Granger')),
       ('1979-09-13', 'Nott Manor, England', (SELECT id FROM "users" WHERE username = 'Theodore.Nott')),
       ('1980-04-29', 'Greengrass Manor, England', (SELECT id FROM "users" WHERE username = 'Daphne.Greengrass')),
       ('1980-10-28', 'Parkinson Manor, England', (SELECT id FROM "users" WHERE username = 'Pansy.Parkinson')),
       ('1980-05-12', 'Davis House, England', (SELECT id FROM "users" WHERE username = 'Tracey.Davis')),
       ('1979-07-19', 'Bulstrode House, England', (SELECT id FROM "users" WHERE username = 'Millicent.Bulstrode')),
       ('1980-01-14', 'Goyle House, England', (SELECT id FROM "users" WHERE username = 'Gregory.Goyle')),
       ('1980-03-11', 'Crabbe House, England', (SELECT id FROM "users" WHERE username = 'Vincent.Crabbe')),
       ('1979-11-22', 'Patil House, England', (SELECT id FROM "users" WHERE username = 'Padma.Patil')),
       ('1979-11-22', 'Patil House, England', (SELECT id FROM "users" WHERE username = 'Parvati.Patil'));


INSERT INTO "training" (trainee_id, trainer_id, training_name, training_type_id, training_date, training_duration)
VALUES ((SELECT id FROM "trainee" WHERE user_id = (SELECT id FROM "users" WHERE username = 'Harry.Potter')),
        (SELECT id FROM "trainer" WHERE user_id = (SELECT id FROM "users" WHERE username = 'Severus.Snape')),
        'Potions 101',
        (SELECT id FROM "training_type" WHERE training_type_name = 'POTIONS'),
        '2024-09-01',
        120),

       ((SELECT id FROM "trainee" WHERE user_id = (SELECT id FROM "users" WHERE username = 'Hermione.Granger')),
        (SELECT id FROM "trainer" WHERE user_id = (SELECT id FROM "users" WHERE username = 'Minerva.McGonagall')),
        'Transfiguration Basics',
        (SELECT id FROM "training_type" WHERE training_type_name = 'TRANSFIGURATION'),
        '2024-09-02',
        90),

       ((SELECT id FROM "trainee" WHERE user_id = (SELECT id FROM "users" WHERE username = 'Ron.Weasley')),
        (SELECT id FROM "trainer" WHERE user_id = (SELECT id FROM "users" WHERE username = 'Gilderoy.Lockhart')),
        'Advanced Charms',
        (SELECT id FROM "training_type" WHERE training_type_name = 'DEFENSE'),
        '2024-09-03',
        75);

INSERT INTO "trainee_trainer" (trainee_id, trainer_id)
VALUES ((SELECT id FROM "trainee" WHERE user_id = (SELECT id FROM "users" WHERE username = 'Harry.Potter')),
        (SELECT id FROM "trainer" WHERE user_id = (SELECT id FROM "users" WHERE username = 'Severus.Snape'))),

       ((SELECT id FROM "trainee" WHERE user_id = (SELECT id FROM "users" WHERE username = 'Hermione.Granger')),
        (SELECT id FROM "trainer" WHERE user_id = (SELECT id FROM "users" WHERE username = 'Minerva.McGonagall'))),

       ((SELECT id FROM "trainee" WHERE user_id = (SELECT id FROM "users" WHERE username = 'Ron.Weasley')),
        (SELECT id FROM "trainer" WHERE user_id = (SELECT id FROM "users" WHERE username = 'Gilderoy.Lockhart')));

INSERT INTO "roles" (id, permission)
VALUES (1, 'ROLE_TRAINEE'),
       (2, 'ROLE_TRAINER'),
       (3, 'ROLE_ADMIN');

SELECT column_name
FROM information_schema.columns
WHERE table_name = 'users';

SELECT * FROM users WHERE username ='Salazar.Slytherin';
SELECT id FROM "roles" WHERE permission = 'ROLE_TRAINER'