package no.ntnu;

/**
 * A sensor which can sense the environment in a specific way.
 */
public class Sensor {
  private final String type;
  private final double min;
  private final double max;
  private double current;
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
    this.current = current;
    this.unit = unit;
    ensureValueBoundsAndPrecision();
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
    return new Sensor(this.type, this.min, this.max, this.current, this.unit);
  }

  /**
   * Get the current sensor value.
   *
   * @return The current sensor value. See the unit to understand the unit of measurement.
   */
  public double getCurrent() {
    return current;
  }

  /**
   * Get the unit of measurement for this sensor.
   *
   * @return The unif or measurement
   */
  public String getUnit() {
    return unit;
  }

  /**
   * Add a random noise to the sensors to simulate realistic values.
   */
  public void addRandomNoise() {
    this.current += generateRealisticNoise();
    ensureValueBoundsAndPrecision();
  }

  private void ensureValueBoundsAndPrecision() {
    roundToTwoDecimals();
    if (current < min) {
      current = min;
    } else if (current > max) {
      current = max;
    }
  }

  private void roundToTwoDecimals() {
    this.current = Math.round(this.current * 100.0) / 100.0;
  }

  private double generateRealisticNoise() {
    final double wholeRange = max - min;
    final double onePercentOfRange = wholeRange / 100.0;
    final double zeroToTwoPercent = Math.random() * onePercentOfRange * 2;
    final double plusMinusOnePercent = zeroToTwoPercent - onePercentOfRange;
    return plusMinusOnePercent;
  }

  /**
   * Apply an external impact (from an actuator) to the current value of the sensor.
   *
   * @param impact The impact to apply - the delta for the value
   */
  public void applyImpact(double impact) {
    this.current += impact;
    ensureValueBoundsAndPrecision();
  }
}
