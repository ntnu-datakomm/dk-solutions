package no.ntnu.communication;


import no.ntnu.communication.message.ControlNodeTypeMessage;
import no.ntnu.communication.message.Message;
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
  }

  @Override
  public void sendActuatorChange(int nodeId, int actuatorId, boolean isOn) {
    throw new UnsupportedOperationException("Not implemented");
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
    } else {
      throw new UnsupportedOperationException("Not implemented processing of : "
          + message.getClass().getSimpleName());
    }
  }

  private void onNewSensorNodeAppeared(SensorNodeTypeMessage sntm) {
    Logger.info("  Server: new node discovered with ID " + sntm.getNodeId());
    SensorActuatorNodeInfo nodeInfo = new SensorActuatorNodeInfo(sntm.getNodeId());
    for (Actuator actuator : sntm.getActuators()) {
      nodeInfo.addActuator(actuator);
    }
    logic.onNodeAdded(nodeInfo);
  }

  private void onSensorNodeDisappeared(int nodeId) {
    Logger.info("  Server: disconnected node with ID " + nodeId);
    logic.onNodeRemoved(nodeId);
  }

  @Override
  public void open() {
    super.openSocket();
  }
}
