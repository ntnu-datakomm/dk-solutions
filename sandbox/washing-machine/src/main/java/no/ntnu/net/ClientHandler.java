package no.ntnu.net;

import java.net.Socket;
import no.ntnu.Command;

/**
 * Handles connection of one TCP client.
 */
public class ClientHandler extends Thread {
  private final Socket socket;

  private boolean clientActive;

  public ClientHandler(Socket socket) {
    this.socket = socket;
  }

  @Override
  public void run() {
    System.out.println("Handling client " + socket.getRemoteSocketAddress() + ":"
        + socket.getPort());
    clientActive = true;
    while (clientActive) {
      String commandString = readNextClientCommand();
      if (commandString != null) {
        Command command = SocketCommandTranslator.clientStringToCommand(commandString);
        Response response;
        if (command != null) {
          response = executeCommand(command);
        } else {
          response = new ErrorResponse("Incorrect command");
        }
        sendResponse(response);
      }
    }
  }

  private void sendResponse(Response response) {
    String responseString = SocketCommandTranslator.serverResponseToString(response);
    // TODO - send the response to the client
  }

  private Response executeCommand(Command command) {
    // TODO
    return null;
  }

  private String readNextClientCommand() {
    // TODO - read a command (string) from the socket
    return null;
  }
}
