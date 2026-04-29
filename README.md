# CEP Service

Esse projeto é uma API REST feita em Java que permite consultar endereços a partir de um CEP. A cada consulta, os dados retornados e o horário da busca são salvos automaticamente no banco de dados, criando um histórico completo de todas as requisições.

A API externa é simulada com WireMock, e o banco de dados roda via Docker junto com o pgAdmin pra facilitar a visualização dos dados.

---

## Tecnologias

- Java 17
- Spring Boot 4.0.6
- PostgreSQL
- WireMock
- Docker e Docker Compose
- Maven
- Swagger para documentação
- JUnit 5 e Mockito para testes
- JaCoCo para cobertura de testes

---

## Como o projeto está organizado

O código segue os princípios SOLID e foi dividido em camadas com responsabilidades bem definidas:

- **Controller** — recebe as requisições HTTP e devolve as respostas
- **Service** — contém a lógica de negócio, coordena a busca e o salvamento do log
- **Client** — responsável por chamar a API externa (WireMock)
- **Repository** — acessa o banco de dados via Spring Data JPA

Cada camada se comunica com a de baixo por meio de interfaces, o que facilita testes e futuras extensões sem precisar mexer no que já está funcionando.

---

## Como rodar

Antes de começar, você vai precisar ter o Java 17, Maven e Docker instalados.

Clone o repositório e entre na pasta do projeto:

```bash
git clone https://github.com/GabrielNunesDev/cep-service.git
cd cep-service
```

Sobe os containers do banco, WireMock e pgAdmin:

```bash
docker compose up -d
```

Inicia a aplicação:

```bash
mvn spring-boot:run
```

Com tudo rodando, acesse a documentação interativa pelo Swagger:

```
http://localhost:8080/swagger-ui.html
```

---

## Endpoints

A API expõe três endpoints:

- `GET /api/v1/cep/{cep}` — consulta o endereço de um CEP e salva o log
- `GET /api/v1/logs` — retorna o histórico de todas as consultas
- `GET /api/v1/logs/{cep}` — retorna o histórico de um CEP específico

---

## CEPs disponíveis para teste

Como a API externa está mockada, os CEPs abaixo estão configurados para retornar dados reais. Qualquer outro CEP retorna 404.

```
01001000 - Praça da Sé, São Paulo/SP
20040020 - Av. Rio Branco, Rio de Janeiro/RJ
30130010 - Av. Afonso Pena, Belo Horizonte/MG
40020010 - Av. Sete de Setembro, Salvador/BA
50010000 - Rua do Sol, Recife/PE
60025000 - Av. Tristão Gonçalves, Fortaleza/CE
70040010 - Esplanada dos Ministérios, Brasília/DF
80010010 - Rua XV de Novembro, Curitiba/PR
88010000 - Rua Felipe Schmidt, Florianópolis/SC
90010150 - Rua dos Andradas, Porto Alegre/RS
69010010 - Av. Eduardo Ribeiro, Manaus/AM
66010000 - Av. Presidente Vargas, Belém/PA
49010000 - Rua João Pessoa, Aracaju/SE
57020000 - Rua do Comércio, Maceió/AL
58010000 - Av. Epitácio Pessoa, João Pessoa/PB
59010000 - Av. Rio Branco, Natal/RN
```

---

## Visualizando o banco de dados

O pgAdmin está disponível em `http://localhost:5050` com o login `admin@admin.com` e senha `admin`.

Para conectar ao banco, use o host `cep-postgres`, porta `5432`, banco `cepservice`, usuário e senha `postgres`.

Se preferir pelo terminal:

```bash
docker exec -it cep-postgres psql -U postgres -d cepservice -c "SELECT * FROM cep_logs;"
```

---

## Testes

Para rodar os testes unitários:

```bash
mvn test
```

O projeto tem 18 testes cobrindo as camadas de Service, Controller e Client.
