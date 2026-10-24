# AI Project Instructions

## Mission and Principle

Java library that removes repetitive backend work (CRUD-oriented architecture): service and mapper contracts, pagination/sorting, typed filtering, Spring/Spring Data JPA integration, transport-aware error handling, controller (resource) contracts, optional default implementations, and (low priority) source-generator plugins.

REST via Spring MVC is the priority transport, but the project is not coupled to REST: `core` stays transport-neutral so other bindings (e.g. GraphQL) can be added later as adapters.

Spring is the priority framework, but the project is not coupled to it: `core` stays framework-neutral so adapters for other frameworks (e.g. Quarkus, Struts, Micronaut) can be added later. Each framework gets its own adapter module; `spring` is the first and reference implementation.

> Strong defaults, explicit behavior, type safety, composability, replaceability, minimal framework magic.

Never replace or hide Spring, Spring Data, JPA, Jakarta Validation, Jackson, Maven, or standard Java tooling.

## AI Operating Rules (read first)

- Act as a senior Java library engineer. Challenge decisions that are complex, brittle, unsafe, outdated, or inconsistent with these goals. Do not agree automatically.
- Be economical: minimum context and output for a correct, production-quality result. Never trade correctness, security, maintainability, or test quality for fewer tokens.
- Inspect only the files needed: search symbols before reading files, check the module POM before touching dependencies/plugins, read adjacent tests. No full-repo scans, no build output or generated files. Reuse existing patterns; prefer existing code over speculation.
- Output focused patches, not whole untouched files. Do not restate these requirements or re-summarize past decisions. No long explanations unless asked.
- Keep changes minimal: preserve behavior and public APIs unless change is requested; no unrelated refactors, renames, or reformatting; no speculative abstractions, unused extension points, or casual dependencies. Add/update tests for meaningful behavior.
- Verify narrowest first (module compile → targeted test → module tests → quality checks → full verify). Before finishing a substantial task run `./mvnw clean verify` when practical. Never claim a build/test passed unless it ran successfully; state what was not verified.
- On failures: read the actual error, find the root cause, fix one thing, rerun the smallest check. Never suppress quality rules or disable tests to get green.
- Do not verify Spring Boot 4 / Framework 7 / Spring Data APIs from memory when unsure; check the current docs or sources.
- New architecture or dependency: identify the concrete limitation, check compatibility impact, keep dependency direction, minimize new abstractions. Prefer JDK/Spring solutions; a dependency must be maintained, license-compatible, secure, and justified by its transitive cost, and must not leak into public APIs unless required.
- Scope check for every proposal: does it solve the recurring technical problem this project exists for? Classify as `V1`, `near-term`, `future`, or `experimental`. Prefer a small strong library over a broad unfinished framework.

## Structure and Dependency Direction

```text
root (io.github.firasrg.apikit)
├── apikit-core          (framework-independent contracts and models)
├── apikit-spring        (Spring/JPA/Web adapters, auto-configuration, defaults)
└── apikit-plugins       (planned, LOW priority, not created yet: development-time generators; Maven is only one possible adapter)
config/    checkstyle.xml, checkstyle-suppressions.xml, spotless-xml.prefs
.githooks/ pre-commit, pre-push
.github/   copilot-instructions.md, dependabot.yml, workflows/ci.yml
```

Below, `core`, `spring`, `plugins` mean the modules above. Do not work on `plugins` unless explicitly asked. Current state: `core` has `exception`, `pagination` (`PageQuery`, `PageResult`, `SortOrder`), `resource` (`Create/Read/Update/Delete/SearchResource`: the controller contracts, a crucial part of the API), `service` (capability services + `CrudService`), and `CrudMapper`. `spring` has `SpringPaging` and `SortableProperties`. Check the tree before assuming anything else exists.

- `spring → core` and `plugins → core` only. `core` never depends on `spring` or `plugins`. No cycles. New modules need a proven size/coupling reason.
- `core` must not depend on Spring, Boot, Spring Data, Hibernate, JPA, Servlet, or MVC. It holds service/CRUD capability contracts, mapper contracts, pagination/sorting/criteria models, exceptions, shared value types.
- `spring` may use `JpaRepository`, `JpaSpecificationExecutor`, `Specification`, `Page`, `Pageable`, `ProblemDetail`, auto-configuration, configuration properties.
- `plugins` generates editable source only; it is never a runtime dependency. Generated code must not depend on plugin internals.

## Technology Baseline

