package no.ntnu.run;

import no.ntnu.controlpanel.ControlPanelLogic;
import no.ntnu.controlpanel.FakeCommunicationChannel;
import no.ntnu.gui.controlpanel.ControlPanelApplication;

/**
 * Starter class for the control panel.
 * Note: we could launch the Application class directly, but then we would have issues with the
 * debugger (JavaFX modules not found)
 */
public class ControlPanelStarter {
  /**
   * Entrypoint for the application.
   *
   * @param args Command-line arguments, not used.
   */
  public static void main(String[] args) {
    ControlPanelLogic logic = new ControlPanelLogic();
    initiateCommunication(logic);
    ControlPanelApplication.startApp(logic);
  }

  private static void initiateCommunication(ControlPanelLogic logic) {
    // TODO - replace this with real socket communication
    // Here we pretend that some events will be received with a given delay
    // In your project you probably want to implement a communication channel (TCP or UDP) which
    // sends the same notifications (events) to this logic class - onNodeAdded, onSensorData, etc.
    FakeCommunicationChannel spawner = new FakeCommunicationChannel(logic);
    logic.setCommandSender(spawner);
    spawner.spawnNode("4;3_window", 2);
    spawner.spawnNode("1", 3);
    spawner.spawnNode("1", 4);
    spawner.advertiseSensorData("4;temperature=27.4 °C,temperature=26.8 °C,humidity=80 %", 4);
    spawner.spawnNode("8;2_heater", 5);
    spawner.advertiseActuatorState(4, 1, true, 5);
    spawner.advertiseActuatorState(4,  1, false, 6);
    spawner.advertiseActuatorState(4,  1, true, 7);
    spawner.advertiseActuatorState(4,  2, true, 7);
    spawner.advertiseActuatorState(4,  1, false, 8);
    spawner.advertiseActuatorState(4,  2, false, 8);
    spawner.advertiseActuatorState(4,  1, true, 9);
    spawner.advertiseActuatorState(4,  2, true, 9);
    spawner.advertiseSensorData("4;temperature=22.4 °C,temperature=26.0 °C,humidity=81 %", 9);
    spawner.advertiseSensorData("1;humidity=80 %,humidity=82 %", 10);
    spawner.advertiseRemovedNode(8, 11);
    spawner.advertiseRemovedNode(8, 12);
    spawner.advertiseSensorData("1;temperature=25.4 °C,temperature=27.0 °C,humidity=67 %", 13);
    spawner.advertiseSensorData("4;temperature=25.4 °C,temperature=27.0 °C,humidity=82 %", 14);
    spawner.advertiseSensorData("4;temperature=25.4 °C,temperature=27.0 °C,humidity=82 %", 16);
  }
}
