package io.github.firasrg.apikit.spring;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Base repository for ApiKit resources: Spring Data JPA CRUD plus {@code Specification} queries.
 *
 * <p>Extend it for each entity; the identifier type is never assumed.
 *
 * @param <E> entity type
 * @param <ID> entity identifier type
 */
@NoRepositoryBean
@SuppressWarnings("java:S119")
public interface CoreJpaRepository<E, ID>
    extends JpaRepository<E, ID>, JpaSpecificationExecutor<E> {}
