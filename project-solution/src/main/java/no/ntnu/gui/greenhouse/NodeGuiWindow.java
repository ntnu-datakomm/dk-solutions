package no.ntnu.gui.greenhouse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
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
  private static final double VERTICAL_OFFSET = 50;
  private static final double HORIZONTAL_OFFSET = 150;
  private static final double WINDOW_WIDTH = 300;
  private static final double WINDOW_HEIGHT = 300;
  private static final double HUGE_HEIGHT = 5000;
  private final SensorActuatorNode node;

  private final Map<Sensor, SimpleStringProperty> sensorProps = new HashMap<>();
  private final Map<Actuator, SimpleStringProperty> actuatorValue = new HashMap<>();
  private final Map<Actuator, SimpleBooleanProperty> actuatorActive = new HashMap<>();

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
    setX((node.getId() - 1) * HORIZONTAL_OFFSET);
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
        vbox.getChildren().add(createAndRememberSensorLabel(sensor))
    );
    return new TitledPane("Sensors", vbox);
  }

  private Node createAndRememberSensorLabel(Sensor sensor) {
    SimpleStringProperty props = new SimpleStringProperty(generateSensorText(sensor));
    sensorProps.put(sensor, props);
    Label label = new Label();
    label.textProperty().bind(props);
    return label;
  }

  private String generateSensorText(Sensor sensor) {
    return sensor.getType() + ": " + sensor.getCurrent() + sensor.getUnit();
  }

  private Node createActuatorSection() {
    VBox vbox = new VBox();
    vbox.setSpacing(10);
    TitledPane actuatorPane = new TitledPane("Actuators", vbox);
    addActuatorControls(vbox);
    stretchVertically(actuatorPane);
    return actuatorPane;
  }

  private void addActuatorControls(Pane parent) {
    node.getActuators().forEach(actuator ->
        parent.getChildren().add(createActuatorGui(actuator))
    );
  }

  private Node createActuatorGui(Actuator actuator) {
    HBox actuatorGui = new HBox(createActuatorLabel(actuator), createActuatorCheckbox(actuator));
    actuatorGui.setSpacing(5);
    return actuatorGui;
  }

  private CheckBox createActuatorCheckbox(Actuator actuator) {
    CheckBox checkbox = new CheckBox();
    SimpleBooleanProperty isSelected = new SimpleBooleanProperty(actuator.isOn());
    actuatorActive.put(actuator, isSelected);
    checkbox.selectedProperty().bindBidirectional(isSelected);
    checkbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue) {
        actuator.turnOn();
      } else {
        actuator.turnOff();
      }
    });
    return checkbox;
  }

  private Label createActuatorLabel(Actuator actuator) {
    SimpleStringProperty props = new SimpleStringProperty(generateActuatorText(actuator));
    actuatorValue.put(actuator, props);
    Label label = new Label();
    label.textProperty().bind(props);
    return label;
  }

  private String generateActuatorText(Actuator actuator) {
    String onOff = actuator.isOn() ? "ON" : "off";
    return actuator.getType() + ": " + onOff;
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
    for (Sensor sensor : sensors) {
      updateSensorLabel(sensor);
    }
  }

  private void updateSensorLabel(Sensor sensor) {
    SimpleStringProperty props = sensorProps.get(sensor);
    if (props == null) {
      throw new IllegalStateException("Can't update GUI for an unknown sensor: " + sensor);
    }

    Platform.runLater(() -> props.set(generateSensorText(sensor)));
  }

  @Override
  public void actuatorUpdated(Actuator actuator) {
    SimpleStringProperty actuatorText = actuatorValue.get(actuator);
    SimpleBooleanProperty actuatorSelected = actuatorActive.get(actuator);
    if (actuatorText == null || actuatorSelected == null) {
      throw new IllegalStateException("Can't update GUI for an unknown actuator: " + actuator);
    }

    Platform.runLater(() -> {
      actuatorText.set(generateActuatorText(actuator));
      actuatorSelected.set(actuator.isOn());
    });
  }
}
