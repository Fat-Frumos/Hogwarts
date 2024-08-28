INSERT INTO "user" (first_name, last_name, username, password, is_active)
VALUES ('Ginevra', 'Granger', 'Ginevra.Granger', 'Password130', TRUE),
       ('Theodore', 'Nott', 'Theodore.Nott', 'Password131', TRUE),
       ('Daphne', 'Greengrass', 'Daphne.Greengrass', 'Password132', TRUE),
       ('Pansy', 'Parkinson', 'Pansy.Parkinson', 'Password133', TRUE),
       ('Tracey', 'Davis', 'Tracey.Davis', 'Password134', TRUE),
       ('Millicent', 'Bulstrode', 'Millicent.Bulstrode', 'Password135', TRUE),
       ('Gregory', 'Goyle', 'Gregory.Goyle', 'Password136', TRUE),
       ('Vincent', 'Crabbe', 'Vincent.Crabbe', 'Password137', TRUE),
       ('Padma', 'Patil', 'Padma.Patil', 'Password138', TRUE),
       ('Parvati', 'Patil', 'Parvati.Patil', 'Password139', TRUE),
       ('Harry', 'Potter', 'Harry.Potter', 'Password123', TRUE),
       ('Hermione', 'Granger', 'Hermione.Granger', 'Password456', TRUE),
       ('Ron', 'Weasley', 'Ron.Weasley', 'Password789', TRUE),
       ('Luna', 'Lovegood', 'Luna.Lovegood', 'Password115', TRUE),
       ('Ginny', 'Weasley', 'Ginny.Weasley', 'Password116', TRUE),
       ('Albus', 'Dumbledore', 'Albus.Dumbledore', 'Password101', TRUE),
       ('Severus', 'Snape', 'Severus.Snape', 'Password111', TRUE),
       ('Minerva', 'McGonagall', 'Minerva.McGonagall', 'Password112', TRUE),
       ('Rubeus', 'Hagrid', 'Rubeus.Hagrid', 'Password113', TRUE),
       ('Sybill', 'Trelawney', 'Sybill.Trelawney', 'Password114', FALSE),
       ('Sirius', 'Black', 'Sirius.Black', 'Password119', TRUE),
       ('Argus', 'Filch', 'Argus.Filch', 'Password120', TRUE),
       ('Filius', 'Flitwick', 'Filius.Flitwick', 'Password121', TRUE),
       ('Gilderoy', 'Lockhart', 'Gilderoy.Lockhart', 'Password122', TRUE),
       ('Poppy', 'Pomfrey', 'Poppy.Pomfrey', 'Password123', TRUE),
       ('Horace', 'Slughorn', 'Horace.Slughorn', 'Password125', TRUE),
       ('Godric', 'Gryffindor', 'Godric.Gryffindor', 'Password126', TRUE),
       ('Helga', 'Hufflepuff', 'Helga.Hufflepuff', 'Password127', TRUE),
       ('Rowena', 'Ravenclaw', 'Rowena.Ravenclaw', 'Password128', TRUE),
       ('Salazar', 'Slytherin', 'Salazar.Slytherin', 'Password129', TRUE);



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
        (SELECT id FROM "user" WHERE username = 'Severus.Snape')),
       ((SELECT id FROM "training_type" WHERE training_type_name = 'CARE'),
        (SELECT id FROM "user" WHERE username = 'Rubeus.Hagrid')),
       ((SELECT id FROM "training_type" WHERE training_type_name = 'TRANSFIGURATION'),
        (SELECT id FROM "user" WHERE username = 'Minerva.McGonagall')),
       ((SELECT id FROM "training_type" WHERE training_type_name = 'QUIDDITCH'),
        (SELECT id FROM "user" WHERE username = 'Albus.Dumbledore')),
       ((SELECT id FROM "training_type" WHERE training_type_name = 'CARDIO'),
        (SELECT id FROM "user" WHERE username = 'Argus.Filch')),
       ((SELECT id FROM "training_type" WHERE training_type_name = 'STRENGTH'),
        (SELECT id FROM "user" WHERE username = 'Filius.Flitwick')),
       ((SELECT id FROM "training_type" WHERE training_type_name = 'FLEXIBILITY'),
        (SELECT id FROM "user" WHERE username = 'Gilderoy.Lockhart')),
       ((SELECT id FROM "training_type" WHERE training_type_name = 'BALANCE'),
        (SELECT id FROM "user" WHERE username = 'Poppy.Pomfrey')),
       ((SELECT id FROM "training_type" WHERE training_type_name = 'COORDINATION'),
        (SELECT id FROM "user" WHERE username = 'Horace.Slughorn')),
       ((SELECT id FROM "training_type" WHERE training_type_name = 'HERBOLOGY'),
        (SELECT id FROM "user" WHERE username = 'Godric.Gryffindor')),
       ((SELECT id FROM "training_type" WHERE training_type_name = 'DIVINATION'),
        (SELECT id FROM "user" WHERE username = 'Salazar.Slytherin'));


