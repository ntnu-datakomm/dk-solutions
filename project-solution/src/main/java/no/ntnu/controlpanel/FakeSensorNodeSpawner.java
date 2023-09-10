package no.ntnu.controlpanel;

import java.util.Timer;
import java.util.TimerTask;
import no.ntnu.greenhouse.Actuator;

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
      throw new IllegalArgumentException("Nose specification can't be empty");
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
   * @param specification A (temporary) manual configuration of the node in the following format
   *                      [nodeId] semicolon
   *                      [actuator_count_1] underscore [actuator_type_1] space ...
   *                      [actuator_count_M] underscore [actuator_type_M]
   * @param delay         Delay in seconds
   */
  public void spawn(String specification, int delay) {
    SensorActuatorNodeInfo nodeInfo = createSensorNodeInfoFrom(specification);
    Timer timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        timer.cancel();
        listener.onNodeSpawned(nodeInfo);
      }
    }, delay * 1000L);
  }
}
