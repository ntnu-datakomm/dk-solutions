package no.ntnu.communication.message;

import java.util.List;
import no.ntnu.greenhouse.SensorReading;

/**
 * A message containing sensor data.
 */
public class SensorDataMessage implements Message {
  private final List<SensorReading> sensors;
  private final int nodeId;

  public SensorDataMessage(List<SensorReading> sensors, int nodeId) {
    this.sensors = sensors;
    this.nodeId = nodeId;
  }

  public int getNodeId() {
    return nodeId;
  }

  public Iterable<SensorReading> getSensors() {
    return sensors;
  }

  @Override
  public String toString() {
    return "SensorDataMessage{nodeId=" + nodeId + ", " + sensors.size() + " sensors}";
  }
}
