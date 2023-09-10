package no.ntnu.controlpanel;

import no.ntnu.greenhouse.Actuator;
import no.ntnu.tools.GroupedItemCollection;

/**
 * Contains information about one sensor/actuator node. This is NOT the node itself, rather
 * an information that can be used on the control-panel side to represent the node.
 */
public class SensorActuatorNodeInfo {

  private final int nodeId;
  private final GroupedItemCollection<Actuator> actuators = new GroupedItemCollection<>();

  public SensorActuatorNodeInfo(int nodeId) {
    this.nodeId = nodeId;
  }

  public void addActuators(String type, Actuator actuator) {
    actuators.add(type, actuator);
  }
}
