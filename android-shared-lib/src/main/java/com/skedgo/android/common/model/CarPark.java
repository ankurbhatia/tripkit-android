package com.skedgo.android.common.model;

import com.google.gson.annotations.JsonAdapter;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Value.Immutable
@Gson.TypeAdapters
@JsonAdapter(GsonAdaptersCarPark.class)
public abstract class CarPark {
  public abstract String name();
  public abstract String identifier();
  public abstract CompanyInfo operator();
  public abstract int totalSpaces();
  public abstract int availableSpaces();
  public abstract long lastUpdated();
  public abstract OpeningHours openingHours();
  public abstract PricingTable pricingTable();
  public abstract DataSourceAttribution source();

}
