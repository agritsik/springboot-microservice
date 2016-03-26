# springboot-microservice
Sample microservices-based app using Spring Boot.
The application provides REST API, persists data into in-memory database and sends change-entity events to rabbitmq. 

## Purpose
The main purpose of this project is demonstrating how to:

1. Create a microservices-based application using Spring Boot
2. Structure an application using BCE pattern
3. Create RESTful services, persist data and send events
4. Use different testing strategies and levels
5. Dockerize the project (TBD)

## Technologies

1. REST API using spring-web
2. REST client using spring rest template 
3. DB persistence using spring-data and in-memory db (hsqldb)
4. Messaging system using spring-amqp and rabbitmq
5. Testing using mockito, spring-tests
6. Virtualization using docker (TBD)
7. CI process using Travis CI

## Testing strategies

1. Unit Testing using mockito
2. Integration testing (TBD)
3. Component testing using spring technologies such as mockmvc, sprint-amqp, spring-data
4. System or e2e testing using spring rest template in order to test application as a blockbox