- Java 17 minimum; CI on JDK 17, 21, 25. Library modules must stay Java 17 compatible (no newer APIs/features).
- Target Spring Boot 4.x / Spring Framework 7.x (currently managed via the `spring-boot-dependencies` 4.1.0 BOM in the parent POM). Use `jakarta.*`. Do not support Java 8/11, Boot 1–3, Framework 4–6, or `javax.*`.
- Maven only, with Maven Wrapper (`mvnw`, `mvnw.cmd`, `.mvn/`). Parent-level `dependencyManagement`, `pluginManagement`, shared properties and quality config; no duplicated versions; prefer BOMs. The project build stays Maven; no Gradle build unless requested.
- Prefer modern Java (records, switch expressions, sealed types, pattern matching within baseline, immutable values). Readability over clever syntax.

## Design

Priority order: correctness → public API clarity → type safety → maintainability → composability → backward compatibility → testability → framework-native behavior → performance → convenience.

- Composition over inheritance; small interfaces over large generic hierarchies; explicit over hidden convention; compile-time safety over reflection; standard Java/Spring over custom replacements; no abstraction just to remove a few duplicated lines.
- **Capability-oriented APIs**: never assume full CRUD. Small contracts (`ReadService<ID,R>`, `CreateService<C,R>`, `UpdateService<ID,U,R>`, `DeleteService<ID>`, `SearchService<F,R>`) composed into `CrudService<ID,C,U,R>`. Read-only, write-only, domain-action, and partial-CRUD resources must stay natural.
- **Generics** stay readable: avoid signatures like `AbstractService<E,ID,C,U,R,F,M,REP,SPEC,X>`. Conventional names: `E` entity, `ID` identifier, `C` create, `U` update, `R` response, `F` filter; use descriptive names when clearer.
- **Spring integration** must feel native. Do not replace `JpaRepository`, `Specification`, `Page`, `Pageable`, `ProblemDetail`, Jakarta Validation, or Spring transactions. Wrap/adapt only for a stable library contract or real added behavior; neutrality must be practical, not ideological. `core` may define its own pagination model to avoid leaking Spring types; `spring` converts to and from Spring types.
- **Persistence**: Spring Data JPA first, in `spring` only. Reusable repository: `@NoRepositoryBean CoreJpaRepository<E, ID> extends JpaRepository<E, ID>, JpaSpecificationExecutor<E>`. Do not rename Spring Data methods without semantic value. Never assume `Long` IDs.
- **Reflection/magic**: avoid reflection, fragile generic-type inference, hidden classpath scanning, bytecode generation, runtime proxies (unless Spring requires them), implicit global registries. No custom annotations (`@AutoCrud`, `@MagicResource`, …) until repeated real use proves value.

## Models, Mapping, Filtering, Pagination

- Never expose JPA entities through public REST APIs. Use semantic names: `Customer`, `CreateCustomerRequest`, `UpdateCustomerRequest`, `CustomerResponse`, `CustomerCriteria`. Avoid `Dto`/`Data`/`Model`/`Object` suffixes when a role name fits. Prefer records for immutable models; do not force them where mutability is needed.
- **Mapping** stays replaceable. Contract example: `CrudMapper<E,C,U,R>` with `toResponse`, `fromCreate`, `update`. MapStruct may be supported but never mandatory in `core`. Prefer compile-time mapping over reflection-based mapping.
- **Filtering** is first-class and typed: e.g. `record CustomerCriteria(String name, CustomerStatus status, LocalDate createdAfter)`. No `Map<String,Object>` for normal query APIs. Integrate with `Specification<E>`. Entity-specific criteria must remain possible. No giant universal filter model; no custom query language without demonstrated need.
- **Pagination/sorting** must be deterministic and documented: default page, size, invalid-value handling, sort syntax, multi-sort behavior. No unbounded list endpoints on potentially large data. Never pass arbitrary client strings as persistence sort paths; validate or map sortable properties.

## REST (priority transport), Errors, Validation, Transactions

- REST adapters (in `spring`) use standard HTTP semantics: `GET/POST /resources`, `GET/PUT/PATCH/DELETE /resources/{id}`. PUT and PATCH are not interchangeable. Domain actions use explicit endpoints (`POST /orders/{id}/cancel`); never force non-CRUD behavior into CRUD abstractions.
- Errors: RFC 9457 `ProblemDetail` in Spring web. Categories: not found, invalid input, validation failure, conflict, duplicate, invalid state, unsupported operation. Core exceptions must stay transport-neutral (no Spring or HTTP types); each transport adapter maps them (HTTP/`ProblemDetail` in `spring.web` first). Never expose stack traces, SQL, Hibernate internals, class names, credentials, secrets, or internal exception details.
- Jakarta Validation (`@NotNull`, `@NotBlank`, `@Size`, `@Email`, `@Positive`) for structural validation only; business invariants live in application/domain logic.
- Transactions belong to service/application logic, never controllers. Use `@Transactional` deliberately, `readOnly = true` where meaningful, not mechanically on every method.
- `Optional`: good for repository lookup results; do not propagate into every service API; never for entity fields, normal DTO fields, or parameters. Return empty collections, not `null`. Document null semantics.

