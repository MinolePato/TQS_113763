# Cucumber, Gherkin, BDD e Automacao com Selenium - Guia Pratico

## Objetivos
- Escrever cenários de teste descritivos com a DSL Gherkin.
- Automatizar cenários com o framework **Cucumber** para **Java** e **JUnit**.

## Conceitos 

### O que é o Cucumber?
- Framework que permite escrever **especificações executáveis**.
- Utiliza **exemplos** para especificar comportamentos desejados do software.
- Os testes (cenários) são escritos **antes** do código de produção (approach BDD).
- Os testes são escritos em **Gherkin**, uma linguagem legível por humanos.

### Gherkin DSL
- Linguagem usada nos ficheiros `.feature` para descrever testes.
- Usa palavras-chave como `Given`, `When`, `Then`, `And`, `Scenario`, `Feature`.

### Mapeamento de Steps
- Os passos escritos no `.feature` devem ser ligados ao código Java.
- Usa-se anotações como `@Given`, `@When`, `@Then`.
- As expressões podem ser:
  - **Regex tradicionais**: `@When("^I add (\\d+) and (\\d+)$")`
  - **Cucumber Expressions** (recomendadas): `@When("I add {int} and {int}")`

### Benefícios do BDD
- Facilita a rastreabilidade entre requisitos de negócio e o código.
- Permite que stakeholders não-técnicos compreendam e validem testes.
- Integra-se bem com **User Stories** (como unidade de especificação, aceitação e entrega).

---

## Getting Started 



### a) Criar ficheiro `.feature`
Localização: `src/test/resources/mypackage/calculator_ops.feature`

Exemplo:
```gherkin
Feature: Operacoes da calculadora RPN

  Scenario: Soma de dois numeros
    When I add 3 and 5
    Then the result should be 8
```

### b) Implementar steps Java
- Um ficheiro com anotações `@Cucumber` ou `@Suite`
- Outro com os passos (step definitions)

```java
@When("I add {int} and {int}")
public void i_add_numbers(int a, int b) {
    result = calculator.add(a, b);
}

@Then("the result should be {int}")
public void result_should_be(int expected) {
    assertEquals(expected, result);
}
```

### c) Executar testes
- Corre os testes com `mvn test` .
- Cucumber mostra "missing steps" se ainda não estão feitos.

### d) Adicionar mais cenarios
Exemplos:
```gherkin
  Scenario: Multiplicacao de dois numeros
    When I multiply 4 and 2
    Then the result should be 8

  Scenario: Operacao invalida
    When I divide 5 by 0
    Then an error should occur
```



---

## Cucumber com selenium

### a) Exeplo de feature
```gherkin
Feature: Busca online na biblioteca

  Scenario: Buscar livros por título
    Given the user is on the homepage
    When the user searches for "Clean Code"
    Then the search results should contain "Clean Code"
```

### b) Exemplo de codigo
```java
@Given("the user is on the homepage")
public void open_homepage(WebDriver driver) {
    driver.get("https://fake-library.test");
}

@When("the user searches for {string}")
public void search_for_book(WebDriver driver, String keyword) {
    WebElement searchBox = driver.findElement(By.name("search"));
    searchBox.sendKeys(keyword);
    searchBox.submit();
}

@Then("the search results should contain {string}")
public void verify_result(WebDriver driver, String expectedTitle) {
    List<WebElement> results = driver.findElements(By.className("book-title"));
    assertTrue(results.stream().anyMatch(e -> e.getText().contains(expectedTitle)));
}
```

---

## 📆 Conclusão
- Escreve primeiro os ficheiros `.feature` (Gherkin), antes dos testes em Java.
- Prefere **Cucumber Expressions** às regex tradicionais.
- Usa `DataTable` e `ParameterType` para dados estruturados.
- Integra com Selenium para testes Web reais.

---

