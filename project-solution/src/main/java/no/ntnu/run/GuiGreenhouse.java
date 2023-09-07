package no.ntnu.run;

import javafx.application.Application;
import javafx.stage.Stage;
import no.ntnu.GreenhouseSimulator;
import no.ntnu.tools.Logger;

/**
 * Run a greenhouse simulation with a graphical user interface (GUI), with JavaFX.
 */
public class GuiGreenhouse extends Application {
  private static GreenhouseSimulator simulator;

  @Override
  public void start(Stage mainStage) {
    mainStage.setScene(new MainGuiWindow());
    mainStage.setMinWidth(200);
    mainStage.setMinHeight(200);
    mainStage.setTitle("Greenhouse simulator");
    mainStage.show();
    mainStage.setOnCloseRequest(event -> {
      simulator.stop();
      try {
        stop();
      } catch (Exception e) {
        Logger.error("Could not stop the application: " + e.getMessage());
      }
    });
  }

  /**
   * Application entrypoint for the GUI version of the simulator.
   *
   * @param args Command line arguments (not used)
   */
  public static void main(String[] args) {
    Logger.info("Running greenhouse simulator with JavaFX GUI...");
    simulator = new GreenhouseSimulator();
    simulator.start();
    launch();
  }

}
