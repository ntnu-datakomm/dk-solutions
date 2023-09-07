package no.ntnu.gui;

import java.util.List;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import no.ntnu.greenhouse.Actuator;
import no.ntnu.greenhouse.ActuatorListener;
import no.ntnu.greenhouse.Sensor;
import no.ntnu.greenhouse.SensorActuatorNode;
import no.ntnu.greenhouse.SensorListener;

/**
 * Window with GUI for overview and control of one specific sensor/actuator node.
 */
public class NodeGuiWindow extends Stage implements SensorListener, ActuatorListener {
  private final SensorActuatorNode node;

  /**
   * Create a GUI window for a specific node.
   *
   * @param node The node which will be handled in this window
   */
  public NodeGuiWindow(SensorActuatorNode node) {
    this.node = node;
    Scene scene = new Scene(createContent(), 200, 100);
    setScene(scene);
    setTitle("Node " + node.getId());
    setOnCloseRequest(windowEvent -> shutDownNode());
    node.addSensorListener(this);
    node.addActuatorListener(this);
  }

  private void shutDownNode() {
    node.stop();
  }

  private Parent createContent() {
    return new VBox(createSensorSection(), createActuatorSection());
  }

  private Node createSensorSection() {
    return null; // TODO
  }

  private Node createActuatorSection() {
    return null;
  }

  @Override
  public void sensorsUpdated(List<Sensor> sensors) {
    // TODO
  }

  @Override
  public void actuatorUpdated(Actuator actuator) {
    // TODO
  }
}
