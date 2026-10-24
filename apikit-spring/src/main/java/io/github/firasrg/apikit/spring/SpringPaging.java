package io.github.firasrg.apikit.spring;

import io.github.firasrg.apikit.core.pagination.PageQuery;
import io.github.firasrg.apikit.core.pagination.PageResult;
import io.github.firasrg.apikit.core.pagination.SortOrder;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Utilities for converting between Spring Data and ApiKit pagination.
 *
 * <p>All sort properties are resolved through a {@link SortableProperties} whitelist.
 */
public final class SpringPaging {

  private SpringPaging() {}

  /**
   * Converts a query to a {@link Pageable}, resolving every sort property through the whitelist.
   *
   * @param query page index, size, and sort instructions
   * @param sortable allowed client-facing sort properties and their persistence paths
   * @return the corresponding Spring Data page request
   */
  public static Pageable toPageable(PageQuery query, SortableProperties sortable) {
    List<Sort.Order> orders =
        query.sort().stream()
            .map(
                order ->
                    new Sort.Order(
                        toDirection(order.direction()), sortable.resolve(order.property())))
            .toList();
    return PageRequest.of(query.page(), query.size(), Sort.by(orders));
  }

  /**
   * Converts a paged Spring Data result; the page must not be {@code Pageable.unpaged()}.
   *
   * @param page Spring Data page to convert
   * @param <T> result item type
   * @return ApiKit page result containing the page content and metadata
   */
  public static <T> PageResult<T> toPageResult(Page<T> page) {
    return new PageResult<>(
        page.getContent(), page.getNumber(), page.getSize(), page.getTotalElements());
  }

  private static Sort.Direction toDirection(SortOrder.Direction direction) {
    return switch (direction) {
      case ASC -> Sort.Direction.ASC;
      case DESC -> Sort.Direction.DESC;
    };
  }
}
