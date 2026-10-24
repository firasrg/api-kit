package io.github.firasrg.apikit.core.exception;

import java.io.Serial;

/** Thrown when a requested resource does not exist. Mapped to HTTP 404 in the web layer. */
public class ResourceNotFoundException extends RuntimeException {

  @Serial private static final long serialVersionUID = 1L;

  private final String resource;
  private final transient Object id;

  /**
   * Creates an exception describing a resource that could not be found.
   *
   * @param resource name of the missing resource
   * @param id identifier used to look up the resource
   */
  public ResourceNotFoundException(String resource, Object id) {
    super(resource + " not found: " + id);
    this.resource = resource;
    this.id = id;
  }

  /** Returns the name of the missing resource. */
  public String resource() {
    return resource;
  }

  /** Returns the identifier used to look up the resource. */
  public Object id() {
    return id;
  }
}
