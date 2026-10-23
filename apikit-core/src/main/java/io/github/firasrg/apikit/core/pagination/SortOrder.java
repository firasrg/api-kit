package io.github.firasrg.apikit.core.pagination;

import java.util.Objects;

/**
 * A single sort instruction.
 *
 * @param property the property to sort by; callers must validate it against a whitelist
 * @param direction the sort direction
 */
public record SortOrder(String property, Direction direction) {

  /** Sort direction. */
  public enum Direction {
    ASC,
    DESC
  }

  public SortOrder {
    Objects.requireNonNull(direction, "direction");
    if (property == null || property.isBlank()) {
      throw new IllegalArgumentException("property must not be null or blank");
    }
  }
}
