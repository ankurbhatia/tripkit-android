package com.skedgo.android.common.model;

public class BikePodStop extends Location {
  private final BikePod bikePod;
  private final ModeInfo modeInfo;

  public BikePodStop(BikePod bikePod, ModeInfo modeInfo) {
    this.bikePod = bikePod;
    this.modeInfo = modeInfo;
  }

  public BikePod getBikePod() {
    return bikePod;
  }

  public ModeInfo getModeInfo() {
    return modeInfo;
  }
}