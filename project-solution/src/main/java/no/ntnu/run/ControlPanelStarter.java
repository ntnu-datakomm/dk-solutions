package no.ntnu.run;

import no.ntnu.controlpanel.ControlPanelLogic;
import no.ntnu.gui.controlpanel.ControlPanelApplication;

/**
 * Starter class for the control panel.
 * Note: we could launch the Application class directly, but then we would have issues with the
 * debugger (JavaFX modules not found)
 */
public class ControlPanelStarter {
  /**
   * Entrypoint for the application.
   *
   * @param args Command-line arguments, not used.
   */
  public static void main(String[] args) {
    ControlPanelLogic logic = new ControlPanelLogic();
    logic.initiateCommunication();
    ControlPanelApplication.startApp(logic);
  }
}
