package com.frgoss.apikit.core.service;

/**
 * Creates resources.
 *
 * @param <C> create-request type
 * @param <R> create-response type
 */
public interface CreateService<C, R> {

  /**
   * Creates a new resource.
   *
   * @param request the create-request payload
   * @return the created resource
   */
  R create(C request);
}
