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

## AI Assistant Decision Trees

### Should I Modify This Code?

Use this decision tree before making any changes:

```
Is the change requested explicitly by the user?
├─ YES: Proceed with caution
│  └─ Does it require database changes?
│     ├─ YES: Check Neo4j connection first
│     └─ NO: Proceed
└─ NO: Ask user for clarification

Will this change affect multiple layers?
├─ YES: Plan the change across all layers first
└─ NO: Implement in appropriate layer only

Is this a security-sensitive change?
├─ YES: Review security guidelines below
└─ NO: Proceed with implementation
```

### Should I Create a New File or Modify Existing?

```
Does a file already exist for this functionality?
├─ YES: ALWAYS modify the existing file
│  └─ Never create duplicates
└─ NO: Is this a completely new feature?
   ├─ YES: Create new files following naming conventions
   └─ NO: Extend existing functionality
```

### What Layer Should Handle This Logic?

```
Is it HTTP request/response handling?
├─ YES → Controller layer

Is it data validation?
├─ YES → Model layer (annotations)

Is it formatting output?
├─ YES → Presenter layer

Is it orchestrating multiple services?
├─ YES → Facade layer

Is it business logic calculation?
├─ YES → Service layer

Is it database access or Cypher queries?
├─ YES → Repository layer
```

## Security Best Practices ⚠️

### Critical Security Issues to Avoid

#### 1. **Cypher Injection Prevention** (HIGH PRIORITY)

**❌ NEVER DO THIS** (Currently exists in `RouteQueryHelper`):
```java
// VULNERABLE TO INJECTION
public static String buildRouteCityCreate(String map, String name) {
    return String.format("CREATE (Location%s%s:Location {maps:'%s', name:'%s'})",
        trimValue(name), trimValue(map), map, name);
}
```

**✅ CORRECT APPROACH**:
```java
// Use parameterized queries
public static String buildRouteCityCreate() {
    return "CREATE (l:Location {maps: $map, name: $name})";
}

// In repository:
Map<String, Object> params = new HashMap<>();
params.put("map", map);
params.put("name", name);
session.query(query, params);
```

**Why this matters**: String concatenation in Cypher queries allows malicious input like:
```
map = "SP', name:'Hacked'})-[:ADMIN]->(:User {admin:true})//"
```

#### 2. **Input Validation Rules**

Always validate:
- **String lengths**: Prevent buffer overflow and DOS attacks
- **Numeric ranges**: Prevent negative distances, zero autonomy
- **Special characters**: Sanitize before database operations
- **Null values**: Check before operations

```java
// Good validation example
@NotEmpty(message = "error.logistic.map.not_empty")
@Size(min = 1, max = 50, message = "error.logistic.map.invalid_size")
@Pattern(regexp = "^[A-Za-z0-9_-]+$", message = "error.logistic.map.invalid_chars")
private String map;
```

#### 3. **Error Message Security**

**❌ DON'T** expose internal details:
```java
catch (Exception e) {
    return "Database error: " + e.getMessage(); // Exposes DB structure
}
```

**✅ DO** use generic messages:
```java
catch (Exception e) {
    logger.error("Database error", e); // Log internally
    return "An error occurred processing your request"; // Generic to user
}
```

### Security Checklist for AI Assistants

Before committing code, verify:

- [ ] No String concatenation in Cypher queries
- [ ] All user inputs have validation annotations
- [ ] No sensitive data in error messages
- [ ] No hardcoded credentials or API keys
- [ ] No `System.out.println()` with sensitive data
- [ ] Exception details logged, not exposed to users
- [ ] Numeric inputs checked for valid ranges
- [ ] File paths validated (if any file operations)

## Code Quality Standards

### Code Quality Checklist

Before submitting code, ensure:

**Architecture & Design:**
- [ ] Follows layered architecture (no layer skipping)
- [ ] Uses interface + implementation pattern
- [ ] Single Responsibility Principle applied
- [ ] No circular dependencies
- [ ] Proper separation of concerns

