package no.ntnu.controlpanel;

import no.ntnu.tools.Logger;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Spawns a fake sensor/actuator node information. Emulates the node discovery (over the Internet).
 */
public class FakeSensorNodeSpawner {


  private final SensorActuatorNodeInfo nodeInfo;

  public FakeSensorNodeSpawner(String specification) {
    this.nodeInfo = createSensorNodeInfoFrom(specification);
  }

  private SensorActuatorNodeInfo createSensorNodeInfoFrom(String specification) {
    String[] parts = specification.split(";");
    if (parts.length < 2) {
      throw new IllegalArgumentException("specification must contain at least node ID and sensors");
    }
    if (parts.length > 3) {
      throw new IllegalArgumentException("Incorrect specification format");
    }
    Integer nodeId = parseIntegerOrError(parts[0], "Invalid node ID:" + parts[0]);
    SensorActuatorNodeInfo info = new SensorActuatorNodeInfo(nodeId);
    parseSensors(parts[1], info);
    if (parts.length == 3) {
      parseActuators(parts[2], info);
    }
    return info;
  }

  private void parseSensors(String sensorSpecification, SensorActuatorNodeInfo info) {
    String[] parts = sensorSpecification.split(" ");
    for (String part : parts) {
      parseSensorInfo(part, info);
    }
  }

  private void parseSensorInfo(String s, SensorActuatorNodeInfo info) {
    String[] sensorInfo = s.split("_");
    if (sensorInfo.length != 2) {
      throw new IllegalArgumentException("Invalid sensor info format: " + s);
    }
    Integer sensorCount = parseIntegerOrError(sensorInfo[0],
        "Invalid sensor count: " + sensorInfo[0]);
    String sensorType = sensorInfo[1];
    info.addSensors(sensorType, sensorCount);
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
    Integer actuatorCount = parseIntegerOrError(actuatorInfo[0],
        "Invalid actuator count: " + actuatorInfo[0]);
    String actuatorType = actuatorInfo[1];
    info.addActuators(actuatorType, actuatorCount);
  }

  private Integer parseIntegerOrError(String s, String errorMessage) {
    try {
      return Integer.valueOf(s);
    } catch (NumberFormatException e) {
      throw new NumberFormatException(errorMessage);
    }
  }

  /**
   * Spawn a new sensor/actuator node information after a given delay.
   *
   * @param delay The delay in seconds
   */
  public void spawnAfter(long delay) {
    Timer timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        Logger.info("Spawning node " + nodeInfo);
        timer.cancel();
      }
    }, delay * 1000);
  }
}
