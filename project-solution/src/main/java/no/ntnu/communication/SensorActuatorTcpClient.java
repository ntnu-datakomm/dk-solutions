package no.ntnu.communication;

import static no.ntnu.communication.TcpServer.TCP_PORT;

import java.io.IOException;
import java.net.Socket;
import no.ntnu.greenhouse.SensorActuatorNode;
import no.ntnu.tools.Logger;

/**
 * Tcp client for the sensor/actuator node.
 */
public class SensorActuatorTcpClient {
  private static final String SERVER_HOST = "localhost";

  private final SensorActuatorNode node;
  Socket socket;

  public SensorActuatorTcpClient(SensorActuatorNode node) {
    this.node = node;
  }

  /**
   * Start the TCP client.
   */
  public void start() {
    try {
      socket = new Socket(SERVER_HOST, TCP_PORT);
    } catch (IOException e) {
      Logger.error("Could not open socket to the server: " + e.getMessage());
    }
  }
}
