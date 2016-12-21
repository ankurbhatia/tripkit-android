package com.skedgo.android.common.model;

import com.google.gson.annotations.JsonAdapter;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@Gson.TypeAdapters
@JsonAdapter(GsonAdaptersOpeningDays.class)
public abstract class OpeningDays {

  public abstract String name();
  public abstract List<OpeningTimes> times(); // Opening times for this day of week. Empty array if it's closed that day.

}
