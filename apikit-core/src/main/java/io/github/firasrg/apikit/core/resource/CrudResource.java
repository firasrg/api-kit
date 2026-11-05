package io.github.firasrg.apikit.core.resource;

/**
 * CRUD resource interface that combines read, create, update and delete operations.
 *
 * @param <ID> identifier type
 * @param <C> create-request type
 * @param <U> update-request type
 * @param <R> response type
 */
@SuppressWarnings("java:S119")
public interface CrudResource<ID, C, U, R>
    extends ReadResource<ID, R>,
        CreateResource<C, R>,
        UpdateResource<ID, U, R>,
        DeleteResource<ID> {}
