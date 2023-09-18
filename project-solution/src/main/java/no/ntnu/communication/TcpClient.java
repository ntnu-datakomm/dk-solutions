package no.ntnu.communication;

import static no.ntnu.communication.TcpServer.TCP_PORT;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import no.ntnu.communication.message.Message;
import no.ntnu.communication.message.MessageSerializer;
import no.ntnu.listeners.common.CommunicationChannelListener;
import no.ntnu.tools.Logger;

/**
 * A generic TCP client base class, used on both sensor/actuator nodes and control panel nodes.
 */
public abstract class TcpClient {
  private static final String SERVER_HOST = "localhost";
  private PrintWriter socketWriter;
  private BufferedReader socketReader;
  private Socket socket;

  private List<CommunicationChannelListener> listeners = new LinkedList<>();

  /**
   * Start the TCP client.
   *
   * @return True when the client has successfully initiated communication with the server,
   *     false on error
   */
  public boolean openSocket() {
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

  private void processIncomingMessages() {
    Logger.info("Receiving messages from the server on thread " + Thread.currentThread().getName());
    Message message;
    do {
      message = receiveServerMessage();
      if (message != null) {
        processServerMessage(message);
      }
    } while (message != null);
    Logger.info("Stopped receiving messages on thread " + Thread.currentThread().getName());
    closeSocket();
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
      Logger.error("Error while receiving message from the server on "
          + Thread.currentThread().getName() + " : " + e.getMessage());
    }
    return message;
  }

  /**
   * Send a message to the server.
   *
   * @param message The message to send, as a string. Newline will be appended automatically.
   * @return True on success, false on error
   */
  protected boolean sendToServer(String message) {
    boolean sent = false;
    try {
      Logger.info(" To Server: " + message);
      socketWriter.println(message);
      sent = true;
    } catch (Exception e) {
      Logger.error("Failed to send message to the server: " + e.getMessage());
    }
    return sent;
  }

  /**
   * Stop the communication, close the socket.
   */
  public void closeSocket() {
    Logger.info("Closing socket");
    if (socket != null) {
      try {
        socket.close();
      } catch (IOException e) {
        Logger.error("Could not close TCP socket: " + e.getMessage());
      }
      socket = null;
    }
    notifyListenersAboutClosing();
  }

  private void notifyListenersAboutClosing() {
    for (CommunicationChannelListener listener : listeners) {
      listener.onCommunicationChannelClosed();
    }
  }

  /**
   * Add a new lifecycle event listener.
   *
   * @param listener A listener who will be notified when the state of communication channel
   *                 changes (communication is closed).
   */
  public void addListener(CommunicationChannelListener listener) {
    if (!listeners.contains(listener)) {
      listeners.add(listener);
    }
  }
}
