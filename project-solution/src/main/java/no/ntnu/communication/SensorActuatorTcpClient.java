package no.ntnu.communication;

import static no.ntnu.communication.TcpServer.TCP_PORT;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import no.ntnu.greenhouse.SensorActuatorNode;
import no.ntnu.tools.Logger;

/**
 * Tcp client for the sensor/actuator node.
 */
public class SensorActuatorTcpClient {
  private static final String SERVER_HOST = "localhost";

  private final SensorActuatorNode node;
  private Socket socket;
  private PrintWriter socketWriter;

  public SensorActuatorTcpClient(SensorActuatorNode node) {
    this.node = node;
  }

  /**
   * Start the TCP client.
   */
  public void start() {
    if (connectToServer() && sendNodeTypeMessage()) {
      Thread listeningThread = new Thread(this::processIncomingMessages);
      listeningThread.start();
    }
  }

  private void processIncomingMessages() {
    Logger.info("Receiving messages from the server on thread " + Thread.currentThread().getName());
    //    Message message = null;
    //    do {
    //      message = receiveNextServerMessage();
    //      if (message != null) {
    //        handleMessage(message);
    //      }
    //    } while (message != null);
    // TODO
    Logger.info("Stop receiving messages on thread " + Thread.currentThread().getName());
  }

  private boolean sendNodeTypeMessage() {
    return sendToServer(MessageSerializer.toString(new SensorNodeTypeMessage(node)));
  }

  private boolean sendToServer(String messageString) {
    boolean sent = false;
    try {
      socketWriter.println(messageString);
      sent = true;
    } catch (Exception e) {
      Logger.error("Failed to send message to the server: " + e.getMessage());
    }
    return sent;
  }

  private boolean connectToServer() {
    boolean connected = false;
    try {
      socket = new Socket(SERVER_HOST, TCP_PORT);
      socketWriter = new PrintWriter(socket.getOutputStream(), true);
      connected = true;
    } catch (IOException e) {
      Logger.error("Could not open socket to the server: " + e.getMessage());
    }
    return connected;
  }
}
