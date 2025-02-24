## Lab2_1

### Benefícios Desta Abordagem
- Mocking evita chamadas reais à API (reduz custos e melhora a velocidade dos testes).
- Testa a desserialização do JSON (garante que um objeto Product é criado corretamente).
- Lida corretamente com produtos ausentes (retorna null em vez de causar erro).
- Garante que doHttpGet() é chamado com a URL correta.

### Caso de Teste 1: findProductDetails(3)
🔹 Simula uma resposta JSON válida, como se a API estivesse retornando um produto existente.

### Caso de Teste 2: findProductDetails(300)
🔹 Simula uma resposta vazia, como se a API não encontrasse um produto com esse ID.

## Lab2_2
## O Que Esse Teste Garante?
- Verifica se a conversão do JSON para Product está funcionando corretamente.
- Garante que o método retorna null quando a API responde com um valor inválido.
- Simula a API sem precisar de chamadas HTTP reais.

## Lab2_3

- Implementamos SimpleHttpClient para realizar chamadas HTTP reais.
- Configuramos o plugin Failsafe para executar testes de integração separadamente.
- Criamos ProductFinderServiceIT para testar contra a API real.
- Executamos os testes com comandos Maven e compreendemos as diferenças entre eles.