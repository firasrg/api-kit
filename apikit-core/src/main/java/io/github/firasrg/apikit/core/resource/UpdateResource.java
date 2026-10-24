package io.github.firasrg.apikit.core.resource;

/**
 * Transport-neutral contract for updating a resource by identifier.
 *
 * @param <ID> identifier type
 * @param <U> update-request type
 * @param <R> response type returned for the updated resource
 */
@SuppressWarnings("java:S119")
public interface UpdateResource<ID, U, R> {

  /**
   * Updates the resource with the supplied identifier.
   *
   * @param id identifier of the resource to update
   * @param request update-request payload
   * @return the updated resource
   */
  R update(ID id, U request);
}
