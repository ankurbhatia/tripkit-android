package com.skedgo.android.common.model;

public class BikePodStop extends Location {
  private final BikePod bikePod;
  private final ModeInfo modeInfo;
  private final String id;

  public BikePodStop(BikePod bikePod, ModeInfo modeInfo, String id) {
    this.bikePod = bikePod;
    this.modeInfo = modeInfo;
    this.id = id;
  }

  public BikePod getBikePod() {
    return bikePod;
  }

  public ModeInfo getModeInfo() {
    return modeInfo;
  }

  public String id() {
    return id;
  }
}