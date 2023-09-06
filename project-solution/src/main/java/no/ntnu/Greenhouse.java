package no.ntnu;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a greenhouse with sensor/actuator nodes inside.
 */
public class Greenhouse {
  private final Map<Integer, SensorActuatorNode> nodes = new HashMap<>();

  public void addNode(SensorActuatorNode node) {
    nodes.put(node.getId(), node);
  }

}
