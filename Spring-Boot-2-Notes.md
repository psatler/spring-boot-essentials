
# Anotações feitas a partir dos vídeos da playlist [Spring Boot 2 Essentials](https://youtube.com/playlist?list=PL62G310vn6nFBIxp6ZwGnm8xMcGE3VA5H)


Ctrl + Alt + O (Intellij) -> Formata o codigo



## Class 03

@ComponentScan -> Anotação que faz uma especie de mapping para buscar o pacote de inicio da aplicacao

```java
@ComponentScan(basePackages = "<package.name>")
```

A forma mostrada acima é no caso que a classe de inicialização não está na raíz do pacote.



"No Serializer found": normalmente quando dá este erro significa que foi esquecido de colocar o get ou set.


## Class 04

Quando se cria uma classe onde queremos que ela seja injetada diretamente no Spring (utilizando o `@Autowired`), 
devemos usar alguma das anotações que vai transformar a classe em um Spring Bean. Algumas são:

- @Component
- @Service
- @Repository



Dica: normalmente o Spring gerencia a versao das dependencias, então baste adicionar o
`groupId` e o `artifactId`.



@AllArgsConstructor - anotação do lombok que cria um construtor com todos os atributos que você tem na classe.
`@RequiredArgsConstructor` - cria um construtor com todos os atributos que são finais.

`@Configuration` - coloca a classe no "filter chain"

`@SpringBootApplication` - agrupa varias das demais anotações citadas acima, como `@EnableAutoConfiguration`, `@ComponentScan`, `@Configuration`, etc


## Class 05 - Hot Swap com Spring Boot Devtools



## Class 06 - Gerando projeto com start.spring.io

