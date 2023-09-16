package no.ntnu.run;

import no.ntnu.communication.TcpServer;

/**
 * Entry point for the TCP server.
 */
public class ServerRunner {
  /**
   * Start the TCP server.
   *
   * @param args Command line arguments. Not used.
   */
  public static void main(String[] args) {
    TcpServer server = new TcpServer();
    server.run();
  }
}
