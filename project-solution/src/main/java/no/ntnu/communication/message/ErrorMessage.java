package no.ntnu.communication.message;

/**
 * A message sent to signal that something went wrong.
 */
public class ErrorMessage implements Message {
  private final ErrorType type;
  private final String message;

  public ErrorMessage(ErrorType type, String message) {
    this.type = type;
    this.message = message;
  }

  /**
   * Get the error message.
   *
   * @return A human-readable error message
   */
  public String getMessage() {
    return message;
  }

  /**
   * Get the type of the error.
   *
   * @return The type of the error
   */
  public ErrorType getType() {
    return type;
  }

  @Override
  public String toString() {
    return "ErrorMessage{type=" + type + ", message='" + message + "'}";
  }
}
