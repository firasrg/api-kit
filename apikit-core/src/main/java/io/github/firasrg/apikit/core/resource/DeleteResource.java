package io.github.firasrg.apikit.core.resource;

/**
 * Transport-neutral contract for deleting a resource by identifier.
 *
 * @param <ID> identifier type
 */
@SuppressWarnings("java:S119")
public interface DeleteResource<ID> {

  /**
   * Deletes the resource with the supplied identifier.
   *
   * @param id identifier of the resource to delete
   */
  void delete(ID id);
}
