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



Service
Solution provided as a Spring boot application and contained follow functionality:

1.Application include three service classes Trainee Service, Trainer Service, Training Service
2.Trainee Service class support possibility to create/update/delete/select Trainee profile.
3.Trainer Service class support possibility to create/update/select Trainer profile.
4.Training Service class support possibility to create/select Training profile.