## Spring Configuration and Concurrency

- Typed `@ConfigurationProperties` (e.g. `library.pagination`) over scattered `@Value`: sensible defaults, validation, documented semantics, stable names; consumers can override beans/config.
- Singleton infrastructure stays stateless; never store request-specific mutable state in singletons; shared caches/state need explicit concurrency design.

## Code Style

- Constructor injection only (`private final` fields); no field injection.
- Lombok is optional and conservative: `@RequiredArgsConstructor`, `@Getter` only. Never `@Data`, `@EqualsAndHashCode`, `@ToString` on JPA entities (proxies, lazy loading, recursion, identity, hidden DB access). Prefer explicit Java when Lombok hides behavior.
- Prefer: clear naming, small cohesive methods, explicit contracts, immutable models, meaningful exceptions, low coupling/high cohesion, pragmatic SOLID.
- Avoid: giant methods, deep nesting, boolean-parameter overload, unexplained constants, wildcard imports, dead or commented-out code, premature utility classes.
- Comments explain why, never what. TODOs must be specific and actionable.
- Base package `io.github.firasrg.apikit`. Packages by responsibility, following existing ones (`core.service`, `core.resource`, `core.pagination`, `core.exception`); add `core.criteria`/mapping packages only when needed. In `spring`, grow into `spring.data`, `spring.web`, `spring.autoconfigure` instead of a flat package; plugin: `plugin.generator`, `plugin.template`. `core.resource` holds the controller contracts: keep them transport-neutral and free of Spring/Servlet/HTTP types; REST bindings (mappings, status codes, headers) live in `spring.web`, and any future transport (e.g. GraphQL) gets its own adapter. No `utils`/`helpers`/`misc`/`common`.
- Logging via SLF4J, conservative: ERROR unexpected failure, WARN degraded/suspicious state, INFO significant lifecycle only, DEBUG/TRACE diagnostics. Never log credentials, secrets, tokens, or authorization values.
- Performance: avoid N+1, unbounded reads, repeated metadata scanning, redundant mapping passes. Measure before complex optimization; no API-clarity sacrifices for micro-optimizations.

## Plugins (low priority) and Generated Source

- Generated code is ordinary, readable, editable Java that compiles with normal library dependencies only, and is owned by the consumer after generation.
- Prefer explicit constructors in generated code; Lombok only if explicitly opted in, so generation adds no extra dependency.
- Safety: never overwrite user files silently. Existing target → `SKIP` by default; `force=true` may override and must clearly list affected files. Deterministic and idempotent output (no timestamps/random content unless required). Never modify unrelated files.
- Input validation: package names, class names, identifiers, filesystem paths, target directories. Prevent path traversal and writes outside project boundaries. No network behavior unless explicitly required.
- V1 scope (small): `crud`, `read-only`, `service`, `repository`, `controller`, `mapper`, `criteria`, `request`, `response`. Not in V1: DSLs, annotation processors, runtime proxies, bytecode generation, project rewriting, interactive wizards, runtime AI generation. Templates are internal implementation details.

## Quality Gate

`./mvnw clean verify` (Windows: `mvnw.cmd clean verify`) is the single authoritative command and must run: compilation, Spotless check, Checkstyle, unit tests, configured integration tests, relevant static/dependency-security checks.

