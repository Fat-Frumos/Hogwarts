# Getting Started

### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.3.2/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.3.2/maven-plugin/build-image.html)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.3.2/reference/htmlsingle/index.html#data.sql.jpa-and-spring-data)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/3.3.2/reference/htmlsingle/index.html#using.devtools)
* [Spring Security](https://docs.spring.io/spring-boot/docs/3.3.2/reference/htmlsingle/index.html#web.security)
* [Thymeleaf](https://docs.spring.io/spring-boot/docs/3.3.2/reference/htmlsingle/index.html#web.servlet.spring-mvc.template-engines)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.3.2/reference/htmlsingle/index.html#web)

### Guides

The following guides illustrate how to use some features concretely:

* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
* [Spring Boot and OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
* [Authenticating a User with LDAP](https://spring.io/guides/gs/authenticating-ldap/)
* [Handling Form Submission](https://spring.io/guides/gs/handling-form-submission/)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)


To use the application (apart from the Registration page), both trainers and trainees need to log in with their
credentials.
The application features three key entities: trainers, trainees, and training sessions. It allows both trainers and
trainees to register and update their personal details in their profiles, such as address, birthdate, and area of
expertise. Additionally, there is an option to activate or deactivate their profiles.
Trainees have the option to select and assign themselves one or more trainers. There is also a feature to manage
training sessions, which will be visible to both trainers and trainees.
For Trainee and Trainer create profile functionality username and password calculation implemented by follow rules:
a)Username going to be calculated from Trainer/Trainee first name and last name by concatenation by using dot as a
separator (eg. John.Smith)
b)In the case that already exists Trainer or Trainee with the same pair of first and last name as a suffix to the
username should be added a serial number.
c)Password should be generated as a random 10 chars length string.

ORM
Implemented as Hibernate solution and include the follow capabilities:
1.Create Trainer profile.
2.Create Trainee profile.
3.Trainee username and password matching.
4.Trainer username and password matching.
5.Select Trainer profile by username.
6.Select Trainee profile by username.
7.Trainee password change.
8.Trainer password change.
9.Update trainer profile.
10.Update trainee profile.
11.Activate/De-activate trainee.
12.Activate/De-activate trainer.
13.Delete trainee profile by username.
14.Get Trainee Trainings List by trainee username and criteria.
15.Get Trainer Trainings List by trainer username and criteria.
16.Add training.
17.Get not assigned on specific trainee active trainers list.
18.Update Tranee's trainers list
1.During Create Trainer/Trainee profile username and password are generated as described in previous module.
2.All functions except Create Trainer/Trainee profile can be executed only after Trainee/Trainer authentication (on this
step should be checked username and password matching)
3.Required field validation before Create/Update action execution implemented.
4.Users Table has parent-child (one to one) relation with Trainer and Trainee tables.
5.Trainees and Trainers have many to many relations.
6.Activate/De-activate Trainee/Trainer profile not idempotent action.
7.Delete Trainee profile is hard deleting action and bring the cascade deletion of relevant trainings.
8.Training duration have a number type.
9.Training Date, Trainee Date of Birth have Date type.
10.Training related to Trainee and Trainer by FK.
11.Is Active field in Trainee/Trainer profile has Boolean type.
12.Training Types table include constant list of values and could not be updated from the application.
13.Each table has its own PK.
14.Transaction management implemented.
15.Hibernate configured for work with DBMS that was chosen.
16.contains proper logging.

Technical Notes:

1. During Create Trainer/Trainee profile username and password should be generated as described in previous modules.
2. Not possible to register as a trainer and trainee both.
3. All functions except Create Trainer/Trainee profile. Should be executed only after Trainee/Trainer authentication (on
   this step should be checked username and password matching).
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
    a. Transaction level (generate transactionId by which you can track all operations for this transaction the same
    transactionId can later be passed to downstream services)
    b. Specific rest call details (which endpoint was called, which request came and the service response - 200 or error
    and response message)
18. Implement error handling.
19. Document methods in RestController file(s) using Swagger 2 annotations.

Service
Solution provided as a Spring boot application and contained follow functionality:

1.Application include three service classes Trainee Service, Trainer Service, Training Service
2.Trainee Service class support possibility to create/update/delete/select Trainee profile.
3.Trainer Service class support possibility to create/update/select Trainer profile.
4.Training Service class support possibility to create/select Training profile.

1.Configure spring application context based on the Spring annotation or on Java based approach.
2.Implement DAO objects for each of the domain model entities (Trainer, Trainee, Training). They should store in and
retrieve data from a common in-memory storage - java map. Each entity should be stored under a separate namespace, so
you could list particular entity types.
3.Storage should be implemented as a separate spring bean. Implement the ability to initialize storage with some
prepared data from the file during the application start (use spring bean post-processing features). Path to the
concrete file should be set using property placeholder and external property file. In other words, Every storage (
java.util.Map) should be implemented as a separate spring bean
4.DAO with storage bean should be inserted into services beans using auto wiring. Services beans should be injected into
the facade using constructor-based injections. The rest of the injections should be done in a setter-based way.
8.All functions except Create Trainer/Trainee profile can be executed only after Trainee/Trainer authentication (on this
step should be checked username and password matching).

Security
Spring security framework used to provide follow functionality:
1.It configured for Authentication access for all endpoints (except Create Trainer/Trainee profile).
3.Spring Security is configured to use Login functionality.
5.Logout functionality implemented and configured it in Spring Security.
6.Authorization implemented as a Bearer token for Create Profile and Login functionality
1.During Create Trainer/Trainee profile username and password should be generated as described in previous module.
2.All functions except Create Trainer/Trainee profile can be executed only after Trainee/Trainer authentication.