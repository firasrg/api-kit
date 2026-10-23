package io.github.firasrg.apikit.core;

/**
 * Maps between entities and responses.
 *
 * @param <E> the entity type
 * @param <C> the create-request type
 * @param <U> the update-request type
 * @param <R> the response type
 */
public interface CrudMapper<E, C, U, R> {

  /**
   * Converts an entity to a response.
   *
   * @param entity the entity
   * @return the response
   */
  R toResponse(E entity);

  /**
   * Converts a create-request to an entity.
   *
   * @param request the create-request
   * @return the created entity
   */
  E fromCreate(C request);

  /**
   * Updates an entity from a request.
   *
   * @param request the update-request
   * @param entity the entity to update
   */
  void update(U request, E entity);
}
