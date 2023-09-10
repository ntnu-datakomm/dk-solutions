package no.ntnu.gui.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import no.ntnu.greenhouse.Sensor;

/**
 * A section of GUI displaying sensor data.
 */
public class SensorPane extends TitledPane {
  private final Map<Sensor, SimpleStringProperty> sensorProps = new HashMap<>();

  /**
   * Create a sensor pane.
   *
   * @param sensors The sensor data to be displayed on the pane.
   */
  public SensorPane(Iterable<Sensor> sensors) {
    super();
    setText("Sensors");
    VBox vbox = new VBox();
    sensors.forEach(sensor ->
        vbox.getChildren().add(createAndRememberSensorLabel(sensor))
    );
    setContent(vbox);
  }

  /**
   * Update the GUI according to the changes in sensor data.
   *
   * @param sensors The sensor data that has been updated
   */
  public void update(List<Sensor> sensors) {
    for (Sensor sensor : sensors) {
      updateSensorLabel(sensor);
    }
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

  private void updateSensorLabel(Sensor sensor) {
    SimpleStringProperty props = sensorProps.get(sensor);
    if (props == null) {
      throw new IllegalStateException("Can't update GUI for an unknown sensor: " + sensor);
    }

    Platform.runLater(() -> props.set(generateSensorText(sensor)));
  }
}
