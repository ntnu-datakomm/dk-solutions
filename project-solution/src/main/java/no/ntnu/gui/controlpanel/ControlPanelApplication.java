package no.ntnu.gui.controlpanel;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import no.ntnu.controlpanel.FakeSensorNodeSpawner;
import no.ntnu.tools.Logger;

/**
 * Run a control panel with a graphical user interface (GUI), with JavaFX.
 */
public class ControlPanelApplication extends Application {
  private static final int WIDTH = 500;
  private static final int HEIGHT = 400;

  /**
   * Application entrypoint for the GUI of a control panel.
   * Note - this is a workaround to avoid problems with JavaFX not finding the modules!
   * We need to use another wrapper-class for the debugger to work.
   */
  public static void startApp() {
    Logger.info("Running control panel...");
    addRandomSensorNodeInfo("4;2_temperature 1_humidity;3_window", 2);
    addRandomSensorNodeInfo("1;1_temperature 1_humidity", 3);
    addRandomSensorNodeInfo("8;1_temperature;2_heater", 5);
    launch();
  }

  /**
   * Add a fake sensor-node info tab after a specified time delay.
   *
   * @param specification A (temporary) manual configuration of the node in the following format
   *                      <nodeId> semicolon
   *                      <count_1> underscore <sensor_type_1> space ...
   *                      <count_N> underscore <sensor_type_N>
   *                      semicolon
   *                      <actuator_count_1> underscore <actuator_type_1> space ...
   *                      <actuator_count_M> underscore <actuator_type_M>
   * @param delay         The delay, in seconds.
   */
  private static void addRandomSensorNodeInfo(String specification, int delay) {
    FakeSensorNodeSpawner spawner = new FakeSensorNodeSpawner(specification);
    spawner.spawnAfter(delay);
  }

  @Override
  public void start(Stage stage) {
    stage.setMinWidth(WIDTH);
    stage.setMinHeight(HEIGHT);
    stage.setTitle("Control panel");
    Scene scene = new Scene(createEmptyContent(), WIDTH, HEIGHT);
    stage.setScene(scene);
    stage.show();
  }

  private static Label createEmptyContent() {
    Label l = new Label("Waiting for node data...");
    l.setAlignment(Pos.CENTER);
    return l;
  }
}
