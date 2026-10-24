package io.github.firasrg.apikit.core.service;

import io.github.firasrg.apikit.core.exception.ResourceNotFoundException;

/**
 * Updates resources.
 *
 * @param <ID> identifier type
 * @param <U> update request type
 * @param <R> response type
 */
@SuppressWarnings("java:S119")
public interface UpdateService<ID, U, R> {

  /**
   * Updates the resource with the given identifier.
   *
   * @param id the identifier
   * @param request the update request payload
   * @return the updated resource
   * @throws ResourceNotFoundException if it does not exist
   */
  R update(ID id, U request) throws ResourceNotFoundException;
}
