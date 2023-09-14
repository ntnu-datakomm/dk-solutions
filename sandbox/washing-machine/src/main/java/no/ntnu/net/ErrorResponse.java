package no.ntnu.net;

/**
 * A response from the server signaling that something went wrong.
 */
public class ErrorResponse implements Response {
  private final String message;

  /**
   * Create an error response.
   *
   * @param message The explanation of what went wrong
   */
  public ErrorResponse(String message) {
    this.message = message;
  }
}
