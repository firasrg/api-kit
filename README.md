# apikit

[![CI](https://github.com/firasrg/api-kit/actions/workflows/ci.yml/badge.svg)](https://github.com/firasrg/api-kit/actions/workflows/ci.yml)
![Java 17+](https://img.shields.io/badge/Java-17%2B-blue)
![Spring Boot 4.x](https://img.shields.io/badge/Spring%20Boot-4.x-6db33f)
[![License](https://img.shields.io/badge/license-TODO-lightgrey)](LICENSE)

A Java library that removes repetitive backend CRUD work without hiding Spring, Spring Data or JPA:
service and mapper contracts, pagination and sorting, typed filtering, transport-aware error
handling and controller (resource) contracts.

> Strong defaults, explicit behavior, type safety, composability, replaceability, minimal framework magic.

> **Status: pre-1.0.** The API is evolving and may change between minor versions. Not yet published
> to Maven Central.

## Why

Most CRUD backends re-implement the same layers for every entity. `apikit` provides small, typed,
composable contracts for those layers and optional Spring adapters, while staying out of your way:

- **Capability-oriented**: never assumes full CRUD. Compose only what a resource needs
  (read-only, write-only, partial CRUD, domain actions).
- **Typed filtering**: entity-specific criteria records, no `Map<String, Object>` queries.
- **Safe pagination**: bounded page sizes and validated sort properties, never raw client strings
  as persistence sort paths.
- **Transport-neutral core**: REST via Spring MVC is the priority; other bindings (e.g. GraphQL)
  can be added later as adapters.
- **Native Spring feel**: `JpaRepository`, `Specification`, `Page`, `Pageable`, `ProblemDetail`,
  Jakarta Validation and Spring transactions are used, not replaced.
- **No magic**: no custom annotations, runtime proxies or classpath scanning.

## Modules

| Module | Description |
| --- | --- |
| `apikit-core` | Framework-independent contracts and models: capability services, `CrudService`, `CrudMapper`, resource contracts, pagination (`PageQuery`, `PageResult`, `SortOrder`), exceptions. No Spring dependency. |
| `apikit-spring` | Spring / Spring Data JPA / Web adapters and defaults (e.g. `SpringPaging`, `SortableProperties`). Depends on `apikit-core`. |
| `apikit-plugins` | Planned: development-time source generators. Not available yet. |

## Compatibility

| | Supported |
| --- | --- |
| Java | 17+ (CI on 17, 21, 25) |
| Spring Boot / Framework | 4.x / 7.x |
| Namespace | `jakarta.*` only |
| Build | Maven 3.9+ (wrapper included) |

## Installation

Coordinates (current development version `0.1.0-SNAPSHOT`):

```xml
<dependency>
  <groupId>io.github.firasrg</groupId>
  <artifactId>apikit-spring</artifactId>
  <version>0.1.0-SNAPSHOT</version>
</dependency>
```

`apikit-spring` brings `apikit-core` transitively. Use `apikit-core` alone if you only need the
framework-independent contracts.

Until a release is published, build and install locally:

```bash
./mvnw clean install
```

## Basic usage

Compose only the capabilities a resource needs. Contracts are small and generics stay readable:

```java
// Typed filter, entity-specific and immutable
public record CustomerCriteria(String name, CustomerStatus status, LocalDate createdAfter) {}

// Full CRUD + search: CrudService<ID, C, U, R> composed with SearchService<F, R>
public interface CustomerService
    extends CrudService<UUID, CreateCustomerRequest, UpdateCustomerRequest, CustomerResponse>,
        SearchService<CustomerCriteria, CustomerResponse> {}

// Read-only resource: expose just what you need
public interface CountryService extends ReadService<String, CountryResponse> {}
```

Entities are never exposed through the API. Mapping stays replaceable through
`CrudMapper<E, C, U, R>` (`toResponse`, `fromCreate`, `update`); hand-written or MapStruct
mappers both work.

> The Spring defaults and auto-configuration are still being built out. See the
> [roadmap](#roadmap) for what is available today.

## Design principles

- Composition over inheritance; small interfaces over large generic hierarchies.
- Explicit over hidden convention; compile-time safety over reflection.
- Errors map to RFC 9457 `ProblemDetail` in the Spring web adapter; core exceptions stay
  transport-neutral and never leak stack traces, SQL or internals.
- Transactions belong to services, never controllers.

## Roadmap

- **Available**: core contracts (services, mapper, resources, pagination, exceptions), Spring
  pagination/sorting helpers.
- **Near-term**: Spring Data JPA integration (`CoreJpaRepository`, `Specification` support),
  Spring MVC resource bindings, `ProblemDetail` error handling, auto-configuration.
- **Future**: source generators (`apikit-plugins`) for crud, read-only, service, repository,
  controller, mapper, criteria, request and response.
- **Non-goals**: reactive/WebFlux, custom ORM/DI/validation engines, custom query DSL,
  annotation processors, runtime code generation.

## Development

```bash
./mvnw clean verify          # authoritative build: compile, Spotless, Checkstyle*, tests
./mvnw spotless:apply        # format sources
./mvnw -pl apikit-core test  # narrowest check while iterating
```

\* Checkstyle runs automatically on JDK 25+; CI's JDK 25 job is the authoritative gate.

Windows: use `mvnw.cmd`. Git hooks (`.githooks/`) are enabled automatically on local builds.

## Contributing

Issues and pull requests are welcome. Before opening a PR:

1. Run `./mvnw clean verify`.
2. Add or update tests for meaningful behavior.
3. Document every public API element with Javadoc and keep a `package-info.java` per package.
4. Keep changes focused; avoid unrelated refactors.

See [`.github/copilot-instructions.md`](.github/copilot-instructions.md) for the project's
architecture rules and conventions.

## License

TODO: replace with your license, e.g. Apache License 2.0. See [LICENSE](LICENSE).