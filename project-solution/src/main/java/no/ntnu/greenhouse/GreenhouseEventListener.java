package no.ntnu.greenhouse;

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
}
