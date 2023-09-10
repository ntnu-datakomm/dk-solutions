package no.ntnu.controlpanel;

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
}
