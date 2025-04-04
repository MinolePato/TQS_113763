# Web Automation with Selenium & JUnit 5


## Objetivos

- Automatizar testes de aceitação em aplicações web
- Aplicar boas práticas de localização de elementos
- Utilizar o padrão **Page Object** para maior manutenibilidade
- Explorar gravação interativa de testes
- Executar testes em diferentes navegadores (incluindo via Docker)

---

## Tecnologias Utilizadas

| Tecnologia         | Descrição |
|--------------------|-----------|
| **Selenium WebDriver** | API Java para automação de browser, como se fosse um utilizador real |
| **JUnit 5**        | Framework de testes em Java |
| **Selenium-Jupiter** | Extensão para facilitar a integração do Selenium com JUnit 5 |
| **Selenium IDE / Katalon Recorder** | Plugins para gravação interativa de testes |
| **Docker**         | Para executar testes em browsers sem instalá-los localmente |

---
# Como funciona o Selenium WebDriver e o padrão Page Object

## 🧪 Selenium WebDriver

O **Selenium WebDriver** é uma API que permite **controlar navegadores web programaticamente**, como se um utilizador humano estivesse a interagir com a interface.

O que faz:
- Abre páginas web
- Clica em botões, preenche formulários
- Espera por elementos ou respostas
- Valida resultados (asserts)

Como funciona:
1. **Instancia o WebDriver** do browser desejado (ex: ChromeDriver)
2. Usa comandos para navegar e interagir com a página
3. Usa **locators** para encontrar elementos (por `id`, `cssSelector`, `xpath`, etc.)
4. Executa ações (click, sendKeys, etc.)
5. Faz verificações (`assertEquals`, `assertTrue`, etc.)

Exemplo :
```java
WebDriver driver = new ChromeDriver();
driver.get("https://example.com");
WebElement btn = driver.findElement(By.id("start"));
btn.click();
assertEquals("Página Inicial", driver.getTitle());
driver.quit(); 
```


# Padrão Page Object
O Page Object Pattern é um padrão de design que torna os testes mais organizados, reutilizáveis e fáceis de manter.

### objetivo:
- Cada página da aplicação web é representada por uma classe Java, onde:
- Os elementos da página são variáveis
- As interações (ações do utilizador) são métodos

### Vantagens:
- Se a página mudar, só precisas alterar uma classe
- Facilita a leitura dos testes
- Evita repetição de código

## Exemplo:
java

    public class LoginPage {
    WebDriver driver;

    @FindBy(id = "username")
    WebElement usernameField;

    @FindBy(id = "password")
    WebElement passwordField;

    @FindBy(id = "login")
    WebElement loginButton;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void login(String user, String pass) {
        usernameField.sendKeys(user);
        passwordField.sendKeys(pass);
        loginButton.click();
    }
}

## Lab 4.3: Selecting elements with locators

### b) Utilizacao de locators para selecionar elementos
A utilizacao de xpath é fragil pois a estrutra do dom pode mudar e o xpath deixar de funcionar. É melhor usar um id dos elementos porque assim é sempre o mesmo. 
