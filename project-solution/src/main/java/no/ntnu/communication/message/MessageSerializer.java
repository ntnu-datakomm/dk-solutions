package no.ntnu.communication.message;

import java.util.LinkedList;
import java.util.List;
import no.ntnu.greenhouse.Actuator;
import no.ntnu.greenhouse.ActuatorCollection;
import no.ntnu.greenhouse.SensorReading;
import no.ntnu.tools.Logger;
import no.ntnu.tools.Parser;

/**
 * Translates message objects to and from strings which are sent over the socket.
 */
public class MessageSerializer {
  private static final String CONTROL_NODE_TYPE_MESSAGE = "type=control";

  /**
   * Not allowed to create instances of this class.
   */
  private MessageSerializer() {
  }

  /**
   * Deserialize a message from a string.
   *
   * @param s The string sent over the TCP socket, according to the protocol
   * @return The deserialized message, null on error
   */
  public static Message fromString(String s) {
    Logger.info("Deserialize " + s);
    if (s == null) {
      return null;
    }

    Message message = null;
    try {
      if (s.startsWith("type=sensor:")) {
        message = parseSensorNodeTypeMessage(s);
      } else if (s.equals(CONTROL_NODE_TYPE_MESSAGE)) {
        message = new ControlNodeTypeMessage();
      } else if (s.startsWith("sensors:")) {
        message = parseSensorDataMessage(s);
      } else {
        Logger.error("  Unknown message, can't deserialize!");
      }
    } catch (NumberFormatException e) {
      Logger.error("Number error while deserializing message `" + s + "`: " + e.getMessage());
    } catch (IllegalArgumentException e) {
      Logger.error("Error while deserializing message `" + s + "`: " + e.getMessage());
    }

    return message;
  }

  private static SensorNodeTypeMessage parseSensorNodeTypeMessage(String s) {
    int nodeId = parseSensorNodeId(s);
    SensorNodeTypeMessage nodeTypeMessage = new SensorNodeTypeMessage(nodeId);
    parseActuators(s, nodeId, nodeTypeMessage);
    return nodeTypeMessage;
  }

  private static void parseActuators(String s, int nodeId, SensorNodeTypeMessage m) {
    int semicolonPosition = s.indexOf(";");
    if (semicolonPosition > 0) {
      String actuatorSpecification = s.substring(semicolonPosition + 1);
      String[] actuatorParts = actuatorSpecification.split(",");
      for (String actuatorPart : actuatorParts) {
        m.addActuator(parseActuator(actuatorPart, nodeId));
      }
    }
  }

  private static Actuator parseActuator(String actuatorPart, int nodeId) {
    String[] typeAndId = actuatorPart.split("=");
    if (typeAndId.length != 2) {
      throw new IllegalArgumentException("Invalid actuator specification: " + actuatorPart);
    }
    String type = typeAndId[0];
    int id = Parser.parseIntegerOrError(typeAndId[1], "Invalid actuator ID: " + actuatorPart);
    return new Actuator(id, type, nodeId);
  }

  private static int parseSensorNodeId(String s) {
    int colonPosition = s.indexOf(":");
    if (colonPosition < 0) {
      throw new NumberFormatException(": not found, can't find node ID");
    }
    int semicolonPosition = s.indexOf(";");
    if (semicolonPosition < 0) {
      semicolonPosition = s.length();
    }
    String nodeIdString = s.substring(colonPosition + 1, semicolonPosition);
    return Parser.parseIntegerOrError(nodeIdString, "Wrong node Id: `" + nodeIdString + "`");
  }


  private static Message parseSensorDataMessage(String s) throws IllegalArgumentException {
    Message message = null;
    int nodeId = parseSensorNodeId(s);
    List<SensorReading> sensorReadings = parseSensorData(s);
    if (sensorReadings != null && !sensorReadings.isEmpty()) {
      message = new SensorDataMessage(sensorReadings, nodeId);
    }
    return message;
  }

  private static List<SensorReading> parseSensorData(String s) throws IllegalArgumentException {
    int semicolonPosition = s.indexOf(";");
    List<SensorReading> sensorReadings = null;
    if (semicolonPosition > 0 && semicolonPosition != s.length() - 1) {
      sensorReadings = new LinkedList<>();
      String sensorSpecification = s.substring(semicolonPosition + 1);
      String[] sensorParts = sensorSpecification.split(";");
      for (String oneSensorReading : sensorParts) {
        sensorReadings.add(parseSensorReading(oneSensorReading));
      }
    }
    return sensorReadings;
  }

  private static SensorReading parseSensorReading(String oneSensorReading)
      throws IllegalArgumentException {
    String[] readingParts = oneSensorReading.split(",");
    if (readingParts.length != 3) {
      throw new IllegalArgumentException("Invalid sensor reading: `" + oneSensorReading + "`");
    }
    String type = readingParts[0];
    double value = Parser.parseDoubleOrError(readingParts[1], "Invalid sensor value: "
        + readingParts[1]);
    String unit = readingParts[2];

    return new SensorReading(type, value, unit);
  }

  /**
   * Serialize a message to a string, according to the protocol.
   *
   * @param message The message to serialize
   * @return A string which can be sent over the TCP socket
   */
  public static String toString(Message message) {
    String result;
    if (message instanceof SensorNodeTypeMessage sensorNodeTypeMessage) {
      result = serializeSensorNodeTypeMessage(sensorNodeTypeMessage);
    } else if (message instanceof ControlNodeTypeMessage) {
      result = CONTROL_NODE_TYPE_MESSAGE;
    } else if (message instanceof SensorDataMessage sensorDataMessage) {
      result = serializeSensorDataMessage(sensorDataMessage);
    } else {
      throw new UnsupportedOperationException("Can't serialize " + message.getClass().getName());
    }
    return result;
  }

  private static String serializeSensorNodeTypeMessage(SensorNodeTypeMessage message) {
    String header = "type=sensor:" + message.getNodeId();
    String actuatorSection = serializeActuators(message.getActuators());
    String result = header;
    if (!actuatorSection.isEmpty()) {
      result += ";" + actuatorSection;
    }
    return result;
  }

  private static String serializeActuators(ActuatorCollection actuators) {
    StringBuilder result = new StringBuilder();
    for (Actuator actuator : actuators) {
      if (!result.isEmpty()) {
        result.append(",");
      }
      result.append(actuator.getType())
          .append("=")
          .append(actuator.getId());
    }
    return result.toString();
  }


  private static String serializeSensorDataMessage(SensorDataMessage message) {
    String header = "sensors:" + message.getNodeId();
    StringBuilder data = new StringBuilder();
    for (SensorReading sensor : message.getSensors()) {
      data.append(";")
          .append(sensor.getType())
          .append(",")
          .append(sensor.getValue())
          .append(",")
          .append(sensor.getUnit());
    }
    return header + data;
  }

}
