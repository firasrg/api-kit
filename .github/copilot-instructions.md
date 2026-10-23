# AI Project Instructions

## 1. Purpose

This repository is a Java library for reducing repetitive technical work in backend applications, especially recurring CRUD-oriented architecture.

Primary goals:

- reusable service contracts
- reusable mapping contracts
- pagination and sorting conventions
- typed filtering/criteria
- Spring integration
- Spring Data JPA integration
- REST/API support
- predictable error handling
- optional default implementations
- source-code generation through a Maven plugin

The library must reduce repetitive infrastructure without hiding normal Java/Spring behavior.

Primary principle:

> Strong defaults, explicit behavior, type safety, composability, replaceability, minimal framework magic.

Do not turn the project into a replacement for Spring, Spring Data, JPA, Jakarta Validation, Jackson, Maven, or established Java tooling.

---

## 2. Project Structure

Initial Maven multi-module structure:

```text
root
├── core
├── spring
└── maven-plugin
```

Possible future modules must be justified by actual size or coupling problems.

### `core`

Framework-independent Java contracts and models.

Must not depend on:

- Spring
- Spring Boot
- Spring Data
- Hibernate
- JPA
- Servlet APIs
- Spring MVC

Possible contents:

- service contracts
- CRUD capabilities
- mapper contracts
- pagination models
- sorting models
- criteria abstractions
- common exceptions
- shared value types

### `spring`

Spring-specific implementation and adapters.

May contain:

- Spring MVC integration
- Spring Boot integration
- Spring Data JPA
- `JpaRepository`
- `JpaSpecificationExecutor`
- `Specification`
- `Page`
- `Pageable`
- `ProblemDetail`
- auto-configuration
- configuration properties
- Spring-oriented default implementations

Keep framework-specific types out of `core`.

### `maven-plugin`

Development-time source generator.

It may generate editable application classes such as:

- controllers
- services
- service implementations
- repositories
- mappers
- criteria
- request models
- response models

Generated code belongs to the consumer after generation.

The plugin must not be required at application runtime.

---

## 3. Technology Baseline

### Java

Minimum:

```text
Java 17
```

CI target JDKs:

```text
17
21
25
```

Public library modules must remain Java 17 compatible unless a future major version explicitly changes the baseline.

Do not use APIs or language features unavailable on Java 17 in Java-17-compatible modules.

Prefer modern Java where supported:

- records
- switch expressions
- sealed types where useful
- pattern matching supported by baseline
- immutable value objects
- standard functional APIs

Prefer readability over clever syntax.

### Spring

Target:

```text
Spring Boot 4.x
Spring Framework 7.x
```

Boot 4.x is the forward-looking target.

Do not support:

```text
Java 8
Java 11
Spring Boot 1.x
Spring Boot 2.x
Spring Framework 4.x
Spring Framework 5.x
Spring Framework 6.X
javax.*
```

Use `jakarta.*`.

---

## 4. Build

Use Maven.

Always provide and use Maven Wrapper:

```text
mvnw
mvnw.cmd
.mvn/
```

Primary full verification command:

```bash
./mvnw clean verify
```

Windows equivalent:

```cmd
mvnw.cmd clean verify
```

Use parent-level:

- `dependencyManagement`
- `pluginManagement`
- shared properties
- shared quality configuration

Avoid duplicated dependency/plugin versions across modules.

Prefer BOMs where appropriate.

Do not introduce Gradle unless explicitly requested.

---

## 5. Dependency Direction

Required direction:

```text
core
 ↑
spring

core
 ↑
maven-plugin
```

`core` must never depend on `spring` or `maven-plugin`.

Avoid circular module dependencies.

The Maven plugin may depend on stable public contracts only when required.

Do not couple generated application source to plugin internals.

---

## 6. Design Principles

Apply these in priority order:

1. correctness
2. public API clarity
3. type safety
4. maintainability
5. composability
6. backward compatibility
7. testability
8. framework-native behavior
9. performance
10. convenience

Prefer:

- composition over inheritance
- small interfaces over large generic hierarchies
- explicit behavior over hidden convention
- compile-time safety over reflection
- standard Java/Spring functionality over custom replacements
- simple implementation over speculative abstraction

Do not create abstractions only to remove a few duplicated lines.

---

## 7. Capability-Oriented APIs

Do not assume every resource supports full CRUD.

