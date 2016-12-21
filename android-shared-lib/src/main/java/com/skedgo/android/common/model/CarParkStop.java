package com.skedgo.android.common.model;

public class CarParkStop extends Location {
  private final CarPark carPark;
  private final ModeInfo modeInfo;
  private final String id;

  public CarParkStop(CarPark carPark, ModeInfo modeInfo, String id) {
    this.carPark = carPark;
    this.modeInfo = modeInfo;
    this.id = id;
  }

  public CarPark getCarPark() {
    return carPark;
  }

  public ModeInfo getModeInfo() {
    return modeInfo;
  }

  public String id() {
    return id;
  }
}
