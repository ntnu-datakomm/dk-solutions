package no.ntnu.controlpanel;

import java.util.LinkedList;
import java.util.List;
import no.ntnu.greenhouse.SensorReading;
import no.ntnu.listeners.controlpanel.GreenhouseEventListener;

/**
 * The central logic of a control panel node. It uses a communication channel to send commands
 * and receive events. It supports listeners who will be notified on changes (for example, a new
 * node is added to the network, or a new sensor reading is received).
 * Note: this class may look like unnecessary forwarding of events to the GUI. In real projects
 * (read: "big projects") this logic class may do some "real processing" - such as storing events
 * in a database, doing some checks, sending emails, notifications, etc. Such things should never
 * be placed inside a GUI class (JavaFX classes). Therefore, we use proper structure here, even
 * though you may have no real control-panel logic in your projects.
 */
public class ControlPanelLogic implements GreenhouseEventListener {
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
   * Initiate communication - start receiving sensor/actuator node events.
   */
  public void initiateCommunication() {
    // TODO - replace this with real socket communication
    FakeCommunicationChannel spawner = new FakeCommunicationChannel(this);
    spawner.spawnNode("4;3_window", 2);
    spawner.spawnNode("1", 3);
    spawner.spawnNode("1", 4);
    spawner.spawnNode("8;2_heater", 5);
    spawner.advertiseSensorData("4;temperature=27.4 °C,temperature=26.8 °C,humidity=80 %", 4);
    spawner.advertiseSensorData("4;temperature=22.4 °C,temperature=26.0 °C,humidity=81 %", 9);
    spawner.advertiseSensorData("4;temperature=25.4 °C,temperature=27.0 °C,humidity=82 %", 14);
    spawner.advertiseSensorData("1;humidity=80 %,humidity=82 %", 10);
    spawner.advertiseRemovedNode(8, 11);
    spawner.advertiseRemovedNode(4, 12);
    spawner.advertiseSensorData("1;temperature=25.4 °C,temperature=27.0 °C,humidity=67 %", 13);
    spawner.advertiseSensorData("4;temperature=25.4 °C,temperature=27.0 °C,humidity=82 %", 16);
    spawner.advertiseRemovedNode(4, 18);
  }

  @Override
  public void onNodeAdded(SensorActuatorNodeInfo nodeInfo) {
    listeners.forEach(listener -> listener.onNodeAdded(nodeInfo));
  }

  @Override
  public void onNodeRemoved(int nodeId) {
    listeners.forEach(listener -> listener.onNodeRemoved(nodeId));
  }

  @Override
  public void onSensorData(int nodeId, List<SensorReading> sensors) {
    listeners.forEach(listener -> listener.onSensorData(nodeId, sensors));
  }
}