Prefer small contracts such as:

```java
ReadService<ID, R>
CreateService<C, R>
UpdateService<ID, U, R>
DeleteService<ID>
SearchService<F, R>
```

Compose them where useful:

```java
CrudService<ID, C, U, R>
```

Read-only, write-only, domain-action, and partial-CRUD resources must remain natural to implement.

Do not force consumers into one universal CRUD interface.

---

## 8. Generic Design

Generics are important but must remain understandable.

Avoid excessive signatures such as:

```java
AbstractService<E, ID, C, U, R, F, M, REP, SPEC, X>
```

Prefer smaller abstractions.

Generic names may use:

```text
E  entity
ID identifier
C  create request/command
U  update request/command
R  response/read model
F  filter/criteria
```

Use descriptive names when single-letter parameters reduce readability.

---

## 9. Spring Integration

Spring integration must feel native to Spring developers.

Do not create custom replacements for:

- `JpaRepository`
- `JpaSpecificationExecutor`
- `Specification`
- `Page`
- `Pageable`
- `ProblemDetail`
- Jakarta Validation
- Spring transactions

Wrap or adapt only when doing so provides a stable library contract or meaningful added behavior.

Do not mirror Spring APIs merely to claim framework neutrality.

Framework-neutrality must be practical, not ideological.

---

## 10. Persistence

Spring Data JPA is the initial persistence integration.

A reusable repository may resemble:

```java
@NoRepositoryBean
public interface CoreJpaRepository<E, ID>
        extends JpaRepository<E, ID>,
                JpaSpecificationExecutor<E> {
}
```

Do not rename existing repository methods without adding semantic value.

Do not assume identifier type is `Long`.

Support generic IDs.

JPA behavior belongs in `spring`, not `core`.

---

## 11. Entities and API Models

Never expose JPA entities directly through public REST APIs.

Prefer separate models:

```text
Customer
CreateCustomerRequest
UpdateCustomerRequest
CustomerResponse
CustomerCriteria
```

Avoid vague names such as:

```text
CustomerDto
CustomerData
CustomerObject
CustomerModel
```

when semantic role can be expressed.

Records are preferred for immutable request/response/value models when appropriate.

Do not force record usage where mutability is required.

---

## 12. Mapping

Mapping must remain replaceable.

Core may define contracts such as:

```java
public interface CrudMapper<E, C, U, R> {

    R toResponse(E entity);

    E fromCreate(C request);

    void update(U request, E entity);
}
```

MapStruct may be supported but must not be mandatory in `core`.

Avoid reflection-based automatic mapping as the primary mechanism.

Prefer compile-time mapping.

---

## 13. Filtering and Criteria

Filtering is a first-class project capability.

Prefer typed criteria:

```java
public record CustomerCriteria(
        String name,
        CustomerStatus status,
        LocalDate createdAfter
) {}
```

Avoid generic:

```java
Map<String, Object>
```

for normal query APIs.

Spring JPA filtering should integrate naturally with:

```java
Specification<E>
```

Do not build a custom query language until demonstrated requirements justify it.

Entity-specific criteria must remain possible.

Do not create a giant universal filter model.

---

## 14. Pagination and Sorting

Pagination must be deterministic and documented.

Define:

- default page
- default size
- maximum size
- invalid-value handling
- sort syntax
- multiple-sort behavior

Avoid unbounded list endpoints for potentially large datasets.

Do not blindly expose arbitrary client strings as persistence sort paths.

Validate or map sortable properties where required.

A stable library pagination model may be used when it avoids leaking Spring implementation details into public API contracts.

---

## 15. REST Semantics

Prefer standard HTTP behavior.

Typical resource operations:

```text
GET    /resources
GET    /resources/{id}
POST   /resources
PUT    /resources/{id}
PATCH  /resources/{id}
DELETE /resources/{id}
```

Do not treat PUT and PATCH as interchangeable.

Domain operations may use explicit actions where justified:

```text
POST /orders/{id}/cancel
POST /accounts/{id}/activate
```

Do not force non-CRUD behavior into CRUD abstractions.

---

## 16. Errors

Spring web integrations should prefer RFC 9457-style `ProblemDetail` behavior where appropriate.

Common error categories:

- not found
- invalid input
- validation failure
- conflict
- duplicate resource
- invalid state
- unsupported operation

Do not expose:

