package no.ntnu.greenhouse;

import no.ntnu.tools.GroupedItemCollection;
import no.ntnu.tools.Logger;

/**
 * A collection of actuators of different types.
 */
public class ActuatorCollection extends GroupedItemCollection<Actuator> {
  /**
   * Print a short info about all the actuators. Usable for debugging. Does NOT print a newline!
   */
  public void debugPrint() {
    for (Actuator actuator : this) {
      Logger.infoNoNewline(" " + actuator.getType() + (actuator.isOn() ? " ON" : " off"));
    }
  }

  /**
   * Add an actuator to the collection.
   *
   * @param actuator The actuator to add.
   */
  public void add(Actuator actuator) {
    super.add(actuator.getType(), actuator);
  }
}
