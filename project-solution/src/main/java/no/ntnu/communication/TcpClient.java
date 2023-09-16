package no.ntnu.communication;

import static no.ntnu.communication.TcpServer.TCP_PORT;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import no.ntnu.communication.message.ControlCommandMessage;
import no.ntnu.communication.message.Message;
import no.ntnu.communication.message.MessageSerializer;
import no.ntnu.tools.Logger;

/**
 * A generic TCP client base class, used on both sensor/actuator nodes and control panel nodes.
 */
public abstract class TcpClient {
  private PrintWriter socketWriter;
  private BufferedReader socketReader;

  private static final String SERVER_HOST = "localhost";

  /**
   * Start the TCP client.
   *
   * @return True when the client has successfully initiated communication with the server,
   *     false on error
   */
  public boolean start() {
    boolean connected = false;
    if (connectToServer() && sendNodeTypeMessage()) {
      Thread listeningThread = new Thread(this::processIncomingMessages);
      listeningThread.start();
      connected = true;
    }
    return connected;
  }

  private boolean sendNodeTypeMessage() {
    Message nodeTypeMessage = createNodeTypeMessage();
    return sendToServer(MessageSerializer.toString(nodeTypeMessage));
  }

  protected abstract Message createNodeTypeMessage();


  private boolean connectToServer() {
    boolean connected = false;
    try (Socket socket = new Socket(SERVER_HOST, TCP_PORT)) {
      socketWriter = new PrintWriter(socket.getOutputStream(), true);
      socketReader = new BufferedReader(new InputStreamReader((socket.getInputStream())));
      connected = true;
    } catch (IOException e) {
      Logger.error("Could not open socket to the server: " + e.getMessage());
    }
    return connected;
  }

  private void processIncomingMessages() {
    Logger.info("Receiving messages from the server on thread " + Thread.currentThread().getName());
    Message message;
    do {
      message = receiveServerMessage();
      if (message instanceof ControlCommandMessage command) {
        processServerMessage(command);
      } else {
        Logger.error("Incorrect message received from the server: " + message);
      }
    } while (message != null);
    Logger.info("Stop receiving messages on thread " + Thread.currentThread().getName());
  }

  /**
   * Process a message coming from the server. Must be implemented by child classes.
   *
   * @param message Message received from the server
   */
  protected abstract void processServerMessage(Message message);

  private Message receiveServerMessage() {
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

  protected boolean sendToServer(String messageString) {
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

}
