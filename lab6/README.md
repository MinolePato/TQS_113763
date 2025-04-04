# Integração de Testes com Spring Boot, TestContainers, Flyway e REST Assured

## 6.1 TestContainers em Spring Boot

**TestContainers** permite usar containers Docker efémeros em testes de integração. São úteis, por exemplo, para levantar uma instância de base de dados temporária.

### Como usar:

1. **Pré-requisitos**: Docker instalado e funcional.
2. **Projeto Spring Boot**:
   - Criar projeto com dependências: `Spring Web`, `Spring Data JPA`, `PostgreSQL Driver`, `Spring Boot Test`, `TestContainers (PostgreSQL)`
   


3. **Teste de Integração**:
   - Criar classe com `@SpringBootTest`
   - Usar container PostgreSQL com TestContainers:
     ```java
     @Container
     static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));
     ```
   - Dinamicamente definir propriedades:
     ```java
     @DynamicPropertySource
     static void setProps(DynamicPropertyRegistry registry) {
         registry.add("spring.datasource.url", postgres::getJdbcUrl);
         registry.add("spring.datasource.username", postgres::getUsername);
         registry.add("spring.datasource.password", postgres::getPassword);
         registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
     }
     ```
   - Criar testes: inserir, procurar, atualizar. Usar `@TestMethodOrder` para controlar a ordem.

---

## 6.2 Migrações com Flyway

### Objetivo:
Evitar inserções manuais e usar scripts SQL para preparar o estado da base de dados.

### Passos:
1. Criar script `V001__INIT.sql` em:
   ```
   src/test/resources/db/migration/
   ```
2. Script de exemplo:
   ```sql
   CREATE TABLE student (
     id SERIAL PRIMARY KEY,
     name VARCHAR(100)
   );
   INSERT INTO student(name) VALUES ('Maria'), ('João');
   ```
3. Criar novo teste:
   - `@SpringBootTest`
   - **Sem** `spring.jpa.hibernate.ddl-auto`
   - Flyway cria o schema com base no script

---

## 6.3 REST Assured

**REST Assured** é uma biblioteca Java para testes de APIs REST.

### Exemplo simples:
```java
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

given()
  .param("x", "y")
.when()
  .get("https://jsonplaceholder.typicode.com/todos")
.then()
  .statusCode(200)
  .body("[3].title", equalTo("et porro tempora"));
```

### Verificações:
- Endpoint `/todos` responde com 200
- `/todos/4` tem título "et porro tempora"
- Lista tem id 198 e 199
- Tempo de resposta < 2s

---

## 6.4 REST Assured com Spring MockMvc

Para reduzir o overhead nos testes com Spring, podemos usar `RestAssuredMockMvc` com `MockMvc`.

### Como usar:

1. Dependência:
```xml
<dependency>
  <groupId>io.rest-assured</groupId>
  <artifactId>spring-mock-mvc</artifactId>
  <scope>test</scope>
</dependency>
```

2. Criar classe de teste:
```java
@WebMvcTest(CarController.class)
class CarControllerTest {

  @Autowired MockMvc mockMvc;
  @MockBean CarService carService;

  @Test
  void getAllCars() {
    RestAssuredMockMvc.given().mockMvc(mockMvc)
      .when().get("/api/cars")
      .then().statusCode(200);
  }
}
```

---

## 6.5 Integração Completa: Cars + TestContainers + Flyway + RestAssured

### Objetivo:
Usar todos os conceitos juntos para testar a API REST `Cars` com:
- RestAssured
- TestContainers (PostgreSQL)
- Flyway

### Requisitos:
1. Flyway script de inicialização (`V001__INIT.sql`) para criar a base de dados.
2. Anotar teste com `@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)`
3. Adicionar container PostgreSQL:
```java
@Container
static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");
```
4. Injetar propriedades no contexto:
```java
@DynamicPropertySource
static void props(DynamicPropertyRegistry registry) {
  registry.add("spring.datasource.url", postgres::getJdbcUrl);
  registry.add("spring.datasource.username", postgres::getUsername);
  registry.add("spring.datasource.password", postgres::getPassword);
}
```
5. Usar RestAssured com porta dinâmica:
```java
@Test
void getCarsTest() {
  RestAssured.port = port;
  given()
    .when().get("/api/cars")
    .then().statusCode(200);
}
```

---


