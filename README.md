# Spring Boot Essentials



### How to run

#### Docker 

Getting the MySQL database up using docker

```
docker-compose up
```

If you, like me, use DBeaver for browsing the database, you might get this _Public Key Retrieval is not allowed_ error, which can be solved at [this link](https://stackoverflow.com/questions/50379839/connection-java-mysql-public-key-retrieval-is-not-allowed).


#### Installing dependencies

```
mvn install

java -jar target/springboot2-essentials-0.0.1-SNAPSHOT.jar
```

#### Protected endpoints

All endpoints but the ones starting with `/actuator/` (for example, `/actuator/info`) are protected by some sort of basic authentication. The security configuration can be found at [SecurityConfig](src/main/java/academy/devdojo/springboot2/config/SecurityConfig.java) class.

An example user you can use is

- admin role:
  - username: admin2
  - password: devdojo-pass

- user role:
  - username: devdojo2
  - password: devdojo-pass



#### Running integration tests only

You can run the integration test profile created by using the command below

```
mvn test -Pintegration-tests
```




