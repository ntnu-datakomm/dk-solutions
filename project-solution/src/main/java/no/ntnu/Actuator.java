package no.ntnu;

import java.util.HashMap;
import java.util.Map;

/**
 * An actuator that can change the environment in a way. The actuator will make impact on the
 * sensors attached to this same node in full scale (100% impact). In addition, it will have a
 * partial impact to respective sensors on other nodes.
 */
public class Actuator {
  private final String type;
  private Map<String, Double> impacts = new HashMap<>();

  private boolean on;

  /**
   * Create an actuator.
   *
   * @param type   The type of the actuator.
   */
  public Actuator(String type) {
    this.type = type;
    this.on = false;
  }

  /**
   * Register the impact of this actuator when active.
   *
   * @param sensorType     Which type of sensor readings will be impacted. Example: "temperature"
   * @param diffWhenActive What will be the introduced difference in the sensor reading when
   *                       the actuator is active. For example, if this value is 2.0 and the
   *                       sensorType is "temperature", this means that "activating this actuator
   *                       will increase the readings of temperature sensors attached to the
   *                       same node by +2 degrees".
   */
  public void setImpact(String sensorType, double diffWhenActive) {
    impacts.put(sensorType, diffWhenActive);
  }

  public String getType() {
    return type;
  }

  /**
   * Create a clone of this actuator.
   *
   * @return A clone of this actuator, where all the fields are the same
   */
  public Actuator clone() {
    Actuator a = new Actuator(this.type);
    // Note - we pass a reference to the same map! This should not be problem, as long as we
    // don't modify the impacts AFTER creating the template
    a.impacts = this.impacts;
    return a;
  }

  public void toggle() {
    // TODO - make impact on relevant sensors
    this.on = !this.on;
  }

  /**
   * Check whether the actuator is active (ON), or inactive (OFF).
   *
   * @return True when the actuator is ON, false if it is OFF
   */
  public boolean isOn() {
    return on;
  }
}
