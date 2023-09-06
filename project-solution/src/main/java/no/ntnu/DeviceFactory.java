package no.ntnu;

/**
 * A factory for producing sensors and actuators of specific types.
 */
public class DeviceFactory {
  private static final double NORMAL_GREENHOUSE_TEMPERATURE = 27;
  private static final double MIN_TEMPERATURE = 0;
  private static final double MAX_TEMPERATURE = 40;
  private static final String TEMPERATURE_UNIT = "°C";
  private static final double MIN_HUMIDITY = 0;
  private static final double MAX_HUMIDITY = 100;
  private static final double NORMAL_GREENHOUSE_HUMIDITY = 80;
  private static final String HUMIDITY_UNIT = "%";

  /**
   * Create a typical temperature sensor.
   *
   * @return A typical temperature sensor, which can be used as a template
   */
  public static Sensor createTemperatureSensor() {
    return new Sensor("temperature", MIN_TEMPERATURE, MAX_TEMPERATURE,
        randomize(NORMAL_GREENHOUSE_TEMPERATURE, 1.0), TEMPERATURE_UNIT);
  }

  /**
   * Create a typical humidity sensor.
   *
   * @return A typical humidity sensor which can be used as a template
   */
  public static Sensor createHumiditySensor() {
    return new Sensor("humidity", MIN_HUMIDITY, MAX_HUMIDITY,
        randomize(NORMAL_GREENHOUSE_HUMIDITY, 5.0), HUMIDITY_UNIT);
  }

  /**
   * Create a typical window-actuator.
   *
   * @param nodeId ID of the node to which the actuator is connected
   * @return The window actuator
   */
  public static Actuator createWindow(int nodeId) {
    Actuator actuator = new Actuator("window", nodeId);
    actuator.setImpact("temperature", -2.0);
    actuator.setImpact("humidity", -5.0);
    return actuator;
  }

  /**
   * Create a typical fan-actuator.
   *
   * @param nodeId ID of the node to which the actuator is connected
   * @return The fan actuator
   */
  public static Actuator createFan(int nodeId) {
    Actuator actuator = new Actuator("fan", nodeId);
    actuator.setImpact("temperature", -1.0);
    return actuator;
  }

  /**
   * Create a typical heater-actuator.
   *
   * @param nodeId ID of the node to which the actuator is connected
   * @return The heater actuator
   */
  public static Actuator createHeater(int nodeId) {
    Actuator actuator = new Actuator("heater", nodeId);
    actuator.setImpact("temperature", 4.0);
    return actuator;
  }

  /**
   * Generate a random value within the range [x-d; x+d].
   *
   * @param x The central value
   * @param d The allowed difference range
   * @return a randomized value within the desired range
   */
  private static double randomize(double x, double d) {
    final double zeroToDoubleD = Math.random() * 2 * d;
    final double plusMinusD = zeroToDoubleD - d;
    return x + plusMinusD;
  }

}
