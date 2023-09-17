package no.ntnu.communication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import no.ntnu.communication.message.Message;
import no.ntnu.communication.message.MessageSerializer;
import no.ntnu.communication.message.SensorDataMessage;
import no.ntnu.communication.message.SensorNodeTypeMessage;
import no.ntnu.greenhouse.Actuator;
import no.ntnu.greenhouse.SensorActuatorNode;
import no.ntnu.greenhouse.SensorReading;
import no.ntnu.tools.Parser;
import org.junit.Test;

/**
 * Tests for the Message serializer.
 */
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
    expectSensorNodeType("type=sensor:667;temperature=2,temperature=13,temperature=14,fan=15",
        node);
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

  @Test
  public void fromStringToInvalidNodeId() {
    assertNull(MessageSerializer.fromString("sensors:"));
    assertNull(MessageSerializer.fromString("sensors:dddd;temperature,12,C"));
    assertNull(MessageSerializer.fromString(
        "type=sensor:ddd;temperature=2,temperature=13,temperature=15,fan=14"));
    assertNull(MessageSerializer.fromString(
        "type=sensor:;temperature=2,temperature=13,temperature=15,fan=14"));
  }

  @Test
  public void fromStringToEmptySensorData() {
    assertNull(MessageSerializer.fromString("sensors:12"));
    assertNull(MessageSerializer.fromString("sensors:12;"));
  }

  @Test
  public void fromStringToInvalidSensorData() {
    assertNull(MessageSerializer.fromString("sensors:12;temperature,27"));
    assertNull(MessageSerializer.fromString("sensors:12;temperature,C"));
    assertNull(MessageSerializer.fromString("sensors:12;24,C"));
    assertNull(MessageSerializer.fromString("sensors:12;temperature,ddd,C"));
    assertNull(MessageSerializer.fromString("sensors:12;24"));
    assertNull(MessageSerializer.fromString("sensors:12;temperature,27,C;humidity,78"));
  }

  @Test
  public void fromStringToOneSensorReading() {
    expectSensorData("sensors:12;temperature,27,C", "temperature", "27", "C");
  }

  @Test
  public void fromStringToMultipleSensorReadings() {
    expectSensorData(
        "sensors:12;temperature,27,C;humidity,80,%",
        "temperature", "27", "C",
        "humidity", "80", "%"
    );
    expectSensorData(
        "sensors:12;temperature,27,C;humidity,80,%;temperature,24,C",
        "temperature", "27", "C",
        "humidity", "80", "%",
        "temperature", "24", "C"
    );
  }

  private static void expectSensorNodeType(String expectedResult, SensorActuatorNode node) {
    SensorNodeTypeMessage message = new SensorNodeTypeMessage(node);
    assertEquals(expectedResult, MessageSerializer.toString(message));
  }

  /**
   * Expect that the given message is deserialized to a sensor-data message with the expected
   * sensor readings in it.
   *
   * @param message                The original message, as a string.
   * @param expectedSensorReadings The expected sensor reading. Each sensor reading is represented
   *                               as three strings: type, value and unit.
   */
  private void expectSensorData(String message, String... expectedSensorReadings) {
    Message m = MessageSerializer.fromString(message);
    assertNotNull(m);
    assertTrue(m instanceof SensorDataMessage);
    Iterator<SensorReading> iterator = ((SensorDataMessage) m).getSensors().iterator();
    for (int i = 0; i < expectedSensorReadings.length; i += 3) {
      String expectedType = expectedSensorReadings[i];
      double expectedValue = Parser.parseDoubleOrError(expectedSensorReadings[i + 1],
          "Invalid value: " + expectedSensorReadings[i + 1]);
      String expectedUnit = expectedSensorReadings[i + 2];
      SensorReading expectedReading = new SensorReading(expectedType, expectedValue, expectedUnit);
      assertTrue(iterator.hasNext());
      SensorReading realReading = iterator.next();
      assertEquals(expectedReading, realReading);
    }
  }

}
