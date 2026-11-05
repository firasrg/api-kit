package io.github.firasrg.apikit.spring;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;

/**
 * Test-only JPA entity for repository filtering and sorting. Its UUID identifier avoids assuming
 * that repository identifiers are numeric.
 */
@Entity
@SuppressWarnings("checkstyle:MissingJavadocType")
public class ProductEntity {

  @Id private UUID id = UUID.randomUUID();
  private String name;
  private String category;

  protected ProductEntity() {}

  ProductEntity(String name, String category) {
    this.name = name;
    this.category = category;
  }

  String getName() {
    return name;
  }
}
