package com.frgoss.apikit.core.pagination;

import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

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
    if (StringUtils.isBlank(property)) {
      throw new IllegalArgumentException("property must not be blank");
    }
  }
}
