package no.ntnu.greenhouse;

/**
 * Represents one sensor reading (value).
 */
public class SensorReading {
  private final String type;
  private double value;
  private final String unit;

  /**
   * Create a new sensor reading.
   *
   * @param type  The type of sensor being red
   * @param value The current value of the sensor
   * @param unit  The unit, for example: %, lux
   */
  public SensorReading(String type, double value, String unit) {
    this.type = type;
    this.value = value;
    this.unit = unit;
  }

  public String getType() {
    return type;
  }

  public double getValue() {
    return value;
  }

  public String getUnit() {
    return unit;
  }

  public void setValue(double newValue) {
    this.value = newValue;
  }

  @Override
  public String toString() {
    return "{ type=" + type + ", value=" + value + ", unit=" + unit + " }";
  }

  /**
   * Get a human-readable (formatted) version of the current reading, including the unit.
   *
   * @return The sensor reading and the unit
   */
  public String getFormatted() {
    return value + unit;
  }
}
