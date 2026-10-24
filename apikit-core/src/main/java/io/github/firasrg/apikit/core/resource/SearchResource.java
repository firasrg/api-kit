package io.github.firasrg.apikit.core.resource;

import io.github.firasrg.apikit.core.pagination.PageQuery;
import io.github.firasrg.apikit.core.pagination.PageResult;

/**
 * Contract for searching a resource with typed criteria and pagination.
 *
 * @param <F> criteria type, typically an entity-specific record
 * @param <R> response type of each result item
 */
public interface SearchResource<F, R> {
  /**
   * Returns one page of results matching the criteria.
   *
   * @param criteria filter to apply; implementations must define whether null means "no filter"
   * @param page page index, size, and sort instructions
   * @return the requested page, never null
   */
  PageResult<R> search(F criteria, PageQuery page);
}
