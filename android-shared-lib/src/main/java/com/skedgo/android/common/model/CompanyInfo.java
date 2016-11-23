package com.skedgo.android.common.model;

import android.support.annotation.Nullable;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Value.Immutable
@Gson.TypeAdapters
public interface CompanyInfo {
  String name();
  @Nullable String website();
  @Nullable String remoteIcon();
}