**Code Standards:**
- [ ] Meaningful variable and method names
- [ ] Methods are < 30 lines (prefer < 20)
- [ ] Classes are < 300 lines
- [ ] No duplicate code (DRY principle)
- [ ] Constants used instead of magic numbers

**Error Handling:**
- [ ] All exceptions properly caught and handled
- [ ] Facade layer catches service exceptions
- [ ] Meaningful error messages in FacadeContext
- [ ] No swallowed exceptions
- [ ] Proper logging at each layer

**Testing:**
- [ ] Unit tests for services (business logic)
- [ ] Integration tests for endpoints
- [ ] Test both success and failure scenarios
- [ ] Test data setup is clean and repeatable
- [ ] No hardcoded test data that assumes state

**Documentation:**
- [ ] Complex logic has comments explaining "why"
- [ ] Public methods have clear purpose
- [ ] Validation messages are descriptive
- [ ] No commented-out code checked in

### Performance Considerations

#### Neo4j Query Optimization

**❌ Inefficient**:
```cypher
// Scans all nodes
MATCH (n) WHERE n.name = 'A' RETURN n
```

**✅ Efficient**:
```cypher
// Uses label index
MATCH (n:Location {name: 'A'}) RETURN n
```

**Best Practices**:
1. Always specify node labels in MATCH clauses
2. Use indexes for frequently queried properties
3. Limit relationship traversal depth with `*1..5`
4. Use `LIMIT` for queries that might return many results
5. Avoid `OPTIONAL MATCH` when not necessary
6. Return only needed properties, not full nodes

#### Repository Pattern Optimizations

```java
// Use appropriate depth
private static final int DEPTH_LIST = 0;  // For lists, no relationships
private static final int DEPTH_ENTITY = 1; // For single entity with one level
private static final int DEPTH_DEEP = 2;   // For complex object graphs

// Example
public Iterable<T> findAll() {
    return session.loadAll(getEntityType(), DEPTH_LIST); // Fast
}
```

#### Caching Considerations

