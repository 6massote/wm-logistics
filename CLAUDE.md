# CLAUDE.md - WM Logistics AI Assistant Guide

## Project Overview

**WM-Logistics** is a Java-based logistics freight calculation system that uses graph databases to calculate the shortest and most cost-effective delivery routes. The application determines optimal paths between cities and calculates freight costs based on distance, fuel autonomy, and fuel prices.

### Business Domain
- **Purpose**: Calculate optimal delivery routes and freight costs for logistics operations
- **Core Features**:
  - Route map management (create and store city-to-city routes)
  - Shortest path calculation using graph algorithms
  - Freight cost calculation based on distance, autonomy, and fuel price
  - RESTful API for route and freight operations

## Technology Stack

### Core Technologies
- **Language**: Java 6 (JDK 8)
- **Framework**: Spring Framework 4.2.0 (Spring MVC)
- **Database**: Neo4j 2.0.3 (Graph Database)
  - OGM (Object-Graph Mapping): Neo4j-OGM 1.1.1
  - JDBC: Neo4j-JDBC 2.0.2
- **Application Server**: JBoss Wildfly 9.0.1
- **Build Tool**: Maven 3
- **Web Services**: REST with JSON (Jackson 2.5.3)

### Key Dependencies
- **Validation**: Hibernate Validator 5.1.3
- **Testing**:
  - JUnit 4.10 (unit tests)
  - REST Assured 2.5.0 (integration tests)
- **JSON Processing**: Jackson Databind, Jackson-JSOG (for circular reference handling)

## Architecture

### Layered Architecture Pattern

The application follows a strict layered architecture with clear separation of concerns:

```
┌─────────────────────────────────────┐
│   Controllers (REST Endpoints)      │  @RestController
├─────────────────────────────────────┤
│   Presenters (Response Formatting)  │  View models for responses
├─────────────────────────────────────┤
│   Facades (Business Orchestration)  │  @Service, extends AbstractFacade
├─────────────────────────────────────┤
│   Services (Business Logic)         │  @Service, interface + impl pattern
├─────────────────────────────────────┤
│   Repositories (Data Access)        │  extends GenericRepository<T>
├─────────────────────────────────────┤
│   Entities (Graph Models)           │  @NodeEntity, @RelationshipEntity
├─────────────────────────────────────┤
│   Models (Domain Objects)           │  POJOs with validation
└─────────────────────────────────────┘
```

#### Layer Responsibilities

1. **Controllers** (`br.com.walmart.*.controllers`)
   - Handle HTTP requests/responses
   - Input validation using `@Valid` and `BindingResult`
   - Delegate to Facades
   - Return `ResponseEntity<LayerObject>` with appropriate HTTP status codes

2. **Presenters** (`br.com.walmart.*.presenters`)
   - Format responses for API consumers
   - Handle error message formatting
   - Implement `LayerObject` interface

3. **Facades** (`br.com.walmart.*.facades`)
   - Orchestrate business operations across multiple services
   - Return `FacadeContext` objects
   - Extend `AbstractFacade`
   - Follow interface + implementation pattern

4. **Services** (`br.com.walmart.*.services`)
   - Implement core business logic
   - Single-responsibility focused
   - Interface + implementation pattern (`Service` + `ServiceImpl`)

5. **Repositories** (`br.com.walmart.*.repositories`)
   - Data access and persistence
   - Extend `GenericRepository<T>`
   - Execute Cypher queries for Neo4j
   - Implement `Repository<T>` interface

6. **Entities** (`br.com.walmart.*.entities.graph`)
   - Neo4j graph entities
   - Annotated with `@NodeEntity` or `@RelationshipEntity`
   - Extend `GraphEntity`

7. **Models** (`br.com.walmart.*.models`)
   - Domain objects (DTOs)
   - Include validation annotations (`@NotNull`, `@NotEmpty`, `@DecimalMin`)
   - Implement `Serializable`

## Directory Structure

