package com.frgoss.apikit.core;

/** Thrown when a requested resource does not exist. Mapped to HTTP 404 in the web layer. */
public class ResourceNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private final String resource;
  private final transient Object id;

  public ResourceNotFoundException(String resource, Object id) {
    super(resource + " not found: " + id);
    this.resource = resource;
    this.id = id;
  }

  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  public String resource() {
    return resource;
  }

  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  public Object id() {
    return id;
  }
}