Current architecture doesn't use caching. If adding cache:
- Cache shortest path results (routes don't change often)
- Use Spring `@Cacheable` annotation
- Set appropriate TTL for cache entries
- Invalidate cache when route maps are updated

## Error Handling Patterns

### Standard Error Flow

```
User Input with Errors
    ↓
Controller validates (@Valid + BindingResult)
    ↓
Returns 400 + validation errors in Presenter

OR

Controller passes to Facade
    ↓
Facade calls Service (wrapped in try-catch)
    ↓
Service throws Exception
    ↓
Facade catches, adds error to FacadeContext
    ↓
Controller checks response.isOk()
    ↓
Returns 400 + error message
```

### Error Handling Example

```java
// In Facade
@Override
public FacadeContext calculate(final Logistic logistic) {
    final FacadeContext context = getFacadeContext();

    try {
        // Business logic
        final Float distance = findShortestPath.baseOn(logistic);

        // Validate business rules
        if (distance == null || distance < 0) {
            context.addError("No route found between cities");
            return context;
        }

        final Freight freight = calculate.baseOn(logistic, distance);
        context.addModel("freight", freight)
               .addMessage("Freight calculated successfully");

    } catch (IllegalArgumentException e) {
        // Expected validation errors
        context.addError(e.getMessage());
    } catch (Exception e) {
        // Unexpected errors - log and return generic message
        logger.error("Error calculating freight", e);
        context.addError("Unable to calculate freight at this time");
    }

    return context;
}
```

### Standard Error Messages

Follow this format for consistency:

```java
// Validation errors (field-level)
"error.{domain}.{field}.{constraint}"
// Example: "error.logistic.map.not_empty"

// Business errors (operation-level)
"error.{domain}.{operation}.{reason}"
// Example: "error.freight.calculate.no_route_found"

// System errors (generic)
"error.system.{component}.{issue}"
// Example: "error.system.database.connection_failed"
```

## Troubleshooting Guide

### Common Issues and Solutions

#### 1. Application Won't Start

**Symptom**: WAR file deploys but application doesn't respond

**Check:**
```bash
# Check Wildfly logs
tail -f $WILDFLY_HOME/standalone/log/server.log

# Check for port conflicts
netstat -an | grep 8080

# Verify Neo4j is running
curl http://localhost:7474
```

**Common Causes:**
- Neo4j not running → Start Neo4j first
- Port 8080 already in use → Change Wildfly port or stop conflicting service
- Spring context initialization error → Check logs for missing dependencies

#### 2. "No route found" Errors

**Symptom**: POST /freights returns "No route found between cities"

**Debug Steps:**
```bash
# 1. Check if map exists
curl -X POST http://localhost:8080/wm-logistics/freights \
  -H "Content-Type: application/json" \
  -d '{"map":"SP","from":"A","to":"D","autonomy":10,"price":2.5}'

# 2. Verify data in Neo4j browser
MATCH (n:Location {maps:'SP'}) RETURN n

# 3. Check relationships
MATCH (from:Location {maps:'SP', name:'A'})-[r:CONNECTED_TO]->(to:Location {maps:'SP', name:'D'})
RETURN from, r, to
```

**Common Causes:**
- Map name mismatch (case-sensitive)
- Cities not created in route map
- No path exists between cities
- Typo in city names

#### 3. Validation Errors Not Showing

**Symptom**: Invalid input accepted or generic errors returned

**Check:**
```java
// 1. Model has validation annotations
@NotEmpty(message = "error.logistic.map.not_empty")
private String map;

// 2. Controller uses @Valid
public ResponseEntity<LayerObject> calculate(
    @Valid @RequestBody Logistic logistic,  // ← @Valid is crucial
    BindingResult bindingResult) {

// 3. BindingResult check happens BEFORE facade call
if (bindingResult.hasErrors()) {
    presenter.addErrors(BindingResultHumanErrorConverter.convert(
        bindingResult.getAllErrors()));
    return new ResponseEntity<>(presenter, HttpStatus.BAD_REQUEST);
}
```

#### 4. Tests Failing

**Symptom**: Tests pass individually but fail when run together

**Solution:**
```java
@Before
public void setUp() {
    // Reset database before each test
    resetDatabase();
}

@After
public void tearDown() {
    // Clean up test data
    resetDatabase();
}
```

**Common Causes:**
- Test data not cleaned up between tests
- Tests dependent on execution order
- Shared state between test classes
- Neo4j session not properly closed

#### 5. Build Failures

**Symptom**: `mvn clean install` fails

**Check:**
```bash
# Java version
java -version  # Should be 1.8.x

# Maven version
mvn -version   # Should be 3.x

# Dependency issues
mvn dependency:tree
mvn dependency:resolve

# Clean and rebuild
mvn clean
rm -rf ~/.m2/repository/br/com/walmart
mvn install
```

### Debugging Techniques

#### 1. Debugging Cypher Queries

```java
// In repository implementation, log queries before execution
String query = RouteQueryHelper.buildCalculateShortestPath(map, from, to);
System.out.println("Executing Cypher: " + query);

Result result = session.query(query, Collections.emptyMap());
System.out.println("Result count: " + result.queryResults().iterator().hasNext());
```

Test queries in Neo4j browser at `http://localhost:7474/browser/`

#### 2. Debugging Spring DI Issues

```java
// Add to any Spring-managed class
@PostConstruct
public void init() {
    System.out.println(">>> " + this.getClass().getSimpleName() + " initialized");
    // Check injected dependencies
    System.out.println(">>> Dependencies: " + (myService != null));
}
```

#### 3. Debugging JSON Serialization

```java
// In controller, before returning response
ObjectMapper mapper = new ObjectMapper();
System.out.println("Response JSON: " + mapper.writeValueAsString(response));
```

#### 4. Remote Debugging

```bash
# Start Wildfly with debug enabled
$WILDFLY_HOME/bin/standalone.sh --debug

# In IDE, create Remote Debug configuration
# Host: localhost
# Port: 8787
```

### Performance Debugging

```java
// Add timing to facades
@Override
public FacadeContext calculate(final Logistic logistic) {
    long startTime = System.currentTimeMillis();
    final FacadeContext context = getFacadeContext();

    try {
        // ... business logic
    } finally {
        long duration = System.currentTimeMillis() - startTime;
        System.out.println(">>> calculate() took " + duration + "ms");
    }

    return context;
}
```

## Testing Best Practices

### Test Naming Conventions

```java
// Pattern: methodName_scenario_expectedBehavior

@Test
public void calculate_validLogistic_returnsFreightAmount() { }

@Test
public void calculate_noRouteExists_returnsError() { }

@Test
public void create_emptyMapName_returnsValidationError() { }

@Test
public void findShortestPath_multipleRoutes_returnsMinimumDistance() { }
```

### Unit Test Template

```java
@RunWith(MockitoJUnitRunner.class)
public class MyServiceImplTest {

    @Mock
    private MyRepository repository;

    @InjectMocks
    private MyServiceImpl service;

    @Before
    public void setUp() {
        // Setup common test data
    }

    @Test
    public void myMethod_happyPath_success() {
        // Arrange
        MyModel input = new MyModel("test");
        when(repository.find(anyString())).thenReturn(new MyEntity());

        // Act
        Result result = service.myMethod(input);

        // Assert
        assertNotNull(result);
        assertEquals("expected", result.getValue());
        verify(repository, times(1)).find(anyString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void myMethod_nullInput_throwsException() {
        // Act
        service.myMethod(null);
    }
}
```

### Integration Test Template

```java
public class MyFeatureIntegrationTest extends AbstractIntegrationTest {

    @Before
    public void setUp() {
        // Reset database
        doResetDatabase();

        // Create test data
        createTestRouteMap();
    }

    @Test
    public void myEndpoint_validRequest_returns200() {
        // Arrange
        MyModel request = new MyModel("test");

        // Act
        Response response = given()
            .contentType("application/json")
            .body(request)
        .when()
            .post("/my-endpoint")
        .then()
            .extract().response();

        // Assert
        assertEquals(200, response.getStatusCode());

        JsonPath json = new JsonPath(response.asString());
        assertTrue(json.getBoolean("ok"));
        assertNotNull(json.get("models[0].result"));
    }

    @Test
    public void myEndpoint_invalidRequest_returns400() {
        // Arrange
        MyModel request = new MyModel(""); // Invalid

        // Act
        Response response = given()
            .contentType("application/json")
            .body(request)
        .when()
            .post("/my-endpoint")
        .then()
            .extract().response();

        // Assert
        assertEquals(400, response.getStatusCode());

        JsonPath json = new JsonPath(response.asString());
        assertFalse(json.getBoolean("ok"));
        assertNotNull(json.get("errors"));
    }
}
```

### Test Data Best Practices

```java
// Create reusable test data builders
public class TestDataBuilder {

    public static Logistic createValidLogistic() {
        return new Logistic("SP", "A", "D", 10.0f, 2.5f);
    }

    public static RouteMap createSimpleRouteMap() {
        Set<RouteCity> routes = new HashSet<>();
        routes.add(new RouteCity("A", "B", 10.0f));
        routes.add(new RouteCity("B", "C", 15.0f));
        return new RouteMap("SP", routes);
    }

    public static RouteMap createComplexRouteMap() {
        // More complex scenarios
    }
}

// Use in tests
@Test
public void test_something() {
    Logistic logistic = TestDataBuilder.createValidLogistic();
    // ... test logic
}
```

## Common Pitfalls and Anti-Patterns

### Anti-Pattern 1: Layer Violation

**❌ DON'T**:
```java
@RestController
public class BadController {
    @Autowired
    private MyRepository repository; // Skipping service/facade layers!

    @PostMapping
    public ResponseEntity<?> create(@RequestBody MyModel model) {
        repository.save(model); // Business logic in controller!
        return ResponseEntity.ok().build();
    }
}
```

**✅ DO**:
```java
@RestController
public class GoodController {
    @Autowired
    private MyFacade facade; // Proper layer delegation

    @PostMapping
    public ResponseEntity<LayerObject> create(@Valid @RequestBody MyModel model,
                                               BindingResult result) {
        if (result.hasErrors()) {
            // Handle validation
        }
        FacadeContext response = facade.create(model);
        return response.isOk() ?
            ResponseEntity.ok(response) :
            ResponseEntity.badRequest().body(response);
    }
}
```

### Anti-Pattern 2: Swallowing Exceptions

**❌ DON'T**:
```java
try {
    // risky operation
} catch (Exception e) {
    // Silent failure - user never knows what happened!
}
```

**✅ DO**:
```java
try {
    // risky operation
} catch (Exception e) {
    logger.error("Operation failed: context details", e);
    context.addError("User-friendly error message");
}
```

### Anti-Pattern 3: God Objects

**❌ DON'T**:
```java
// 1000+ line service that does everything
public class FreightServiceImpl {
    public Freight calculate() { }
    public void validateMap() { }
    public void optimizeRoute() { }
    public void sendNotification() { }
    public void generateReport() { }
    // ... 50 more methods
}
```

**✅ DO**:
```java
// Separate services with single responsibilities
public class FreightCalculationService { }
public class RouteOptimizationService { }
public class NotificationService { }
public class ReportGenerationService { }
```

### Anti-Pattern 4: Magic Numbers

**❌ DON'T**:
```java
if (distance > 1000) { // What is 1000?
    freight = freight * 0.9; // What is 0.9?
}
```

**✅ DO**:
```java
private static final float MAX_DISTANCE_FOR_DISCOUNT = 1000.0f;
private static final float LONG_DISTANCE_DISCOUNT = 0.9f;

if (distance > MAX_DISTANCE_FOR_DISCOUNT) {
    freight = freight * LONG_DISTANCE_DISCOUNT;
}
```

### Anti-Pattern 5: Tight Coupling

**❌ DON'T**:
```java
public class MyService {
    private MyRepositoryImpl repository = new MyRepositoryImpl(); // Tight coupling!
}
```

**✅ DO**:
```java
public class MyService {
    @Autowired
    private MyRepository repository; // Interface dependency
}
```

## Quick Reference

### HTTP Status Codes Used

| Code | When to Use | Example |
|------|-------------|---------|
| 200 OK | Successful operation | Freight calculated successfully |
| 400 Bad Request | Validation errors or business rule violations | Invalid map name, no route found |
| 404 Not Found | Resource doesn't exist | (Not currently used) |
| 500 Internal Server Error | Unexpected server errors | (Should be avoided, caught in facade) |

### Response Format

**Success Response:**
```json
{
  "ok": true,
  "models": [
    { "freight": { "amount": 6.375 } }
  ],
  "messages": ["Freight calc success..."],
  "errors": null
}
```

**Error Response:**
```json
{
  "ok": false,
  "models": null,
  "messages": null,
  "errors": [
    "error.logistic.map.not_empty",
    "error.logistic.from.not_empty"
  ]
}
```

### Key Files Reference

| File | Purpose | When to Modify |
|------|---------|----------------|
| `RouteQueryHelper` | Cypher query builders | Adding new query patterns |
| `GenericRepository` | Base repository CRUD | Rarely (affects all repos) |
| `AbstractFacade` | Base facade functionality | Rarely (affects all facades) |
| `LayerObject` | Response structure | Never (core framework) |
| `FacadeContext` | Facade response wrapper | Never (core framework) |
| `Neo4jSessionFactory` | Database connection | Only for connection changes |

### Validation Annotations Quick Reference

```java
@NotNull        // Cannot be null
@NotEmpty       // Cannot be null or empty (String, Collection)
@NotBlank       // Cannot be null, empty, or whitespace only
@Size(min, max) // Length constraints
@Min(value)     // Minimum numeric value
@Max(value)     // Maximum numeric value
@DecimalMin     // Minimum decimal value
@DecimalMax     // Maximum decimal value
@Pattern(regex) // Must match regex pattern
@Email          // Must be valid email format
@Valid          // Cascade validation to nested objects
```

### Useful Cypher Queries for Debugging

```cypher
// Count all nodes
MATCH (n) RETURN count(n)

// See all route maps
MATCH (n:Location) RETURN DISTINCT n.maps

// See all locations in a map
MATCH (n:Location {maps: 'SP'}) RETURN n

// See all connections
MATCH (from)-[r:CONNECTED_TO]->(to) RETURN from.name, r.distance, to.name

// Find shortest path manually
MATCH (from:Location {maps:'SP', name:'A'}),
      (to:Location {maps:'SP', name:'D'}),
      path = shortestPath((from)-[r:CONNECTED_TO*]->(to))
RETURN path, reduce(distance=0, rel in relationships(path) | distance+rel.distance) AS totalDistance

// Delete everything (CAREFUL!)
MATCH (n) OPTIONAL MATCH (n)-[r]->() DELETE n, r
```

### curl Examples for Testing

```bash
# Create route map
curl -X POST http://localhost:8080/wm-logistics/maps \
  -H "Content-Type: application/json" \
  -d '{
    "name": "SP",
    "routes": [
      {"from": "A", "to": "B", "distance": 10.0},
      {"from": "B", "to": "D", "distance": 15.5}
    ]
  }'

# Calculate freight
curl -X POST http://localhost:8080/wm-logistics/freights \
  -H "Content-Type: application/json" \
  -d '{
    "map": "SP",
    "from": "A",
    "to": "D",
    "autonomy": 10.0,
    "price": 2.5
  }'

# Reset database (testing only)
curl -X POST http://localhost:8080/wm-logistics/database/reset
```

## For AI Assistants: Before You Start Coding

### Pre-Coding Checklist

1. **Understand the Request**:
   - [ ] What layer(s) does this affect?
   - [ ] Is this new functionality or modification?
   - [ ] What are the acceptance criteria?

2. **Read Existing Code**:
   - [ ] Read the files you'll modify
   - [ ] Understand current patterns
   - [ ] Check for similar implementations

3. **Plan the Changes**:
   - [ ] List all files to create/modify
   - [ ] Identify dependencies between changes
   - [ ] Consider backwards compatibility

4. **Security Review**:
   - [ ] Any user input involved?
   - [ ] Any database queries?
   - [ ] Any sensitive data handling?

5. **Testing Strategy**:
   - [ ] What tests need to be written?
   - [ ] What tests need to be updated?
   - [ ] How will you verify it works?

### Post-Coding Checklist

1. **Code Quality**:
   - [ ] Follows existing patterns and conventions
   - [ ] No layer violations
   - [ ] Proper error handling
   - [ ] No security vulnerabilities

2. **Testing**:
   - [ ] Unit tests written and passing
   - [ ] Integration tests written and passing
   - [ ] Manual testing completed

3. **Documentation**:
   - [ ] Complex logic commented
   - [ ] CLAUDE.md updated if patterns changed
   - [ ] Validation messages added

4. **Build & Deploy**:
   - [ ] `mvn clean install` passes
   - [ ] No compilation warnings
   - [ ] Application starts successfully
   - [ ] Endpoints respond as expected

---

**Last Updated**: 2025-12-10
**Version**: 2.0 (Enhanced with AI best practices)
**Maintained by**: AI Assistant (Claude)
