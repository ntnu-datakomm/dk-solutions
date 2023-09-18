package no.ntnu.communication.message;

/**
 * A command for requesting to set state of actuators on sensor/actuator node(s).
 */
public class ActuatorCommandMessage implements Message {
  public static final int ANY = -1;
  private final int nodeId;
  private final int actuatorId;
  private final boolean on;

  /**
   * Create a control command to turn on/off a specific actuator on a specific node.
   *
   * @param nodeId     ID of the node
   * @param actuatorId ID of the actuator
   * @param on         Whether the actuator must be turned on or off
   */
  public ActuatorCommandMessage(int nodeId, int actuatorId, boolean on) {
    this.nodeId = nodeId;
    this.actuatorId = actuatorId;
    this.on = on;
  }

  /**
   * Create a control command to turn on/off all actuators on a specific node.
   *
   * @param nodeId The ID of the node
   * @param on     Whether the actuator must be turned on or off
   */
  public ActuatorCommandMessage(int nodeId, boolean on) {
    this.nodeId = nodeId;
    this.actuatorId = ANY;
    this.on = on;
  }

  /**
   * Create a control command to turn on/off actuator with specific ID on all nodes.
   *
   * @param on         Whether the actuator must be turned on or off
   * @param actuatorId ID of the actuator
   */
  public ActuatorCommandMessage(boolean on, int actuatorId) {
    this.nodeId = ANY;
    this.actuatorId = actuatorId;
    this.on = on;
  }

  /**
   * Create a control command to turn on/off all actuators on all nodes.
   *
   * @param on Whether the actuator must be turned on or off
   */
  public ActuatorCommandMessage(boolean on) {
    this.nodeId = ANY;
    this.actuatorId = ANY;
    this.on = on;
  }

  public int getNodeId() {
    return nodeId;
  }

  public boolean isAnyNode() {
    return nodeId == ANY;
  }

  public int getActuatorId() {
    return actuatorId;
  }

  public boolean isAnyActuatorId() {
    return actuatorId == ANY;
  }

  public boolean isOn() {
    return on;
  }
}
