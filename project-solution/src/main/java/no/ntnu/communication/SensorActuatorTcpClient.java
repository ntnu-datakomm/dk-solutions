package no.ntnu.communication;

import static no.ntnu.communication.TcpServer.TCP_PORT;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import no.ntnu.communication.message.ControlCommandMessage;
import no.ntnu.communication.message.Message;
import no.ntnu.communication.message.MessageSerializer;
import no.ntnu.communication.message.SensorDataMessage;
import no.ntnu.communication.message.SensorNodeTypeMessage;
import no.ntnu.greenhouse.Sensor;
import no.ntnu.greenhouse.SensorActuatorNode;
import no.ntnu.greenhouse.SensorReading;
import no.ntnu.listeners.greenhouse.SensorListener;
import no.ntnu.tools.Logger;

/**
 * Tcp client for the sensor/actuator node.
 */
public class SensorActuatorTcpClient implements SensorListener {
  private static final String SERVER_HOST = "localhost";

  private final SensorActuatorNode node;
  private Socket socket;
  private PrintWriter socketWriter;
  private BufferedReader socketReader;

  public SensorActuatorTcpClient(SensorActuatorNode node) {
    this.node = node;
  }

  /**
   * Start the TCP client.
   */
  public void start() {
    if (connectToServer() && sendNodeTypeMessage()) {
      node.addSensorListener(this);
      Thread listeningThread = new Thread(this::processIncomingMessages);
      listeningThread.start();
    }
  }

  private void processIncomingMessages() {
    Logger.info("Receiving messages from the server on thread " + Thread.currentThread().getName());
    Message message;
    do {
      message = receiveServerCommand();
      if (message instanceof ControlCommandMessage command) {
        processServerCommand(command);
      } else {
        Logger.error("Incorrect message received from the server: " + message);
      }
    } while (message != null);
    Logger.info("Stop receiving messages on thread " + Thread.currentThread().getName());
  }


  private Message receiveServerCommand() {
    Message message = null;
    try {
      String messageString = socketReader.readLine();
      Logger.info("Server: " + messageString);
      message = MessageSerializer.fromString(messageString);
    } catch (IOException e) {
      Logger.error("Error while receiving message from the server: " + e.getMessage());
    }
    return message;
  }

  private void processServerCommand(ControlCommandMessage command) {
    throw new UnsupportedOperationException("Not implemented");
  }

  private boolean sendNodeTypeMessage() {
    return sendToServer(MessageSerializer.toString(new SensorNodeTypeMessage(node)));
  }

  private boolean sendToServer(String messageString) {
    boolean sent = false;
    try {
      Logger.info(" To Server: " + messageString);
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
      socketReader = new BufferedReader(new InputStreamReader((socket.getInputStream())));
      connected = true;
    } catch (IOException e) {
      Logger.error("Could not open socket to the server: " + e.getMessage());
    }
    return connected;
  }

  @Override
  public void sensorsUpdated(List<Sensor> sensors) {
    List<SensorReading> readings = sensors.stream().map(Sensor::getReading).toList();
    Message message = new SensorDataMessage(readings, node.getId());
    String serializedSensorData = MessageSerializer.toString(message);
    sendToServer(serializedSensorData);
  }
}
