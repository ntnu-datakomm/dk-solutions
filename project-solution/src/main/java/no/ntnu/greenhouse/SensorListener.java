package no.ntnu.greenhouse;

import java.util.List;

/**
 * Listener for sensor update events.
 */
public interface SensorListener {
  /**
   * An event that is fired every time sensor values are updated.
   *
   * @param sensors A list of sensors having new values (readings)
   */
  void sensorsUpdated(List<Sensor> sensors);
}
