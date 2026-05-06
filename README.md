# GastroFlow

Sistema de gestão compartilhado para restaurantes — backend desenvolvido como **Tech Challenge Fase 1** da Pós-Tech FIAP (Arquitetura e Desenvolvimento Java).

Nesta fase o foco é a **gestão de usuários**, contemplando dois perfis: **Cliente** (`Customer`) e **Dono de Restaurante** (`RestaurantOwner`).

## Stack

- Java 21 + Spring Boot 4.0.6
- Spring Data JPA / Hibernate
- PostgreSQL 16
- SpringDoc OpenAPI 3 (Swagger UI)
- Docker + Docker Compose
- Maven (via wrapper `mvnw`)

## Funcionalidades

- CRUD de Clientes e Donos de Restaurante
- Endpoint **separado** para troca de senha (`PUT /{id}/password`)
- Endpoint **distinto** para atualização parcial dos demais dados (`PUT /{id}`)
- Busca por nome (`ILIKE`, case-insensitive, paginada)
- Validação de login (login + senha) em `POST /auth/login`
- Unicidade de e-mail, login, CPF e CPF/CNPJ
- Registro automático da data da última alteração (`@UpdateTimestamp`)
- API versionada em `/api/v1`
- Erros padronizados pela **RFC 7807 (ProblemDetail)**

## Como executar

### Pré-requisitos

- Docker Desktop / Docker Engine ≥ 24
- Docker Compose ≥ 2.20
- Portas `8080` e `5432` livres

### Passo a passo

1. **Clonar e entrar no projeto:**

   ```bash
   git clone https://github.com/Pedro-Jaber/GastroFlow.git
   cd gastroflow
   ```

2. **Criar o arquivo `.env`** a partir do exemplo:

   ```bash
   cp .env.example .env
   ```

   Conteúdo recomendado:

   ```dotenv
   DB_URL=jdbc:postgresql://postgres:5432/gastroflow_db
   DB_NAME=gastroflow_db
   DB_USERNAME=gastroflow_user
   DB_PASSWORD=troque_esta_senha
   ```

   > Se for rodar a aplicação fora do container (`./mvnw spring-boot:run`), troque `postgres` por `localhost` no `DB_URL`.

3. **Subir os containers (build + start):**

   ```bash
   docker compose up -d --build
   ```

4. **Verificar se subiu:**

   ```bash
   docker compose logs -f app
   curl http://localhost:8080/actuator/health
   ```

5. **Encerrar:**

   ```bash
   docker compose down       # mantém o volume do banco
   docker compose down -v    # apaga também o volume
   ```

## Acesso

| Recurso        | URL                                             |
|----------------|-------------------------------------------------|
| API base       | `http://localhost:8080/api/v1`                  |
| Swagger UI     | `http://localhost:8080/swagger-ui/index.html#/` |
| OpenAPI JSON   | `http://localhost:8080/v3/api-docs`             |
| Health check   | `http://localhost:8080/actuator/health`         |

## Endpoints principais

| Método | Endpoint                                  | Descrição                          |
|--------|-------------------------------------------|------------------------------------|
| POST   | `/api/v1/auth/login`                      | Validar login                      |
| GET    | `/api/v1/customers`                       | Listar clientes (paginado)         |
| GET    | `/api/v1/customers/{id}`                  | Buscar cliente por ID              |
| GET    | `/api/v1/customers/search/{name}`         | Buscar clientes pelo nome          |
| POST   | `/api/v1/customers`                       | Cadastrar cliente                  |
| PUT    | `/api/v1/customers/{id}`                  | Atualizar cliente                  |
| PUT    | `/api/v1/customers/{id}/password`         | Trocar senha do cliente            |
| DELETE | `/api/v1/customers/{id}`                  | Remover cliente                    |
| GET    | `/api/v1/restaurant-owners`               | Listar donos de restaurante        |
| GET    | `/api/v1/restaurant-owners/{id}`          | Buscar dono por ID                 |
| GET    | `/api/v1/restaurant-owners/search/{name}` | Buscar donos pelo nome             |
| POST   | `/api/v1/restaurant-owners`               | Cadastrar dono de restaurante      |
| PUT    | `/api/v1/restaurant-owners/{id}`          | Atualizar dono de restaurante      |
| PUT    | `/api/v1/restaurant-owners/{id}/password` | Trocar senha do dono de restaurante|
| DELETE | `/api/v1/restaurant-owners/{id}`          | Remover dono de restaurante        |

## Coleção Postman

O arquivo `Gastroflow.postman_collection.json` na raiz do projeto contém os requests para todos os cenários (sucesso e erro). Para usar:

1. **Postman → File → Import** → selecionar o arquivo.
2. Definir a variável `baseURL` como `http://localhost:8080/api/v1`.

## Estrutura do projeto

```
src/main/java/br/com/group14/gastroflow
├── controllers/   # REST controllers + ControllerAdvice de erros
├── services/      # Lógica de negócio (UserBaseService genérico)
├── repositories/  # Spring Data JPA
├── entities/      # Entidades JPA (UserBase abstrato + Customer + RestaurantOwner + Address)
├── dtos/          # DTOs de create / update / response
├── enums/
└── interfaces/
```