- stack traces
- SQL
- Hibernate internals
- class names
- credentials
- secrets
- internal exception details

Core exceptions should not depend on Spring HTTP classes.

Map application/core errors to HTTP errors inside Spring web integration.

---

## 17. Validation

Use Jakarta Validation for structural validation.

Examples:

```text
@NotNull
@NotBlank
@Size
@Email
@Positive
```

Do not confuse structural validation with business rules.

Business invariants belong in application/domain logic.

---

## 18. Transactions

Transaction boundaries belong primarily in service/application logic.

Use `@Transactional` deliberately.

Use `readOnly = true` where meaningful.

Do not place transactions mechanically on every method.

Controllers must not own transaction boundaries.

---

## 19. Optional, Nullability, Collections

Use `Optional` deliberately.

Good use:

```java
Optional<E>
```

for repository lookup semantics.

Do not automatically propagate repository `Optional` into all service APIs.

Do not use `Optional`:

- as entity fields
- as normal DTO fields
- as parameters

Collections should normally return empty collections rather than `null`.

Avoid undocumented null semantics.

---

## 20. Dependency Injection

Use constructor injection.

Prefer:

```java
private final CustomerRepository repository;
```

with constructor injection or Lombok `@RequiredArgsConstructor`.

Do not use field injection.

---

## 21. Lombok

Lombok is optional and must be used conservatively.

Reasonable:

```text
@RequiredArgsConstructor
@Getter
```

Use caution with JPA entities.

Do not blindly use:

```text
@Data
@EqualsAndHashCode
@ToString
```

on entities.

Consider:

- proxies
- lazy relationships
- recursion
- entity identity
- unintended DB access

Prefer explicit Java when Lombok hides important behavior.

---

## 22. Generated Source

Generated code must be ordinary, readable Java.

Prefer:

```java
@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService service;
}
```

Avoid generated classes that depend on obscure runtime magic.

The user must be able to modify generated files manually.

Generation must not require continued ownership by the plugin.

Generated source should compile without special generated-source runtime machinery beyond normal library dependencies.

---

## 23. Generator Safety

Never overwrite user-owned source silently.

If a target exists:

```text
SKIP by default
```

Possible explicit override:

```text
force=true
```

Even with forced overwrite, clearly surface affected files.

Generation must be deterministic.

Do not generate timestamps or random content unless semantically required.

Do not modify unrelated source files.

Prefer idempotent behavior where practical.

---

## 24. Generator Scope

Initial generator capabilities should remain small.

Potential V1 generation:

```text
crud
read-only
service
repository
controller
mapper
criteria
request
response
```

Do not initially implement:

- custom DSLs
- annotation processors
- runtime proxies
- bytecode generation
- arbitrary project rewriting
- complex interactive wizards
- AI-generated source at runtime

Templates used internally by the plugin are implementation details.

---

## 25. Public API Stability

This is a reusable library.

Treat public APIs carefully.

Before changing any public:

- interface
- method
- class
- annotation
- enum
- exception
- generic signature
- configuration property

evaluate backward compatibility.

Do not rename/remove public APIs casually.

Use semantic versioning:

```text
MAJOR.MINOR.PATCH
```

Interpretation:

```text
MAJOR breaking API changes
MINOR backward-compatible features
PATCH backward-compatible fixes
```

Pre-1.0 may evolve faster, but breaking changes must remain intentional.

---

## 26. Dependencies

Before adding any dependency:

1. check JDK
2. check Jakarta APIs
3. check Spring
4. check Spring Boot
5. check Spring Data
6. check existing project dependencies
7. verify maintenance status
8. verify license
9. evaluate security
10. evaluate transitive dependency cost

Prefer widely adopted dependencies.

Avoid overlapping libraries.

Do not add dependencies to save trivial amounts of Java code.

Do not expose internal dependencies through public API unless required.

---

## 27. Code Quality

Mandatory quality baseline:

- Checkstyle
- Spotless
- compiler warnings/checks
- unit tests
- integration tests where relevant
- Maven verification
- dependency/security analysis
- GitHub Actions
- Dependabot

Sonar may be added when useful and economically reasonable.

Do not add redundant quality tools without demonstrated value.

---

## 28. Checkstyle

Use a recognized baseline such as Google Java Style with justified project-specific adjustments.

Check primarily Java source.

Rules must improve consistency or correctness.

