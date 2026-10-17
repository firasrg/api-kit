package com.frgoss.apikit.core.pagination;

import java.util.List;
import java.util.Objects;

/**
 * A request for one page of results.
 *
 * @param page zero-based page index
 * @param size number of items per page, at least 1
 * @param sort ordered sort instructions, never null; empty means unsorted
 */
public record PageQuery(int page, int size, List<SortOrder> sort) {

  /** Page size used when the caller does not specify one. */
  public static final int DEFAULT_SIZE = 20;

  public PageQuery {
    if (page < 0) {
      throw new IllegalArgumentException("page must be >= 0, was " + page);
    }
    if (size < 1) {
      throw new IllegalArgumentException("size must be >= 1, was " + size);
    }
    sort = List.copyOf(Objects.requireNonNull(sort, "sort"));
  }

  /** Returns an unsorted query for the given page and size. */
  public static PageQuery of(int page, int size) {
    return new PageQuery(page, size, List.of());
  }

  /** Returns the first page with {@link #DEFAULT_SIZE}, unsorted. */
  public static PageQuery firstPage() {
    return of(0, DEFAULT_SIZE);
  }
}