- Current build facts (parent POM): coordinates `io.github.firasrg:apikit:0.1.0-SNAPSHOT`; `maven.compiler.release=17`; compiler runs `-Xlint:all,-processing -Werror`, so any warning fails the build (fix it, do not blanket-suppress); `project.build.outputTimestamp` is fixed for reproducible builds; Spotless check runs in `validate`; Enforcer requires Maven ≥ 3.9 and JDK ≥ 17; JUnit Jupiter and AssertJ are inherited by every module as test dependencies. Failsafe is version-managed but not yet bound: add its executions when the first integration test is introduced.
- Checkstyle runs only through the `checkstyle` profile, auto-active on JDK 25+ (bound to `verify`). On JDK 17/21 `verify` skips it, so CI's JDK 25 job is the authoritative Checkstyle gate.
- The `git-hooks` profile sets `core.hooksPath=.githooks` automatically on non-CI builds; do not add a separate hook installer.
- Quality config lives in `config/`; do not add suppressions to hide new violations.
- **Checkstyle**: Google Java Style baseline with justified adjustments; Java sources; only rules with engineering value; failures fail CI.
- **Spotless**: deterministic formatter, currently configured for Java (google-java-format) and XML (`pom.xml`, `config/**/*.xml`); extend to other formats only when justified; no formatter overlap; violations fail verification; `./mvnw spotless:apply` formats.
- CI: GitHub Actions + Dependabot. Sonar only if useful and economical. No redundant tools.
- **Git hooks** (in `.githooks/`): lightweight, reproducible through the project (no manual setup), Windows-friendly. Pre-commit: Spotless + fast checks. Pre-push: `verify` only if reasonably fast. Hooks are feedback; CI is authoritative.
- **Git hygiene**: `.gitattributes` with LF for text files (`* text=auto`, `*.java/xml/yml/yaml/json/properties/md/sh text eol=lf`), CRLF for `*.bat`/`*.cmd`, binary for images/jars/zips. No line-ending-only diffs. Never commit IDE metadata (`*.iml`, `.idea/` except explicitly shared files), build output, debug/temp files (`mvn-debug.log`, `project-tree.txt`), secrets, credentials, or local config.
- **Compatibility**: JDK/Spring support claims must be CI-verified. Keep the matrix small and representative.

## Testing

- JUnit Jupiter, AssertJ and Mockito where useful, Spring Boot Test for Spring integration, Testcontainers when real infrastructure is justified. Behavior over implementation details; do not mock everything or mock what a real lightweight object handles.
- Names communicate behavior: `get_shouldReturnResponse_whenEntityExists()`, `get_shouldThrowNotFound_whenEntityDoesNotExist()`. Never `test1`/`works`. Given/When/Then structure where useful; no redundant comments.
- Integration tests for repositories, specifications, auto-configuration, web error handling, validation, serialization, pagination, configuration properties.
- Do not rely only on H2 for SQL compatibility; use at least one real database via Testcontainers for substantial JPA behavior; no multi-database matrix without a concrete requirement. Docker is test infrastructure only: never required for normal `core` development, never a runtime requirement, never used to containerize library JARs.

## Public API, Docs, Release

- Treat public APIs (interfaces, methods, classes, annotations, enums, exceptions, generic signatures, configuration properties) as contracts: evaluate backward compatibility before any change; no casual rename/removal. Semantic versioning; pre-1.0 may evolve faster but breaking changes must be intentional.
- **Every public API element must have Javadoc** (types, methods, constructors, fields, enum constants), and **every package must have a `package-info.java`** describing its responsibility. Content must add meaning: generic parameters, nullability, exceptions, extension points, ordering, compatibility; never just repeat names. New or changed public code without both is incomplete. Enforce through Checkstyle (`MissingJavadocType`, `MissingJavadocMethod`, `JavadocPackage`) in `config/checkstyle.xml`.
- README stays concise: purpose, installation/Maven coordinates, compatibility, basic usage, generator usage, common commands, contribution, supported versions. Examples show recommended production patterns.
- Release only from a verified tree: full verify, compatibility checks, quality checks, public API review, generated-source smoke test, docs validation, dependency/security review, changelog. Automate only after the workflow is stable.

## Review Priorities

1. correctness 2. public API design 3. backward compatibility 4. architecture boundaries 5. security 6. persistence/query behavior 7. concurrency 8. validation/error behavior 9. test coverage 10. maintainability 11. performance 12. style. Never spend review effort on cosmetics while higher-impact issues exist.

## Definition of Done

Correct implementation; coherent API; module boundaries and dependency direction respected; Java 17 and supported Spring compatibility preserved; tests added; Spotless/Checkstyle pass; edge cases considered; public API has Javadoc and every package has package-info.java; generator behavior safe; dependencies justified; backward compatibility reviewed; docs/examples updated; `./mvnw clean verify` succeeds.

## Non-Goals Unless Explicitly Requested

Gradle build, Kotlin/Groovy production code, GraphQL implementation (design must not preclude it), reactive/WebFlux, custom ORM/DI/validation/JSON engines, custom query DSL, microservice infrastructure, Kafka, Redis, Elasticsearch/OpenSearch, Kubernetes, Docker images for the library, annotation processors, bytecode or runtime source generation, reflection-heavy frameworks, AI runtime dependencies, code-generation cloud service.

## Final Decision Rule

When uncertain, choose the option that is: simple, explicit, typed, composable, testable, maintainable, backward-compatible, native to Java/Spring, low in dependency cost and operational complexity. Remove repetitive engineering work without building a more complicated framework.