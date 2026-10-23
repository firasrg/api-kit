package io.github.firasrg.apikit.core.service;

import io.github.firasrg.apikit.core.pagination.PageQuery;
import io.github.firasrg.apikit.core.pagination.PageResult;

/**
 * Filtered search over a resource.
 *
 * @param <F> typed criteria
 * @param <R> response type
 */
public interface SearchService<F, R> {

  /**
   * Searches for resources matching the given criteria.
   *
   * @param criteria the criteria
   * @param query the paging query
   * @return the matching resources
   */
  PageResult<R> search(F criteria, PageQuery query);
}
