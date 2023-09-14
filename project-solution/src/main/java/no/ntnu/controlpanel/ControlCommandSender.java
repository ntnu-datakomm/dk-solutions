package no.ntnu.controlpanel;

/**
 * A notification channel for disseminating control commands to the sensor nodes.
 * Your socket class(es) on the control panel side should implement this.
 */
public interface ControlCommandSender {
  /**
   * Request that state of an actuator is changed.
   *
   * @param nodeId     ID of the node to which the actuator is attached
   * @param actuatorId Node-wide unique ID of the actuator
   * @param isOn       When true, actuator must be turned on; off when false.
   */
  void sendActuatorChange(int nodeId, int actuatorId, boolean isOn);
}
