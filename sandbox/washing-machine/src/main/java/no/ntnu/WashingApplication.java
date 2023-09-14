package no.ntnu;

import java.io.IOException;
import no.ntnu.net.WashingServer;

/**
 * The application which binds together all the components.
 */
public class WashingApplication {
  /**
   * Entrypoint of the application.
   *
   * @param args Command-line arguments
   */
  public static void main(String[] args) {
    WashingMachine machine = new WashingMachine();
    WashingServer server = new WashingServer();
    try {
      server.start();
    } catch (IOException e) {
      System.out.println("Could not start the server, aborting the application");
    }
  }
}
