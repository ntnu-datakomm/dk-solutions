package no.ntnu;

/**
 * An exception that is thrown when the user asks to perform a command which the washing machine
 * is not able to perform in the current state.
 */
public class InvalidStateException extends Exception {
  public InvalidStateException(String message) {
    super(message);
  }
}
