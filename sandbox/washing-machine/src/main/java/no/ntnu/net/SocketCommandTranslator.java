package no.ntnu.net;

import no.ntnu.Command;

/**
 * Translates strings received over the socket to washing machine commands, and vice versa.
 */
public class SocketCommandTranslator {
  /**
   * Translate a command-string from the TCP socket to a washing machine command.
   *
   * @param commandString A command string
   * @return The right command, or null if the string is not a valid command
   */
  public static Command clientStringToCommand(String commandString) {
    // TODO
    return null;
  }

  /**
   * Translate a server's response to a string that can be sent to the TCP client socket.
   *
   * @param response The server's response
   * @return The corresponding client response string
   */
  public static String serverResponseToString(Response response) {
    // TODO
    return null;
  }
}
