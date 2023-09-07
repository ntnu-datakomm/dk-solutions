package no.ntnu.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * The main GUI window.
 */
public class MainGuiWindow extends Scene {
  private static final int WIDTH = 300;
  private static final int HEIGHT = 200;

  public MainGuiWindow() {
    super(createMainContent(), WIDTH, HEIGHT);
  }

  private static Parent createMainContent() {
    Label l = new Label("Close this window to stop the whole simulation");
    l.setWrapText(true);
    HBox hbox = new HBox(l);
    hbox.setPadding(new Insets(20));
    hbox.setAlignment(Pos.CENTER);
    return hbox;
  }

}
