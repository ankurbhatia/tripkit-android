package com.skedgo.android.common.model;

import android.support.annotation.Nullable;

import com.google.gson.annotations.JsonAdapter;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Value.Immutable
@Gson.TypeAdapters
@JsonAdapter(GsonAdaptersPricing.class)
public abstract class Pricing {

  public abstract String label();
  public abstract int price();
  @Nullable public abstract String currency();
  @Nullable public abstract String currencySymbol();

}
