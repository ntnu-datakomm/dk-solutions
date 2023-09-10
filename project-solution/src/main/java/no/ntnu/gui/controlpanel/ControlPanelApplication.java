package no.ntnu.gui.controlpanel;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import no.ntnu.controlpanel.ControlPanelLogic;
import no.ntnu.controlpanel.SensorActuatorNodeInfo;
import no.ntnu.greenhouse.GreenhouseEventListener;
import no.ntnu.gui.common.ActuatorPane;
import no.ntnu.tools.Logger;

/**
 * Run a control panel with a graphical user interface (GUI), with JavaFX.
 */
public class ControlPanelApplication extends Application implements GreenhouseEventListener {
  private static ControlPanelLogic logic;
  private static final int WIDTH = 500;
  private static final int HEIGHT = 400;

  private TabPane nodeTabPane;
  private Scene mainScene;

  /**
   * Application entrypoint for the GUI of a control panel.
   * Note - this is a workaround to avoid problems with JavaFX not finding the modules!
   * We need to use another wrapper-class for the debugger to work.
   *
   * @param logic The logic of the control panel node
   */
  public static void startApp(ControlPanelLogic logic) {
    if (logic == null) {
      throw new IllegalArgumentException("Control panel logic can't be null");
    }
    ControlPanelApplication.logic = logic;
    Logger.info("Running control panel GUI...");
    launch();
  }

  @Override
  public void start(Stage stage) {
    stage.setMinWidth(WIDTH);
    stage.setMinHeight(HEIGHT);
    stage.setTitle("Control panel");
    mainScene = new Scene(createEmptyContent(), WIDTH, HEIGHT);
    stage.setScene(mainScene);
    stage.show();
    logic.addListener(this);
  }

  private static Label createEmptyContent() {
    Label l = new Label("Waiting for node data...");
    l.setAlignment(Pos.CENTER);
    return l;
  }

  @Override
  public void onNodeAdded(SensorActuatorNodeInfo nodeInfo) {
    Platform.runLater(() -> addNodeTab(nodeInfo));
  }

  private void addNodeTab(SensorActuatorNodeInfo nodeInfo) {
    if (nodeTabPane == null) {
      nodeTabPane = new TabPane();
      mainScene.setRoot(nodeTabPane);
    }
    nodeTabPane.getTabs().add(createNodeTab(nodeInfo));
  }

  private Tab createNodeTab(SensorActuatorNodeInfo nodeInfo) {
    Tab tab = new Tab("Node " + nodeInfo.getId());
    tab.setContent(new VBox(createEmptySensorSection(), new ActuatorPane(nodeInfo.getActuators())));
    return tab;
  }

  private Node createEmptySensorSection() {
    VBox vbox = new VBox(new Label("Waiting for sensor data"));
    return new TitledPane("Sensors", vbox);
  }
}
