package no.ntnu.greenhouse;

/**
 * Listener for actuator state changes.
 */
public interface ActuatorListener {
  /**
   * An event that is fired every time an actuator changes state.
   *
   * @param actuator The actuator that has changed its state
   */
  void actuatorUpdated(Actuator actuator);
}
