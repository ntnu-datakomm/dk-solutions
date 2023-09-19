package no.ntnu.communication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import no.ntnu.communication.message.ActuatorStateMessage;
import no.ntnu.communication.message.Message;
import no.ntnu.communication.message.MessageSerializer;
import no.ntnu.communication.message.SensorNodeOfflineMessage;
import no.ntnu.communication.message.SensorNodeTypeMessage;
import no.ntnu.tools.Logger;

/**
 * A TCP server, handles multiple clients.
 */
public class TcpServer {
  public static final int TCP_PORT = 1212;
  private ServerSocket serverSocket;
  private boolean isRunning;
  private final List<ClientHandler> controlPanelNodes = new LinkedList<>();
  private final Map<Integer, String> sensorNodeActuatorMessages = new HashMap<>();
  private final Map<Integer, ClientHandler> sensorNodes = new HashMap<>();

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

  /**
   * Call this method when a new control panel node has connected as a TCP client.
   *
   * @param client Handler for the TCP socket for this client
   */
  public void onControlPanelNodeConnected(ClientHandler client) {
    Logger.info("Control node connected");
    controlPanelNodes.add(client);
    sendSensorNodeConfigTo(client);
  }

  /**
   * Call this method when a new sensor/actuator node has connected as a TCP client.
   *
   * @param message The message containing data about the sensor/actuator node
   *                (it's ID and actuators)
   * @param client  Handler for the TCP socket for this client
   */
  public void onSensorNodeConnected(SensorNodeTypeMessage message, ClientHandler client) {
    Logger.info("Sensor node " + message.getNodeId() + " connected with "
        + message.getActuators().size() + " actuators");
    String actuatorConfigMessage = MessageSerializer.toString(message);
    sensorNodeActuatorMessages.put(message.getNodeId(), actuatorConfigMessage);
    sensorNodes.put(message.getNodeId(), client);
    broadcastToControlPanels(actuatorConfigMessage);
  }

  /**
   * Notify all currently connected control panel nodes that a sensor/actuator node
   * has disconnected from the server.
   *
   * @param nodeId ID of the disconnected node
   */
  public void onSensorNodeShutdown(int nodeId) {
    Logger.info("Sensor node " + nodeId + " disconnected");
    sensorNodeActuatorMessages.remove(nodeId);
    sensorNodes.remove(nodeId);
    broadcastToControlPanels(new SensorNodeOfflineMessage(nodeId));
  }

  private void broadcastToControlPanels(Message message) {
    broadcastToControlPanels(MessageSerializer.toString(message));
  }

  private void broadcastToControlPanels(String rawMessage) {
    for (ClientHandler client : controlPanelNodes) {
      client.sendToClient(rawMessage);
    }
  }

  private void sendSensorNodeConfigTo(ClientHandler client) {
    for (String message : sensorNodeActuatorMessages.values()) {
      client.sendToClient(message);
    }
  }

  /**
   * Notify all currently connected control panel nodes that a new sensor data message is received.
   *
   * @param sensorDataMessage Message with the sensor data
   */
  public void onSensorData(String sensorDataMessage) {
    broadcastToControlPanels(sensorDataMessage);
  }

  /**
   * Notify all currently connected control panel nodes that a new actuator state message
   * is received.
   *
   * @param actuatorStateMessage Message containing actuator state
   */
  public void onActuatorState(String actuatorStateMessage) {
    broadcastToControlPanels(actuatorStateMessage);
  }

  /**
   * Forward actuator control command to necessary sensor/actuator nodes (only the ones matching
   * the nodeId filter).
   *
   * @param command The actuator command to forward
   */
  public void forwardActuatorCommandToSensors(ActuatorStateMessage command) {
    String messageToSend = MessageSerializer.toString(command);
    if (command.isAnyNode()) {
      for (ClientHandler clientHandler : sensorNodes.values()) {
        clientHandler.sendToClient(messageToSend);
      }
    } else {
      ClientHandler clientHandler = sensorNodes.get(command.getNodeId());
      if (clientHandler != null) {
        clientHandler.sendToClient(messageToSend);
      }
    }
  }
}