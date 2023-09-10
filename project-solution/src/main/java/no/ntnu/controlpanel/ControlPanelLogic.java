package no.ntnu.controlpanel;

import java.util.LinkedList;
import java.util.List;
import no.ntnu.greenhouse.GreenhouseEventListener;

/**
 * The central logic of a control panel node. It uses a communication channel to send commands
 * and receive events. It supports listeners who will be notified on changes (for example, a new
 * node is added to the network, or a new sensor reading is received).
 */
public class ControlPanelLogic implements FakeSpawnerListener {
  private final List<GreenhouseEventListener> listeners = new LinkedList<>();

  /**
   * Add an event listener.
   *
   * @param listener The listener who will be notified on all events
   */
  public void addListener(GreenhouseEventListener listener) {
    if (!listeners.contains(listener)) {
      listeners.add(listener);
    }
  }

  /**
   * Initiate some fake events, for testing without real data.
   */
  public void initiateFakeEvents() {
    // TODO - remove this when socket communication with real events is implemented
    FakeSensorNodeSpawner spawner = new FakeSensorNodeSpawner(this);
    spawner.spawn("4;3_window", 2);
    spawner.spawn("1", 3);
    spawner.spawn("8;2_heater", 5);
  }

  @Override
  public void onNodeSpawned(SensorActuatorNodeInfo nodeInfo) {
    // TODO
    listeners.forEach(listener -> listener.onNodeAdded(nodeInfo));
  }
}
