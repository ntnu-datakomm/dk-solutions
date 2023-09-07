package no.ntnu;

import no.ntnu.greenhouse.DeviceFactory;
import no.ntnu.greenhouse.Greenhouse;
import no.ntnu.greenhouse.PeriodicActuator;
import no.ntnu.greenhouse.SensorActuatorNode;
import no.ntnu.tools.Logger;

/**
 * Application entrypoint - a simulator for a greenhouse.
 */
public class GreenhouseSimulator {
  /**
   * Start a simulation of a greenhouse - all the sensor and actuator nodes inside it.
   */
  public void start() {
    Greenhouse greenhouse = new Greenhouse();
    SensorActuatorNode node1 = DeviceFactory.createNode(1, 2, 1, 0, 0);
    SensorActuatorNode node2 = DeviceFactory.createNode(1, 0, 0, 2, 1);
    SensorActuatorNode node3 = DeviceFactory.createNode(2, 0, 0, 0, 0);

    greenhouse.addNode(node1);
    greenhouse.addNode(node2);
    greenhouse.addNode(node3);

    node1.addActuatorListener(greenhouse);
    node2.addActuatorListener(greenhouse);
    node3.addActuatorListener(greenhouse);

    greenhouse.addPeriodicActuator(new PeriodicActuator(node2, "heater", 0, 8000));
    greenhouse.addPeriodicActuator(new PeriodicActuator(node1, "window", 0, 20000));

    greenhouse.startSimulation();
    Logger.info("Simulator started");
  }

}
