package io.github.firasrg.apikit.spring;

import java.util.UUID;

/** Test-only repository fixture for exercising {@link CoreJpaRepository} with a UUID identifier. */
interface ProductRepository extends CoreJpaRepository<ProductEntity, UUID> {}
