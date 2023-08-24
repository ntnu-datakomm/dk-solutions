package no.ntnu;

/**
 * Generic interface for those interested in receiving updates from the washing machine.
 */
public interface WashingListener {
  public void onWashingStatusUpdated(WashingStatus status);
}
