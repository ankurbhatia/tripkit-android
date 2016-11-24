package com.skedgo.android.common.model;

import android.support.annotation.Nullable;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Value.Immutable
@Gson.TypeAdapters
public abstract class BikePodDetail {

  abstract String identifier();
  abstract CompanyInfo operator();

  @Value.Default int totalSpaces() {
    return -1;
  }

  @Value.Default int availableBikes() {
    return -1;
  }

  @Value.Default int lastUpdated() {
    return -1;
  }

  @Nullable abstract DataSourceAttribution source();
}