```
wm-logistics/
├── pom.xml                          # Parent POM
├── README.md                        # Project documentation (Portuguese)
├── CLAUDE.md                        # This file
├── .gitignore                       # Git ignore rules
└── wm-web/                          # Main web module
    ├── pom.xml                      # Module POM with dependencies
    ├── src/
    │   ├── main/
    │   │   ├── java/
    │   │   │   └── br/com/walmart/
    │   │   │       ├── core/                      # Framework/infrastructure code
    │   │   │       │   ├── converters/            # Data converters
    │   │   │       │   ├── entities/              # Base graph entities
    │   │   │       │   ├── facade/                # Base facade classes
    │   │   │       │   ├── factories/             # Neo4j connection factories
    │   │   │       │   ├── layers/                # Layer abstractions
    │   │   │       │   └── repositories/          # Generic repository pattern
    │   │   │       ├── database/                  # Database utilities
    │   │   │       │   ├── controllers/           # Database reset endpoint
    │   │   │       │   ├── facade/
    │   │   │       │   └── repository/
    │   │   │       └── freight/                   # Freight domain
    │   │   │           ├── controllers/           # REST endpoints
    │   │   │           ├── entities/graph/        # Neo4j entities
    │   │   │           ├── facades/               # Business orchestration
    │   │   │           ├── models/                # Domain models/DTOs
    │   │   │           ├── presenters/            # Response formatters
    │   │   │           ├── repositories/          # Data access
    │   │   │           └── services/              # Business logic
    │   │   └── webapp/
    │   │       ├── WEB-INF/
    │   │       │   ├── web.xml                    # Servlet configuration
    │   │       │   └── spring-servlet.xml         # Spring MVC configuration
    │   │       └── index.jsp
    │   └── test/
    │       └── java/
    │           └── br/com/walmart/freight/
    │               ├── itest/rest/                # Integration tests
    │               └── utest/                     # Unit tests
    └── target/                                    # Build output (ignored)
```

## Key Conventions

### Code Style & Patterns

1. **Naming Conventions**:
   - Controllers: `*Controller` (e.g., `FreightController`)
   - Services: `*Service` interface + `*ServiceImpl` implementation
   - Facades: `*Facade` interface + `*FacadeImpl` implementation
   - Repositories: `*Repository` interface + `*RepositoryImpl` implementation
   - Models: Domain name (e.g., `Logistic`, `RouteMap`)
   - Entities: `*GraphEntity` (e.g., `RouteCityGraphEntity`)
   - Presenters: `*Presenter` (e.g., `FreightPresenter`)

2. **Interface + Implementation Pattern**:
   - Always create an interface for services, facades, and repositories
   - Implementation classes append `Impl` suffix
   - Use `@Autowired` to inject interfaces, not implementations

3. **Dependency Injection**:
   - Use Spring's `@Autowired` annotation
   - Field injection is used throughout (not constructor injection)
   - Component scanning configured for `br.com.walmart` package

4. **Validation**:
   - Use Hibernate Validator annotations on model classes
   - Controller methods use `@Valid` annotation with `BindingResult`
   - Custom error messages follow pattern: `error.<domain>.<field>.<constraint>`
   - Convert validation errors using `BindingResultHumanErrorConverter`

5. **Response Pattern**:
   - Controllers return `ResponseEntity<LayerObject>`
   - Use `FacadeContext` for facade responses
   - Check `response.isOk()` to determine success/failure
   - Return HTTP 200 for success, HTTP 400 for validation/business errors

6. **Repository Pattern**:
   - All repositories extend `GenericRepository<T>`
   - Implement `getEntityType()` method to return entity class
   - Use Neo4j `Session` for database operations
   - Depth parameters: `DEPTH_LIST = 0`, `DEPTH_ENTITY = 1`

### Package Organization

- **Domain-driven structure**: Organized by business domain (`freight`, `database`)
- **Layer-based within domain**: Each domain has its own controllers, services, etc.
- **Core package**: Shared infrastructure and framework code
- **Avoid circular dependencies**: Follow strict layer hierarchy (Controller → Facade → Service → Repository)

## REST API Endpoints

### Route Map Management
```http
POST /maps
Content-Type: application/json

{
  "name": "SP",
  "routeCities": [
    { "from": "A", "to": "B", "distance": 10.0 },
    { "from": "B", "to": "D", "distance": 15.5 }
  ]
}
```

### Freight Calculation
```http
POST /freights
Content-Type: application/json

{
  "map": "SP",
  "from": "A",
  "to": "D",
  "autonomy": 10.0,
  "price": 2.5
}
```

### Database Reset (Testing)
```http
POST /database/reset
```

## Development Workflow

### Setting Up Development Environment

1. **Prerequisites**:
   - JDK 8 installed
   - Maven 3 installed
   - Neo4j Community Edition running
   - JBoss Wildfly 9.0.1 application server

2. **Neo4j Configuration**:
   - Default connection handled by `Neo4jConnectionFactory`
   - Connection factory uses singleton pattern
   - Session management via `Neo4jSessionFactory`

3. **Build Project**:
   ```bash
   mvn clean install
   ```

4. **Deploy to Wildfly**:
   ```bash
   mvn wildfly:deploy
   ```

### Making Changes

