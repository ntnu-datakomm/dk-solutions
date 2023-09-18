package no.ntnu.communication.message;

/**
 * A message for either requesting to set state of actuators on sensor/actuator node(s) or to
 * report actuator state.
 */
public class ActuatorStateMessage implements Message {
  public static final int ANY = -1;
  private final int nodeId;
  private final int actuatorId;
  private final boolean on;

  /**
   * Create a message specifying (desired) state of specific actuator on a specific node.
   *
   * @param nodeId     ID of the node. Use ANY if this command is meant for all nodes.
   * @param actuatorId ID of the actuator. Use ANY if this command is meant for all actuators.
   * @param on         Whether the actuator is (must be) on or off
   */
  public ActuatorStateMessage(int nodeId, int actuatorId, boolean on) {
    this.nodeId = nodeId;
    this.actuatorId = actuatorId;
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

  public boolean isAnyActuator() {
    return actuatorId == ANY;
  }

  public boolean isOn() {
    return on;
  }

  /**
   * Check whether this message depicts one specific actuator on one specific sensor/actuator node.
   *
   * @return True if this message is about one actuator, false if it may be directed to multiple
   *     actuators and/or multiple nodes.
   */
  public boolean isSpecific() {
    return !isAnyNode() && !isAnyActuator();
  }
}
