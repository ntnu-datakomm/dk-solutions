package no.ntnu.controlpanel;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains information about one sensor/actuator node. This is NOT the node itself, rather
 * an information that can be used on the control-panel side to represent the node.
 */
public class SensorActuatorNodeInfo {

  private final int nodeId;
  private final Map<String, Integer> sensorCount = new HashMap<>();
  private final Map<String, Integer> actuatorCount = new HashMap<>();

  public SensorActuatorNodeInfo(int nodeId) {
    this.nodeId = nodeId;
  }

  public void addSensors(String type, int count) {
    sensorCount.merge(type, count, Integer::sum);
  }

  public void addActuators(String type, Integer count) {
    actuatorCount.merge(type, count, Integer::sum);
  }
}
