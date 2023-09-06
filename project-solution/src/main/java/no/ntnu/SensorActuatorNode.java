package no.ntnu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents one node with sensors and actuators.
 */
public class SensorActuatorNode {
  private final int id;

  private final Map<String, List<Sensor>> sensors = new HashMap<>();
  private final Map<String, List<Actuator>> actuators = new HashMap<>();

  /**
   * Create a sensor/actuator node. Note: the node itself does not check whether the ID is unique.
   * This is done at the greenhouse-level.
   *
   * @param id A unique ID of the node
   */
  public SensorActuatorNode(int id) {
    this.id = id;
  }

  /**
   * Get the unique ID of the node.
   *
   * @return the ID
   */
  public int getId() {
    return id;
  }

  /**
   * Add sensors to the node.
   *
   * @param template The template to use for the sensors. The template will be cloned.
   *                 This template defines the type of sensors, the value range, value
   *                 generation algorithms, etc.
   * @param n        The number of sensors to add to the node.
   */
  public void addSensors(Sensor template, int n) {
    if (template == null) {
      throw new IllegalArgumentException("Sensor template is missing");
    }
    String type = template.getType();
    if (type == null || type.isEmpty()) {
      throw new IllegalArgumentException("Sensor type missing");
    }
    if (n <= 0) {
      throw new IllegalArgumentException("Can't add a negative number of sensors");
    }

    List<Sensor> sensorsOfThatType = getSensorsOfGivenType(type);
    for (int i = 0; i < n; ++i) {
      sensorsOfThatType.add(template.clone());
    }
  }

  private List<Sensor> getSensorsOfGivenType(String type) {
    return sensors.computeIfAbsent(type, k -> new ArrayList<>());
  }

  /**
   * Add a set of actuators to the node.
   *
   * @param template The actuator to use as a template.
   * @param n        The number of actuators to add
   */
  public void addActuators(Actuator template, int n) {
    if (template == null) {
      throw new IllegalArgumentException("Actuator template is missing");
    }
    if (n <= 0) {
      throw new IllegalArgumentException("Can't add a negative number of actuators");
    }

    List<Actuator> actuatorsOfThatType = getActuatorsOfGivenType(template.getType());
    for (int i = 0; i < n; ++i) {
      actuatorsOfThatType.add(template.clone());
    }
  }

  private List<Actuator> getActuatorsOfGivenType(String type) {
    return actuators.computeIfAbsent(type, k -> new ArrayList<>());
  }
}
