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
    greenhouse.addNode(DeviceFactory.createNode(1, 2, 1, 0, 0));
    SensorActuatorNode node2 = DeviceFactory.createNode(1, 0, 0, 2, 1);
    greenhouse.addNode(node2);
    greenhouse.addNode(DeviceFactory.createNode(2, 0, 0, 0, 0));

    greenhouse.addPeriodicActuator(new PeriodicActuator(node2, "fan", 0, 8000));
    greenhouse.addPeriodicActuator(new PeriodicActuator(node2, "window", 10, 1000));

    greenhouse.startSimulation();
  }

}