1. **Adding a New REST Endpoint**:
   - Create/update model in `*.models` package (add validation annotations)
   - Create presenter in `*.presenters` package
   - Create facade interface and implementation in `*.facades`
   - Create service interface and implementation if needed
   - Create/update repository if database access needed
   - Create controller with `@RestController` and `@RequestMapping`

2. **Adding Business Logic**:
   - Create service interface in `*.services`
   - Implement service with `*ServiceImpl` class
   - Add `@Service` annotation to implementation
   - Inject dependencies via `@Autowired`
   - Call service from facade layer

3. **Working with Neo4j**:
   - Create graph entities in `*.entities.graph` package
   - Use `@NodeEntity` for nodes, `@RelationshipEntity` for relationships
   - Extend `GraphEntity` base class
   - Write Cypher queries in repository layer
   - Use `session.query()` for custom queries

## Testing Strategy

### Unit Tests (`utest` package)
- Located in `src/test/java/.../utest/`
- Test individual service methods in isolation
- Use JUnit 4
- Mock dependencies as needed
- Example: `CalculateFreightServiceImplTest`

### Integration Tests (`itest` package)
- Located in `src/test/java/.../itest/rest/`
- Test REST API endpoints end-to-end
- Use REST Assured library
- Extend `AbstractIntegrationTest` base class
- Set up test data in `@Before` methods
- Test happy path: `IntegrationTest`
- Test failure scenarios: `IntegrationTestFail`
- Acceptance tests: `AcceptanceTest`

### Test Data Setup
```java
@Before
public void makeNodesAndRelationships() {
    // Create route map with cities
    final Set<RouteCity> routeCities = new HashSet<>();
    routeCities.add(new RouteCity("A", "B", 10f));
    // ... more routes

    final RouteMap routeMap = new RouteMap("SP", routeCities);
    final Response response = doRouteMapRequest(routeMap);

    assertEquals(200, response.getStatusCode());
}
```

### Running Tests
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=IntegrationTest

# Skip tests during build
mvn clean install -DskipTests
```

## Database: Neo4j Graph Database

### Why Neo4j?
- Efficient shortest path algorithms (Dijkstra's algorithm)
- Natural representation of city-to-city route networks
- Built-in graph traversal capabilities
- Optimal for logistics and routing problems

### Graph Structure
```
(City:RouteCity {map, name})
    -[:DISTANCE_TO {distance}]->
(City:RouteCity {map, name})
```

### Cypher Query Examples

The application uses Cypher queries for graph operations. Key patterns used:

1. **Finding Shortest Path** (in `RouteQueryHelper`):
   - Uses Neo4j's built-in shortest path algorithms
   - Filters by map name to isolate route networks
   - Returns path with total distance

2. **Creating Routes**:
   - MERGE nodes to avoid duplicates
   - CREATE relationships with distance property
   - Use map property to segregate different route networks

### Repository Queries
- Repositories execute Cypher queries via `session.query()`
- Use parameterized queries to prevent injection
- Return results as `Result` objects and map to domain models

## Build Configuration

### Maven Build Lifecycle

1. **Parent POM** (`/pom.xml`):
   - Defines project structure
   - Module aggregation (wm-web)
   - JUnit dependency for all modules

2. **Module POM** (`wm-web/pom.xml`):
   - WAR packaging
   - Spring Framework dependencies
   - Neo4j dependencies and custom repository
   - Wildfly Maven plugin configuration

### Build Commands
```bash
# Clean build
mvn clean install

# Skip tests
mvn clean install -DskipTests

# Deploy to Wildfly
mvn wildfly:deploy

# Undeploy from Wildfly
mvn wildfly:undeploy

# Package only
mvn package
```

### Build Output
- Final artifact: `wm-logistics.war` (in `wm-web/target/`)
- Deployed to Wildfly's deployment directory

## Important Notes for AI Assistants

### When Reading Code
1. **Check the layer**: Understand which layer you're in and what its responsibility is
2. **Follow dependency direction**: Controllers → Facades → Services → Repositories
3. **Never skip layers**: Don't call repositories directly from controllers
4. **Respect interface contracts**: Always use interfaces, not implementations

### When Writing Code
1. **Follow existing patterns**: Use interface + impl pattern for services, facades, repos
2. **Add validation**: Use Hibernate Validator annotations on models
3. **Error handling**: Use `BindingResult` for validation, `FacadeContext` for responses
4. **Spring annotations**:
   - `@RestController` for controllers
   - `@Service` for services and facades
   - `@Repository` for repositories (though not explicitly used here)
   - `@Autowired` for dependency injection

### When Modifying Architecture
1. **Don't break layers**: Maintain strict separation of concerns
2. **Graph operations stay in repositories**: Cypher queries belong in repository layer
3. **Business logic in services**: Keep facades thin, services fat
4. **Keep models clean**: Models are DTOs with validation only, no business logic

### Common Patterns to Follow

#### Creating a New Feature
```java
// 1. Model (domain object)
public class MyModel implements Serializable {
    @NotEmpty(message = "error.mymodel.field.not_empty")
    private String field;
    // getters, setters
}

