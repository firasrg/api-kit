package com.frgoss.apikit.core.service;

import com.frgoss.apikit.core.ResourceNotFoundException;

/**
 * Updates resources.
 *
 * @param <ID> identifier type
 * @param <U> update request type
 * @param <R> response type
 */
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
