package com.frgoss.apikit.core.service;

/** Convenience composition of the four CRUD capabilities. Search is added separately. */
@SuppressWarnings("java:S119")
public interface CrudService<ID, C, U, R>
    extends ReadService<ID, R>, CreateService<C, R>, UpdateService<ID, U, R>, DeleteService<ID> {}
