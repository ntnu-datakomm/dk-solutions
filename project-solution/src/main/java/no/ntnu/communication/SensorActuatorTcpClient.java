package no.ntnu.communication;

import java.util.List;
import no.ntnu.communication.message.Message;
import no.ntnu.communication.message.MessageSerializer;
import no.ntnu.communication.message.SensorDataMessage;
import no.ntnu.communication.message.SensorNodeTypeMessage;
import no.ntnu.greenhouse.Sensor;
import no.ntnu.greenhouse.SensorActuatorNode;
import no.ntnu.greenhouse.SensorReading;
import no.ntnu.listeners.greenhouse.NodeStateListener;
import no.ntnu.listeners.greenhouse.SensorListener;
import no.ntnu.tools.Logger;

/**
 * Tcp client for the sensor/actuator node.
 */
public class SensorActuatorTcpClient extends TcpClient
    implements SensorListener, NodeStateListener {

  private final SensorActuatorNode node;

  public SensorActuatorTcpClient(SensorActuatorNode node) {
    this.node = node;
  }

  @Override
  protected Message createNodeTypeMessage() {
    return new SensorNodeTypeMessage(node);
  }

  protected void processServerMessage(Message message) {
    throw new UnsupportedOperationException("Not implemented");
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
    openSocket();
  }

  @Override
  public void onNodeStopped(SensorActuatorNode node) {
    Logger.info("Node " + node.getId() + " is shut down, close the socket");
    closeSocket();
  }
}
