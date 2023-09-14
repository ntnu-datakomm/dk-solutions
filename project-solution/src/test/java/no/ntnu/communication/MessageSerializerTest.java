package no.ntnu.communication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import no.ntnu.greenhouse.Actuator;
import no.ntnu.greenhouse.SensorActuatorNode;
import org.junit.Test;

public class MessageSerializerTest {

  @Test
  public void emptyNodeType() {
    SensorActuatorNode node = new SensorActuatorNode(44);
    expectSensorNodeType("type=sensor:44", node);
  }

  @Test
  public void nodeTypeWithOneActuator() {
    SensorActuatorNode node = new SensorActuatorNode(667);
    node.addActuator(new Actuator(13, "temperature", 667));
    expectSensorNodeType("type=sensor:667;temperature=13", node);
  }

  @Test
  public void nodeTypeWithMultipleActuators() {
    SensorActuatorNode node = new SensorActuatorNode(667);
    node.addActuator(new Actuator(13, "temperature", 667));
    node.addActuator(new Actuator(12, "fan", 667));
    expectSensorNodeType("type=sensor:667;fan=12,temperature=13", node);
  }

  @Test
  public void nodeTypeWithMultipleActuatorsOfSameType() {
    SensorActuatorNode node = new SensorActuatorNode(667);
    node.addActuator(new Actuator(13, "temperature", 667));
    node.addActuator(new Actuator(14, "temperature", 667));
    node.addActuator(new Actuator(15, "fan", 667));
    expectSensorNodeType("type=sensor:667;temperature=13,temperature=14,fan=15", node);

    node.addActuator(new Actuator(2, "temperature", 667));
    expectSensorNodeType("type=sensor:667;temperature=2,temperature=13,temperature=14,fan=15", node);
  }

  @Test
  public void fromStringToEmptyNodeType() {
    Message m = MessageSerializer.fromString("type=sensor:44");
    assertTrue(m instanceof SensorNodeTypeMessage);
    SensorNodeTypeMessage sntm = (SensorNodeTypeMessage) m;
    assertEquals(44, sntm.getNodeId());
    assertEquals(0, sntm.getActuators().size());
  }

  @Test
  public void fromStringToSingleActuatorNode() {
    Message m = MessageSerializer.fromString("type=sensor:667;temperature=13");
    assertTrue(m instanceof SensorNodeTypeMessage);
    SensorNodeTypeMessage sntm = (SensorNodeTypeMessage) m;
    assertEquals(667, sntm.getNodeId());
    assertEquals(1, sntm.getActuators().size());
    Actuator a = sntm.getActuators().iterator().next();
    assertEquals(13, a.getId());
    assertEquals("temperature", a.getType());
    assertEquals(667, a.getNodeId());
  }

  @Test
  public void fromStringToMultiActuatorNode() {
    Message m = MessageSerializer.fromString(
        "type=sensor:667;temperature=2,temperature=13,temperature=15,fan=14");
    assertTrue(m instanceof SensorNodeTypeMessage);
    SensorNodeTypeMessage sntm = (SensorNodeTypeMessage) m;
    assertEquals(667, sntm.getNodeId());
    assertEquals(4, sntm.getActuators().size());

    Iterator<Actuator> it = sntm.getActuators().iterator();
    Actuator a = it.next();
    assertEquals(2, a.getId());
    assertEquals("temperature", a.getType());
    assertEquals(667, a.getNodeId());

    a = it.next();
    assertEquals(13, a.getId());
    assertEquals("temperature", a.getType());
    assertEquals(667, a.getNodeId());

    a = it.next();
    assertEquals(14, a.getId());
    assertEquals("fan", a.getType());
    assertEquals(667, a.getNodeId());

    a = it.next();
    assertEquals(15, a.getId());
    assertEquals("temperature", a.getType());
    assertEquals(667, a.getNodeId());
  }

  private static void expectSensorNodeType(String expectedResult, SensorActuatorNode node) {
    SensorNodeTypeMessage message = new SensorNodeTypeMessage(node);
    assertEquals(expectedResult, MessageSerializer.toString(message));
  }

}
