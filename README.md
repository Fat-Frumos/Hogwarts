# Java Specialization X-Stack

[![Java CI with Maven](https://github.com/Fat-Frumos/XStack/actions/workflows/maven.yml/badge.svg)](https://github.com/Fat-Frumos/XStack/actions/workflows/maven.yml)

### General description

The main goal of this project is to develop a gym application. To use the application (apart from the Registration
page), both trainers and trainees need to log in with their credentials.
The application features three key entities: trainers, trainees, and training sessions. It allows both trainers and
trainees to register and update their personal details in their profiles, such as address, birthdate, and area of
expertise. Additionally, there is an option to activate or deactivate their profiles.
Trainees have the option to select and assign themselves one or more trainers. There is also a feature to
manage training sessions, which will be visible to both trainers and trainees.
Each trainer receives a weekly report detailing the duration of training sessions for that period.
Below is a description of each component of the solution.

## DB

1. Solution is based on the attached DB schema.

### Technical Notes:

1. For Trainee and Trainer create profile functionality username and password calculation
   implemented by follow rules:
   a) Username going to be calculated from Trainer/Trainee first name and last name by
   concatenation by using dot as a separator (eg. John.Smith)
   b) In the case that already exists Trainer or Trainee with the same pair of first and last
   name as a suffix to the username should be added a serial number.
   c) Password should be generated as a random 10 chars length string.

## ORM

#### Implemented as Hibernate solution and include the follow capabilities:

1. Create Trainer profile.
2. Create Trainee profile.
3. Trainee username and password matching.
4. Trainer username and password matching.
5. Select Trainer profile by username.
6. Select Trainee profile by username.
7. Trainee password change.
8. Trainer password change.
9. Update trainer profile.
10. Update trainee profile.
11. Activate/De-activate trainee.
12. Activate/De-activate trainer.
13. Delete trainee profile by username.
14. Get Trainee Trainings List by trainee username and criteria.
15. Get Trainer Trainings List by trainer username and criteria.
16. Add training.
17. Get not assigned on specific trainee active trainers list.
18. Update Tranee's trainers list

### Technical Notes:

1. During Create Trainer/Trainee profile username and password are generated as described in
   previous module.
2. All functions except Create Trainer/Trainee profile can be executed only after Trainee/Trainer
   authentication (on this step should be checked username and password matching)
3. Required field validation before Create/Update action execution implemented.

-[x] Users Table has parent-child (one to one) relation with Trainer and Trainee tables.

6. Trainees and Trainers have many to many relations.
6. Activate/De-activate Trainee/Trainer profile not idempotent action.
7. Delete Trainee profile is hard deleting action and bring the cascade deletion of relevant
   trainings.
8. Training duration have a number type.
9. Training Date, Trainee Date of Birth have Date type.
10. Training related to Trainee and Trainer by FK.
11. Is Active field in Trainee/Trainer profile has Boolean type.
12. Training Types table include constant list of values and could not be updated from the
    application.

-[x] Each table has its own PK.

14. Transaction management implemented.
15. Hibernate configured for work with DBMS that was chosen.
16. Code covered with unit tests and contains proper logging.

## REST

#### Following REST API endpoints (as a RestController) are implemented:

1. Trainee Registration (POST method)
    - Request
        - First Name (required)
        - Last Name (required)
        - Date of Birth (optional)
        - Address (optional)
    - Response
        - Username
        - Password
2. Trainer Registration (POST method)
    - Request
        - First Name (required)
        - Last Name (required)
        - Specialization (required) (Training type reference)
    - Response
        - Username
        - Password
3. Login (GET method)
    - Request
        - Username (required)
        - Password (required)
    - Response
    - 200 OK
4. Change Login (PUT method)
    - Request
        - Username (required)
        - Old Password (required)
        - New Password (required)
    - Response
    - 200 OK
