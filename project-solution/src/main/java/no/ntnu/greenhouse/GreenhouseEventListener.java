package no.ntnu.greenhouse;

import java.util.List;
import no.ntnu.controlpanel.SensorActuatorNodeInfo;

/**
 * Listener of events happening "inside a greenhouse", such as a node appearing, disappearing,
 * new sensor readings, etc.
 */
public interface GreenhouseEventListener {
  /**
   * This event is fired when a new node is added to the greenhouse.
   *
   * @param nodeInfo Information about the added node
   */
  void onNodeAdded(SensorActuatorNodeInfo nodeInfo);

  /**
   * This event is fired when new sensor data is received from a node.
   *
   * @param nodeId  ID of the node
   * @param sensors List of all current sensor values
   */
  void onSensorData(int nodeId, List<SensorReading> sensors);

  /**
   * This event is fired when a node is removed from the greenhouse.
   *
   * @param nodeId ID of the node which has disappeared (removed)
   */
  void onNodeRemoved(int nodeId);
}
