package io.github.firasrg.apikit.core.resource;

/**
 * Transport-neutral contract for creating a resource.
 *
 * @param <C> create-request type
 * @param <R> response type returned for the created resource
 */
public interface CreateResource<C, R> {

  /**
   * Creates a resource from the supplied request.
   *
   * @param request create-request payload
   * @return the created resource
   */
  R create(C request);
}
