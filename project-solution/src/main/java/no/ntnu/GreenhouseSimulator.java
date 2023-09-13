package no.ntnu;

import no.ntnu.greenhouse.DeviceFactory;
import no.ntnu.greenhouse.Greenhouse;
import no.ntnu.greenhouse.PeriodicSwitch;
import no.ntnu.greenhouse.SensorActuatorNode;
import no.ntnu.tools.Logger;

/**
 * Application entrypoint - a simulator for a greenhouse.
 */
public class GreenhouseSimulator {
  private final Greenhouse greenhouse = new Greenhouse();
  private final SensorActuatorNode[] nodes = new SensorActuatorNode[3];

  /**
   * Initialise the greenhouse but don't start the simulation just yet.
   */
  public void initialize() {
    nodes[0] = DeviceFactory.createNode(1, 2, 1, 0, 0);
    nodes[1] = DeviceFactory.createNode(1, 0, 0, 2, 1);
    nodes[2] = DeviceFactory.createNode(2, 0, 0, 0, 0);

    for (SensorActuatorNode node : nodes) {
      greenhouse.addNode(node);
    }
    Logger.info("Greenhouse initialized");
  }

  /**
   * Start a simulation of a greenhouse - all the sensor and actuator nodes inside it.
   */
  public void start() {
    greenhouse.addPeriodicSwitch(new PeriodicSwitch("Heater DJ", nodes[1], "heater", 0, 8000));
    greenhouse.addPeriodicSwitch(new PeriodicSwitch("Window DJ", nodes[0], "window", 0, 20000));

    greenhouse.startSimulation();
    Logger.info("Simulator started");
  }

  /**
   * Stop the simulation of the greenhouse - all the nodes in it.
   */
  public void stop() {
    greenhouse.stopSimulation();
  }

  /**
   * Get the associated greenhouse.
   *
   * @return The greenhouse with all the sensor/actuator nodes
   */
  public Greenhouse getGreenhouse() {
    return greenhouse;
  }

}
