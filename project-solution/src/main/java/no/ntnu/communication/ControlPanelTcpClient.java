package no.ntnu.communication;


import no.ntnu.communication.message.ActuatorStateMessage;
import no.ntnu.communication.message.ControlNodeTypeMessage;
import no.ntnu.communication.message.ErrorMessage;
import no.ntnu.communication.message.Message;
import no.ntnu.communication.message.MessageSerializer;
import no.ntnu.communication.message.SensorDataMessage;
import no.ntnu.communication.message.SensorNodeOfflineMessage;
import no.ntnu.communication.message.SensorNodeTypeMessage;
import no.ntnu.controlpanel.CommunicationChannel;
import no.ntnu.controlpanel.ControlPanelLogic;
import no.ntnu.controlpanel.SensorActuatorNodeInfo;
import no.ntnu.greenhouse.Actuator;
import no.ntnu.tools.Logger;

/**
 * TCP client for control-panel nodes.
 */
public class ControlPanelTcpClient extends TcpClient implements CommunicationChannel {
  private final ControlPanelLogic logic;

  public ControlPanelTcpClient(ControlPanelLogic logic) {
    this.logic = logic;
    addListener(logic);
  }

  @Override
  public void sendActuatorChange(int nodeId, int actuatorId, boolean isOn) {
    ActuatorStateMessage message = new ActuatorStateMessage(nodeId, actuatorId, isOn);
    if (!sendToServer(MessageSerializer.toString(message))) {
      Logger.error("Could not send control command to the server, closing socket");
      closeSocket();
    }
  }

  @Override
  protected Message createNodeTypeMessage() {
    return new ControlNodeTypeMessage();
  }

  @Override
  protected void processServerMessage(Message message) {
    if (message instanceof SensorNodeTypeMessage sntm) {
      onNewSensorNodeAppeared(sntm);
    } else if (message instanceof SensorNodeOfflineMessage offlineMessage) {
      onSensorNodeDisappeared(offlineMessage.getNodeId());
    } else if (message instanceof SensorDataMessage sensorDataMessage) {
      onSensorData(sensorDataMessage);
    } else if (message instanceof ActuatorStateMessage actuatorStateMessage) {
      onActuatorState(actuatorStateMessage);
    } else if (message instanceof ErrorMessage errorMessage) {
      Logger.error("Error from server[" + errorMessage.getType() + "]: "
          + errorMessage.getMessage());
    } else {
      Logger.error("Not implemented processing of : " + message.getClass().getSimpleName());
    }
  }

  private void onNewSensorNodeAppeared(SensorNodeTypeMessage sntm) {
    Logger.info("  Server: new node discovered with ID " + sntm.getNodeId());
    SensorActuatorNodeInfo nodeInfo = new SensorActuatorNodeInfo(sntm.getNodeId());
    for (Actuator actuator : sntm.getActuators()) {
      nodeInfo.addActuator(actuator);
      actuator.setListener(logic);
    }
    logic.onNodeAdded(nodeInfo);
  }

  private void onSensorNodeDisappeared(int nodeId) {
    Logger.info("  Server: disconnected node with ID " + nodeId);
    logic.onNodeRemoved(nodeId);
  }

  private void onSensorData(SensorDataMessage sensorDataMessage) {
    logic.onSensorData(sensorDataMessage.getNodeId(), sensorDataMessage.getSensors());
  }

  private void onActuatorState(ActuatorStateMessage message) {
    logic.onActuatorStateChanged(message.getNodeId(), message.getActuatorId(), message.isOn());
  }

  @Override
  public boolean open() {
    return openSocket();
  }
}
