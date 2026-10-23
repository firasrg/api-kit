package io.github.firasrg.apikit.core.exceptions;

import java.io.Serial;

/** Base class for all exceptions thrown by ApiKit. */
public class ApiKitException extends RuntimeException {

  @Serial private static final long serialVersionUID = 1L;

  protected ApiKitException(String message) {
    super(message);
  }
}
