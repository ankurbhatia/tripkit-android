package com.skedgo.android.common.model;

import com.google.gson.annotations.JsonAdapter;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Value.Immutable
@Gson.TypeAdapters
@JsonAdapter(GsonAdaptersOpeningTimes.class)
public abstract class OpeningTimes {

  public abstract String opens(); // Opening time formatted as HH:MM
  public abstract String closes();// Closing time formatted as HH:MM

}
