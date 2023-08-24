package no.ntnu.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A TCP server for receiving washing commands over the socket.
 * Accepts client connections.
 * Forwards the commands to the washing machine.
 * Forwards status updates from the machine to the socket.
 */
public class WashingServer {
  private ServerSocket socket;
  private static final int PORT_NUMBER = 1234;

  private boolean isRunning = true;

  /**
   * Start the TCP server.
   *
   * @throws IOException If something goes wrong.
   */
  public void start() throws IOException {
    openSocket();

    while (isRunning) {
      Socket clientSocket = acceptClient();
      if (clientSocket != null) {
        ClientHandler clientHandler = new ClientHandler(clientSocket);
        clientHandler.start();
      }
    }
  }

  private void openSocket() throws IOException {
    try {
      socket = new ServerSocket(PORT_NUMBER);
      System.out.println("Waiting for client connections on TCP port " + PORT_NUMBER);
    } catch (IOException e) {
      System.out.println("Could not open a listening socket: " + e.getMessage());
      throw e;
    }
  }

  private Socket acceptClient() throws IOException {
    try {
      return socket.accept();
    } catch (IOException e) {
      System.out.println("Could not accept a client connection: " + e.getMessage());
      throw e;
    }
  }

}
