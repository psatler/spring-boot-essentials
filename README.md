# Spring Boot Essentials
> Spring Boot app showcasing several features of Spring and also using cool tools such as Prometheus and Grafana, all set up with Docker

The project was developed using Spring Boot 2.4 and Java 11.

### How to run

- TLDR: you need docker to run the application. 

The container for this application with all services can be found on _Docker Hub_
at [https://hub.docker.com/r/psatler/springboot2-essentials](https://hub.docker.com/r/psatler/springboot2-essentials).

You can also follow the steps below to run this application:

```
git clone https://github.com/psatler/spring-boot-essentials.git

cd spring-boot-essentials

docker-compose up
```

This project has authentication enabled. You can authentication yourself by accessing the
 `http://localhost:8080/login` endpoint. Two example accounts are shown at the [protected endpoints](#protected-endpoints) section below. To logout of the application, just access `http://localhost:8080/logout`.

- _Main application_: accessed on `http://localhost:8080`
  - you can find the swagger on `http://localhost:8080/swagger-ui.html`
- _Prometheus_: accessed on `http://localhost:9090`
- _Grafana_: accessed on `http://localhost:3000`
  - at grafana's login page, the credentials are:
    - username: admin
    - password: admin


If you, like me, use DBeaver for browsing the database, you might get this _Public Key Retrieval is not allowed_ error, which can be solved at [this link](https://stackoverflow.com/questions/50379839/connection-java-mysql-public-key-retrieval-is-not-allowed).


In the [prometheus.yml](src/main/resources/prometheus.yml) file you need to modify the IP in the `static_configs`, line 27, to the IP of the machine you are running this application on. I used this on a Linux machine (Ubuntu 18.04)


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



## Running integration tests only

You can run the integration test profile created by using the command below

```
mvn test -Pintegration-tests
```


## Aknowledgements

Notes taken throughout the implementation of this can be found at [Spring-Boot-2-Notes.md](Spring-Boot-2-Notes.md).


#### Other cool related links

- Other DevDojo's [playlists and videos](https://www.youtube.com/c/DevDojoBrasil/playlists) on youtube
- [Spring Boot JPA Tutorial](https://youtu.be/8SGI_XS5OPw) by Amigoscode
- Courses from [AmigosCode](https://amigoscode.com/courses)'s website
- [Java Testing with JUnit 5](https://youtu.be/flpmSXVTqBI) by Freecodecamp
