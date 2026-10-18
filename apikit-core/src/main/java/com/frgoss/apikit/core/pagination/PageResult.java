package com.frgoss.apikit.core.pagination;

import java.util.List;
import java.util.Objects;
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

    Objects.requireNonNull(content, "content");

    content = List.copyOf(content);

    if (content.size() > size) {
      throw new IllegalArgumentException(
          "content has " + content.size() + " items, which exceeds page size " + size);
    }

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

  /** Returns whether a page exists after this one. */
  public boolean hasNext() {
    return page + 1 < totalPages();
  }

  /** Returns whether a page exists before this one. */
  public boolean hasPrevious() {
    return page > 0;
  }

  /** Returns whether this page contains at least one item. */
  public boolean hasContent() {
    return !content.isEmpty();
  }

  /** Returns the total number of pages. */
  public int totalPages() {
    return Math.toIntExact((totalElements + size - 1) / size);
  }

  /** Converts every item while keeping the paging metadata. */
  public <R> PageResult<R> map(Function<? super T, ? extends R> mapper) {
    return new PageResult<>(content.stream().<R>map(mapper).toList(), page, size, totalElements);
  }
}
