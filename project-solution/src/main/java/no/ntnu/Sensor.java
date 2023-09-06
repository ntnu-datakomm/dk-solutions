package no.ntnu;

/**
 * A sensor which can sense the environment in a specific way.
 */
public class Sensor {
  private final String type;
  private final double min;
  private final double max;
  private double currentValue;
  private final String unit;

  /**
   * Create a sensor.
   *
   * @param type    The type of the sensor. Examples: "temperature", "humidity"
   * @param min     Minimum allowed value
   * @param max     Maximum allowed value
   * @param current The current (starting) value of the sensor
   * @param unit    The measurement unit. Examples: "%", "C", "lux"
   */
  public Sensor(String type, double min, double max, double current, String unit) {
    this.type = type;
    this.min = min;
    this.max = max;
    this.currentValue = current;
    this.unit = unit;
  }

  public String getType() {
    return type;
  }

  /**
   * Create a clone of this sensor.
   *
   * @return A clone of this sensor, where all the fields are the same
   */
  public Sensor clone() {
    return new Sensor(this.type, this.min, this.max, this.currentValue, this.unit);
  }
}
