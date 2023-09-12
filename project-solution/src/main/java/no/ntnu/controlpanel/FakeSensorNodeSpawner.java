package no.ntnu.controlpanel;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import no.ntnu.greenhouse.Actuator;
import no.ntnu.greenhouse.SensorReading;

/**
 * Spawns a fake sensor/actuator node information. Emulates the node discovery (over the Internet).
 */
public class FakeSensorNodeSpawner {


  private final FakeSpawnerListener listener;

  /**
   * Create a new fake sensor node spawner.
   *
   * @param listener The listener who will be notified when the node is spawned
   */
  public FakeSensorNodeSpawner(FakeSpawnerListener listener) {
    this.listener = listener;
  }

  private SensorActuatorNodeInfo createSensorNodeInfoFrom(String specification) {
    if (specification == null || specification.isEmpty()) {
      throw new IllegalArgumentException("Node specification can't be empty");
    }
    String[] parts = specification.split(";");
    if (parts.length > 3) {
      throw new IllegalArgumentException("Incorrect specification format");
    }
    Integer nodeId = parseIntegerOrError(parts[0], "Invalid node ID:" + parts[0]);
    SensorActuatorNodeInfo info = new SensorActuatorNodeInfo(nodeId);
    if (parts.length == 2) {
      parseActuators(parts[1], info);
    }
    return info;
  }

  private void parseActuators(String actuatorSpecification, SensorActuatorNodeInfo info) {
    String[] parts = actuatorSpecification.split(" ");
    for (String part : parts) {
      parseActuatorInfo(part, info);
    }
  }

  private void parseActuatorInfo(String s, SensorActuatorNodeInfo info) {
    String[] actuatorInfo = s.split("_");
    if (actuatorInfo.length != 2) {
      throw new IllegalArgumentException("Invalid actuator info format: " + s);
    }
    int actuatorCount = parseIntegerOrError(actuatorInfo[0],
        "Invalid actuator count: " + actuatorInfo[0]);
    String actuatorType = actuatorInfo[1];
    for (int i = 0; i < actuatorCount; ++i) {
      info.addActuators(actuatorType, new Actuator(actuatorType, null));
    }
  }

  private int parseIntegerOrError(String s, String errorMessage) {
    try {
      return Integer.parseInt(s);
    } catch (NumberFormatException e) {
      throw new NumberFormatException(errorMessage);
    }
  }

  private double parseDoubleOrError(String s, String errorMessage) {
    try {
      return Double.parseDouble(s);
    } catch (NumberFormatException e) {
      throw new NumberFormatException(errorMessage);
    }
  }

  /**
   * Spawn a new sensor/actuator node information after a given delay.
   *
   * @param specification A (temporary) manual configuration of the node in the following format
   *                      [nodeId] semicolon
   *                      [actuator_count_1] underscore [actuator_type_1] space ... space
   *                      [actuator_count_M] underscore [actuator_type_M]
   * @param delay         Delay in seconds
   */
  public void spawnNode(String specification, int delay) {
    SensorActuatorNodeInfo nodeInfo = createSensorNodeInfoFrom(specification);
    Timer timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        listener.onNodeSpawned(nodeInfo);
      }
    }, delay * 1000L);
  }

  /**
   * Advertise new sensor readings.
   *
   * @param specification Specification of the readings in the following format:
   *                      [nodeID]
   *                      semicolon
   *                      [sensor_type_1] equals [sensor_value_1] space [unit_1]
   *                      comma
   *                      ...
   *                      comma
   *                      [sensor_type_N] equals [sensor_value_N] space [unit_N]
   * @param delay         Delay in seconds
   */
  public void advertiseSensorData(String specification, int delay) {
    if (specification == null || specification.isEmpty()) {
      throw new IllegalArgumentException("Sensor specification can't be empty");
    }
    String[] parts = specification.split(";");
    if (parts.length != 2) {
      throw new IllegalArgumentException("Incorrect specification format: " + specification);
    }
    int nodeId = parseIntegerOrError(parts[0], "Invalid node ID:" + parts[0]);
    List<SensorReading> sensors = parseSensors(parts[1]);
    Timer timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        listener.onSensorData(nodeId, sensors);
      }
    }, delay * 1000L);
  }

  /**
   * Advertise that a node is removed.
   *
   * @param nodeId ID of the removed node
   * @param delay  Delay in seconds
   */
  public void advertiseRemovedNode(int nodeId, int delay) {
    Timer timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        listener.onNodeRemoved(nodeId);
      }
    }, delay * 1000L);
  }

  private List<SensorReading> parseSensors(String sensorInfo) {
    List<SensorReading> readings = new LinkedList<>();
    String[] readingInfo = sensorInfo.split(",");
    for (String reading : readingInfo) {
      readings.add(parseReading(reading));
    }
    return readings;
  }

  private SensorReading parseReading(String reading) {
    String[] assignmentParts = reading.split("=");
    if (assignmentParts.length != 2) {
      throw new IllegalArgumentException("Invalid sensor reading specified: " + reading);
    }
    String[] valueParts = assignmentParts[1].split(" ");
    if (valueParts.length != 2) {
      throw new IllegalArgumentException("Invalid sensor value/unit: " + reading);
    }
    String sensorType = assignmentParts[0];
    double value = parseDoubleOrError(valueParts[0], "Invalid sensor value: " + valueParts[0]);
    String unit = valueParts[1];
    return new SensorReading(sensorType, value, unit);
  }
}
