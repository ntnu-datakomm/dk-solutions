package no.ntnu.communication.message;

/**
 * A message saying "a sensor/actuator node has disappeared from the network".
 */
public class SensorNodeOfflineMessage implements Message {
  private final int nodeId;

  /**
   * Create a message.
   *
   * @param nodeId ID of the disconnected sensor/actuator node
   */
  public SensorNodeOfflineMessage(int nodeId) {
    this.nodeId = nodeId;
  }

  /**
   * Get ID of the disconnected node.
   *
   * @return ID of the disconnected node
   */
  public int getNodeId() {
    return nodeId;
  }
}
