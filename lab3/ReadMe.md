lab3.1

a)
## Identificar alguns exemplos que utilizam a cadeia de métodos expressivos do AssertJ.
- O AssertJ oferece uma API fluente para afirmações. Aqui estão dois exemplos do guião onde a cadeia de métodos expressivos é usada:

No `D_EmployeeRestControllerIT`:
   
   `assertThat(found).extracting(Employee::getName).containsOnly("bob");`
 
b)
## Tome nota das anotações transitivas incluídas em @DataJpaTest.
### A anotação @DataJpaTest é usada para testar repositórios JPA. Ela inclui várias anotações transitivas para conveniência:

- @EnableAutoConfiguration: usa a auto-configuração da aplicação, que é o comportamento padrão no Spring Boot.
- @EntityScan: Realiza a limpeza das entidades JPA.
- @EnableJpaRepositories: Usa os repositórios JPA no teste.
- @Transactional: Garante que cada teste seja executado dentro de uma transação, e por padrão, a transação é revertida após cada teste, para que as alterações não persistam.
Estas anotações são combinadas para configurar um ambiente de testes para a camada JPA, sem precisar de um contexto completo da aplicação.
c) 
## Identificar um exemplo em que se faz mock do comportamento do repositório (e evitar usar uma base de dados).

- No EmployeeService_UnitTest, fazemos mock do comportamento do repositório usando o Mockito.O mock evita a interação com a base de dados, retornando diretamente uma resposta simulada.
d)
## Qual é a diferença entre @Mock e @MockBean?


### @Mock:

- Esta anotação vem do framework Mockito.
- É utilizada para criar um objeto mock de um serviço ou repositório.
- Geralmente é usada em testes unitários onde o bean não faz parte do contexto Spring, então não interage com o ciclo de vida do Spring.
### @MockBean:

- Esta anotação é fornecida pelo Spring Boot (parte dos testes Spring).
- É utilizada para criar um objeto mock que é gerido pelo contexto Spring.
@MockBean substitui o bean no contexto da aplicação pelo mock durante os testes de integração, permitindo que o mock participe do ciclo de vida do Spring.


lab3.2
c)
# Vantagens e Desvantagens do Uso de um Banco de Dados Real em Testes
## Vantagens
- Testes mais realistas → O comportamento é mais similar ao ambiente de produção.
- Detecta problemas com SQL real → Garante que consultas SQL funcionam corretamente na base de dados.
- Testa migrações e schemas → Valida alterações no banco antes do deploy.
## Desvantagens
- Maior tempo de execução → Testes podem ser mais lentos que a usar memória como H2.
- Necessidade de configurar a base de dados → É preciso ter o MySQL a correr durantes os testes.
- Dificuldade na limpeza de dados → Dados podem persistir entre testes, exigindo uma estratégia de reset.
