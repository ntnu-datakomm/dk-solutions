package no.ntnu.gui;

import java.util.List;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import no.ntnu.greenhouse.Actuator;
import no.ntnu.greenhouse.ActuatorCollection;
import no.ntnu.greenhouse.ActuatorListener;
import no.ntnu.greenhouse.Sensor;
import no.ntnu.greenhouse.SensorActuatorNode;
import no.ntnu.greenhouse.SensorListener;

/**
 * Window with GUI for overview and control of one specific sensor/actuator node.
 */
public class NodeGuiWindow extends Stage implements SensorListener, ActuatorListener {
  private static final int VERTICAL_OFFSET = 50;
  private static final int HORIZONTAL_OFFSET = 100;
  private static final double WINDOW_WIDTH = 300;
  private static final double WINDOW_HEIGHT = 300;
  private static final double HUGE_HEIGHT = 5000;
  private final SensorActuatorNode node;

  /**
   * Create a GUI window for a specific node.
   *
   * @param node The node which will be handled in this window
   */
  public NodeGuiWindow(SensorActuatorNode node) {
    this.node = node;
    Scene scene = new Scene(createContent(), WINDOW_WIDTH, WINDOW_HEIGHT);
    setScene(scene);
    setTitle("Node " + node.getId());
    initializeListeners(node);
    setPositionAndSize();
  }

  private void setPositionAndSize() {
    setX(node.getId() * HORIZONTAL_OFFSET - (double) HORIZONTAL_OFFSET / 2.0);
    setY(node.getId() * VERTICAL_OFFSET);
    setMinWidth(WINDOW_HEIGHT);
    setMinHeight(WINDOW_WIDTH);
  }


  private void initializeListeners(SensorActuatorNode node) {
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
    VBox vbox = new VBox();
    node.getSensors().forEach(sensor ->
        vbox.getChildren().add(
            new Label(sensor.getType() + ": " + sensor.getCurrent() + sensor.getUnit())
        )
    );
    stretchVertically(vbox);
    return new TitledPane("Sensors", vbox);
  }

  private Node createActuatorSection() {
    VBox vbox = new VBox();
    TitledPane actuatorPane = new TitledPane("Actuators", vbox);
    addActuatorControls(vbox);
    stretchVertically(actuatorPane);
    return actuatorPane;
  }

  private void addActuatorControls(Pane parent) {
    for (Actuator actuator : node.getActuators()) {
      String onOff = actuator.isOn() ? "ON" : "off";
      parent.getChildren().add(new Label(actuator.getType() + ": " + onOff)); // !!!
    }
  }

  /**
   * Ensure that this node always try to get as much vertical space as is available to it
   * (proportionally to other siblings).
   *
   * @param region The region to stretch vertically
   */
  private void stretchVertically(Region region) {
    region.setPrefHeight(HUGE_HEIGHT);
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
