import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;
import no.ntnu.greenhouse.Actuator;
import no.ntnu.greenhouse.ActuatorCollection;
import org.junit.Test;

public class ActuatorCollectionTest {
  @Test
  public void testEmptyIterator() {
    ActuatorCollection a = new ActuatorCollection();
    assertFalse(a.iterator().hasNext());
  }

  @Test
  public void testIteratorOfSingleType() {
    ActuatorCollection a = new ActuatorCollection();
    Actuator fan1 = new Actuator("fan", 1);
    Actuator fan2 = new Actuator("fan", 1);
    Actuator fan3 = new Actuator("fan", 1);
    a.add(fan1);
    a.add(fan2);
    a.add(fan3);
    Set<Actuator> actuators = getAllActuators(a);
    assertEquals(3, actuators.size());
    assertTrue(actuators.contains(fan1));
    assertTrue(actuators.contains(fan2));
    assertTrue(actuators.contains(fan3));
  }

  @Test
  public void testIteratorOfMultipleTypes() {
    ActuatorCollection a = new ActuatorCollection();
    Actuator fan1 = new Actuator("fan", 1);
    Actuator fan2 = new Actuator("fan", 1);
    Actuator fan3 = new Actuator("fan", 1);
    Actuator window1 = new Actuator("window", 1);
    Actuator window2 = new Actuator("window", 1);
    Actuator heater1 = new Actuator("heater", 1);
    Actuator heater2 = new Actuator("heater", 1);
    Actuator heater3 = new Actuator("heater", 1);
    a.add(fan1);
    a.add(fan2);
    a.add(window1);
    a.add(heater1);
    a.add(fan3);
    a.add(window2);
    a.add(heater2);
    a.add(heater3);

    Set<Actuator> actuators = getAllActuators(a);
    assertEquals(8, actuators.size());
    assertTrue(actuators.contains(fan1));
    assertTrue(actuators.contains(fan2));
    assertTrue(actuators.contains(fan3));
    assertTrue(actuators.contains(window1));
    assertTrue(actuators.contains(window2));
    assertTrue(actuators.contains(heater1));
    assertTrue(actuators.contains(heater2));
    assertTrue(actuators.contains(heater3));
  }

  private Set<Actuator> getAllActuators(ActuatorCollection collection) {
    Set<Actuator> actuators = new HashSet<>();
    for (Actuator a : collection) {
      actuators.add(a);
    }
    return actuators;
  }
}