5. Get Trainee Profile (GET method)
    - Request
        - Username (required)
    - Response
        - First Name
        - Last Name
        - Date of Birth
        - Address
        - Is Active
        - Trainers List:
            - Trainer Username
            - Trainer First Name
            - Trainer Last Name
            - Trainer Specialization (Training type reference)

6. Update Trainee Profile (PUT method)
   a. Request
   i. Username (required)
   ii. First Name (required)
   iii. Last Name (required)
   iv. Date of Birth (optional)
   v. Address (optional)
   vi. Is Active (required)
   b. Response
   i. Username
   ii. First Name
   iii. Last Name
   iv. Date of Birth
   v. Address
   vi. Is Active
   vii. Trainers List


1. Trainer Username
2. Trainer First Name
3. Trainer Last Name
4. Trainer Specialization (Training type reference)
7. Delete Trainee Profile (DELETE method)
   a. Request
   i. Username (required)
   b. Response
   i. 200 OK
8. Get Trainer Profile (GET method)
   a. Request
   i. Username (required)
   b. Response
   i. First Name
   ii. Last Name
   iii. Specialization (Training type reference)
   iv. Is Active
   v. Trainees List
1. Trainee Username
2. Trainee First Name
3. Trainee Last Name
9. Update Trainer Profile (PUT method)
   a. Request
   i. Username (required)
   ii. First Name (required)
   iii. Last Name (required)
   iv. Specialization (read only) (Training type reference)
   v. Is Active (required)
   b. Response
   i. Username
   ii. First Name
   iii. Last Name
   iv. Specialization (Training type reference)
   v. Is Active
   vi. Trainees List
1. Trainee Username
2. Trainee First Name
3. Trainee Last Name
10. Get not assigned on trainee active trainers. (GET method)
    a. Request
    i. Username (required)
    b. Response
    i. Trainer Username
    ii. Trainer First Name
    iii. Trainer Last Name
    iv. Trainer Specialization (Training type reference)
11. Update Trainee's Trainer List (PUT method)
    a. Request
    i. Trainee Username
    ii. Trainers List (required)
1. Trainer Username (required)
   b. Response
   i. Trainers List
1. Trainer Username
2. Trainer First Name
3. Trainer Last Name
4. Trainer Specialization (Training type reference)
12. Get Trainee Trainings List (GET method)
    a. Request
    i. Username (required)
    ii. Period From (optional)
    iii. Period To (optional)
    iv. Trainer Name (optional)
    v. Training Type (optional)
    b. Response
    i. Training Name
    ii. Training Date
    iii. Training Type
    iv. Training Duration
    v. Trainer Name
13. Get Trainer Trainings List (GET method)
    a. Request
    i. Username (required)
    ii. Period From (optional)
    iii. Period To (optional)
    iv. Trainee Name (optional)
    b. Response
    i. Training Name
    ii. Training Date
    iii. Training Type
    iv. Training Duration
    v. Trainee Name
14. Add Training (POST method)
    a. Request
    i. Trainee username (required)
    ii. Trainer username (required)
    iii. Training Name (required)
    iv. Training Date (required)
    v. Training Duration (required)
    b. Response
    i. 200 OK
15. Activate/De-Activate Trainee (PATCH method)
    a. Request
    i. Username (required)
    ii. Is Active (required)
    b. Response
    i. 200 OK
16. Activate/De-Activate Trainer (PATCH method)
    a. Request
    i. Username (required)
    ii. Is Active (required)
    b. Response
    i. 200 OK
17. Get Training types (GET method)
    a. Request -no data
    b. Response
    i. Training types

1. Training type
2. Training type Id

### Technical Notes:

1. During Create Trainer/Trainee profile username and password should be generated as described in
   previous modules.
2. Not possible to register as a trainer and trainee both.
3. All functions except Create Trainer/Trainee profile. Should be executed only after Trainee/Trainer
   authentication (on this step should be checked username and password matching).
