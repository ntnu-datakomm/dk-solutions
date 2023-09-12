package no.ntnu.controlpanel;

import java.util.List;
import no.ntnu.greenhouse.SensorReading;

/**
 * A listener for fake-spawner events.
 */
public interface FakeSpawnerListener {
  /**
   * This event is fired when a new (fake) sensor/actuator node is spawned.
   *
   * @param nodeInfo Information of the sensor/actuator node
   */
  void onNodeSpawned(SensorActuatorNodeInfo nodeInfo);

  /**
   * This event is fired when new sensor data is received from a node.
   *
   * @param nodeId  ID of the node
   * @param sensors List of all current sensor values
   */
  void onSensorData(int nodeId, List<SensorReading> sensors);
}
