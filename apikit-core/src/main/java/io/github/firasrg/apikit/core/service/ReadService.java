package io.github.firasrg.apikit.core.service;

import io.github.firasrg.apikit.core.exceptions.ResourceNotFoundException;
import io.github.firasrg.apikit.core.pagination.PageQuery;
import io.github.firasrg.apikit.core.pagination.PageResult;

/**
 * Read access to a resource.
 *
 * @param <ID> identifier type
 * @param <R> response type
 */
@SuppressWarnings("java:S119")
public interface ReadService<ID, R> {

  /**
   * Returns the resource with the given identifier.
   *
   * @throws ResourceNotFoundException if it does not exist
   */
  R get(ID id) throws ResourceNotFoundException;

  /** Returns one page of all resources. */
  PageResult<R> list(PageQuery query);
}