Avoid arbitrary rules that create noise without engineering value.

Checkstyle failures must fail CI.

---

## 29. Spotless

Use Spotless as formatter.

Apply where practical to:

- Java
- XML
- YAML/YML
- JSON
- Markdown
- properties
- other supported text formats

Avoid formatter overlap.

Spotless must be deterministic.

Formatting violations must fail verification.

Provide an explicit formatting command, e.g.:

```bash
./mvnw spotless:apply
```

---

## 30. Maven Quality Lifecycle

Quality checks should integrate into normal Maven lifecycle.

Target behavior:

```bash
./mvnw clean verify
```

must execute required:

- compilation
- formatting validation
- Checkstyle
- tests
- integration tests where configured
- relevant static checks

Do not require developers to remember unrelated commands for essential validation.

---

## 31. Git Hooks

Provide lightweight Git hooks where useful.

Hooks are developer feedback, not the authoritative gate.

CI remains authoritative.

Recommended pre-commit behavior:

- Spotless check or formatting
- fast static checks
- targeted unit tests only if sufficiently fast

Recommended pre-push behavior may include:

```bash
./mvnw verify
```

only if runtime remains reasonable.

Do not make every commit execute unnecessarily expensive integration suites.

Provide hooks through a reproducible project mechanism rather than requiring undocumented manual setup.

Hooks must work on Windows where practical.

---

## 32. Git Standards

Include `.gitattributes`.

Use LF for repository text files.

Typical baseline:

```gitattributes
* text=auto

*.java       text eol=lf
*.xml        text eol=lf
*.yml        text eol=lf
*.yaml       text eol=lf
*.json       text eol=lf
*.properties text eol=lf
*.md         text eol=lf
*.sh         text eol=lf

*.bat        text eol=crlf
*.cmd        text eol=crlf

*.png        binary
*.jpg        binary
*.jpeg       binary
*.gif        binary
*.jar        binary
*.zip        binary
```

Never introduce line-ending-only diffs.

Do not commit:

- IDE metadata not explicitly shared
- build outputs
- generated temporary files
- secrets
- credentials
- local environment configuration

---

## 33. Tests

Use:

- JUnit Jupiter
- AssertJ where useful
- Mockito where useful
- Spring Boot Test for Spring integration
- Testcontainers for real infrastructure integration when justified

Prefer behavior tests over implementation-detail tests.

Do not mock everything.

Do not add mocks when real lightweight objects are simpler.

---

## 34. Test Naming

Tests must communicate behavior.

Prefer:

```java
get_shouldReturnResponse_whenEntityExists()
```

```java
get_shouldThrowNotFound_whenEntityDoesNotExist()
```

Avoid:

```text
test1
testMethod
works
```

Use Given/When/Then organization where useful.

Do not add redundant comments when test structure is already clear.

---

## 35. Integration Tests

Use integration tests for:

- repository behavior
- specifications
- Spring auto-configuration
- web error handling
- validation
- serialization
- pagination
- configuration properties

Use Testcontainers when correctness depends on actual database behavior.

Do not require Docker for normal core development.

Docker/Testcontainers are test infrastructure, not runtime requirements for the library.

Do not containerize library JARs as applications.

---

## 36. Database Testing

Do not rely exclusively on H2 to prove compatibility with real SQL databases.

Use H2 only where its behavior is sufficient for the tested concern.

Prefer at least one real database through Testcontainers for substantial JPA behavior.

Do not add multiple database matrices until there is a concrete compatibility requirement.

---

## 37. Performance

Avoid known problems:

- N+1 queries
- unbounded reads
- repeated metadata scanning
- unnecessary reflection
- unnecessary mapping passes
- redundant remote calls
- avoidable allocations in hot infrastructure paths

Measure before complex optimization.

Do not sacrifice public API clarity for speculative micro-optimizations.

---

## 38. Reflection and Runtime Magic

Avoid reflection when normal Java typing solves the problem.

Do not infer generic types through fragile reflection merely to remove explicit configuration.

Avoid:

- runtime proxies unless Spring itself requires them
- hidden classpath scanning without strong reason
- bytecode generation
- implicit global registries

Prefer compile-time safety.

---

## 39. Annotation Design

Custom annotations require clear value.

Do not create annotations merely to make the project appear framework-like.

Avoid early designs such as:

```text
@AutoCrud
@GenerateEverything
@MagicResource
```

