package com.skedgo.android.common.model;

import android.support.annotation.Nullable;

import com.google.gson.annotations.JsonAdapter;
import com.skedgo.android.common.model.CompanyInfo;
import com.skedgo.android.common.model.DataSourceAttribution;
import com.skedgo.android.common.model.OpeningHours;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Value.Immutable
@Gson.TypeAdapters
@JsonAdapter(GsonAdaptersCarRental.class)
public abstract class CarRental {
  public abstract String identifier();
  public abstract CompanyInfo company();
  @Nullable public abstract OpeningHours openingHours();
  @Nullable public abstract DataSourceAttribution source();
}