Sprint initializer: [https://start.spring.io/](https://start.spring.io/)

Algumas dependencias iniciais utilizadas:
- Spring web
- Spring devtools
- Lombok



java8 (vscode extension) - v0.64.1


##### A parte - Java Ubuntu

- [Java Runtimes](https://github.com/redhat-developer/vscode-java/wiki/JDK-Requirements#java.configuration.runtimes)
- [Java alternatives](https://askubuntu.com/questions/315646/update-java-alternatives-vs-update-alternatives-config-java)
- Instalar extensões do vscode para Java, Lombok, etc


## Class 07 - GET parte 1

- normalmente o nome no `RequestMapping` é declarado no plural. Então ficaria:

```
@RequestMapping("animes")
``` 

#### Service

Normalmente é a classe responsável pela regra de negócio.



@Data - no lombok, gera todos os get, set, equal, hashcode
@AllArgsConstructor - gera um construtor com todos os atributos

```java
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Anime {
  private Long id;
  private String name;

}
```

Separandos as responsabilidades:

- controller: onde estarão os endpoints
- domain: representado o que temos no banco de dados
- repository: será a conexão diretamente com o banco de dados, então, teremos métodos de listar, procurar, etc
- service: onde ficará a lógica de negócios



## Class 08 - GET parte 2


To hide the errors from being shown to the client, we can do the following in the `application.yml`:

```yml

server:
  error:
    include-stacktrace: on-param # hiding the errors on return. Only show if we set param as ?trace=true
```

So, to display the errors we add `http://localhost:8080/animes/3?trace=true`



## Class 09 - POST

Podemos retornar somente o id do objeto criado e o status 201, ou nada e somente o status 201, ou então retornamos
o objeto criado por inteiro e o status 201.

A requisicao recebe um Body e usamos o _Jackson_ (o spring o faz de forma automática) para mapear o corpo
para a classe em questao.

Se o nome dos atributo no json for igual ao da classe, o Jackson mapeia para. Se o nome do atributo
na classe for diferente, deve-se usar a annotation `@JsonProperty` indicando o nome do atributo da classe
para o qual desejamos mapear ele. Caso contrário, o atributo será ignorado.

- Exemplo:

```java
@Data
@AllArgsConstructor
public class Anime {
  private Long id;
  @JsonProperty("name")
  private String nameCharacter;

}
```



## Class 10 - DELETE

Normalmente, nada é retornado no DELETE.

Devemos ficar ligados no quesito de Idempotencia quando estivermos construindo as apis:

- Referencia para implementacao do protocolo HTTP [RFC-7231](https://tools.ietf.org/html/rfc7231)
- Link para [safe methods](https://tools.ietf.org/html/rfc7231#section-4.2.1)
- [Tabela exibindo os métodos "safe" e métodos idempotentes](https://tools.ietf.org/html/rfc7231#section-8.1.3).

Ou seja, os métodos GET, PUT e DELETE devem ser idempotentes. Em outras palavras, eles não alteram
o estado do servidor dado a mesma requisição sendo executada.


## Class 11 - PUT


## Class 12 - Docker e MySQL


Adicionamos a dependecia do `starter-data-jpa`. Não é necessário inserir a versão
pois esta é inferida a partir da versão do `<parent>` no `pom.xml`.

```xml
<dependency>
 <groupId>org.springframework.boot</groupId>
 <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

Além disso, devemos especificar qual conector iremos utilizar (a qual banco de dados vamos nos conectar).
No nosso caso, iremos utilizar o `mysql-connector-java`

```xml
		<dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
		</dependency> 
```

Com as dependencias instaladas, realizamos a configuracao de conexao com o banco de dados no `application.yml`.

```yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/anime?useSSL=false&createDatabaseIfNotExist=true&allowPublicKeyRetrieval=true
    username: root
    password: root
  jpa:
  # every time we start our application, our database gets updated by the command below
    hibernate:
      ddl-auto: update
```


Na classe de dominio, realizamos alguns ajustes para uso da _annotation_ `@Entity`. 

```java
@Data
@AllArgsConstructor
@NoArgsConstructor                                             <----- 
@Entity                                                        <-----
public class Anime {
  @Id							       <-----
  @GeneratedValue(strategy = GenerationType.IDENTITY)          <-----
  private Long id;
  private String name;

}
```


## Class 14 - Spring Data JPA pt 2

Conectamos as classes e pegamos os dados direto do BD.

Com a interface de repositorio, "extendemos" a mesma a partir do `JPARepository` passando o nome da classe/entidade que
o repositorio e o tipo do atributo que representa o ID. 

O JPARepository já nos fornece diversos métodos para manipular os dados do banco de dados, por exemplo, `findAll`, `findAllById`, etc.

```java
public interface AnimeRepository extends JpaRepository<Anime, Long> {


}

```


PS: Há uma annotation do `lombok` chamada `@Builder`, que facilita a criação de objetos. Por exemplo, para a classe `Anime` usando tal anotação,
podemos criar uma instancia da seguinte forma:

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder                     <--------------------------
public class Anime {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;

}


...
...
...


Anime anime = Anime.builder().name(animePostRequestBody.getName()).build();   <---------------

// ou

Anime anime = Anime.builder()
                    .id(savedAnime.getId())
                    .name(animePutRequestBody.getName())
                  .build();

```


## Class 15 - Framework de Mapeamento MapStruct

Da forma feita até o momento, teríamos que fazer o mapeamento manualmente de cada um dos atributos. Contudo,
existe a possibilidade de alguns atributos serem `null`, de outros não serem válidos, etc.

Há uma forma de agilizar esse mapeamento utilizando o framework [MapStruct](https://mapstruct.org/). Ele facilita
bastante, principalmente se temos DTOs ou classes com mesmo nome, mas mesmo se não tiver o mesmo nome, podemos dizer
o campo da classe origem e o campo onde ele deve ser colocado.

Iremos, então, adicionar mais uma dependência no `pom.xml`:

```xml
<properties>
		<java.version>11</java.version>
		<org.mapstruct.version>1.4.1.Final</org.mapstruct.version>
	</properties>

	<dependencies>
		<dependency>
        <groupId>org.mapstruct</groupId>
        <artifactId>mapstruct</artifactId>
        <version>${org.mapstruct.version}</version>
    </dependency>

...
...

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>

			<plugin>
					<groupId>org.apache.maven.plugins</groupId>
					<artifactId>maven-compiler-plugin</artifactId>
					<version>${org.mapstruct.version}</version>
					<configuration>
							<source>11</source> <!-- depending on your project -->
							<target>11</target> <!-- depending on your project -->
							<annotationProcessorPaths>
									<path>
											<groupId>org.mapstruct</groupId>
											<artifactId>mapstruct-processor</artifactId>
											<version>${org.mapstruct.version}</version>
									</path>
									<!-- other annotation processors -->
							</annotationProcessorPaths>
					</configuration>
        </plugin>
		</plugins>
	</build>
```


Ou seja, foi adicionado uma entrada no `<properties>`, a dependencia em si no `<dependecy>` e também um _plugin_ no build (mais embaixo no `pom.xml`).


Então, criamos um pacote/diretorio chamado `mapper` e adicionamos nossos _mappers_.


```java
@Mapper(componentModel = "spring")
public abstract class AnimeMapper {
  public static final AnimeMapper INSTANCE = Mappers.getMapper(AnimeMapper.class); // a way to call the abstract methods

  // automatically makes the converstion from all values (with same name) in the argument (AnimePostRequestBody; AnimePutRequestBody) to the Anime
  public abstract Anime toAnime(AnimePostRequestBody animePostRequestBody);
  public abstract Anime toAnime(AnimePutRequestBody animePutRequestBody);  
}

```


## Class 16 - Request Params


Usando Request Params para retornar a lista de Animes pelo nome


- Exibindo o SQL gerado pelo JPA:

```yml
jpa:
  # every time we start our application, our database gets updated by the command below
    hibernate:
      ddl-auto: update
    show-sql: true      <----------
```

O `RequestParam` não é necessário passar o `name`, como mostrado a seguir:

```java
@RequestParam(name = "paramName")
```

Caso omitido, o nome do parametro será obtido da variável utilizada.

```java
@RequestParam String name
```

Podemos deixar o _RequestParam_ não obrigatório também, usando _required_ false

```java
findByName(@RequestParam(required = false) String name)
```

## Class 17 - Exceções Customizadas

Criamos um pacote/diretório chamado `exception`.

Por exemplo, criando uma exceção `BadRequest` (herdamos de `RuntimeException`):

```java
package academy.devdojo.springboot2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public BadRequestException(String message) {
    super(message);
  }
}
```

e utilizando no _service_ a exceção criada:

```java
public Anime findByIdOrThrowBadRequestError(long id) {
    return animeRepository.findById(id)
            .orElseThrow(() -> new BadRequestException("Anime not found"));
  }
```


## Class 18 - Handler Global

Criando um Handler Global para as exceções.

- Por exemplo, podemos querer retornar na exceção os campos de forma padronizada dependendo da exceção.
	
- exemplo: criando uma exceção quando for BadRequest

```java
@Data
@Builder
public class BadRequestExceptionDetails {
  private String title;
  private int status;
  private String details;
  private String developerMessage;
  private LocalDateTime timestamp;
}
```

Entao, criamos um pacote/diretório chamado _handler_ e dentro uma classe chamada `RestExceptionHandler`. Para dizer para todos os _controllers_ para
usarem essa classe, colocamos uma anotação do tipo `@ControllerAdvice`. Dessa forma, dizemos para todos os controllers para utilizar o que colocarmos
dentro dessa classe e o que/qual método utilizar é feito através de uma espécie de "flag". 

No caso de exceção, usamos o `@ExceptionHandler` e se for lançada uma exceção do tipo denotado, é executado o método do respectivo handler.

```java
@ControllerAdvice // indicating this is a class all controllers should use
public class RestExceptionHandler {

  @ExceptionHandler(BadRequestException.class) // when there is an exception of the type BadRequest, execute the method below
  public ResponseEntity<BadRequestExceptionDetails> handlerBadRequestException(BadRequestException bre) {
    return new ResponseEntity<>(
      BadRequestExceptionDetails.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.BAD_REQUEST.value())
        .title("Bad Request Exception. Check the documentation!")
        .details(bre.getMessage())
        .developerMessage(bre.getClass().getName())
        .build(),
        HttpStatus.BAD_REQUEST
    );
  }
  
}
```


## Class 19 - Transações

Vendo como o Spring Boot trabalha com o rollback de transações caso uma exceção aconteça.

Para verificar isso, podemos observar se na tabela do banco de dados é mostrado como _engine_ o nome `innoDB` usando o comando `show table status`.


Para evitar que, em caso de exceção, haja o commit da informação no banco de dados, podemos anotar o método com o `@Transactional` do pacote spring framework.
Dessa forma, o Spring não vai commitar o código, por exemplo, um _save_ no banco de dados, enquanto o método não for finalizado.

```java
 @Transactional
  public Anime save(AnimePostRequestBody animePostRequestBody) {
    // Anime anime = Anime.builder().name(animePostRequestBody.getName()).build();
    Anime anime = AnimeMapper.INSTANCE.toAnime(animePostRequestBody);

    return animeRepository.save(anime);
  }
```

Por padrão, o `@Transactional` não retorna/dar rollback para exceções do tipo `checked`. Para explicitar o rollback para uma dada exceção, podemos usar a anotação
da seguinte maneira: 

```java
@Transactional(rollbackFor = Exception.class)
```


## Class 20 - Validação de campos

Vamos utilizar a dependencia de validação do spring mostrada abaixo:

```xml
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-validation</artifactId>
		</dependency>
```

Essa dependencia vai nos fornecer umas bibliotecas que vão fazer a validação dos nossos campos.


Existem diversas validações que podem ser feitas com o `javax.validation`, como verificação de números (min, max), URL, não nulo, não vazio, etc.

```java
@Data
public class AnimePostRequestBody {

  @NotEmpty(message = "The anime name cannot be empty")
  private String name;  
}
```

Para o spring efetuar a validação, devemos adicionar a anotação `@Valid` onde o objeto com validações é usado, por exemplo, fazendo a validação
do objeto recebido na requisição no controller.

```java
@PostMapping
  public ResponseEntity<Anime> save(@RequestBody @Valid AnimePostRequestBody animePostRequestBody) {                     <----------------
    // Jackson already maps the json properties to the class Anime so that we don't need to set the name in the animeService
    return new ResponseEntity<>(animeService.save(animePostRequestBody), HttpStatus.CREATED);
  }
```

O erro é retorno e gerenciado pelo proprio Spring e um exemplo de resposta de erro é mostrado abaixo:

```json
{
  "timestamp": "2020-12-08T01:01:36.859+00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed for object='animePostRequestBody'. Error count: 1",
  "errors": [
    {
      "codes": [
        "NotEmpty.animePostRequestBody.name",
        "NotEmpty.name",
        "NotEmpty.java.lang.String",
        "NotEmpty"
      ],
      "arguments": [
        {
          "codes": [
            "animePostRequestBody.name",
            "name"
          ],
          "arguments": null,
          "defaultMessage": "name",
          "code": "name"
        }
      ],
      "defaultMessage": "The anime name cannot be empty",
      "objectName": "animePostRequestBody",
      "field": "name",
      "rejectedValue": null,
      "bindingFailure": false,
      "code": "NotEmpty"
    }
  ],
  "path": "/animes"
}
```

## Class 21 - Handler para validação de campos

Vamos fazer algumas modificações para que a resposta de erro do spring seja um pouco mais "amigável".

Como queremos que os detalhes nas exceçoes sejam os mesmos para quase todas as exceções, somente especializando conforme a necessidade, vamos criar uma nova
classe chamada `ExceptionDetails`.

Usamos a annotation `@SuperBuilder` na classe `ExceptionDetails`, o qual servirá de base para as outras classes que irão herdar (extends) dela.

Portanto, a classe de BadException antes criada ficou assim: 

- `ExceptionDetails.java`

```java
@Data
@SuperBuilder
public class ExceptionDetails {
  protected String title;
  protected int status;
  protected String details;
  protected String developerMessage;
  protected LocalDateTime timestamp;
}
```

- `BadExceptionDetails.java`

```java
@Getter
@SuperBuilder
public class BadRequestExceptionDetails extends ExceptionDetails {
  
}
```

Para os errors de validação do Spring e os demais campos, criamos uma nova classe para eles chamada `ValidationExceptionDetails`.

```java
@Getter
@SuperBuilder
public class ValidationExceptionDetails extends ExceptionDetails {
  private final String fields;
  private final String fieldsMessage;
}
```

Agora utilizamos o _handler_ para interceptar a exceção lançada e adicionar o que foi definido no método.

Para identificar qual exceção é lançada no controller quando a validação é executada, basta colocar no final da URL do endpoint chamado o `?trace=true`.
Fazendo isso, podemos verificar que a exceção lançada é `MethodArgumentNotValidException`.

Abaixo, temos o _excetion handler_ criada para a exceção acima:

```java
@ExceptionHandler(MethodArgumentNotValidException.class) // when there is an exception of the type BadRequest, execute the method below
  public ResponseEntity<ValidationExceptionDetails> handlerMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
    log.info("Fields {}", exception.getBindingResult().getFieldError().getField());

    List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
    String fields = fieldErrors.stream().map(FieldError::getField).collect(Collectors.joining(", "));
    String fieldsMessage = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));

    return new ResponseEntity<>(
      ValidationExceptionDetails.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.BAD_REQUEST.value())
        .title("Bad Request Exception. Invalid fields")
        // .details(exception.getMessage())
        .details("Check the field(s) error")
        .developerMessage(exception.getClass().getName())
        .fields(fields)
        .fieldsMessage(fieldsMessage)
        .build(),
        HttpStatus.BAD_REQUEST
    );
  }
```

E um exemplo de resposta do handler mostrando os dois campos a mais, de _fields_ e _fieldsMessage_:

```json
{
  "title": "Bad Request Exception. Invalid fields",
  "status": 400,
  "details": "Check the field(s) error",
  "developerMessage": "org.springframework.web.bind.MethodArgumentNotValidException",
  "timestamp": "2020-12-07T22:39:54.534807",
  "fields": "name",
  "fieldsMessage": "The anime name cannot be empty"
}
```


## Class 22 - Sobrescrevendo handler do Spring

Vamos ver agora uma forma de trabalhar com as outras exceções de uma forma que poderemos padronizar praticamente tudo com relação à exceções.

Podemos então, na nossa classe de `RestExceptionHandler`, herdar a classe `ResponseEntityExceptionHandler`.

Abaixo é mostrado o `@Override` nos métodos `handleMethodArgumentNotValid` e `handleExceptionInternal`.

```java
@ControllerAdvice // indicating this is a class all controllers should use
@Log4j2
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(BadRequestException.class) // when there is an exception of the type BadRequest, execute the method below
  public ResponseEntity<BadRequestExceptionDetails> handleBadRequestException(BadRequestException bre) {
    return new ResponseEntity<>(
      BadRequestExceptionDetails.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.BAD_REQUEST.value())
        .title("Bad Request Exception. Check the documentation!")
        .details(bre.getMessage())
        .developerMessage(bre.getClass().getName())
        .build(),
        HttpStatus.BAD_REQUEST
    );
  }
  
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
    log.info("Fields {}", exception.getBindingResult().getFieldError().getField());

    List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
    String fields = fieldErrors.stream().map(FieldError::getField).collect(Collectors.joining(", "));
    String fieldsMessage = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));

    return new ResponseEntity<>(
      ValidationExceptionDetails.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.BAD_REQUEST.value())
        .title("Bad Request Exception. Invalid fields")
        // .details(exception.getMessage())
        .details("Check the field(s) error")
        .developerMessage(exception.getClass().getName())
        .fields(fields)
        .fieldsMessage(fieldsMessage)
        .build(),
        HttpStatus.BAD_REQUEST
    );
  }

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(
			Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

    ExceptionDetails exceptionDetails = ExceptionDetails.builder()
                                          .timestamp(LocalDateTime.now())
                                          .status(status.value())
                                          .title(ex.getCause().getMessage())
                                          .details(ex.getMessage())
                                          .developerMessage(ex.getClass().getName())
                                          .build();
		return new ResponseEntity<>(exceptionDetails, headers, status);
	}
  
}
```


## Class 23 - Paginação

Uma forma de nao retornar todos os dados de uma vez só para o frontend é
utilizando paginação.

No Spring, para retornarmos um resultado paginado, utilizamos o `Page`.

Então, por exemplo, ao invés de utilizarmos um `List` para retornar uma lista,
usamos o `Page`. Também fazemos o método que irá listar paginado receber como
parâmetro um `Pageable` do _spring.data.domain_.

- controller

```java
@GetMapping
  public ResponseEntity<Page<Anime>> list(Pageable pageable) {
    log.info("Testing");
    log.info(dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));

    return new ResponseEntity<>(animeService.listAll(pageable), HttpStatus.OK);
  }

```

- service (passando o _pageable_ para o repositorio tambem)

```java
public Page<Anime> listAll(Pageable pageable) {
    return animeRepository.findAll(pageable);
  }
```

O repository herda de `JpaRepository` que por sua vez herda de uma interface que implementa o _pageable_, portanto, podemos utilizar da forma mostrada acima.

```java
public interface JpaRepository<T, ID> extends PagingAndSortingRepository<T, ID>, QueryByExampleExecutor<T> 
```


Para listar as paginas, podemos colocar na URL do endpoint paginado a _query_ dizendo o tamanho
e a página desejada, ou seja, o `size` e o `page`.

```
http://localhost:8080/animes?size=5&page=0
	...
http://localhost:8080/animes?size=5&page=1
```

O retorno do _pageable_ é semelhante JSON ao mostrado abaixo:

```json
{
  "content": [
    {
      "id": 9,
      "name": "bbbb"
    },
    {
      "id": 10,
      "name": "cccc"
    },
    {
      "id": 11,
      "name": "dddd"
    },
    {
      "id": 12,
      "name": "eeeee"
    },
    {
      "id": 13,
      "name": "fffff"
    }
  ],
  "pageable": {
    "sort": {
      "sorted": false,
      "unsorted": true,
      "empty": true
    },
    "offset": 5,
    "pageNumber": 1,
    "pageSize": 5,
    "paged": true,
    "unpaged": false
  },
  "totalPages": 5,
  "totalElements": 21,
  "last": false,
  "size": 5,
  "number": 1,
  "sort": {
    "sorted": false,
    "unsorted": true,
    "empty": true
  },
  "first": false,
  "numberOfElements": 5,
  "empty": false
}
```


## Class 24 - WebMvcConfigurer

Por padrao, o Spring retorna 20 elementos por página. Isso pode ser modificado
adicionando uma configuração que afete o Spring como um todo.

Para isso, vamos criar uma classe que implementa a interface `WebMvcConfigurer`.

Como é uma configuração que queremos que seja aplicada globalmente no Spring,
adicionamos a anotação `@Configuration`.

```java
@Configuration
public class DevDojoWebMvcConfigurer implements WebMvcConfigurer {
  
  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    PageableHandlerMethodArgumentResolver pageHandler = new PageableHandlerMethodArgumentResolver();
    int page = 0;
    int size = 5;
    pageHandler.setFallbackPageable(PageRequest.of(page, size));
    resolvers.add(pageHandler);
  }
}
```

Portanto, a resposta para um request sem _query parameters_ retornará páginas com
no máximo 5 elementos.

```json
{
  "content": [
    {
      "id": 2,
      "name": "updated"
    },
    {
      "id": 4,
      "name": "updated test"
    },
    {
      "id": 6,
      "name": "null"
    },
    {
      "id": 7,
      "name": "dadasd"
    },
    {
      "id": 8,
      "name": "aaaa"
    }
  ],
  "pageable": {
    "sort": {
      "sorted": false,
      "unsorted": true,
      "empty": true
    },
    "offset": 0,
    "pageNumber": 0,
    "pageSize": 5,
    "paged": true,
    "unpaged": false
  },
  "totalPages": 5,
  "totalElements": 21,
  "last": false,
  "size": 5,
  "number": 0,
  "sort": {
    "sorted": false,
    "unsorted": true,
    "empty": true
  },
  "first": true,
  "numberOfElements": 5,
  "empty": false
}
```

Sendo que temos 21 elementos, se quisermos retornar todos eles de uma vez, podemos
modificar a request passando o _size_ no endpoint paginado:

```
http://localhost:8080/animes?size=21
```


## Class 25 - Sorting, Log SQL

Por padrao, ao adicionarmos o _Pageable_, nós já temos o _Sort_.

Precisamos falar sobre qual atributo queremos fazer o sort e se o mesmo é
ascendente ou descendente.

Exemplos:

```
http://localhost:8080/animes?sort=name,asc

http://localhost:8080/animes?sort=name,desc


// sort pelo id
http://localhost:8080/animes?sort=id,desc

http://localhost:8080/animes?sort=id,asc
```

Esse sort é feito no nivel de banco de dados, ou seja, é feito uma query `order by` para obter a ordenação.


Obs.: Até o momento, a query do _hibernate_ são exibidas a parte do logs do Spring no terminal.

Para modificar isso, podemos adicionar `logging` no `application.yml`, restringindo ao _hibernate_, como mostrado abaixo:

```yml
spring:
	...
	...
  jpa:
    hibernate:
    # show-sql: true            <----

logging:                        <----
  level:			                  <----
    org:                        <----
      hibernate:                <----  
        SQL: DEBUG              <----
```


## Class 26 - RestTemplate getForObject e getForEntity

O spring fornece uma ferramenta para realizar chamadas para serviços externos
e a mesma já faz o mapeamento da resposta automaticamente.


```java
@Log4j2
public class SpringClient {
  
  public static void main(String[] args) {
    String url = "http://localhost:8080/animes/2";
    String urlWithPlaceholder = "http://localhost:8080/animes/{id}";

    // returns the whole response with status code, etc
    ResponseEntity<Anime> entity = new RestTemplate().getForEntity(url, Anime.class);

    log.info(entity);

    // returns only the object/body of the response
    Anime object = new RestTemplate().getForObject(url, Anime.class);
    log.info(object);

    // using placeholder variables instead of inserting them directly in the URL
    Anime object2 = new RestTemplate().getForObject(urlWithPlaceholder, Anime.class, 2);
    log.info(object2);
  }
}
```


## Class 27 - RestTemplate exchange

Para fazer o mapeamento de uma lista de objetos usando o _getForObject_,
poderíamos fazer da seguinte forma:

```java
String urlListOfAnimes = "http://localhost:8080/animes/all";

Anime[] animes = new RestTemplate().getForObject(urlListOfAnimes, Anime[].class);
log.info(Arrays.toString(animes));
```

Isso retorna um array de animes. Poderíamos fazer uma conversão para lista, mas isso
não seria recomendado pois estaríamos fazendo um _cast_.

Poderíamos, então, utilizar _super type tokens_ para realizar tal conversão, e com o _RestTemplate_, isso
é possível através do método _exchange_.

Abaixo é mostrada a sintaxe do método _exchange_.

```java
   // returning a list of animes
    ResponseEntity<List<Anime>> exchange = new RestTemplate().exchange(urlListOfAnimes, HttpMethod.GET, null, 
      new ParameterizedTypeReference<List<Anime>>(){
      });
    log.info(exchange.getBody());
```

Ou seja, com o _getForObject_ teríamos um _Array_ e com o _exchange_ temos
uma lista.


## Class 28 - RestTemplate POST

Agora será exibido como executar requisições POST utilizando o _rest template_.

Assim como o GET, temos o _postForObject_, _postForEntity_ e o _exchange_ utilizando o POST.


Abaixo é mostrado com o _postForObject_ e o _exchange_.

```java
// POST
    Anime kingdom = Anime.builder().name("kingdom").build();
    Anime kingdomSaved = new RestTemplate().postForObject("http://localhost:8080/animes/",kingdom, Anime.class);
    log.info("Saved animed {}", kingdomSaved);
    
    Anime samurai = Anime.builder().name("samurai").build();
    ResponseEntity<Anime> samuraiSaved = new RestTemplate().exchange(
      "http://localhost:8080/animes/",
      HttpMethod.POST,
      new HttpEntity<>(samurai, createHttpHeaders()),  
      Anime.class
    );
    log.info("Saved animed {}", samuraiSaved);
  }

  private static HttpHeaders createHttpHeaders() {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    // httpHeaders.setBearerAuth(token);
    return httpHeaders;
  }
```

## Class 29 - RestTemplate PUT and DELETE

O rest template possui os métodos PUT e DELETE, porém, ambos retornam `void` e na maioria das
vezes queremos saber o que aconteceu na requisição. Portanto, utilizaremos o `exchange` com tais métodos.

Abaixo temos alguns exemplos:

```java
// PUT
    Anime animeToBeUpdated = samuraiSaved.getBody();
    animeToBeUpdated.setName("samurai 222");
    ResponseEntity<Void> animeUpdated = new RestTemplate().exchange(
      "http://localhost:8080/animes/",
      HttpMethod.PUT,
      new HttpEntity<>(animeToBeUpdated, createHttpHeaders()),  
      Void.class
    );
    log.info("Updated animed {}", animeUpdated);
    
    
    // DELETE
    ResponseEntity<Void> animeDeleted = new RestTemplate().exchange(
      "http://localhost:8080/animes/{id}",
      HttpMethod.DELETE,
      null,  
      Void.class,
      animeToBeUpdated.getId()
    );
    log.info("Updated animed {}", animeDeleted);
```

No DELETE, o id esperado na URL é passado como último parâmetro do método `exchange`.



## Class 30 - Spring Data JPA Test pt 01


Iniciamos os testes unitários com o nosso banco de dados. Para isso, não utilizaremos
como banco o `MySQL` (que é o usado para esta aplicação em si), mas sim um banco em memória o qual todas as vezes que executarmos os testes
o mesmo é criado e destruído em seguida.

O banco utilizado será o `H2`, e colocamos o escopo dele como **teste**.

No `pom.xml` adicionamos a dependência do **H2** e `mvn install` para instalá-la (usei o vscode).

```xml
		<dependency>
			<groupId>com.h2database</groupId>
			<artifactId>h2</artifactId>
			<scope>test</scope>
		</dependency>
```


Criamos um teste para o `repository` e o anotamos com _@DataJpaTest_.

Podemos modificar o nome exibido da classe de teste com a anotação _@DisplayName_.

O uso do _@Autowired_ apesar de não indicado de usar nos campos, nos testes podemos usar sem problemas.

Para realizar as asserções nos testes, podemos utilzar diversos pacotes. O utilizado no exemplo abaixo foi o `assertj`.

Para o nome do método do teste, a convenção utilizada foi

-  o nome do método seguido do que ele tem que fazer e finalizado se deve ser quando for executado com sucesso.


```java
@DataJpaTest
@DisplayName("Tests for Anime Repository")
public class AnimeRepositoryTest {
  @Autowired
  private AnimeRepository animeRepository;
  
  @Test
  @DisplayName("Save creates anime when successfull")
  void save_PersistAnime_WhenSuccessfull() {
    Anime animeToBeSaved = createAnime();
    Anime animeSaved = this.animeRepository.save(animeToBeSaved);
    
    // using assertj for the assertions
    Assertions.assertThat(animeSaved).isNotNull();
    Assertions.assertThat(animeSaved.getId()).isNotNull();
    Assertions.assertThat(animeSaved.getName()).isEqualTo(animeToBeSaved.getName());
  }

  private Anime createAnime() {
    return Anime.builder()
            .name("Dragon Ball Z")
            .build();
  }
}
```


Após executar o teste, é feito o `Rollback` da transação.



## Class 31 - Spring Data JPA Test pt 02

Mais exemplos de testes do repositório JPA com "edge cases", por exemplo, quando um item não é encontrado no banco.

```java
@Test
  @DisplayName("Find by name returns list of anime when successfull")
  void findByName_ReturnsListOfAnime_WhenSuccessfull() {
    Anime animeToBeSaved = createAnime();

    Anime animeSaved = this.animeRepository.save(animeToBeSaved);

    String name = animeSaved.getName();

    List<Anime> animes = this.animeRepository.findByName(name);

    Assertions.assertThat(animes).isNotEmpty();

    Assertions.assertThat(animes).contains(animeSaved);
  }

  @Test
  @DisplayName("Find by name returns empty list when no anime is found")
  void findByName_ReturnsEmptyList_WhenAnimeIsNotFound() {
    List<Anime> animes = this.animeRepository.findByName("Not found name");

    Assertions.assertThat(animes).isEmpty();
  }
```

## Class 32 - Spring Data JPA Test pt 03 - Exceções

Uma dica: quando temos o mesmo objeto e diversas asserções no mesmo, podemos
encadear as asserções, como mostrado abaixo:

```java
Assertions.assertThat(animes)
            .isNotEmpty()
            .contains(animeSaved);
```

Vamos agora fazer asserções de exceções. Elas podem ser executadas de duas formas, como mostrado abaixo:

```java
@Test
  @DisplayName("Save throws ConstraintViolationException when name is empty")
  void save_ThrowConstraintViolationException_WhenNameIsEmpty() {
    Anime anime = new Anime();

    // first way of asserting the exception
    // Assertions.assertThatThrownBy(() -> this.animeRepository.save(anime))
    //   .isInstanceOf(ConstraintViolationException.class);

    // another way of asserting the exception
    Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
      .isThrownBy(() -> this.animeRepository.save(anime))
      .withMessageContaining("The anime name cannot be empty");

  }
```


## Class 33 - Unit Tests pt 01

Vamos agora criar testes unitários para o controller.

Quando vamos criar testes unitários, temos algumas opções para utilizar, como:

- `@SpringBootTest`: o problema de utilizar este é que teremos o contexto do Spring
sendo inicializado, ou seja, ele meio que vai inicializar a aplicação para realizar os testes. Portanto, se ao executarmos os testes utilizando o `@SpringBootTest` e o nosso banco de dados, por exemplo, não estiver executando (e portanto, não conseguimos nos conectar ao mesmo), ocorrerá uma exceção.

Portanto, quando trabalharmos com teste unitário, vamos utilizar o `@ExtendWith` que através do `SpringExtension.class` estamos dizendo que queremos
utilizar o `JUnit` com o `Spring`.

- `@ExtendWith(SpringExtension.class)`

Existem duas anotações que precisamos utilizar. São elas:

- `@InjectMocks`: este deve ser utilizado quando se quer testar a classe em si

- `@Mock`: o @Mock é utilizado para todas as classes utilizadas dentro do `@InjectMocks`. Por exemplo, em AnimeController, temos outras duas classes, são elas, `DateUtil` e `AnimeService`. O `AnimeService`, por exemplo, tem mais coisas, como o `AnimeRepository`. Portanto, para não termos
que inicializar todas essas classes, é feito o `Mock` do comportamento da classe. Assim, se a classe tem um método de listar, podemos "mockar" tal método/comportamento para tomar precedência e ser executado durante os testes.


A definição do comportamento de cada método pelo mock pode ser feito antes de cada teste através da anotação `@BeforeEach`.

O Mock pode ser feito com o `BDDMockito`. Ou seja, quando o método em questão for chamado (e muitas vezes não importando o argumento passado), então retornamos um valor criado por nós, ou seja, uma resposta mockada.

Abaixo, o exemplo de teste unitário do `AnimeController` (então é usado o `@InjectMock` nesta classe) e na classe interna, que é o `AnimeService`, é feito o mock do comportamento de `listAll`.

```java
@ExtendWith(SpringExtension.class)
public class AnimeControllerTests {
  
  @InjectMocks
  private AnimeController animeController;

  @Mock
  private AnimeService animeServiceMock;

  @BeforeEach
  void setup() {
    // mocking the method animeService.listAll
    PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createValidAnime()));
    BDDMockito.when(animeServiceMock.listAll(ArgumentMatchers.any()))
      .thenReturn(animePage);

  }

  @Test
  @DisplayName("List returns list of anime inside page object when successful")
  void list_ReturnsListOfAnimesInsidePageObject_WhenSuccessful() {
    String expectedName = AnimeCreator.createValidAnime().getName();

    // Beginning the test of the method list of the controller
    Page<Anime> animePage = animeController.list(null).getBody();

    Assertions.assertThat(animePage).isNotNull();
    
    Assertions.assertThat(animePage.toList())
                .isNotEmpty()
                .hasSize(1);

    Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);

  }
}
```

## Class 34 - Unit Tests pt 02

Nesta parte continuamos os testes dos demais métodos do _controller_.

É mostrado como sobreescrever um comportamento/mock criado no `@BeforeEach`.

Por exemplo, foi definido no `@BeforeEach` que ao chamarmos o método `findByName` uma lista de _Animes_ é retornada.

```java
  @BeforeEach
  void setup() {

    BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString()))
      .thenReturn(List.of(AnimeCreator.createValidAnime()));

  }
```

Para testar um retorno quando um anime não é encontrado, sobreescrevemos no próprio teste o método mockado.

```java
  @Test
  @DisplayName("findByName returns an empty list of anime when anime is not found")
  void findByName_ReturnsAnEmptyList_WhenAnimeIsNotFound() {

    // returning an empty list
    BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString()))
      .thenReturn(Collections.emptyList());
      
    // Beginning the test of the method list of the controller
    List<Anime> animes = animeController.findByName("name").getBody();

    Assertions.assertThat(animes)
                .isNotNull()
                .isEmpty();

  }
```


Quando temos um método que retorna `void`, a forma que utilizamos o _mockito_ é
colocando o `doNothing()` no início, como mostrado a seguir: 

```java
BDDMockito.doNothing()
            .when(animeServiceMock).replace(ArgumentMatchers.any(AnimePutRequestBody.class));

BDDMockito.doNothing()
            .when(animeServiceMock).delete(ArgumentMatchers.anyLong());

```

Abaixo o teste do método `replace`, que retorna `null`.

```java
  @Test
  @DisplayName("replace updates anime when successful")
  void replace_UpdatesAnime_WhenSuccessful() {
    
    Assertions.assertThatCode(
      () -> animeController.replace(AnimePutRequestBodyCreator.createAnimePutRequestBody())
    ).doesNotThrowAnyException();

    ResponseEntity<Void> entity = animeController.replace(AnimePutRequestBodyCreator.createAnimePutRequestBody());

    Assertions.assertThat(entity).isNotNull();

    Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

  }



  @Test
  @DisplayName("delete removes anime when successful")
  void delete_RemovesAnime_WhenSuccessful() {
    
    Assertions.assertThatCode(
      () -> animeController.delete(1)
    ).doesNotThrowAnyException();

    ResponseEntity<Void> entity = animeController.delete(1);

    Assertions.assertThat(entity).isNotNull();

    Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

  }
```

## Class 35 - Unit Tests pt 03

Vamos agora criar os testes unitários para o serviço.

Agora, o `@InjectMocks` é para o `AnimeService` e o `@Mock` é para o `AnimeRepository`.


Abaixo, é mostrado um exemplo de teste lançando uma exceção:

```java
  @BeforeEach
  void setup() {
	...
	...

    BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))
      .thenReturn(Optional.of(AnimeCreator.createValidAnime()));
	
	...
  }


  @Test
  @DisplayName("findByIdOrThrowBadRequestError throws BadRequestException when anime is not found")
  void findByIdOrThrowBadRequestError_ThrowsBadRequestException_WhenAnimeIsNotFound() {
    BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))
      .thenReturn(Optional.empty());

    Assertions.assertThatExceptionOfType(BadRequestException.class)
      .isThrownBy(() -> this.animeService.findByIdOrThrowBadRequestError(1));

  }
```
 

## Class 36 - Testes de Integração pt 01

Foi visto como testar o banco de dados, fazer testes de unidade, agora será executado
a aplicação por inteira, o mais próximo possível de como será em produção.

Quando vamos realizar testes de integração, necessitamos inicializar todo o Spring,
então utilizamos a anotação `@SpringBootTest`.

Podemos definir que os testes sejam executados em uma porta aleatória a cada vez que forem executados através do `webEnvironment`.

Também, ao invés de utilizarmos as classes, iremos utilizar o `TestRestTemplate` e `@Autowired` (ou seja, sem uso de mocks).

Dica: Para obter a porta que está sendo utilizada no momento da execução do teste podemos utilizar a anotação `@LocalServerPort`, mostrada a seguir:

```java
@LocalServerPort
private int port;
```

Também queremos executar os testes com um banco de dados em memória, então, utilizamos a anotação `@AutoConfigureTestDatabase` para definir a configuração do banco.


Obs.: Por padrão, o `TestRestTemplate` onde está localizado o domínio e a porta, então, necessita somente passar o endpoint.



```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class AnimeControllerIT {
  @Autowired
  private TestRestTemplate testRestTemplate;

  @Autowired
  private AnimeRepository animeRepository;

  @LocalServerPort
  private int port;



  @Test
  @DisplayName("list returns list of anime inside page object when successful")
  void list_ReturnsListOfAnimesInsidePageObject_WhenSuccessful() {
    // using the actual repository to create the anime
    Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());

    String expectedName = savedAnime.getName();

    // PageableResponse is a wrapper created for the PageImpl class
    PageableResponse<Anime> animePage = testRestTemplate.exchange("/animes", HttpMethod.GET, null, 
      new ParameterizedTypeReference<PageableResponse<Anime>>(){
      }).getBody();

    Assertions.assertThat(animePage).isNotNull();
    
    Assertions.assertThat(animePage.toList())
                .isNotEmpty()
                .hasSize(1);

    Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);

  }
}
```

Como pode ser visto, os testes de integração iniciam o servidor como um todo, iniciando o banco em memória também.




## Class 37 - Testes de Integração pt 02

É adicionado mais testes de integração. 


Se observamos nos logs, a aplicação está listando os testes em modo DEBUG. Neste modo, um tempo maior é consumido
para finalizar os testes. 

Podemos modificar isso definindo um logback. Na pasta testes, criamos uma nova pasta/pacote chamaod `resources` e dentro da mesma, um arquivo chamado `logback-test.xml`, como mostrado abaixo e visto também [aqui](https://www.baeldung.com/logback):

```xml
<configuration>
  <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
    <encoder>
      <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
    </encoder>
  </appender>

  <root level="info">
    <appender-ref ref="STDOUT" />
  </root>
</configuration>
```

Obs.: o `root level` foi modificado de `debug` para `info`.



**Observação**: O `@DataJpaTest` remove os dados/objetos criados no banco ao fim do teste. Entretanto, a anotação `@AutoConfigureTestDatabase` usada
nos testes de integração não faz isso. Existe uma notação no Spring chamada `@DirtiesContext` onde podemos definir para o Spring que antes de cada teste
ele deve considerar que o contexto esteja "sujo". Dessa forma, o Spring vai deltar/fazer um DROP no banco inteiro, recriando o mesmo para cada método.

```java
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
```

Os testes de integração por padrão são mais lentos, por isso é recomendado deixá-los em uma pasta/pacote separado, de forma que possam todos serem executados à parte dos testes unitários.



## Class 38 - Maven Profile Para Testes de Integração

Nesta parte, vamos separar os _profiles_ do maven, ou seja, queremos executar
os testes em diferentes ciclos do `maven`. Exemplo: sabe-se que os testes de
integração demoram mais que os unitários, portanto, não é desejado executar
os de integração a todo momento.


No `pom.xml`, adicionamos uma tag `profiles`, e dentro, uma `profile`. Observando
os logs, o plugin utilizado nos testes é o `maven-surefire`. Utilizaremos ele
para executar os testes de integração.
Observe que definimos que os arquivos com `IT.` serão executados no ciclo do teste de integração.

```xml
	<profiles>
		<profile>
			<id>integration-tests</id>
			<build>
				<plugins>
					<plugin>
						<groupId>org.apache.maven.plugins</groupId>
						<artifactId>maven-surefire-plugin</artifactId>
						<version>${maven-surefire-plugin.version}</version>
						<configuration>
							<includes>
								<include>**/*IT.*</include>
							</includes>
						</configuration>
					</plugin>
				</plugins>
			</build>
		</profile>
	</profiles>
```

Para executar o _profile_ dos testes de integração, utizamos o comando abaixo, onde `-P` significa o _profile_:

```bash
mvn test -Pintegration-tests
```


## Class 39 - Spring Security pt 01 - Autenticação em memória

Para adicionar segurança, iniciamos colocando no `pom.xml` a dependência `spring-security`.

```xml
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-security</artifactId>
		</dependency>
```




Após instalar a dependência, na próxima vez que executar a aplicação, é exibida uma senha nos logs.
A mesma será utilizada como `Basic Authorization` nas _requests_, onde, por padrão, o _username_ é _user_ e o _password_ a senha exibida, por exemplo, como a mostrada abaixo:

```
Using generated security password: edef18f2-2e13-4c17-bf27-4fe5a9827721
```

Entretanto, toda vez que a aplicação for reiniciada, uma nova senha será gerada.

#### Customizando a parte de segurança

Criamos um pacote `config` e dentro, uma classe chamada, por exemplo, `SecurityConfig`.

A segurança no Spring funciona por meio de filtros. Mais info sobre pode ser encontrada ao pesquisar por [security filter chain](https://www.google.com/search?q=security+filter+chain&safe=active&sxsrf=ALeKk03QCMGl4s22zT8Jg5jlcF--rbfWaQ:1610808157773&source=lnms&tbm=isch&sa=X&ved=2ahUKEwjT6K2A2KDuAhXQHLkGHY8jCzcQ_AUoAnoECBYQBA&biw=2283&bih=863#imgrc=xakhEWwn-1FyzM).

Para indicar que a classe é uma configuração de segurança, pode ser usado a anotação `@EnableWebSecurity`. Além disso, é preciso trabalhar com _beans_ que o Spring irá reconhecer,
então herdamos a classe `WebSecurityConfigurerAdapter`.

Na classe, sobreescrevemos dois métodos:

- o primeiro indicamos o que se está querendo proteger com o protocolo HTTP. Na configuração mostrada, está indicado que todas as requisições necessitam passar por uma autorização e que todas elas sejam autenticadas por `Basic Auth`.
- no segundo _override_, é configurado o usuário em memória. O _password_ deve ser criptografado. Isso pode ser feito com um `PasswordEncoder`. Note que **ao configurar o usuário, a senha automática criada pelo Spring Security não é mais gerada/exibida nos logs após o reinicio da aplicação**, sendo o _password_ exibido aquele utilizado no _override_ da autenticação. Um exemplo é mostrado abaixo:

```
a.d.springboot2.config.SecurityConfig    : Password encoded {bcrypt}$2a$10$YWhnCTjaKgXOZlm9afF/remoSyA0thBSCAcHQXsN4RkurEyZGSwA.
```

Abaixo é mostrada a classe de configuração criada até o momento, onde é adicionado dois usuários em memória:

```java
@EnableWebSecurity
@Log4j2
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
          .anyRequest()
          .authenticated()
          .and()
          .httpBasic();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    PasswordEncoder passwordEncoder= PasswordEncoderFactories.createDelegatingPasswordEncoder();

    log.info("Password encoded {}", passwordEncoder.encode("test"));

    auth.inMemoryAuthentication()
            .withUser("AdminUser")
            .password(passwordEncoder.encode("admin-pass"))
            .roles("USER", "ADMIN")
            .and()
            .withUser("devdojo")
            .password(passwordEncoder.encode("devdojo-pass"))
            .roles("USER");
  }
}
```


## Class 40 - Spring Security pt 01 - CSRF Token

Ao executar o GET com o usuario/senha como Basic Auth, obtem-se a lista de animes. Porém, ao executar
um POST, no momento, obtém-se 403, ou seja, o usuário está autenticado mas não possui permissão para executar determinado recurso (que seria a criação de um anime no banco de dados).

Como não se está sendo utilizado qualquer tipo de _role_ na aplicação, o que está acontecendo é [CSRF](https://www.youtube.com/watch?v=eWEgUcHPle0&ab_channel=PwnFunction). Por padrão, o Spring oferece suporte à CSRF.


Utilizando o CSRF como mostrado abaixo, 

```java
.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
```

possibilita-se que aplicações frontend (React, Angular, etc) venham a obter o token enviado via Cookie no header da resposta do servidor.
Por exemplo, por meio de uma requisicao GET, pode-se obter o token CSRF da resposta. Um exemplo é mostrado abaixo:

```
XSRF-TOKEN	5ded52ac-e217-4948-b753-6ae12da9d7be
```

Ou seja, o nome do token encontrado no Cookie é `XSRF-TOKEN`. 
Assim, nas demais requests (do tipo POST, por exemplo), basta adiciona esse token ao header da requisicao com um `X-` à frente, ou seja, escrevendo `X-XSRF-TOKEN`.

No curso, o CSRF ficará desabilitado, mas em produção isso não deve acontecer.

```java
@Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .csrf().disable()
        // .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
        .authorizeRequests()
          .anyRequest()
          .authenticated()
          .and()
          .httpBasic();
  }
```


## Class 41 - Spring Security pt 02 - Segurança a nível de métodos com PreAuthori

Podemos querer protejer diferentes métodos/endpoints com diferentes _roles_.

Dependendo do enpoint/método, pode-ser querer permitir somente que alguns _roles_ o executem.

Isso é possível usando o  `@PreAuthorize`, ou seja, antes de executar o método/endpoint, é mencionado qual _role_ é permitada a executar o método/endpoint.

O `@PreAuthorize` necessita ser habilitado na configuração global de segurança (na classe SecurityConfig, onde temos o `@EnableWebSecurity`). Para isso, utiliza-se a anotação `@EnableGlobalMethodSecurity` como mostrada abaixo.


- habilitando o `@PreAuthorize` na classe global de configuração de segurança.

```java
@EnableWebSecurity
@Log4j2
@EnableGlobalMethodSecurity(prePostEnabled = true)         <--------------
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  
	...
	...
}
```

- adiciona autorização para somente usuários com _role_ `ADMIN` poderem realizar o _save_.

```java
  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")     <-----------
  public ResponseEntity<Anime> save(@RequestBody @Valid AnimePostRequestBody animePostRequestBody) {
    // Jackson already maps the json properties to the class Anime so that we don't need to set the name in the animeService
    return new ResponseEntity<>(animeService.save(animePostRequestBody), HttpStatus.CREATED);
  }
```


## Class 42 - Spring Security pt 03 - Authentication Principal e página padrão de login do próprio Spring

Será visto como pegar o usuário que está autenticado na requisição.

Para isso, pode-se utilizar a anotação `@AuthenticationPrincipal`, onde o tipo
do parâmetro esperado é `UserDetails`. O detalhes do usuário será encontrado na variável _userDetails_.

```java
  @GetMapping(path = "by-id/{id}")
  public ResponseEntity<Anime> findByIdAuthenticationPrincipal(
    @PathVariable long id,
    @AuthenticationPrincipal UserDetails userDetails      <---------------
  ){

    log.info(userDetails);

    return ResponseEntity.ok(animeService.findByIdOrThrowBadRequestError(id));
  }
```


Outra coisa é que se pode utilizar uma página de login padrão do próprio Spring.
Na classe de configuração de segurança, pode-se adicionar um `formLogin()`, e manter o `httpBasic(), então o Spring utilizará ambos.

```java
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .csrf().disable()
        // .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
        .authorizeRequests()
          .anyRequest()
          .authenticated()
          .and()
          .formLogin()   <----------
          .and()         <----------
          .httpBasic();
  }
```

Assim, ao acessar um endpoint (no caso todos estão protegidos nessa aplicação), o usuário
é redirecionado para o endpoint `/login` onde é mostrado o formulário de login  (_http://localhost:8080/login_). Para se deslogar, para acessar o endpoint `/logout` (_http://localhost:8080/logout_).

Na configuração de segurança, pode-se utilizar outros filtros

- `UsernamePasswordAuthenticationFilter`
- filtro que gera a página _default_ de login: `DefaultLoginPageGeneratingFilter`
- filtro que gera a página _default_ de logout: `DefaultLogoutPageGeneratingFilter`
- `FilterSecurityInterceptor`: verifica se o usuário está autorizado
	- tem-se dois processos na parte de segurança, que são:
		- autenticação (Authentication)
		- autorização (Authorization)


## Class 43 - Spring Security pt 04 - Autenticação com usuário no banco de dados

Como obter o usuario do banco de dados e realizar a autenticação ao invés de utilizar o banco em memória será mostrado nesta seção.

- Uma classe para representar o usuário será criada. A classe se chamará `DevDojoUser`. 
- A classe de usuário criada irá implementar a interface `UserDetails`, de modo que ela poderá fazer parte do `AuthenticationPrincipal`.
	- alguns métodos não serão utilizados nesse tutorial, como o `isEnabled`, `isCredentialsNonExpired`, `isAccountNonLocked`, e `isAccountNonExpired`, portanto, todos retornarão `true`.



É criado um `Repository` para a classe de domínio (`Entity`) _DevDojoUser_.

Também é criado um serviço chamado `DevDojoUserDetailsService` que implementa a interface do Spring `UserDetailsService`.

Após criar o serviço acima, o utilizamos na classe `SecurityConfig` (classe de configuraçao de segurança).


Então, ao executar uma request, o usuário é buscado do banco de dados, ou seja, na classe de segurança global `SecurityConfig` o método abaixo é executado

```java
@Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    PasswordEncoder passwordEncoder= PasswordEncoderFactories.createDelegatingPasswordEncoder();

    log.info("Password encoded {}", passwordEncoder.encode("devdojo-pass"));


    auth.userDetailsService(devDojoUserDetailsService)
          .passwordEncoder(passwordEncoder);
  }
```

Este, por sua vez, por meio de polimorfismo, executa o serviço `DevDojoUserDetailsService` executando o método _loadUserByUsername_:

```java
@Service
@RequiredArgsConstructor
public class DevDojoUserDetailsService implements UserDetailsService {
  private final DevDojoRepository devDojoRepository;

  @Override
  public UserDetails loadUserByUsername(String username) {
    return Optional.ofNullable(devDojoRepository.findByUsername(username))
            .orElseThrow(() -> new UsernameNotFoundException("DevDojoUser not found"));
  }
  
}
```

o qual executa o método _findByUsername_ buscando o usuário no banco de dados

```java
public interface DevDojoRepository extends JpaRepository<DevDojoUser, Long> {
  
  DevDojoUser findByUsername(String username);
  
}
```


Extra: a parte de configuração do Spring suporta múltipla autenticação de diferentes
tipos de providers, ou seja, pode-se querer obter o usuário tanto do banco de dados em memória quanto de um banco MySQL na mesma configuração:

```java
  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    PasswordEncoder passwordEncoder= PasswordEncoderFactories.createDelegatingPasswordEncoder();

    log.info("Password encoded {}", passwordEncoder.encode("devdojo-pass"));

    auth.inMemoryAuthentication()
            .withUser("admin2")
            .password(passwordEncoder.encode("devdojo-pass"))
            .roles("USER", "ADMIN")
            .and()
            .withUser("devdojo2")
            .password(passwordEncoder.encode("devdojo-pass"))
            .roles("USER");

    auth.userDetailsService(devDojoUserDetailsService)
          .passwordEncoder(passwordEncoder);
  }
```  

Ou seja, agora temos dois pontos de entrada de usuários: do InMemory e do banco MySQL (por meio do _devDojoUserDetailsService_).


## Class 44 - Spring Security pt 05 - Proteção de URL com Antmatcher

Foi visto anteriormente que em algumas URLs foi utilizado o `@PreAuthorize`
para proteger o endpoint do acesso dado um determinado _role_.

```java
@PreAuthorize("hasRole('ADMIN')")
```

Contudo, pode-se imaginar que todas as URLs que executarão uma mudança de estado no servidor
se iniciarão com um padrão, por exemplo, `/admin`.

Se existe tal padrão, pode-se utilizar os `antMatchers`, que são expressões regulares, e então verificar se quem está acessando tal URL compreendida pela expressão indicada no `antMatcher` possui um determinado _role_.
Não é necessário colocar a palavra `ROLE_` como argumento do método _hasRole_ pois o mesmo já realiza essa verificação, ou seja, o nome do _role_ já suficiente.

Abaixo, é mostrado a implementação do método _hasRole_ utilizado e onde o mesmo é usado na classe de configuração global (após o `authorizeRequests()`).

```java
// class ExpressUrlAuthorizationConfigurer.class
	private static String hasRole(String role) {
		Assert.notNull(role, "role cannot be null");
		Assert.isTrue(!role.startsWith("ROLE_"),
				() -> "role should not start with 'ROLE_' since it is automatically inserted. Got '" + role + "'");
		return "hasRole('ROLE_" + role + "')";
	}



// class SecurityConfig.java in the project
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .csrf().disable()
        // .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
        .authorizeRequests()
          .antMatchers("/animes/admin/**").hasRole("ADMIN")  // <--------
          .antMatchers("/animes/**").hasRole("USER")	     // <--------			
          .anyRequest() 
          .authenticated()
          .and()
          .formLogin()
          .and()
          .httpBasic();
  }

```

Lembrando que a ordem dos `antMatchers` importa, então deve-se colocar o que
for mais restritivo primeiro. O `anyRequest()` continuou para o caso de haver
outros endpoints que não estejam no padrão dos _matchers_ e ainda assim desejamos
que o usuário só possa acessar se estiver autenticado.



## Class 45 - Spring Security pt 06 - Testes de Integração com Spring Security

Ao adicionar o Spring Security na aplicação, os testes de integração passaram a falhar pois estes não estão cientes das camadas
de segurança inseridas.


Abaixo os passos para adicionar segurança à _suite_ de testes e ao _testRestTemplate_.

1. Cria-se uma classe estática de configuração (que será um Spring Bean). Como é criado um novo bean, no `@AutoWired` devemos qualificar qual o _bean_ que desejamos
injetar.

```java
public class AnimeControllerIT {
  @Autowired
  @Qualifier(value = "testRestTemplateRoleUser") // injecting the Bean created below
  private TestRestTemplate testRestTemplate;

	...
	...

  @TestConfiguration
  @Lazy // wait a little bit before setting up the configurations
  static class Config {
    @Bean(name = "testRestTemplateRoleUser")
    public TestRestTemplate testRestTemplateRoleUserCreator(@Value("${local.server.port}") int port) {
      // creating a new user and password in the in memory database for testing
      RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                      .rootUri("http://localhost:" + port)
                      .basicAuthentication("devdojo", "devdojo-pass");

      return new TestRestTemplate(restTemplateBuilder);
    }
  }


	...
	...
}
```

2. Adiciona-se o `DevDojoRepository` para salvar o usuario no banco de dados em memória e cria-se os usuários de testes com as _roles_ de usuário e admin.

```java
  @Autowired
  private DevDojoRepository devDojoRepository;

  private static final DevDojoUser USER = DevDojoUser.builder()
                  .name("Dev Dojo")
                  .username("devdojo")
                  .password("{bcrypt}$2a$10$a5.cNRnxng6KDN52.fj.veZQcj.imiiqu01MK/Rilt6yKtP7.Jpc2")
                  .authorities("ROLE_USER")
                .build();

  private static final DevDojoUser ADMIN = DevDojoUser.builder()
                  .name("Admin Dev Dojo")
                  .username("admin")
                  .password("{bcrypt}$2a$10$a5.cNRnxng6KDN52.fj.veZQcj.imiiqu01MK/Rilt6yKtP7.Jpc2")
                  .authorities("ROLE_USER,ROLE_ADMIN")
                .build();
```

3. Atualiza o teste para salvar o usuario no banco de dados antes de realizar a requisição ao endpoint

```java
@Test
  @DisplayName("list returns list of anime inside page object when successful")
  void list_ReturnsListOfAnimesInsidePageObject_WhenSuccessful() {
    // using the actual repository to create the anime
    Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());

    // creating a user in the in memory database
    devDojoRepository.save(USER);  // <--------------------------------

    String expectedName = savedAnime.getName();

    // PageableResponse is a wrapper created for the PageImpl class
    PageableResponse<Anime> animePage = testRestTemplate.exchange("/animes", HttpMethod.GET, null, 
      new ParameterizedTypeReference<PageableResponse<Anime>>(){
      }).getBody();

    Assertions.assertThat(animePage).isNotNull();
    
    Assertions.assertThat(animePage.toList())
                .isNotEmpty()
                .hasSize(1);

    Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);

  }
```

 
Para autenticar com o ADMIN, é necessário criar um novo `Bean` na classe estática de configuração de testes.

```java
public class AnimeControllerIT {
	...
	...

  @Autowired
  @Qualifier(value = "testRestTemplateRoleAdmin") // injecting the Bean created below
  private TestRestTemplate testRestTemplateRoleAdmin;


  @TestConfiguration
  @Lazy // wait a little bit before setting up the configurations
  static class Config {

	...
	...
    
    @Bean(name = "testRestTemplateRoleAdmin")
    public TestRestTemplate testRestTemplateRoleAdminCreator(@Value("${local.server.port}") int port) {
      // creating a new user and password in the in memory database for testing
      RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                      .rootUri("http://localhost:" + port)
                      .basicAuthentication("admin", "devdojo-pass");

      return new TestRestTemplate(restTemplateBuilder);
    }

  }

	...
	...
}
```

Então, como adicionamos `antMatcher` para o endpoint de `DELETE`, atualizamos o endpoint nos testes e testamos quando o 403 quando um usuário não ADMIN tenta
realizar o _delete_.

```java
  @Test
  @DisplayName("delete removes anime when successful")
  void delete_RemovesAnime_WhenSuccessful() {
    // first, creating an Anime in the database
    Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
    devDojoRepository.save(ADMIN);
    
    // using testRestTemplateRoleAdmin to perform request
    ResponseEntity<Void> animeResponseEntity = testRestTemplateRoleAdmin.exchange(
      "/animes/admin/{id}",
      HttpMethod.DELETE,
      null,
      Void.class,
      savedAnime.getId()
    );

    Assertions.assertThat(animeResponseEntity).isNotNull();
    
    Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  }
  
  @Test
  @DisplayName("delete returns 403 when user is not admin")
  void delete_Returns403_WhenUserIsNotAdmin() {
    // first, creating an Anime in the database
    Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
    devDojoRepository.save(USER);
    
    ResponseEntity<Void> animeResponseEntity = testRestTemplateRoleUser.exchange(
      "/animes/admin/{id}",
      HttpMethod.DELETE,
      null,
      Void.class,
      savedAnime.getId()
    );

    Assertions.assertThat(animeResponseEntity).isNotNull();
    
    Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
  }

```


## Class 46 - Documentação com SpringDoc OpenAPI pt 01

Vamos utilizar o Swagger para criar a documentação dos endpoints da api.

A [documentação do OpenAPI - Swagger](https://springdoc.org/) fornece os passos para
sua instalação e uso na aplicação.


1. Adicione a seguinte dependência ao `pom.xml`

```xml
   <dependency>
      <groupId>org.springdoc</groupId>
      <artifactId>springdoc-openapi-ui</artifactId>
      <version>1.5.2</version>
   </dependency>
``` 
**obs.:** a versão acima pode ser diferente dependendo da época em que estiver lendo isto.

2. Ao adicionar tal dependência, teremos acesso ao seguinte endereço

```
/swagger-ui.html
```

que exibe os endpoints e seus parâmetros / body esperados nas requisições.


Assim, após instalar a dependência e acessar a página `http://localhost:8080/swagger-ui.html`, é possível visualizar os endpoints.


#### Algumas dicas do Swagger - OpenAPI

Observando os endpoints, alguns parâmetros da maneira atual estão sendo mostrados como obrigatórios quando não deveriam ser, como é o caso do `Pageable`.

- Para esconder um parâmetro do endpoint, pode-se utilizar a anotação `@Parameter(hidden = true)`
- Em rotas com `@AuthenticationPrincipal` do _Spring Security_, o _userDetails_ aparece como obrigatório, quando na verdade não deveria ser.
	- Pensando nisso e no problema do `Pageable` citado antes, existe suporte para o _Spring Security_ e _Spring Data_ (isso é mostrado na [documentação do Spring Doc OpenAPI](https://springdoc.org/#spring-data-rest-support))
	- então, para o `Pageable` e para ignora o `AuthenticationPrincipal`, adiciona-se as seguintes dependências:

então, as dependências adicionadas (na versão 1.5.2) foram: 

```xml
	...
	...

	<properties>
		<java.version>11</java.version>
		<org.mapstruct.version>1.4.1.Final</org.mapstruct.version>
		<springdoc-openapi-ui.version>1.5.2</springdoc-openapi-ui.version>
	</properties>

	<dependencies>

		<dependency>
      			<groupId>org.springdoc</groupId>
      			<artifactId>springdoc-openapi-ui</artifactId>
      			<version>${springdoc-openapi-ui.version}</version>
   		</dependency>

	 	<dependency>
      			<groupId>org.springdoc</groupId>
      			<artifactId>springdoc-openapi-data-rest</artifactId>
      			<version>${springdoc-openapi-ui.version}</version>
   		</dependency>

	 	<dependency>
      			<groupId>org.springdoc</groupId>
      			<artifactId>springdoc-openapi-security</artifactId>
      			<version>${springdoc-openapi-ui.version}</version>
   		</dependency>

	...
	...
```

**dica**: observe que a versão do _Spring Doc_ está indicada no `properties` do `pom.xml` e a referência para tal
versão é utilizada nas demais dependências do _Spring Doc OpenAPI_.



No endpoint com `Pageable`, podemos utilizar a anotação `@ParameterObject` (versão 1.4.1 +). Ao utilizarmos tal anotação, o URL é montada no swagger da forma desejada, onde
é mostrado um campo para a página e outro para o tamanho de cada página (de itens por página).

```java
  @GetMapping
  public ResponseEntity<Page<Anime>> list(@ParameterObject Pageable pageable) {
   
    return new ResponseEntity<>(animeService.listAll(pageable), HttpStatus.OK);
  }
```


## Class 47 - Documentação com SpringDoc OpenAPI pt 02

Vamos adicionar algumas informações a mais nos endpoints, de forma a deixá-los
mais autoexplicativos. Isso é feito adicionando descrições aos parâmetros, dizendo se o mesmo é `required` ou não, etc.

Por exemplo, para o `AnimePostRequestBody`:

```java
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnimePostRequestBody {

  @NotEmpty(message = "The anime name cannot be empty")
  @Schema(description = "This is the Anime's name", example = "Dragon Ball Z", required = true)       // <------------------------
  private String name;  
}
```


Para o _controller_, podemos descrever o que um dado endpoint faz, e ainda agrupar certos endpoints, utilizando o `tag`

```java
@GetMapping
  @Operation(summary = "List all animes paginated", description = "The default size is 20, use the parameter size to change the default value", tags = { "anime" })
  public ResponseEntity<Page<Anime>> list(@ParameterObject Pageable pageable) {
  
    return new ResponseEntity<>(animeService.listAll(pageable), HttpStatus.OK);
  }

```

Podemos também fornecer mais informações relacionadas à resposta do endpoint com a anotação `@ApiResponses`

```java
  @DeleteMapping(path = "/admin/{id}")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "204", description = "Successful operation"),
    @ApiResponse(responseCode = "400", description = "When anime does not exist in database"),
  })
  public ResponseEntity<Void> delete(@PathVariable long id) {
    animeService.delete(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
```


**Dica:** Se modificarmos a url de `/swagger-ui.html` para `/v3/api-docs` (ou seja, algo como `http://localhost:8080/v3/api-docs`), teremos acesso à especificação em json
gerado pelo swagger.


## Class 48 - Spring Boot Actuator

Especialmente quando a aplicação está sendo executando em microserviços e em um Cloud Provider,
é desejado saber se a aplicação está "de pé", funcionando como deveria.

O [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-features.html) nos fornece diversas métricas úteis à aplicação, nos informando o estado em que ela se encontra.


Para usá-lo, adicionamos no `pom.xml` a sua dependência (encontrado no link acima):

```xml
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
```

Observando na [documentação](https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-features.html#production-ready-endpoints), vemos que podemos acessar o endpoint `/actuator/health` para verificar
o estado da aplicação.


Existem outros endpoints interessantes. Na documentação é mostrado como habilitar
os endpoints. É possível habilitar especificamente um determinado endpoint ou até mesmo todos eles.

Para isso, no `application.yml`, podemos configurar da seguinte forma:

- exemplo, para expor todos os endpoints de web

```yml
management:
  endpoints:
    web:
      exposure:
        include: "*"
```

ao fazer isso, teremos acessar, por exemplo, ao endpoint de [_metrics_](https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-features.html#production-ready-endpoints-exposing-endpoints): `http://localhost:8080/actuator/metrics`

Ele lista todos os recursos disponíveis, então basta adiciona ao final da URL o recurso que se deseja acessar,
por exemplo, para _cpu usage_, podemos acessar a URL `http://localhost:8080/actuator/metrics/process.cpu.usage`


- no `info`, podemos adicionar informações da seguinte forma:

```yml
info:
  app: Spring Boot 2 Essetials
  website: https://devdojo.academy/
  github: https://github.com/psatler/spring-boot-essentials
```


**DICA**: da forma como está configurado o projeto até o momento, o endpoint `actuator/info` também está bloqueado para acessos externos.
Podemos liberar acesso ao endpoints do `/actuator` configurando os `antMatchers` na classe de configuração de segurança (`SecurityConfig` neste projeto), adicionando, por exemplo, um `permitAll()`. Vale lembrar que em
produção a liberação de acesso dessa forma (para todos os endpoints) não é realizada assim. Mas para demonstração, é mostrado abaixo a configuração:

```java
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .csrf().disable()
        // .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
        .authorizeRequests()
          .antMatchers("/animes/admin/**").hasRole("ADMIN")
          .antMatchers("/animes/**").hasRole("USER")
          .antMatchers("/actuator/**").permitAll() // <-----------
          .anyRequest()
          .authenticated()
          .and()
          .formLogin()
          .and()
          .httpBasic();
  }
```


**DICA 2**: Caso desejar expor somente alguns endpoints do `actuator` ao invés de todos (`"*"`), podemos
utilizar a configuração `exposure` para incluir somente os endpoints desejados.
Por exemplo:

```yml
management:
  endpoints:
    web:
      exposure:
        include: info, health, metrics
```

Os endpoints abaixo dos indicados acima, por exemplo, `/actuator/metrics/process.uptime` também ficam expostos.



## Class 49 - Monitorando aplicação com prometheus

Ainda falando relacionado ao Spring Actuator, vamos utilizar o [_Prometheus_](https://prometheus.io/), que
nos permite monitorar e visualizar, de forma temporal, as métricas da aplicação.


Na parte de métricas no `application.yml`, podemos incluir o `prometheus` (o Spring Boot possui um endpoint que retorna os dados da maneira que o _Prometheus_ espera). Isso é exibido abaixo:

```yml
management:
  endpoints:
    web:
      exposure:
        include: info, health, metrics, prometheus
```

Ao incluir o _prometheus_, contudo, devemos adicionar uma outra dependência ao `pom.xml` (uma vez que ele não pertence ao Spring Actuator).

```xml
		<dependency>
			<groupId>io.micrometer</groupId>
			<artifactId>micrometer-registry-prometheus</artifactId>
		</dependency>
```


Após a instalação, ao acessar o endereço `http://localhost:8080/actuator/`, podemos ver que o endereço do `prometheus` é exibido na lista.


##### Prometheus com docker

Para utilizarmos o _Prometheus_, vamos adicionar ao `docker-compose.yml` uma imagem do mesmo.

Além disso, vamos criar um arquivo de configuração do _prometheus_ para fazer com que o mesmo se auto monitore. Um
exemplo para tal configuração pode ser visto [aqui](https://prometheus.io/docs/prometheus/latest/getting_started/#configuring-prometheus-to-monitor-itself).

Criamos um arquivo dentro da pasta _resources_ chamado _prometheus.yml_: `src/main/resources/prometheus.yml`

Como visto, há um _job_ chamado _prometheus_ o qual faz com que o _prometheus_ se escute/auto monitore.

Criamos um outro _job_ chamado _springboot-essentials-actuator_ o qual vai monitorar as métricas obtidas do caminho `/actuator/prometheus`. 
Em `static_configs`, definimos o ip da nossa máquina e a porta na qual, no caso deste projeto, o Spring Boot está rodando, ou seja, `8080`.

- abaixo o arquivo `prometheus.yml`:

```yml
global:
  scrape_interval:     15s # By default, scrape targets every 15 seconds.

  # Attach these labels to any time series or alerts when communicating with
  # external systems (federation, remote storage, Alertmanager).
  # external_labels:
  #   monitor: 'codelab-monitor'

# A scrape configuration containing exactly one endpoint to scrape:
# Here it's Prometheus itself.
scrape_configs:
  # The job name is added as a label `job=<job_name>` to any timeseries scraped from this config.
  - job_name: 'prometheus'

    # Override the global default and scrape targets from this job every 5 seconds.
    scrape_interval: 5s

    static_configs:
      - targets: ['localhost:9090']
  
  - job_name: 'springboot-essentials-actuator'

    # Override the global default and scrape targets from this job every 5 seconds.
    scrape_interval: 5s
    metrics_path: "/actuator/prometheus"
    static_configs:
      - targets: ['192.168.0.13:8080'] # 192.168.0.13 is the IPv4 address of my machine, and 8080 is the port which Spring Boot is running on
      # - targets: ['localhost:8080'] # 8080 is the port which Spring Boot is running on
```

- e o arquivo `docker-compose.yml` atualizado para utilizar a imagem do _prometheus_.

```yml
version: '3.2'
services:
  db:
    image: mysql
    container_name: mysql
    environment: 
      MYSQL_ROOT_PASSWORD: root
    
    ports:
      - 3306:3306
    volumes:
      - devdojo_data:/var/lib/mysql

  prometheus:
    image: prom/prometheus:latest
    container_name: prometheus
    volumes:
      - "./src/main/resources/prometheus.yml:/etc/prometheus/prometheus.yml" # mapping the prometheus config created to the container
    command: # when the container is created, run this command setting up the config file mapped above
      - "--config.file=/etc/prometheus/prometheus.yml"
    ports:
      - 9090:9090

volumes:
  devdojo_data:
```

Podemos verificar se o _prometheus_ está executando okay acessando a porta `9090` exposta, ou seja, `http://localhost:9090`.

Na página que aparece, se acessarmos o caminho `status -> targets`, podemos visualizar as métricas/aplicações/endpoints que estão sendo monitorados.

Para visualizarmos os gráficos, na aba `Graph` podemos selecionar a métrica desejada.



## Class 50 - Monitorando métricas com gráficos no Grafana

O objetivo nesta parte é ter uma melhor visualização das métricas obtidas na aula anterior com o _Prometheus_.
Para isto, utilizaremos o [Grafana](https://grafana.com/).

O Grafana será utilizado via Docker, portanto, criaremos um container com a imagem do Grafana via `docker-compose`.

- arquivo `docker-compose.yml`

```yml
version: '3.2'
services:
  db:
    image: mysql
    container_name: mysql
    environment: 
      MYSQL_ROOT_PASSWORD: root
    
    ports:
      - 3306:3306
    volumes:
      - devdojo_data:/var/lib/mysql

  prometheus:
    image: prom/prometheus:latest
    container_name: prometheus
    volumes:
      - "./src/main/resources/prometheus.yml:/etc/prometheus/prometheus.yml" # mapping the prometheus config created to the container
    command: # when the container is created, run this command setting up the config file mapped above
      - "--config.file=/etc/prometheus/prometheus.yml"
    ports:
      - 9090:9090

  grafana:
    image: grafana/grafana:latest
    container_name: grafana
    ports:
      - 3000:3000

volumes:
  devdojo_data:
```

- em seguida, acessamos o endereço na porta exposta, nosso caso, a `3000` (`http://localhost:3000`).
Uma tela de login será inicialmente mostrada. Por padrão, _username_ e _senha_ são `admin` e `admin`, respectivamente.


Para criarmos uma _Dashboard_, adicionamos uma _data source_ (ou seja, quem irá prover a fonte de dados).
Como temos o _Prometheus_ instalado, podemos selecioná-lo. Definimos a URL (a padrão é `http://localhost:9090` para o Prometheus).

Com o _data source_ criado, vamos no `+` no lado esquerdo, selecionamos _Dashboard_, e então, adicionamos um painel.


**DICA**: Para utilizarmos um dashboard já pronto, por exemplo, podemos
ir [no site do Grafana e procurar por uma dashboard pronta](https://grafana.com/grafana/dashboards), por exemplo
a dashboard [JVM Micrometer](https://grafana.com/grafana/dashboards/4701). 

Abaixo, os passos para adicionar tal _dashboard_.

- adiciona o _bean_ exibido na página

```java
  @Bean
	MeterRegistryCustomizer<MeterRegistry> configurer(
			@Value("${spring.application.name}") String applicationName) {
			return (registry) -> registry.config().commonTags("application", applicationName);
	}
```

- adiciona o nome da aplicação no `application.yml` (este é usado no _bean_ acima)

```yml
	...

spring:
  application:
    name: springboot2-essentials

	...
```

- copiamos o ID mostrado na página do Grafana, neste caso, o ID é `4701`
- na aplicação do Grafana, vamos em `+` e selecionamos `import`, inserido o ID copiado, e então clicamos em `Load`.
- neste ponto o _dashboard_ do _JVM Micrometer_ deve ser apresentado.


## Class 51 - Criado imagem com jib e executando via docker-compose

Vamos agora gerar uma imagem da aplicação deste projeto e enviá-la para o docker / docker hub.

Existem diversas formas de gerar uma imagem da aplicação, por exemplo, via _Dockerfile_. Contudo, nesta parte se usado para isso uma ferramenta
do Google chamada [_Jib_](https://github.com/GoogleContainerTools/jib), que auxilia
na hora de "conteinerizar" uma aplicação Java.

Instruções de como utilizá-lo com o _Maven_ podem ser vistas [aqui](https://github.com/GoogleContainerTools/jib/tree/master/jib-maven-plugin).

1. Iniciamos adicionado o plugin do Jib ao `pom.xml`
2. nas configurações do plugin, no `<from>`, indicamos de onde vamos obter a imagem base da aplicação. 
Esta imagem (em `<image>`) será a `gcr.io/distroless/java:11`, que é uma imagem que possui acess limite (exemplo, não acessamos como _root_ ao fazer um `exec -it` no container),
o que dificulta acessos maliciosas à aplicação. Veja que estamos utilizando a versão 11 do java.
3. no [docker hub](https://hub.docker.com/) criamos um repositório para a imagem docker da aplicação
4. com o repositório criado, voltamos nas configurações do plugin jib e indicamos o `<to>`, ou seja, para onde/qual repo queremos enviar a imagem da nossa aplicação.
5. com o destino da imagem criado, precisamos agora da `tag` da imagem (sua versão). Isso é indicado em `<tags> <tag>`. No nosso caso, utilizaremos a própria versão do maven/aplicação, ou seja, `project.version`.
6. para finalizar a configuração do container, em `<container>` falta adicionarmos qual classe irá iniciar a aplicação. Indicamos isso colocando o pacote completo seguido do nome da classe.
No caso desta aplicação, em `<mainClass>` ficaria `academy.devdojo.springboot2.Springboot2EssentialsApplication`.

Abaixo trechos do `pom.xml` com as alterações feitas (mencionadas acima):

```xml
	<properties>
		<java.version>11</java.version>
		<org.mapstruct.version>1.4.1.Final</org.mapstruct.version>
		<!-- <maven-compiler-plugin.version>3.8.1</maven-compiler-plugin.version> -->
		<springdoc-openapi-ui.version>1.5.2</springdoc-openapi-ui.version>
		<jib-maven-plugin.version>2.7.1</jib-maven-plugin.version>
		<docker.distroless.image>gcr.io/distroless/java:11</docker.distroless.image>
		<docker.account>psatler</docker.account>
		<docker.repo.project>springboot2-essentials</docker.repo.project>
		<docker.image.name>${docker.account}/${docker.repo.project}</docker.image.name>
	</properties>
	
	...
	...


	<build>
		<plugins>

				<plugin>
						<groupId>com.google.cloud.tools</groupId>
						<artifactId>jib-maven-plugin</artifactId>
						<version>${jib-maven-plugin.version}</version>
						<configuration>
							<from>
								<image>${docker.distroless.image}</image>
							</from>
							<to>
								<image>${docker.image.name}</image>
								<tags>
									<tag>${project.version}</tag>
								</tags>
							</to>
							<container>
								<mainClass>academy.devdojo.springboot2.Springboot2EssentialsApplication</mainClass>
							</container>
						</configuration>
				</plugin>


	...
	...


		</plugins>
	</build>
```

Agora, necessitamos de montar a nossa imagem antes de criá-la no `docker-compose.yml`. Para isso, podemos executar o seguinte comando:

```
mvn jib:dockerBuild
```

quando executamos tal comando, estamos montando a imagem de forma local, ou seja, ela não é enviada para o _docker hub_.

Podemos visualizar a imagem criada através do comando `docker image ls`. Veremos que a imagem `psatler/springboot2-essentials` é listada.

Com a imagem local criada, podemos iniciar a alteração do `docker-compose.yml`:

- iniciamos fazendo um _downgrade_ da versao `3.2` para `2.4`. 
- ajustamos a forma de usar o `command`
- podemos também ajustar o limite de memória utilizada por cada imagem/serviço docker. Para visualizar as estatísticas do container, executamos `docker stats`. 
Para limitar a quantidade de memória, usado o `mem_limit:`. 
É possível limitar CPU, limitar memória, etc.
- agora, adicionamos mais um container ao `docker-compose.yml`, que será da nossa aplicação.
- como a aplicação está dentro de um container, o _localhost_ do container da aplicação não sabe da existência
do container com o banco de dados (`mysql`). Assim, deve-se indicar que a _spring datasource url_ está em um local diferente.
	- isso será feito com variáveis de ambiente e utilizaremos o conceito de _extensions_ do docker.
	- a extension foi nomeada como `x-database-variables:`, onde usou-se a variável de referência como `&database-variable`.
	- na url de conexão com o banco de dados, utiliza-se o nome do container ao invés de _localhost_ (ao invés de como é encontrado no `src/main/resources/application.yml`)
	

Abaixo, o `docker-compose.yml` final:

```yml
version: '2.4'

x-database-variables: &database-variables
  SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/anime?useSSL=false&createDatabaseIfNotExist=true&allowPublicKeyRetrieval=true
  SPRING_DATASOURCE_USERNAME: root # ${USERNAME}
  SPRING_DATASOURCE_PASSWORD: root # ${PASSWORD}

services:
  db:
    image: mysql
    container_name: mysql
    environment: 
      MYSQL_ROOT_PASSWORD: root
    
    ports:
      - 3306:3306
    volumes:
      - devdojo_data:/var/lib/mysql
    mem_limit: 512m

  prometheus:
    image: prom/prometheus:latest
    container_name: prometheus
    volumes:
      - "./src/main/resources/prometheus.yml:/etc/prometheus/prometheus.yml" # mapping the prometheus config created to the container
    command: "--config.file=/etc/prometheus/prometheus.yml" # when the container is created, run this command setting up the config file mapped above
    ports:
      - 9090:9090
    mem_limit: 128m

  grafana:
    image: grafana/grafana:latest
    container_name: grafana
    ports:
      - 3000:3000
    mem_limit: 128m

  springboot2-essentials:
    image: psatler/springboot2-essentials:0.0.1-SNAPSHOT
    depends_on:
      - db
    ports:
      - 8080:8080
    environment:
      <<: *database-variables # inserting all the env vars defined above
    mem_limit: 512m

volumes:
  devdojo_data:
```


Antes de fazer o upload da nossa imagem para o _container registry_, primeiro vamos remover as imagens locais da
nossa aplicação. Podemos fazer isto utilizado o seguinte comando:

```
docker image rmi -f <image-id>
```

Para enviar para o docker, necessitamos antes de realizar o login no docker hub, realizado com seguinte comando: 

```
docker login
```

Após logar com sucesso, podemos executar o próximo comando para realizar tanto o build quanto o upload da nossa imagem
para o _docker hub_:

```
mvn jib:build
```

Ao fim da execução do comando acima, a imagem estará publicada no Docker Hub.


