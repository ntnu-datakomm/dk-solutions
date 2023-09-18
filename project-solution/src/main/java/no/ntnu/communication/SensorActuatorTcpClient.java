package no.ntnu.communication;

import static no.ntnu.communication.message.ErrorType.UNKNOWN;

import java.util.List;
import no.ntnu.communication.message.ActuatorStateMessage;
import no.ntnu.communication.message.ErrorMessage;
import no.ntnu.communication.message.Message;
import no.ntnu.communication.message.MessageSerializer;
import no.ntnu.communication.message.SensorDataMessage;
import no.ntnu.communication.message.SensorNodeTypeMessage;
import no.ntnu.greenhouse.Actuator;
import no.ntnu.greenhouse.Sensor;
import no.ntnu.greenhouse.SensorActuatorNode;
import no.ntnu.greenhouse.SensorReading;
import no.ntnu.listeners.common.ActuatorListener;
import no.ntnu.listeners.greenhouse.NodeStateListener;
import no.ntnu.listeners.greenhouse.SensorListener;
import no.ntnu.tools.Logger;

/**
 * Tcp client for the sensor/actuator node.
 */
public class SensorActuatorTcpClient extends TcpClient
    implements SensorListener, NodeStateListener, ActuatorListener {

  private final SensorActuatorNode node;

  public SensorActuatorTcpClient(SensorActuatorNode node) {
    this.node = node;
  }

  @Override
  protected Message createNodeTypeMessage() {
    return new SensorNodeTypeMessage(node);
  }

  protected void processServerMessage(Message message) {
    if (message instanceof ActuatorStateMessage actuatorCommand) {
      processActuatorCommand(actuatorCommand);
    } else if (message instanceof ErrorMessage errorMessage) {
      Logger.error("Error from server[" + errorMessage.getType() + "]: "
          + errorMessage.getMessage());
    } else {
      Logger.error("Processing not implemented: " + message);
      ErrorMessage errorMessage = new ErrorMessage(UNKNOWN, "Unknown command received");
      sendToServer(MessageSerializer.toString(errorMessage));
    }
  }

  private void processActuatorCommand(ActuatorStateMessage actuatorCommand) {
    if (actuatorCommand.isAnyActuator()) {
      node.setAllActuators(actuatorCommand.isOn());
    } else {
      node.setActuator(actuatorCommand.getActuatorId(), actuatorCommand.isOn());
    }
  }

  @Override
  public void sensorsUpdated(List<Sensor> sensors) {
    List<SensorReading> readings = sensors.stream().map(Sensor::getReading).toList();
    Message message = new SensorDataMessage(readings, node.getId());
    String serializedSensorData = MessageSerializer.toString(message);
    sendToServer(serializedSensorData);
  }

  @Override
  public void onNodeReady(SensorActuatorNode node) {
    Logger.info("Node " + node.getId() + " is ready, opening the socket");
    if (!openSocket()) {
      closeSocket();
    }
  }

  @Override
  public void onNodeStopped(SensorActuatorNode node) {
    Logger.info("Node " + node.getId() + " is shut down, close the socket");
    closeSocket();
  }

  @Override
  public void actuatorUpdated(int nodeId, Actuator actuator) {
    Logger.info(actuator + " updated on node " + nodeId + ", sending message to server...");
    ActuatorStateMessage message = new ActuatorStateMessage(
        nodeId, actuator.getId(), actuator.isOn());
    sendToServer(MessageSerializer.toString(message));
  }
}
