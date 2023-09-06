package no.ntnu;

/**
 * Application entrypoint - a simulator for a greenhouse.
 */
public class GreenhouseSimulator {
  private static int nextId = 1;

  /**
   * Main entrypoint of the simulator.
   *
   * @param args Command-line arguments, not used.
   */
  public static void main(String[] args) {
    Greenhouse greenhouse = new Greenhouse();
    greenhouse.addNode(createNode(1, 2, 1, 0, 0));
    greenhouse.addNode(createNode(1, 0, 0, 2, 1));
    greenhouse.addNode(createNode(2, 0, 0, 0, 0));
  }

  private static SensorActuatorNode createNode(int temperatureSensorCount, int humiditySensorCount,
                                               int windowCount, int fanCount, int heaterCount) {
    SensorActuatorNode node = new SensorActuatorNode(generateUniqueId());
    if (temperatureSensorCount > 0) {
      node.addSensors(DeviceFactory.createTemperatureSensor(), temperatureSensorCount);
    }
    if (humiditySensorCount > 0) {
      node.addSensors(DeviceFactory.createHumiditySensor(), humiditySensorCount);
    }
    if (windowCount > 0) {
      node.addActuators(DeviceFactory.createWindow(node.getId()), windowCount);
    }
    if (fanCount > 0) {
      node.addActuators(DeviceFactory.createFan(node.getId()), fanCount);
    }
    if (heaterCount > 0) {
      node.addActuators(DeviceFactory.createHeater(node.getId()), heaterCount);
    }
    return node;
  }

  /**
   * Generate an integer that can be used as a unique ID of sensor/actuator nodes.
   *
   * @return a Unique ID for sensor/actuator nodes
   */
  private static int generateUniqueId() {
    return nextId++;
  }
}
