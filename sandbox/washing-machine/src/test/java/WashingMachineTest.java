import static no.ntnu.WashingMachine.ROOM_TEMPERATURE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

import no.ntnu.InvalidStateException;
import no.ntnu.WashingListener;
import no.ntnu.WashingMachine;
import no.ntnu.WashingProgram;
import no.ntnu.WashingStatus;
import org.junit.Test;

/**
 * Tests for washing machine logic.
 */
public class WashingMachineTest {
  @Test
  public void testStartingState() {
    WashingMachine machine = new WashingMachine();
    assertEquals(new WashingStatus(WashingProgram.NONE, ROOM_TEMPERATURE, 0),
        machine.getStatus());
  }

  @Test
  public void testStart() {
    WashingMachine machine = new WashingMachine();
    try {
      machine.start(WashingProgram.COTTON);
      assertEquals(WashingProgram.COTTON, machine.getStatus().program());
    } catch (InvalidStateException e) {
      fail("Failed to start a program");
    }
  }

  @Test
  public void testRepeatedStartFails() throws InvalidStateException {
    WashingMachine machine = new WashingMachine();
    machine.start(WashingProgram.COTTON);
    assertThrows(InvalidStateException.class, () -> machine.start(WashingProgram.KIDS));
    assertEquals(WashingProgram.COTTON, machine.getStatus().program());
  }

  @Test
  public void testAbortFailsWithoutProgram() {
    WashingMachine machine = new WashingMachine();
    assertThrows(InvalidStateException.class, machine::abort);
  }

  @Test
  public void testAbort() {
    WashingMachine machine = new WashingMachine();
    try {
      machine.start(WashingProgram.COTTON);
      machine.abort();
    } catch (InvalidStateException e) {
      fail("Failed to abort a running program");
    }
  }

  @Test
  public void testListenerNotification() throws InvalidStateException {
    WashingMachine machine = new WashingMachine();
    DummyWashingListener listener = new DummyWashingListener();
    machine.addListener(listener);

    assertNull(listener.getStatus());
    machine.start(WashingProgram.COTTON);
    assertEquals(new WashingStatus(WashingProgram.COTTON, ROOM_TEMPERATURE, 0),
        listener.getStatus());
  }

}

class DummyWashingListener implements WashingListener {
  private WashingStatus status = null;

  @Override
  public void onWashingStatusUpdated(WashingStatus status) {
    this.status = status;
  }

  public WashingStatus getStatus() {
    return status;
  }
}

