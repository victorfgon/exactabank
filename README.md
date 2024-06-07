# Desafio exactabank:
A aplicação que deverá ser construída é chamada Exactabank, uma plataforma para gerenciar as transações de uma pessoa. Nessa plataforma será possível inserir transações, ver suas transações e a totalização de entradas e saídas. Uma transação pode resultar em aumento ou diminuição de saldo.

## Tecnologias usadas:
- Kotlin
- Spring Boot
- Swagger
- JUnit
- Docker
- Postgres

## Como rodar:
1. Clone esse repository.
2. Rode o comando "./mvnw clean package"(windows). Depois "docker-compose up --build" (com docker daemon inicializado) na raiz do projeto para iniciar o container.
3. Acesse a documentação da API no Swagger: http://localhost:8080/swagger-ui/#/
4. Para testar os endpoints de transação primeiro crie dois usários. Por exemplo:
   {
    "name": "John Doe",
    "email": "john.doe@example.com",
    "cpf": "12345678903",
    "phone": "12345678903",
    "balance": 1000.0
}

## Observações:
Os campos dos Dtos estão sendo validados.

Os services possuem logs e tratamento de erros com Exception Handler.

## Pontos de melhoria:
Separar os tipos de transação para evitar enviar um "AgencyNumber" em uma transação pix por exemplo.

Otimizar a consulta ao banco de dados.

Vincular as chaves pix como uma lista na entidade usuário.
