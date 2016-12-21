package com.skedgo.android.common.model;

import android.support.annotation.Nullable;

public abstract class Vehicle {
  @Nullable public abstract String name();
  @Nullable public abstract String description();
  @Nullable public abstract String licensePlate();
  @Nullable public abstract String engineType();
  @Nullable public abstract String fuelType();
  public abstract int fuelLevel();
  @Nullable public abstract PricingTable pricingTable();
}
