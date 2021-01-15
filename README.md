# Spring Boot Essentials



### How to run

```
mvn install

java -jar target/springboot2-essentials-0.0.1-SNAPSHOT.jar
```

#### Running integration tests only

You can run the integration test profile created by using the command below

```
mvn test -Pintegration-tests
```


### Docker 

Getting the MySQL database up using docker

```
docker-compose up
```

If you, like me, use DBeaver for browsing the database, you might get this _Public Key Retrieval is not allowed_ error, which can be solved at [this link](https://stackoverflow.com/questions/50379839/connection-java-mysql-public-key-retrieval-is-not-allowed).

