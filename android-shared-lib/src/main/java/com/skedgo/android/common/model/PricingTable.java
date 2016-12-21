package com.skedgo.android.common.model;

import android.support.annotation.Nullable;

import com.google.gson.annotations.JsonAdapter;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@Gson.TypeAdapters
@JsonAdapter(GsonAdaptersPricingTable.class)
public abstract class PricingTable {
  public abstract String title();
  @Nullable public abstract String subtitle();
  public abstract List<Pricing> entries();

}
