package no.ntnu.communication.message;

import no.ntnu.greenhouse.SensorReading;

/**
 * A message containing sensor data.
 */
public class SensorDataMessage implements Message {
  private final Iterable<SensorReading> sensors;
  private final int nodeId;

  public SensorDataMessage(Iterable<SensorReading> sensors, int nodeId) {
    this.sensors = sensors;
    this.nodeId = nodeId;
  }

  public int getNodeId() {
    return nodeId;
  }

  public Iterable<SensorReading> getSensors() {
    return sensors;
  }
}