INSERT INTO "trainee" (date_of_birth, address, user_id)
VALUES ('1980-07-31', '4 Privet Drive, Little Whinging', (SELECT id FROM "user" WHERE username = 'Harry.Potter')),
       ('1979-09-19', 'Hogwarts, Scotland', (SELECT id FROM "user" WHERE username = 'Hermione.Granger')),
       ('1980-03-01', 'The Burrow, Ottery St Catchpole', (SELECT id FROM "user" WHERE username = 'Ron.Weasley')),
       ('1981-02-13', 'The Lovegood House', (SELECT id FROM "user" WHERE username = 'Luna.Lovegood')),
       ('1981-08-11', 'The Burrow, Ottery St Catchpole', (SELECT id FROM "user" WHERE username = 'Ginny.Weasley')),
       ('1981-08-11', 'Hogwarts, Scotland', (SELECT id FROM "user" WHERE username = 'Ginevra.Granger')),
       ('1979-09-13', 'Nott Manor, England', (SELECT id FROM "user" WHERE username = 'Theodore.Nott')),
       ('1980-04-29', 'Greengrass Manor, England', (SELECT id FROM "user" WHERE username = 'Daphne.Greengrass')),
       ('1980-10-28', 'Parkinson Manor, England', (SELECT id FROM "user" WHERE username = 'Pansy.Parkinson')),
       ('1980-05-12', 'Davis House, England', (SELECT id FROM "user" WHERE username = 'Tracey.Davis')),
       ('1979-07-19', 'Bulstrode House, England', (SELECT id FROM "user" WHERE username = 'Millicent.Bulstrode')),
       ('1980-01-14', 'Goyle House, England', (SELECT id FROM "user" WHERE username = 'Gregory.Goyle')),
       ('1980-03-11', 'Crabbe House, England', (SELECT id FROM "user" WHERE username = 'Vincent.Crabbe')),
       ('1979-11-22', 'Patil House, England', (SELECT id FROM "user" WHERE username = 'Padma.Patil')),
       ('1979-11-22', 'Patil House, England', (SELECT id FROM "user" WHERE username = 'Parvati.Patil'));


INSERT INTO "training" (trainee_id, trainer_id, training_name, training_type_id, training_date, training_duration)
VALUES ((SELECT id FROM "trainee" WHERE user_id = (SELECT id FROM "user" WHERE username = 'Harry.Potter')),
        (SELECT id FROM "trainer" WHERE user_id = (SELECT id FROM "user" WHERE username = 'Severus.Snape')),
        'Potions 101',
        (SELECT id FROM "training_type" WHERE training_type_name = 'POTIONS'),
        '2024-09-01',
        120),

       ((SELECT id FROM "trainee" WHERE user_id = (SELECT id FROM "user" WHERE username = 'Hermione.Granger')),
        (SELECT id FROM "trainer" WHERE user_id = (SELECT id FROM "user" WHERE username = 'Minerva.McGonagall')),
        'Transfiguration Basics',
        (SELECT id FROM "training_type" WHERE training_type_name = 'TRANSFIGURATION'),
        '2024-09-02',
        90),

       ((SELECT id FROM "trainee" WHERE user_id = (SELECT id FROM "user" WHERE username = 'Ron.Weasley')),
        (SELECT id FROM "trainer" WHERE user_id = (SELECT id FROM "user" WHERE username = 'Gilderoy.Lockhart')),
        'Advanced Charms',
        (SELECT id FROM "training_type" WHERE training_type_name = 'DEFENSE'),
        '2024-09-03',
        75);

INSERT INTO "trainee_trainer" (trainee_id, trainer_id)
VALUES ((SELECT id FROM "trainee" WHERE user_id = (SELECT id FROM "user" WHERE username = 'Harry.Potter')),
        (SELECT id FROM "trainer" WHERE user_id = (SELECT id FROM "user" WHERE username = 'Severus.Snape'))),

       ((SELECT id FROM "trainee" WHERE user_id = (SELECT id FROM "user" WHERE username = 'Hermione.Granger')),
        (SELECT id FROM "trainer" WHERE user_id = (SELECT id FROM "user" WHERE username = 'Minerva.McGonagall'))),

       ((SELECT id FROM "trainee" WHERE user_id = (SELECT id FROM "user" WHERE username = 'Ron.Weasley')),
        (SELECT id FROM "trainer" WHERE user_id = (SELECT id FROM "user" WHERE username = 'Gilderoy.Lockhart')));
