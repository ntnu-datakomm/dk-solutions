package no.ntnu;

/**
 * Application entrypoint - a simulator for a greenhouse.
 */
public class GreenhouseSimulator {
  /**
   * Main entrypoint of the simulator.
   *
   * @param args Command-line arguments, not used.
   */
  public static void main(String[] args) {
    Greenhouse greenhouse = new Greenhouse();
    SensorActuatorNode node1 = DeviceFactory.createNode(greenhouse, 1, 2, 1, 0, 0);
    greenhouse.addNode(node1);
    SensorActuatorNode node2 = DeviceFactory.createNode(greenhouse, 1, 0, 0, 2, 1);
    greenhouse.addNode(node2);
    greenhouse.addNode(DeviceFactory.createNode(greenhouse, 2, 0, 0, 0, 0));

    greenhouse.addPeriodicActuator(new PeriodicActuator(node2, "heater", 0, 8000));
    greenhouse.addPeriodicActuator(new PeriodicActuator(node1, "window", 0, 20000));

    greenhouse.startSimulation();
  }

}
