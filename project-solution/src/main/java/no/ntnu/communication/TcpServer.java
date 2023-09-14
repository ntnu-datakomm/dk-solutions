package no.ntnu.communication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import no.ntnu.tools.Logger;

/**
 * A TCP server, handles multiple clients.
 */
public class TcpServer {
  public static final int TCP_PORT = 1212;
  private ServerSocket serverSocket;
  private boolean isRunning;

  /**
   * Run the TCP server. The method call does not return (i.e., returns only when the server
   * is shutting down)
   */
  public void run() {
    if (openListeningSocket()) {
      isRunning = true;
      while (isRunning) {
        Socket clientSocket = acceptNextClient();
        ClientHandler clientHandler = new ClientHandler(clientSocket, this);
        clientHandler.start();
      }
    }

    Logger.info("Server exiting...");
  }


  /**
   * Open a listening TCP socket.
   *
   * @return True on success, false on error.
   */
  private boolean openListeningSocket() {
    boolean success = false;
    try {
      serverSocket = new ServerSocket(TCP_PORT);
      Logger.info("Server listening on port " + TCP_PORT);
      success = true;
    } catch (IOException e) {
      Logger.error("Could not open a listening socket on port " + TCP_PORT
          + ", reason: " + e.getMessage());
    }
    return success;
  }

  private Socket acceptNextClient() {
    Socket clientSocket = null;
    try {
      clientSocket = serverSocket.accept();
    } catch (IOException e) {
      Logger.error("Could not accept the next client: " + e.getMessage());
    }
    return clientSocket;
  }

  /**
   * Shut down the server.
   */
  public void shutdown() {
    isRunning = false;
  }

  public void broadcastSensorNodeShutdown(int nodeId) {
    // TODO - send a message to all control panel nodes with info that this
    //  sensor/actuator node has disappeared
  }
}