4. Implement required validation for each endpoint.
5. Users Table has parent-child (one to one) relation with Trainer and Trainee tables.
6. Training functionality does not include delete/update possibility via REST
7. Username cannot be changed.
8. Trainees and Trainers have many to many relations.
9. Activate/De-activate Trainee/Trainer profile not idempotent action.
10. Delete Trainee profile is hard deleting action and bring the cascade deletion of relevant trainings.
11. Training duration have a number type.
12. Training Date, Trainee Date of Birth have Date type.
13. Is Active field in Trainee/Trainer profile has Boolean type.
14. Training Types table include constant list of values and could not be updated from the application.
15. Implement error handling for all endpoints.
16. Cover code with unit tests.
17. Two levels of logging should be implemented:
    a. Transaction level (generate transactionId by which you can track all operations for this
    transaction the same transactionId can later be passed to downstream services)
    b. Specific rest call details (which endpoint was called, which request came and the service
    response - 200 or error and response message)
18. Implement error handling.
19. Document methods in RestController file(s) using Swagger 2 annotations.

## Service

#### Solution provided as a Spring boot application and contained follow functionality:

1. Application include three service classes Trainee Service, Trainer Service, Training Service
2. Trainee Service class support possibility to create/update/delete/select Trainee profile.
3. Trainer Service class support possibility to create/update/select Trainer profile.
4. Training Service class support possibility to create/select Training profile.

-[x] Actuator is enabled.
-[x] Few custom health indicators implemented.
-[x] Few custom metrics using Prometheus implemented
-[x] Possibility to support different environments (local, dev, stg, prod) with Spring profiles is
   implemented.

#### Technical Notes:

1. Configure spring application context based on the Spring annotation or on Java based approach.
2. Implement DAO objects for each of the domain model entities (Trainer, Trainee, Training).
   They should store in and retrieve data from a common in-memory storage - java map. Each
   entity should be stored under a separate namespace, so you could list particular entity types.
3. Storage should be implemented as a separate spring bean. Implement the ability to initialize
   storage with some prepared data from the file during the application start (use spring bean postprocessing features).
   Path to the concrete file should be set using property placeholder and
   external property file. In other words, Every storage (java.util.Map) should be implemented as a
   separate spring bean
4. DAO with storage bean should be inserted into services beans using auto wiring. Services beans
   should be injected into the facade using constructor-based injections. The rest of the injections
   should be done in a setter-based way.
5. Cover code with unit tests.
6. Code should contain proper logging.
7. Each environment has different DB properties.
8. All functions except Create Trainer/Trainee profile can be executed only after Trainee/Trainer
   authentication (on this step should be checked username and password matching).

## Security

#### Spring security framework used to provide follow functionality:

-[x] It configured for Authentication access for all endpoints (except Create Trainer/Trainee profile).
-[x] salt and hashing are used to store user passwords in DB.
-[x] Spring Security is configured to use Login functionality.
-[x] Brute Force protector added. User is blocked for 5 minutes on 3 unsuccessful logins
-[x] Logout functionality implemented and configured it in Spring Security.
-[x] Authorization implemented as a Bearer token for Create Profile and Login functionality (JWT token).
-[x] CORS policy in Spring Security configured.

##### Technical Notes:

1. During Create Trainer/Trainee profile username and password should be generated as described
   in previous module.
2. All functions except Create Trainer/Trainee profile can be executed only after Trainee/Trainer
   authentication.

### Maven commands

Validate the project is correct and all necessary information is available

`mvn validate`

Compile the source code of the project

`mvn compile`

Run unit tests from Command line

`mvn test`

Take the compiled code and package it in its distributable format

`mvn package`

Run integration tests from Command line

`mvn verify`

Build the project with Maven Tool

`mvn -B package --file pom.xml`

Clean, install, and generate report

`mvn clean install site -P test`

Clean, install with details

`mvn clean install -X`

Clean, install skip Test

`mvn clean install -DskipTests`
