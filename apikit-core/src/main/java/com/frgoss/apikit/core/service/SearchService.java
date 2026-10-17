package com.frgoss.apikit.core.service;

import com.frgoss.apikit.core.pagination.PageQuery;
import com.frgoss.apikit.core.pagination.PageResult;

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
