package com.skedgo.android.common.model;

import android.support.annotation.Nullable;

public interface DataSourceAttribution {
  CompanyInfo provider();
  @Nullable String disclaimer();
}
