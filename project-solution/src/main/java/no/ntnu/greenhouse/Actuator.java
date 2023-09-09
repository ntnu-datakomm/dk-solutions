package no.ntnu.greenhouse;

import java.util.HashMap;
import java.util.Map;

/**
 * An actuator that can change the environment in a way. The actuator will make impact on the
 * sensors attached to this same node in full scale (100% impact). In addition, it will have a
 * partial impact to respective sensors on other nodes.
 */
public class Actuator {
  // How much an actuator impacts sensors connected to other nodes
  // (which can be far away from the actuator node)
  private static final double PARTIAL_IMPACT_FACTOR = 0.2;
  private final String type;
  private final SensorActuatorNode node;
  private Map<String, Double> impacts = new HashMap<>();

  private boolean on;

  /**
   * Create an actuator.
   *
   * @param type The type of the actuator.
   * @param node The node to which this actuator is connected. Can be null. When non-null, the node
   *             will get notifications when the actuator changes.
   */
  public Actuator(String type, SensorActuatorNode node) {
    this.type = type;
    this.node = node;
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
  public Actuator createClone() {
    Actuator a = new Actuator(type, node);
    // Note - we pass a reference to the same map! This should not be problem, as long as we
    // don't modify the impacts AFTER creating the template
    a.impacts = impacts;
    return a;
  }

  /**
   * Toggle the actuator - if it was off, not it will be ON, and vice versa.
   */
  public void toggle() {
    this.on = !this.on;
    notifyChanges();
  }

  private void notifyChanges() {
    if (node != null) {
      node.notifyActuatorChange(this);
    }
  }

  /**
   * Check whether the actuator is active (ON), or inactive (OFF).
   *
   * @return True when the actuator is ON, false if it is OFF
   */
  public boolean isOn() {
    return on;
  }

  /**
   * Apply impact of this actuator to all sensors of one specific sensor node.
   *
   * @param node The sensor node to affect by this actuator.
   */
  public void applyImpact(SensorActuatorNode node) {
    double impactFactor = getImpactFactorFor(node);
    for (Map.Entry<String, Double> impactEntry : impacts.entrySet()) {
      String sensorType = impactEntry.getKey();
      double impact = impactEntry.getValue() * impactFactor;
      if (!on) {
        impact = -impact;
      }
      node.applyActuatorImpact(sensorType, impact);
    }
  }

  /**
   * Get the factor of impact of this actuator to the given sensor node.
   *
   * @param node The node to check
   * @return The factor to use when applying impact of this actuator to the given node
   */
  private double getImpactFactorFor(SensorActuatorNode node) {
    if (this.node == null) {
      throw new IllegalStateException("Can't apply impact, actuator not connected to any node");
    }
    return this.node.getId() == node.getId() ? 1.0 : PARTIAL_IMPACT_FACTOR;
  }

  @Override
  public String toString() {
    return "Actuator{"
        + "type='" + type + '\''
        + ", on=" + on
        + '}';
  }

  /**
   * Turn on the actuator.
   */
  public void turnOn() {
    if (!on) {
      on = true;
      node.notifyActuatorChange(this);
    }
  }

  /**
   * Turn off the actuator.
   */
  public void turnOff() {
    if (on) {
      on = false;
      node.notifyActuatorChange(this);
    }
  }
}
