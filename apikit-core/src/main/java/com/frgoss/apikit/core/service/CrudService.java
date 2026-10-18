package com.frgoss.apikit.core.service;

/**
 * CRUD service interface that combines read, create, update and delete operations.
 *
 * @param <ID> identifier type
 * @param <C> create-request type
 * @param <U> update-request type
 * @param <R> response type
 */
@SuppressWarnings("java:S119")
public interface CrudService<ID, C, U, R>
    extends ReadService<ID, R>, CreateService<C, R>, UpdateService<ID, U, R>, DeleteService<ID> {}
