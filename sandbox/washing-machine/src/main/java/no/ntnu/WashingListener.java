package no.ntnu;

/**
 * Generic interface for those interested in receiving updates from the washing machine.
 */
public interface WashingListener {
  void onWashingStatusUpdated(WashingStatus status);
}
