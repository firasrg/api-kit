package io.github.firasrg.apikit.core.resource;

/**
 * Transport-neutral contract for reading a resource by identifier.
 *
 * @param <ID> identifier type
 * @param <R> response type
 */
@SuppressWarnings("java:S119")
public interface ReadResource<ID, R> {

  /**
   * Returns the resource with the supplied identifier.
   *
   * @param id identifier of the resource to read
   * @return the resource
   */
  R get(ID id);
}
