package com.skedgo.android.common.model;

import com.google.gson.annotations.JsonAdapter;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Value.Immutable
@Gson.TypeAdapters
@JsonAdapter(GsonAdaptersBikePodStop.class)
public abstract class BikePodStop extends Location {
  abstract BikePod bikePod();
  abstract ModeInfo modeInfo();
}
