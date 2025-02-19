## Regras para a contrução de testes Unitários

- Os testes devem ser atómicos isto é um teste não deve depender de outro.
- Os testes devem ser básicos com apenas um propósito.
- O nome dos destes deve ser distinto e ser facil de saber o que o teste está a testar.
- Usar asserts para verificar o resultado esperado.
- Utilizar Exception para verificar que lança erros quando é esperado.

## Anotaçoes a utilizar

### @BeforeEach

- Executado antes de cada teste, onde são chamados os contrutores por exemplo.

### @AfterEach 

- Executado depois de cada teste , para limpar estruturas de dados.

### @Test

- Identifica que o metodo vai ser um teste.

### @Disabled

- Identifica um teste que não fai ser utilizado.

### @DisplayName

- Para mudar o nome do teste para não ser o nome do método.

## 1-f

O facto de os testes cobrirem todos os caminhos de um método ou classe não significa que estes estejam 100% certos uma vez que isto depende da qualidade dos testes e se estes verificam todos os possiveis casos em que o metodo ou classe será utulizada.
Caso removermos mais elementos da stack no que nela existe, nesta situação apesar de returnar um erro o estado da stack vai ser alterado por isso se for utilizado no futuro poderá causar problemas.