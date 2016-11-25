package com.skedgo.android.common.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BikePodStopTest {
  Gson gson = new Gson();

  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void shouldDeserializeLocationAttributes() throws Exception {
    JsonObject bikePod = new JsonObject();
    bikePod.addProperty("lat", 100);
    ImmutableBikePod build = ImmutableBikePod.builder().available(0).capacity(0).lastUpdated(0).build();
    bikePod.add("bikePod", gson.toJsonTree(build));
    bikePod.add("modeInfo", gson.toJsonTree(new ModeInfo()));
    BikePodStop bikePodStop = gson.fromJson(bikePod, BikePodStop.class);
    assertThat(bikePodStop.getLat()).isEqualTo(100);
  }
}