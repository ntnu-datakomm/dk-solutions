package no.ntnu.communication;

import static no.ntnu.communication.ClientType.CONTROL_PANEL_NODE;
import static no.ntnu.communication.ClientType.SENSOR_ACTUATOR_NODE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import no.ntnu.communication.message.ControlNodeTypeMessage;
import no.ntnu.communication.message.Message;
import no.ntnu.communication.message.MessageSerializer;
import no.ntnu.communication.message.SensorDataMessage;
import no.ntnu.communication.message.SensorNodeTypeMessage;
import no.ntnu.tools.Logger;

/**
 * Handle one TCP client connection.
 */
public class ClientHandler extends Thread {
  private final Socket clientSocket;
  private PrintWriter socketWriter;
  private BufferedReader socketReader;
  private ClientType clientType;
  private final TcpServer server;
  private int nodeId;

  /**
   * Create a new client handler.
   *
   * @param clientSocket The TCP socket associated with this client
   */
  public ClientHandler(Socket clientSocket, TcpServer server) {
    this.clientSocket = clientSocket;
    this.server = server;
    Logger.info("Client connected from " + clientSocket.getRemoteSocketAddress()
        + ", port " + clientSocket.getPort());
  }

  /**
   * Run the handling logic of this TCP client.
   */
  @Override
  public void run() {
    if (establishStreams()) {
      receiveClientTypeMessage();
      handleClientRequests();
      closeSocket();
      if (clientType == SENSOR_ACTUATOR_NODE) {
        server.onSensorNodeShutdown(nodeId);
      }
    }

    Logger.info("Exiting the handler of client "
        + clientSocket.getRemoteSocketAddress());
  }


  /**
   * Establish the input and output streams of the socket.
   *
   * @return True on success, false on error
   */
  private boolean establishStreams() {
    boolean success = false;
    try {
      socketWriter = new PrintWriter(clientSocket.getOutputStream(), true);
      socketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
      success = true;
    } catch (IOException e) {
      Logger.error("Error while processing the client: " + e.getMessage());
    }
    return success;
  }

  private void receiveClientTypeMessage() {
    Message message = MessageSerializer.fromString(receiveClientMessage());
    if (message instanceof SensorNodeTypeMessage sntm) {
      clientType = SENSOR_ACTUATOR_NODE;
      nodeId = sntm.getNodeId();
      server.onSensorNodeConnected(sntm);
    } else if (message instanceof ControlNodeTypeMessage) {
      clientType = CONTROL_PANEL_NODE;
      server.onControlPanelNodeConnected(this);
    } else {
      throw new IllegalStateException("Client must send a node-type message first, got " + message);
    }
  }

  private void handleClientRequests() {
    String rawMessage;
    do {
      rawMessage = receiveClientMessage();
      Message message = MessageSerializer.fromString(rawMessage);
      if (message != null) {
        handleMessage(rawMessage, message);
      }
    } while (rawMessage != null);
  }

  /**
   * Receive one message from the client (over the TCP socket).
   *
   * @return The client message, null on error
   */
  private String receiveClientMessage() {
    String message = null;
    try {
      message = socketReader.readLine();
    } catch (IOException e) {
      Logger.error("Error while receiving data from the client: " + e.getMessage());
    }
    return message;
  }

  /**
   * Handle one massage from the client.
   *
   * @param rawMessage The raw message string received from the socket
   * @param message    A message received from the client parsed version of the string)
   */
  private void handleMessage(String rawMessage, Message message) {
    Logger.info("Message from the client: " + message);

    if (message instanceof SensorDataMessage) {
      server.onSensorData(rawMessage);
    }
  }

  /**
   * Send a message to the client, over the socket.
   *
   * @param message The message to send. Newline will be appended automatically.
   */
  public void sendToClient(String message) {
    try {
      socketWriter.println(message);
    } catch (Exception e) {
      Logger.error("Error while sending a message to the client: " + e.getMessage());
    }
  }

  private void closeSocket() {
    try {
      clientSocket.close();
    } catch (IOException e) {
      Logger.error("Error while closing socket for client "
          + clientSocket.getRemoteSocketAddress() + ", reason: " + e.getMessage());
    }
  }
}