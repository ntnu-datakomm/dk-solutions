package no.ntnu.greenhouse;

import java.util.HashMap;
import java.util.Map;
import no.ntnu.listeners.greenhouse.NodeStateListener;
import no.ntnu.tools.Logger;

/**
 * Represents a greenhouse with sensor/actuator nodes inside.
 */
public class Greenhouse {
  private final Map<Integer, SensorActuatorNode> nodes = new HashMap<>();

  public void addNode(SensorActuatorNode node) {
    nodes.put(node.getId(), node);
  }

  /**
   * Start the greenhouse - all the sensor/actuator nodes in it.
   */
  public void start() {
    for (SensorActuatorNode node : nodes.values()) {
      node.start();
    }
  }

  /**
   * Stop the greenhouse - all the sensor/actuator nodes in it.
   */
  public void stop() {
    Logger.info("Stopping greenhouse...");
    for (SensorActuatorNode node : nodes.values()) {
      node.stop();
    }
  }

  /**
   * Add a listener to all sensor/actuator node lifecycle updates.
   *
   * @param listener The listener which will get notified
   */
  public void subscribeToLifecycleUpdates(NodeStateListener listener) {
    for (SensorActuatorNode node : nodes.values()) {
      node.addStateListener(listener);
    }
  }
}
