package no.ntnu.gui;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import no.ntnu.greenhouse.SensorActuatorNode;

/**
 * Window with GUI for overview and control of one specific sensor/actuator node.
 */
public class NodeGuiWindow extends Stage {

  /**
   * Create a GUI window for a specific node.
   *
   * @param node The node which will be handled in this window
   */
  public NodeGuiWindow(SensorActuatorNode node) {
    Scene scene = new Scene(createContent(node.getId()), 200, 100);
    setScene(scene);
    setOnCloseRequest(windowEvent -> shutDownNode(node));
  }

  private void shutDownNode(SensorActuatorNode node) {
    node.stop();
  }

  private static Parent createContent(int nodeId) {
    return new HBox(new Label("Node " + nodeId));
  }

}
