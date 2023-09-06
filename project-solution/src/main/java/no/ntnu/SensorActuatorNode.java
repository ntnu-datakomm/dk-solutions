package no.ntnu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Represents one node with sensors and actuators.
 */
public class SensorActuatorNode {
  // How often to generate new sensor values, in seconds.
  private static final long SENSING_DELAY = 5000;
  private final int id;

  private final List<Sensor> sensors = new LinkedList<>();
  private final Map<String, List<Actuator>> actuators = new HashMap<>();

  private final List<SensorActuatorListener> listeners = new LinkedList<>();

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

    for (int i = 0; i < n; ++i) {
      sensors.add(template.clone());
    }
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

  /**
   * Start simulating the sensor node's operation.
   */
  public void startSimulation() {
    startPeriodicSensorReading();
    openCommunicationChannel();
  }

  private void openCommunicationChannel() {
    // TODO
  }

  private void startPeriodicSensorReading() {
    Timer timer = new Timer();
    TimerTask newSensorValueTask = new TimerTask() {
      @Override
      public void run() {
        generateNewSensorValues();
      }
    };
    long randomStartDelay = (long) (Math.random() * SENSING_DELAY);
    timer.scheduleAtFixedRate(newSensorValueTask, randomStartDelay, SENSING_DELAY);
  }

  /**
   * Generate new sensor values and send a notification to all listeners.
   */
  public void generateNewSensorValues() {
    System.out.print("Node #" + id);
    addRandomNoiseToSensors();
    notifySensorChanges();
    debugPrint();
  }

  private void addRandomNoiseToSensors() {
    for (Sensor sensor : sensors) {
      sensor.addRandomNoise();
    }
  }

  private void debugPrint() {
    for (Sensor sensor : sensors) {
      System.out.print(" " + sensor.getCurrent() + sensor.getUnit());
    }
    System.out.print(" :");
    for (List<Actuator> actuatorList : actuators.values()) {
      for (Actuator actuator : actuatorList) {
        System.out.print(" " + actuator.getType() + (actuator.isOn() ? " ON" : " off"));
      }
    }
    System.out.println();
  }

  /**
   * Toggle an actuator attached to this device.
   *
   * @param type  The type of the actuator
   * @param index The index of the actuator (within the list of actuators with the specified type).
   *              Indexing starts at zero.
   * @throws IllegalArgumentException If no actuator with given configuration is found on this node
   */
  public void toggleActuator(String type, int index) {
    Actuator actuator = getActuator(type, index);
    if (actuator == null) {
      throw new IllegalArgumentException(type + "[" + index + "] not found on node " + id);
    }
    actuator.toggle();
    notifyActuatorChange(actuator);
  }

  private Actuator getActuator(String type, int index) {
    Actuator actuator = null;
    List<Actuator> actuatorsOfThatType = actuators.get(type);
    if (actuatorsOfThatType != null && index >= 0 && index < actuatorsOfThatType.size()) {
      actuator = actuatorsOfThatType.get(index);
    }
    return actuator;
  }

  private void notifySensorChanges() {
    for (SensorActuatorListener listener : listeners) {
      listener.sensorsUpdated(sensors);
    }
  }

  private void notifyActuatorChange(Actuator actuator) {
    for (SensorActuatorListener listener : listeners) {
      listener.actuatorUpdated(actuator);
    }
  }

  /**
   * An actuator has been turned on or off. Apply an impact from it to all sensors of given type.
   *
   * @param sensorType The type of sensors affected
   * @param impact     The impact to apply
   */
  public void applyActuatorImpact(String sensorType, double impact) {
    for (Sensor sensor : sensors) {
      if (sensor.getType().equals(sensorType)) {
        sensor.applyImpact(impact);
      }
    }
  }
}
