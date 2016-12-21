package com.skedgo.android.common.model;

import com.google.gson.annotations.JsonAdapter;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@Gson.TypeAdapters
@JsonAdapter(GsonAdaptersOpeningHours.class)
public abstract class OpeningHours {

  public abstract String timezone();
  public abstract List<OpeningDays> days();
}