Prefer explicit contracts first.

Annotations may be introduced after repeated real use proves their value.

---

## 40. Configuration

Spring configuration should use typed configuration properties.

Prefer:

```java
@ConfigurationProperties("library.pagination")
```

over scattered `@Value`.

Configuration must provide:

- sensible defaults
- validation
- documented semantics
- stable naming

Consumers must be able to override default beans/configuration.

---

## 41. Documentation

Public APIs require useful Javadoc when behavior is not obvious.

Document:

- generic parameters
- nullability
- exceptions
- extension points
- ordering
- compatibility expectations

Do not write Javadoc that simply repeats names.

README must remain concise and practical:

- purpose
- installation
- compatibility
- basic usage
- Maven coordinates
- generator usage
- common commands
- contribution
- supported versions

Examples must demonstrate recommended production patterns.

---

## 42. Packaging

Prefer packages based on coherent responsibility.

Example:

```text
...core.service
...core.mapping
...core.pagination
...core.criteria
...core.error

...spring.data
...spring.web
...spring.autoconfigure

...plugin.generator
...plugin.template
```

Avoid generic dumping grounds:

```text
utils
helpers
misc
common
stuff
```

Place utility behavior near the domain it supports.

---

## 43. Security

Treat all external input as untrusted.

Generator input must validate:

- package names
- class names
- filesystem paths
- identifiers
- target directories

Prevent path traversal.

Never generate outside configured project boundaries unintentionally.

Never log:

- credentials
- secrets
- authorization values
- private tokens

Do not add network behavior to the generator unless explicitly required.

---

## 44. Logging

Use SLF4J where runtime logging is required.

Library logs must be conservative.

Prefer:

```text
ERROR unexpected failure
WARN  degraded/suspicious state
INFO  significant lifecycle event
DEBUG diagnostic details
TRACE very detailed internals
```

Do not log normal internal method execution at INFO.

Avoid noisy logs in consumer applications.

---

## 45. Thread Safety

Spring singleton infrastructure must remain stateless unless state is explicitly designed for concurrency.

Never store request-specific mutable state in singleton beans.

Shared caches/state require explicit concurrency consideration.

---

## 46. Scope Control

Before adding functionality ask:

> Does this solve the recurring technical problem this project exists to solve?

Classify proposals:

```text
V1
near-term
future
experimental
```

Prefer a small strong library over a broad unfinished framework.

Do not build infrastructure for hypothetical future requirements.

---

## 47. AI Operating Mode

Act as a senior Java library engineer.

Do not behave as a passive code generator.

Challenge decisions that are:

- unnecessarily complex
- brittle
- unsafe
- incompatible
- outdated
- expensive to maintain
- inconsistent with project goals

Do not automatically agree with the user or existing code.

When alternatives exist, prefer the simplest design compatible with expected evolution.

---

## 48. AI Context Economy

AI/context usage must be economical.

This is a strict requirement.

Before reading files:

1. identify the minimum files required
2. prefer targeted search over broad repository reads
3. avoid reopening already-known content
4. avoid inspecting unrelated modules
5. avoid reading generated/build output

Before generating code:

1. inspect relevant existing contracts
2. reuse existing project patterns
3. avoid generating untouched surrounding files
4. output patches/targeted changes when possible

Do not restate known project requirements.

Do not repeatedly summarize previous decisions.

Do not generate large explanations unless requested.

Do not consume context to prove obvious facts.

Never reduce correctness, security, maintainability, or test quality merely to save tokens.

Optimization target:

> Minimum context and output required for a correct production-quality result.

---

## 49. AI File Inspection

For focused tasks:

- inspect only relevant files
- search symbols before reading large files
- read parent/config files only when dependency/build context matters
- inspect tests adjacent to changed behavior
- inspect module POM before adding dependencies/plugins

Do not scan the full repository unless task scope genuinely requires it.

If existing implementation answers a design question, prefer it over speculation.

---

## 50. AI Code Changes

When modifying code:

- preserve existing behavior unless change is requested
- preserve public APIs unless breaking change is intentional
- keep changes focused
- do not refactor unrelated code
- do not rename unrelated symbols
- do not reformat unrelated files
- do not add speculative abstractions
- do not create unused extension points
- do not add dependencies casually
- add/update tests for meaningful behavior

Prefer minimal coherent diffs.

