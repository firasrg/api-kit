package io.github.firasrg.apikit.core.exceptions;

import java.io.Serial;

/** Thrown when a client asks to sort by a property that is not sortable. */
public class InvalidSortException extends ApiKitException {

  @Serial private static final long serialVersionUID = 1L;

  private final String property;

  public InvalidSortException(String property) {
    super("Unsupported sort property: " + property);
    this.property = property;
  }

  /** Returns the rejected property name. */
  public String property() {
    return property;
  }
}
