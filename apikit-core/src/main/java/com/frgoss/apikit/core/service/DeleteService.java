package com.frgoss.apikit.core.service;

import com.frgoss.apikit.core.ResourceNotFoundException;

/**
 * Deletes resources.
 *
 * @param <ID> identifier type
 */
public interface DeleteService<ID> {

  /**
   * Deletes the resource with the given identifier.
   *
   * @param id the identifier
   * @throws ResourceNotFoundException if it does not exist
   */
  void delete(ID id) throws ResourceNotFoundException;
}
