package no.ntnu.communication.message;

/**
 * Allowed error types.
 */
public enum ErrorType {
  UNKNOWN, INVALID;

  @Override
  public String toString() {
    return this == UNKNOWN ? "E_unknown" : "E_invalid";
  }
}
