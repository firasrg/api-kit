package io.github.firasrg.apikit.core.exception;

import java.io.Serial;

/** Thrown when a client asks to sort by a property that is not sortable. */
public class InvalidSortException extends ApiKitException {

  @Serial private static final long serialVersionUID = 1L;

  private final String property;

  /**
   * Creates an exception for a sort property that is not supported.
   *
   * @param property the unsupported property
   */
  public InvalidSortException(String property) {
    super("Unsupported sort property: " + property);
    this.property = property;
  }

  /** Returns the unsupported sort property. */
  public String property() {
    return property;
  }
}