// 2. Graph Entity (if database needed)
@NodeEntity
public class MyGraphEntity extends GraphEntity {
    // fields
}

// 3. Repository Interface & Implementation
public interface MyRepository extends Repository<MyGraphEntity> {
    MyGraphEntity findByField(String field);
}

public class MyRepositoryImpl extends GenericRepository<MyGraphEntity>
        implements MyRepository {
    @Override
    public Class<MyGraphEntity> getEntityType() {
        return MyGraphEntity.class;
    }
    // custom methods
}

// 4. Service Interface & Implementation
public interface MyService {
    void doSomething(MyModel model);
}

@Service
public class MyServiceImpl implements MyService {
    @Autowired
    private MyRepository repository;

    @Override
    public void doSomething(MyModel model) {
        // business logic
    }
}

// 5. Facade Interface & Implementation
public interface MyFacade {
    FacadeContext process(MyModel model);
}

@Service
public class MyFacadeImpl extends AbstractFacade implements MyFacade {
    @Autowired
    private MyService service;

    @Override
    public FacadeContext process(MyModel model) {
        FacadeContext context = getFacadeContext();
        // orchestration logic
        return context;
    }
}

// 6. Presenter
@Service
public class MyPresenter extends LayerObject {
    // response formatting
}

// 7. Controller
@RestController
@RequestMapping("/my-endpoint")
public class MyController {
    @Autowired
    private MyFacade facade;

    @Autowired
    private MyPresenter presenter;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<LayerObject> process(
            @Valid @RequestBody MyModel model,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            presenter.addErrors(
                BindingResultHumanErrorConverter.convert(
                    bindingResult.getAllErrors()));
            return new ResponseEntity<>(presenter, HttpStatus.BAD_REQUEST);
        }

        FacadeContext response = facade.process(model);

        if (response.isOk()) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
```

## Documentation Links

- **Project Wiki**: https://github.com/6massote/wm-logistics/wiki
  - Motivation: [Link](https://github.com/6massote/wm-logistics/wiki/1.-Motivação)
  - Project Structure: [Link](https://github.com/6massote/wm-logistics/wiki/2.-Estrutura-do-projeto)
  - Environment Setup: [Link](https://github.com/6massote/wm-logistics/wiki/3.-Preparando-ambiente-e-aplicação)
  - Testing: [Link](https://github.com/6massote/wm-logistics/wiki/4.-Testando-a-aplicação)
  - Improvements: [Link](https://github.com/6massote/wm-logistics/wiki/5.-Melhorias)

## Git Workflow

### Branch Strategy
- Work on feature branches with prefix `claude/`
- Current branch: `claude/claude-md-mj00zns4f7ngrj46-01PFZUMp9bkNCfNMs8JsVesz`
- Never push directly to main/master

### Commit Guidelines
- Write clear, descriptive commit messages
- Focus on "why" rather than "what"
- Commit after completing a logical unit of work

### Files to Ignore (per .gitignore)
- `*.class` files
- `*.jar`, `*.war`, `*.ear` archives
- `.mtj.tmp/` directory
- `hs_err_pid*` crash logs
- `target/` directory (Maven build output)

## Common Tasks Reference

### Adding a New Route Map
1. Extend `RouteMap` model if needed
2. Update `RouteMapFacade` and related services
3. Update `RouteMapController` endpoint
4. Add tests in `itest` package

### Adding Freight Calculation Logic
1. Create/update service in `freight.services`
2. Wire service into `FreightFacadeImpl`
3. Update `FreightController` if API changes needed
4. Add unit tests in `utest` package

### Debugging Neo4j Issues
1. Check `Neo4jConnectionFactory` for connection settings
2. Verify Cypher queries in repository implementations
3. Use Neo4j browser to test queries manually
4. Check session management in `Neo4jSessionFactory`

### Running the Application
1. Start Neo4j database
2. Start Wildfly application server
3. Deploy application: `mvn wildfly:deploy`
4. Access at: `http://localhost:8080/wm-logistics/`
5. Test endpoints with tools like Postman or curl

---

**Last Updated**: 2025-12-10
**Version**: 1.0
**Maintained by**: AI Assistant (Claude)
