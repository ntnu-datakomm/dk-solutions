package no.ntnu.greenhouse;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import no.ntnu.listeners.greenhouse.NodeStateListener;
import no.ntnu.tools.Logger;

/**
 * Represents a greenhouse with sensor/actuator nodes inside.
 */
public class Greenhouse {
  private final Map<Integer, SensorActuatorNode> nodes = new HashMap<>();
  private final List<PeriodicSwitch> periodicSwitches = new LinkedList<>();

  public void addNode(SensorActuatorNode node) {
    nodes.put(node.getId(), node);
  }

  /**
   * Start simulating the greenhouse - all the sensor/actuator nodes in it.
   */
  public void startSimulation() {
    for (SensorActuatorNode node : nodes.values()) {
      node.start();
    }
    for (PeriodicSwitch periodicSwitch : periodicSwitches) {
      periodicSwitch.start();
    }
  }

  /**
   * Stop simulating the greenhouse - all the sensor/actuator nodes in it.
   */
  public void stopSimulation() {
    Logger.info("Stopping greenhouse simulation...");
    for (SensorActuatorNode node : nodes.values()) {
      node.stop();
    }
    for (PeriodicSwitch periodicSwitch : periodicSwitches) {
      periodicSwitch.stop();
    }
  }

  public void addPeriodicSwitch(PeriodicSwitch periodicActuator) {
    periodicSwitches.add(periodicActuator);
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
