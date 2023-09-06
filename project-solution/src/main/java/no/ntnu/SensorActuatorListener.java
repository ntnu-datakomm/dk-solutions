package no.ntnu;

import java.util.List;

/**
 * Listener for sensor update events.
 */
public interface SensorActuatorListener {
  /**
   * An event that is fired every time sensor values are updated.
   *
   * @param sensors A list of sensors having new values (readings)
   */
  void sensorsUpdated(List<Sensor> sensors);

  /**
   * An event that is fired every time an actuator changes state.
   *
   * @param actuator The actuator that has changed its state
   */
  void actuatorUpdated(Actuator actuator);
}
