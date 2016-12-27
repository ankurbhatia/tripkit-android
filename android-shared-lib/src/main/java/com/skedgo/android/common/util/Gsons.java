package com.skedgo.android.common.util;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.skedgo.android.common.model.BikePodStop;
import com.skedgo.android.common.model.CarParkStop;
import com.skedgo.android.common.model.CarPodStop;
import com.skedgo.android.common.model.CarRentalStop;
import com.skedgo.android.common.model.GsonAdaptersBikePod;
import com.skedgo.android.common.model.GsonAdaptersBooking;
import com.skedgo.android.common.model.GsonAdaptersRealtimeAlert;
import com.skedgo.android.common.model.Region;
import com.skedgo.android.common.model.ScheduledStop;

import java.lang.reflect.Type;

public final class Gsons {
  private Gsons() {}

  @NonNull
  public static Gson createForLowercaseEnum() {
    return new GsonBuilder()
        .registerTypeAdapterFactory(new LowercaseEnumTypeAdapterFactory())
        .registerTypeAdapterFactory(new GsonAdaptersBooking())
        .registerTypeAdapterFactory(new GsonAdaptersRealtimeAlert())
        .registerTypeAdapterFactory(new GsonAdaptersBikePod())
        .registerTypeHierarchyAdapter(ScheduledStop.class, new JsonDeserializer<BikePodStop>() {
          @Override
          public BikePodStop deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            String klass = jsonObject.get("class").getAsString();
            switch (klass) {
              case "CarPod":
                return context.deserialize(json, CarPodStop.class);
              case "CarPark":
                return context.deserialize(json, CarParkStop.class);
              case "BikePod":
                return context.deserialize(json, BikePodStop.class);
              case "CarRental":
                return context.deserialize(json, CarRentalStop.class);
              default:

            }
            return null;
          }
        })
        .create();
  }

  @NonNull
  public static Gson createForRegion() {
    return new GsonBuilder()
        .registerTypeAdapter(Region.City.class, new CityJsonDeserializer())
        .create();
  }
}