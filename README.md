# TODO Management Application

##### Todo Management Application (REST-API)

<center>
	<a target='_blank' href='https://donatello-todo-api.herokuapp.com/donatello/swagger-ui/index.html?configUrl=/donatello/v3/api-docs/swagger-config'>CLICK HERE TO VIEW API DOCS (Deployed Swagger-UI)</a>
</center>

## Tech Stack Used 
* Java 15
* Spring Boot
* Spring Security (JWT Based Authentication and Authorization)
* Spring Data JPA/Hibernate
* Spring Boot Validator
* PostgreSQL PL/pgSQL
* Flyway
* Open-API
* Lombok
* Junit/Mockito
* TestContainers

## Entities
##### SQL Migration Scripts can be viewed at src/main/resources/db/migration

* users
* todos (many-to-one relationship with users)

## Security Flow
* On Successful validation of login credentials, a JWT will be returned representing the user **(decode the below sample JWT on jwt.io for reference)**

```
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJoYXJkaWsuYmVobDc0NDRAZ21haWwuY29tIiwiYWNjb3VudF9jcmVhdGlvbl90aW1lc3RhbXAiOiIyMDIxLTA1LTI5VDIzOjU1OjM0LjgyMzQ1MCIsInVzZXJfaWQiOiJmODA1OWZlMC04ODE5LTQ1M2UtYTc2NC01ZDlkMzg3NjJiY2EiLCJzY29wZSI6InVzZXIiLCJuYW1lIjoiSGFyZGlrIFNpbmdoIEJlaGwiLCJleHAiOjE2MjIzNDg3NDYsImlhdCI6MTYyMjMxMjc0Nn0.jCirtPsrAlSeIeNuZshh-7PuCoStShBYacHFyFyiBmM
```
* The received JWT should be included in the headers when calling a protected API
* Authentication Bearer format to be used **(Header key should be 'Authentication' and value should start with Bearer followed with a single blank space and recieved JWT)**

```
Authentication : Bearer <JWT>
```

## Setup

* Install Java 15
* Install Maven

Recommended way is to use [sdkman](https://sdkman.io/) for installing both maven and java

Run mvn clean install in the core

```
mvn clean install
```

Run docker commands

```
sudo docker-compose build
sudo docker-compose up -d
```

Service port is 9090 and Postgres Port is 6432. They both can be changed in the [docker-compose.yml](docker-compose.yml) file

To View Logs

```
docker-compose logs -f service
```

To stop the container run

```
sudo docker-compose stop
```

## To Run Locally Without Docker

Create postgres user (superuser) with name and password as pomfrey

```
CREATE USER donatello WITH PASSWORD 'donatello' SUPERUSER;
```
Create Database with name 'donatello' and assign the above created user to the database with preferable CLI or GUI tool

```
create database donatello;
```

```
grant all privileges on database donatello to donatello;
```

Run mvn clean install in the core 

```
mvn clean install
```

Run Tests

```
mvn test
```

Run Application 

```
run as -> spring boot application
```

API Documentation can be viewed by visiting the below link (can be altered in application.properties)

```
http://localhost:9093/donatello/swagger-ui.html
```

## Quick Guide To Use Swagger-UI

* Click on API that you wish to hit by clicking the **Try It Out** button
* Fill in the input if required as mentioned and click on **execute**
* Some API's do not require the user to authenticate themselves before using it like account-registeration/account-login API's
* In order to gain JWT required for authentication, execute the login API with valid credentials and paste the received JWT in repsonse in the Top Right section y clicking on **Authorize** and paste the JWT there to authorize
* After successfully authorization, all protected API's can be executed the same way non-protected API's were being executed, the JWT will be **automatically** sent to the server inside headers following **bearerAuth** security flow. 

