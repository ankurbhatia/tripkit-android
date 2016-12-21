package com.skedgo.android.common.model;

public class CarRentalStop extends Location {
  private final CarRental carRental;
  private final ModeInfo modeInfo;
  private final String id;

  public CarRentalStop(CarRental carRental, ModeInfo modeInfo, String id) {
    this.carRental = carRental;
    this.modeInfo = modeInfo;
    this.id = id;
  }

  public ModeInfo getModeInfo() {
    return modeInfo;
  }

  public String id() {
    return id;
  }

  public CarRental getCarRental() {
    return carRental;
  }
}
