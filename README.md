## WM-Logistics

API REST que calcula a menor rota e o custo do frete entre cidades, usando um banco de grafos (Neo4j).

### Tecnologias

- Linguagem: Java 25 (LTS)
- Framework: Spring Boot 4.1 (Spring Framework 7, Spring MVC, Jakarta Validation)
- Banco: Neo4j 2026.10 Community (grafo), acessado via driver oficial (Bolt) com Cypher parametrizado
- Build: Maven 3 (jar executável com Tomcat embutido, sem servidor de aplicação)
- Testes: JUnit 5, REST Assured e Testcontainers (Neo4j real em Docker)
- Execução: Docker + Docker Compose

### Rodando com Docker (recomendado)

```bash
docker compose up --build
```

- API: http://localhost:8080/wm-logistics
- Neo4j Browser: http://localhost:7474 (usuário `neo4j`, senha `wm-logistics-dev`; altere com `NEO4J_PASSWORD`)

```bash
# criar um mapa
curl -X POST http://localhost:8080/wm-logistics/maps -H 'Content-Type: application/json' \
  -d '{"name":"SP","routes":[{"from":"A","to":"B","distance":10},{"from":"B","to":"D","distance":15.5}]}'

# calcular o frete
curl -X POST http://localhost:8080/wm-logistics/freights -H 'Content-Type: application/json' \
  -d '{"map":"SP","from":"A","to":"D","autonomy":10,"price":2.5}'
```

### Rodando sem Docker para a aplicação

Suba apenas o Neo4j e rode a aplicação com Maven:

```bash
docker compose up -d neo4j
SPRING_NEO4J_AUTHENTICATION_PASSWORD=wm-logistics-dev WM_DATABASE_RESET_ENABLED=true \
  mvn -pl wm-web spring-boot:run
```

### Testes

```bash
mvn test
```

Os testes de integração sobem um Neo4j descartável com Testcontainers, então precisam de Docker (sem Docker eles são ignorados).

### Configuração

| Variável | Padrão | Descrição |
|---|---|---|
| `SPRING_NEO4J_URI` | `bolt://localhost:7687` | Endereço do Neo4j |
| `SPRING_NEO4J_AUTHENTICATION_USERNAME` | `neo4j` | Usuário |
| `SPRING_NEO4J_AUTHENTICATION_PASSWORD` | (nenhum) | Senha |
| `WM_DATABASE_RESET_ENABLED` | `false` | Habilita `POST /core/database/reset/neo4j`, que apaga todo o grafo. Use só em dev/teste |

Wiki com a motivação e a estrutura do projeto: https://github.com/6massote/wm-logistics/wiki
