package no.ntnu.communication;

import static no.ntnu.communication.ClientType.SENSOR_ACTUATOR_NODE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import no.ntnu.communication.message.Message;
import no.ntnu.communication.message.MessageSerializer;
import no.ntnu.communication.message.NodeTypeMessage;
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
        server.broadcastSensorNodeShutdown(nodeId);
      }
    }

    Logger.info("Exiting the handler of the client "
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
    Message message = receiveClientMessage();
    if (!(message instanceof NodeTypeMessage)) {
      throw new IllegalStateException("Client must send a node-type message first");
    }

    if (message instanceof SensorNodeTypeMessage sntm) {
      Logger.info("Sensor node " + sntm.getNodeId() + " connected with "
          + sntm.getActuators().size() + " actuators");
    }
    // TODO - save client type, handle it
  }

  private void handleClientRequests() {
    Message message;
    do {
      message = receiveClientMessage();
      if (message != null) {
        handleMessage(message);
      }
    } while (message != null);
  }

  /**
   * Receive one message from the client (over the TCP socket).
   *
   * @return The client message, null on error
   */
  private Message receiveClientMessage() {
    Message message = null;
    try {
      message = MessageSerializer.fromString(socketReader.readLine());
    } catch (IOException e) {
      Logger.error("Error while receiving data from the client: " + e.getMessage());
    }
    return message;
  }

  /**
   * Handle one massage from the client.
   *
   * @param message A message received from the client
   */
  private void handleMessage(Message message) {
    Logger.info("Message from the client: " + message);

    Message response = null;

    // TODO

    if (response != null) {
      sendToClient(response);
    }
  }

  private void sendToClient(Message message) {
    try {
      socketWriter.println(MessageSerializer.toString(message));
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