---

## 51. AI Build Behavior

After relevant code changes, run the narrowest useful checks first.

Example progression:

```text
module compilation
targeted test
module tests
quality checks
full verify
```

Do not repeatedly execute full Maven builds when a targeted check is sufficient during iteration.

Before considering a substantial task complete, run:

```bash
./mvnw clean verify
```

when practical.

If full verification cannot run, clearly identify what was not verified.

Do not claim tests/builds passed unless executed successfully.

---

## 52. AI Debugging

When a build/test fails:

1. read the actual failure
2. isolate root cause
3. fix root cause
4. rerun the smallest relevant check
5. run broader verification afterward

Do not make multiple speculative changes simultaneously.

Do not suppress failing quality rules merely to make builds green.

Do not disable tests without explicit justification.

---

## 53. AI Dependency Changes

When proposing a dependency, state internally whether:

- JDK already solves it
- Spring already solves it
- dependency is maintained
- license is acceptable
- public API becomes coupled to it
- transitive cost is justified

Prefer no new dependency when benefits are marginal.

---

## 54. AI Architecture Changes

Before major architecture changes:

- inspect current structure
- identify concrete existing limitation
- evaluate compatibility impact
- avoid speculative future-proofing
- preserve dependency direction
- minimize new abstractions

Do not introduce a pattern because it is fashionable.

Patterns are implementation tools, not project goals.

---

## 55. AI Review Priorities

Review in this order:

1. correctness
2. public API design
3. backward compatibility
4. architecture boundaries
5. security
6. persistence/query behavior
7. concurrency
8. validation/error behavior
9. test coverage
10. maintainability
11. performance
12. style

Do not focus primarily on cosmetic findings while higher-impact issues exist.

---

## 56. Style

Write production-quality Java.

Prefer:

- clear naming
- small cohesive methods
- explicit contracts
- constructor injection
- immutable models
- final variables where useful
- meaningful exceptions
- low coupling
- high cohesion
- pragmatic SOLID

Avoid:

- unnecessary comments
- excessive nesting
- giant methods
- boolean parameter overload
- unexplained constants
- wildcard imports
- dead code
- commented-out code
- premature utility classes
- speculative generic abstractions

---

## 57. Comments

Comments must explain why, not restate what.

Avoid:

```java
// Get customer
var customer = repository.findById(id);
```

Use comments only when intent, constraint, workaround, or non-obvious behavior requires explanation.

TODOs must be actionable and specific.

---

## 58. Compatibility Verification

Supported JDK/Spring claims must be CI-verified.

Do not document compatibility based only on assumptions.

Compatibility matrices should remain intentionally small.

Do not test every possible patch combination unless required.

Prioritize representative supported versions.

---

## 59. Release Discipline

Before release:

- full Maven verify
- compatibility checks
- quality checks
- public API review
- generated-source smoke test
- documentation validation
- dependency/security review
- changelog update

Do not publish from an unverified working tree.

Automate releases only after release workflow is stable and understandable.

---

## 60. Definition of Done

A meaningful feature is complete when applicable:

- implementation is correct
- API is coherent
- module boundary is respected
- Java 17 compatibility is preserved
- supported Spring compatibility is preserved
- tests exist
- formatter/checkstyle pass
- build passes
- edge cases considered
- public API documented
- generator behavior is safe
- dependencies justified
- backward compatibility reviewed
- docs/examples updated
- `./mvnw clean verify` succeeds

---

## 61. Non-Goals Unless Explicitly Requested

Do not introduce by default:

- Gradle
- Kotlin production source
- Groovy production source
- GraphQL
- reactive Spring/WebFlux
- custom ORM
- custom DI
- custom validation engine
- custom JSON engine
- microservices infrastructure
- Kafka
- Redis
- Elasticsearch/OpenSearch
- Kubernetes
- Docker images for the library
- annotation processors
- bytecode generation
- runtime source generation
- reflection-heavy frameworks
- AI runtime dependencies
- custom query DSL
- code-generation cloud service

These require concrete project requirements.

---

## 62. Final Decision Rule

When uncertain, choose the option that best satisfies:

```text
simple
explicit
typed
composable
testable
maintainable
backward-compatible
native to Java/Spring
low dependency cost
low operational complexity
```

The project exists to remove repetitive Java/Spring engineering work.

Do not solve that problem by creating a more complicated framework.