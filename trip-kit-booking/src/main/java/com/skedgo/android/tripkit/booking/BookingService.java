package com.skedgo.android.tripkit.booking;

import android.support.annotation.NonNull;

import rx.Observable;
import rx.Single;

public interface BookingService {

  Observable<BookingForm> getFormAsync(String url);
  Observable<BookingForm> postFormAsync(String url, InputForm inputForm, @NonNull String userToken);
  Single<ExternalOAuth> getExternalOauth (String authId);
}
