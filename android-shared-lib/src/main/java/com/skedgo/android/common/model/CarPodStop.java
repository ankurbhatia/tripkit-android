package com.skedgo.android.common.model;

public class CarPodStop extends Location {
  private final CarPod carPod;
  private final ModeInfo modeInfo;
  private final String id;

  public CarPodStop(CarPod carPod, ModeInfo modeInfo, String id) {
    this.carPod = carPod;
    this.modeInfo = modeInfo;
    this.id = id;
  }

  public ModeInfo getModeInfo() {
    return modeInfo;
  }

  public String id() {
    return id;
  }

  public CarPod carPod() {
    return carPod;
  }
}
