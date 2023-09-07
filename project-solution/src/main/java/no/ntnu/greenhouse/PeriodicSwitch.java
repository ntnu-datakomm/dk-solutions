package no.ntnu.greenhouse;

import java.util.Timer;
import java.util.TimerTask;
import no.ntnu.tools.Logger;

/**
 * A dummy switch which periodically turns an actuator on and off. Used for manual testing.
 */
public class PeriodicSwitch {
  private final Timer timer;
  private final SensorActuatorNode node;
  private final String actuatorType;
  private final int actuatorIndex;
  private final long delay;
  private final String name;

  /**
   * Create a periodic switcher.
   *
   * @param name          Name of the switch, used for debugging
   * @param node          The associated actuator node
   * @param actuatorType  The type of sensor to actuate
   * @param actuatorIndex The index of the actuator (of that specific type) to actuate
   * @param m             The actuator will be turned on and off every m milliseconds
   */
  public PeriodicSwitch(String name, SensorActuatorNode node, String actuatorType,
                        int actuatorIndex, long m) {
    this.node = node;
    this.actuatorType = actuatorType;
    this.actuatorIndex = actuatorIndex;
    this.delay = m;
    this.name = name;

    timer = new Timer(name);
  }

  /**
   * Start the periodic actuator toggling.
   */
  public void start() {
    timer.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        Logger.info(" > " + name + ": toggle " + actuatorType + "[" + actuatorIndex + "] on node "
            + node.getId());
        if (node.isRunning()) {
          try {
            node.toggleActuator(actuatorType, actuatorIndex);
          } catch (Exception e) {
            Logger.error("Failed to toggle an actuator: " + e.getMessage());
            timer.cancel();
          }
        } else {
          Logger.info("   Node stopped, stopping the switch");
          timer.cancel();
        }
      }
    }, delay, delay);
  }

  /**
   * Stop the periodic actuator toggling.
   */
  public void stop() {
    Logger.info("-- Stopping " + this.name);
    timer.cancel();
  }
}
