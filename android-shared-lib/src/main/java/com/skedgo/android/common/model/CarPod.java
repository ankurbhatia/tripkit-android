package com.skedgo.android.common.model;

import com.google.gson.annotations.JsonAdapter;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@Gson.TypeAdapters
@JsonAdapter(GsonAdaptersCarPod.class)
public abstract class CarPod {
  public abstract String identifier();
  public abstract CompanyInfo operator();
  public abstract List<Vehicle> vehicles();
}
