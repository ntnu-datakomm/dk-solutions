package no.ntnu.greenhouse;

import java.util.LinkedList;
import java.util.List;
import no.ntnu.tools.Logger;

/**
 * Application entrypoint - a simulator for a greenhouse.
 */
public class GreenhouseSimulator {
  private final Greenhouse greenhouse = new Greenhouse();
  private final SensorActuatorNode[] nodes = new SensorActuatorNode[3];

  private final List<PeriodicSwitch> periodicSwitches = new LinkedList<>();

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
    initializePeriodicSwitches();
    greenhouse.start();
    for (PeriodicSwitch periodicSwitch : periodicSwitches) {
      periodicSwitch.start();
    }

    Logger.info("Simulator started");
  }

  private void initializePeriodicSwitches() {
    periodicSwitches.add(new PeriodicSwitch("Heater DJ", nodes[1], 7, 8000));
    periodicSwitches.add(new PeriodicSwitch("Window DJ", nodes[0], 2, 20000));
  }

  /**
   * Stop the simulation of the greenhouse - all the nodes in it.
   */
  public void stop() {
    for (PeriodicSwitch periodicSwitch : periodicSwitches) {
      periodicSwitch.stop();
    }
    greenhouse.stop();
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
