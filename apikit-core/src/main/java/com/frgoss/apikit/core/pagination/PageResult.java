package com.frgoss.apikit.core.pagination;

import java.util.List;
import java.util.function.Function;

/**
 * One page of results.
 *
 * @param content the items on this page, never null
 * @param page zero-based page index
 * @param size requested page size
 * @param totalElements total number of items across all pages
 * @param <T> item type
 */
public record PageResult<T>(List<T> content, int page, int size, long totalElements) {

  public PageResult {
    content = List.copyOf(content);
    if (page < 0) {
      throw new IllegalArgumentException("page must be >= 0, was " + page);
    }
    if (size < 1) {
      throw new IllegalArgumentException("size must be >= 1, was " + size);
    }
    if (totalElements < 0) {
      throw new IllegalArgumentException("totalElements must be >= 0, was " + totalElements);
    }
  }

  /** Returns the total number of pages. */
  public int totalPages() {
    return (int) ((totalElements + size - 1) / size);
  }

  /** Converts every item while keeping the paging metadata. */
  public <R> PageResult<R> map(Function<? super T, ? extends R> mapper) {
    return new PageResult<>(content.stream().<R>map(mapper).toList(), page, size, totalElements);
  }
}
