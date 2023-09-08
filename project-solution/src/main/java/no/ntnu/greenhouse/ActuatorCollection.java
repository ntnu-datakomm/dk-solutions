package no.ntnu.greenhouse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import no.ntnu.tools.Logger;

/**
 * A collection of actuators of different types.
 */
public class ActuatorCollection implements Iterable<Actuator> {
  private final Map<String, List<Actuator>> actuators = new HashMap<>();

  /**
   * Add an actuator to the collection. Duplicates are discarded.
   *
   * @param actuator The actuator to add
   */
  public void add(Actuator actuator) {
    List<Actuator> actuatorsOfThatType = getActuatorsOfGivenType(actuator.getType());
    if (!actuatorsOfThatType.contains(actuator)) {
      actuatorsOfThatType.add(actuator);
    }
  }

  private List<Actuator> getActuatorsOfGivenType(String type) {
    return actuators.computeIfAbsent(type, k -> new ArrayList<>());
  }

  /**
   * Get the i-th actuator of a given type.
   *
   * @param type The type of the actuator
   * @param i    The index of the actuator in the list of all actuators with the given type.
   *             Indexing starts at zero.
   * @return The desired actuator or null if it is not found
   */
  public Actuator get(String type, int i) {
    Actuator actuator = null;
    List<Actuator> actuatorsOfThatType = actuators.get(type);
    if (actuatorsOfThatType != null && i >= 0 && i < actuatorsOfThatType.size()) {
      actuator = actuatorsOfThatType.get(i);
    }
    return actuator;
  }

  @Override
  public Iterator<Actuator> iterator() {
    return actuators.values().stream().flatMap(List::stream).iterator();
  }

  /**
   * Print a short info about all the actuators. Usable for debugging. Does NOT print a newline!
   */
  public void debugPrint() {
    for (List<Actuator> actuatorList : actuators.values()) {
      for (Actuator actuator : actuatorList) {
        Logger.infoNoNewline(" " + actuator.getType() + (actuator.isOn() ? " ON" : " off"));
      }
    }
  }
}
