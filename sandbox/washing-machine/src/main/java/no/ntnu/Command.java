package no.ntnu;

/**
 * A command that a client sends to the washing machine.
 */
public interface Command {
  /**
   * Execute the command. Each specific implementation-class holds the necessary logic.
   */
  void execute();
}
