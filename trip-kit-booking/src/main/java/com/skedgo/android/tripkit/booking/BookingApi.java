package com.skedgo.android.tripkit.booking;

import com.skedgo.android.tripkit.BuiltInInterceptor;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

public interface BookingApi {
  @GET Observable<Response<BookingForm>> getFormAsync(@Url String url);
  @POST Observable<Response<BookingForm>> postFormAsync(
      @Url String url,
      @Body InputForm inputForm,
      @Header(BuiltInInterceptor.HEADER_USER_TOKEN) String userToken
  );
}