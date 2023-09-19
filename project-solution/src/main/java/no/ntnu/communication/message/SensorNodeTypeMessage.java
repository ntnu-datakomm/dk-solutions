package no.ntnu.communication.message;

import no.ntnu.greenhouse.Actuator;
import no.ntnu.greenhouse.ActuatorCollection;
import no.ntnu.greenhouse.SensorActuatorNode;

/**
 * A message saying "I'm a sensor/actuator node, my actuator list is as follows".
 */
public class SensorNodeTypeMessage extends NodeTypeMessage {
  private final ActuatorCollection actuators;
  private final int nodeId;

  /**
   * Create a message.
   *
   * @param node The node which is described in this message
   */
  public SensorNodeTypeMessage(SensorActuatorNode node) {
    super();
    this.nodeId = node.getId();
    this.actuators = node.getActuators();
  }

  /**
   * Create a message. Actuators can be added later.
   *
   * @param nodeId The ID of the node described in this message.
   */
  public SensorNodeTypeMessage(int nodeId) {
    super();
    this.nodeId = nodeId;
    this.actuators = new ActuatorCollection();
  }

  /**
   * Add an actuator to the message.
   *
   * @param actuator The actuator to add
   */
  public void addActuator(Actuator actuator) {
    actuators.add(actuator);
  }

  /**
   * Get all the actuators described in the message.
   *
   * @return The actuator collection
   */
  public ActuatorCollection getActuators() {
    return actuators;
  }

  /**
   * Get the ID of the associated sensor/actuator node.
   *
   * @return The ID of the node
   */
  public int getNodeId() {
    return nodeId;
  }
}
