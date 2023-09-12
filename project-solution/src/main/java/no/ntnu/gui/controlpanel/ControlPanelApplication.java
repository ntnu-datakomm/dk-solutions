package no.ntnu.gui.controlpanel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import no.ntnu.controlpanel.ControlPanelLogic;
import no.ntnu.controlpanel.SensorActuatorNodeInfo;
import no.ntnu.greenhouse.GreenhouseEventListener;
import no.ntnu.greenhouse.SensorReading;
import no.ntnu.gui.common.ActuatorPane;
import no.ntnu.gui.common.SensorPane;
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
  private final Map<Integer, SensorPane> sensorPanes = new HashMap<>();
  private final Map<Integer, Tab> nodeTabs = new HashMap<>();

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

  @Override
  public void onSensorData(int nodeId, List<SensorReading> sensors) {
    Logger.info("Sensor data from node " + nodeId);
    SensorPane sensorPane = sensorPanes.get(nodeId);
    if (sensorPane != null) {
      sensorPane.update(sensors);
    } else {
      Logger.info("Tab for node " + nodeId + " without a sensor section?");
    }
  }

  @Override
  public void onNodeRemoved(int nodeId) {
    Tab nodeTab = nodeTabs.get(nodeId);
    if (nodeTab != null) {
      Platform.runLater(() -> {
        removeNodeTab(nodeId, nodeTab);
        forgetNodeSensorSection(nodeId);
      });
      Logger.info("Node " + nodeId + " removed");
    } else {
      Logger.info("Can't remove node " + nodeId + ", there is no Tab for it");
    }
  }

  private void forgetNodeSensorSection(int nodeId) {
    SensorPane sensorPane = sensorPanes.get(nodeId);
    if (sensorPane != null) {
      sensorPanes.remove(nodeId);
    } else {
      Logger.info("Tried to remove non-existing sensor pane for node " + nodeId);
    }
  }

  private void removeNodeTab(int nodeId, Tab nodeTab) {
    nodeTab.getTabPane().getTabs().remove(nodeTab);
    nodeTabs.remove(nodeId);
  }

  private void addNodeTab(SensorActuatorNodeInfo nodeInfo) {
    if (nodeTabPane == null) {
      nodeTabPane = new TabPane();
      mainScene.setRoot(nodeTabPane);
    }
    Tab nodeTab = nodeTabs.get(nodeInfo.getId());
    if (nodeTab == null) {
      nodeTabPane.getTabs().add(createNodeTab(nodeInfo));
    } else {
      Logger.info("Duplicate node spawned, ignore it");
    }
  }

  private Tab createNodeTab(SensorActuatorNodeInfo nodeInfo) {
    Tab tab = new Tab("Node " + nodeInfo.getId());
    SensorPane sensorPane = createEmptySensorPane();
    sensorPanes.put(nodeInfo.getId(), sensorPane);
    tab.setContent(new VBox(sensorPane, new ActuatorPane(nodeInfo.getActuators())));
    nodeTabs.put(nodeInfo.getId(), tab);
    return tab;
  }

  private static SensorPane createEmptySensorPane() {
    return new SensorPane();
  }
}
