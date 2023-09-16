package no.ntnu.communication;


import no.ntnu.communication.message.ControlNodeTypeMessage;
import no.ntnu.communication.message.Message;
import no.ntnu.controlpanel.ControlCommandSender;
import no.ntnu.controlpanel.ControlPanelLogic;

/**
 * TCP client for control-panel nodes.
 */
public class ControlPanelTcpClient extends TcpClient implements ControlCommandSender {
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
    throw new UnsupportedOperationException("Not implemented");
  }
}
