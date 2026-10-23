package io.github.firasrg.apikit.core.service;

import io.github.firasrg.apikit.core.exceptions.ResourceNotFoundException;

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
