package no.ntnu;

import java.util.LinkedList;
import java.util.List;

/**
 * The real machine washing clothes.
 */
public class WashingMachine {
  /**
   * Represents a room temperature. We should move such constants to another class
   * (such as Environment). But until there is more than one, let's keep it here.
   */
  public static final int ROOM_TEMPERATURE = 23;
  private WashingProgram program = WashingProgram.NONE;

  private int temperature;

  private int percentComplete;

  private final List<WashingListener> listeners = new LinkedList<>();

  public WashingMachine() {
    resetState();
  }

  /**
   * Start executing a new washing program.
   *
   * @param program The program to execute
   * @throws InvalidStateException When a program new program can't be started in the current state
   */
  public void start(WashingProgram program) throws InvalidStateException {
    if (this.program != WashingProgram.NONE) {
      throw new InvalidStateException("Can't start a program when one is already in progress");
    }
    resetState();
    this.program = program;
    sendStatusUpdate();
    startWashingThread();
  }

  private void startWashingThread() {
    // TODO
  }

  private void resetState() {
    percentComplete = 0;
    temperature = ROOM_TEMPERATURE;
  }

  /**
   * Abort the currently running program.
   *
   * @throws InvalidStateException When no program is running or when aborting is not possible.
   */
  public void abort() throws InvalidStateException {
    if (program == WashingProgram.NONE) {
      throw new InvalidStateException("No program to stop");
    }
    resetState();
    sendStatusUpdate();
  }

  /**
   * Get the current status of the machine.
   *
   * @return The current status
   */
  public WashingStatus getStatus() {
    return new WashingStatus(program, temperature, percentComplete);
  }

  /**
   * Add a new listener which will received updates of the machine status.
   *
   * @param listener The listener to add. If it already exists in the listener list,
   *                 no action is taken.
   */
  public void addListener(WashingListener listener) {
    if (!listeners.contains(listener)) {
      listeners.add(listener);
    }
  }

  /**
   * Unregister a listener for washing state updates.
   *
   * @param listener The listener to unregister.
   */
  public void removeListener(WashingListener listener) {
    listeners.remove(listener);
  }

  private void sendStatusUpdate() {
    listeners.forEach(listener -> listener.onWashingStatusUpdated(getStatus()));
  }
}
