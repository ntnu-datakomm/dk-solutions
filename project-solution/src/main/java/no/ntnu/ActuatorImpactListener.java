package no.ntnu;

/**
 * A listener which watches events related to turning on and off actuators.
 */
public interface ActuatorImpactListener {
  /**
   * This event is fired when an actuator is toggled.
   *
   * @param actuator The actuator that was toggled
   */
  void onActuatorToggled(Actuator actuator);
}
