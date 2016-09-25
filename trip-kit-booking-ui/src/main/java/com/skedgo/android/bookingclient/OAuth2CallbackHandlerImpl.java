package com.skedgo.android.bookingclient;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.VisibleForTesting;

import com.skedgo.android.tripkit.account.AccountService;
import com.skedgo.android.tripkit.account.LogInResponse;
import com.skedgo.android.tripkit.booking.BookingForm;
import com.skedgo.android.tripkit.booking.BookingService;
import com.skedgo.android.tripkit.booking.ExternalOAuth;
import com.skedgo.android.tripkit.booking.ExternalOAuthService;
import com.skedgo.android.tripkit.booking.InputForm;

import rx.Observable;
import rx.functions.Func0;
import rx.functions.Func1;

public class OAuth2CallbackHandlerImpl implements OAuth2CallbackHandler {

  private final ExternalOAuthService externalOAuthService;
  private final BookingService bookingService;
  private final Context context;
  private final AccountService accountService;
  private final Func0<String> userTokenProvider;
  @VisibleForTesting AccountManager accountManager;

  public OAuth2CallbackHandlerImpl(Context context, ExternalOAuthService externalOAuthService,
                                   BookingService bookingService, AccountService accountService,
                                   Func0<String> userTokenProvider) {
    this.externalOAuthService = externalOAuthService;
    this.bookingService = bookingService;
    this.context = context;
    this.accountService = accountService;
    this.userTokenProvider = userTokenProvider;
    this.accountManager = AccountManager.get(context);
  }

  public Observable<BookingForm> handleOAuthURL(final BookingForm form, Uri uri, final String callback) {

    if (form != null) {

      // save code or show error
      final String code = uri.getQueryParameter("code");
      if (code != null) {

        // we need userToken at this point

        // get access token
        return silentLogIn()
            .flatMap(new Func1<Boolean, Observable<ExternalOAuth>>() {
              @Override public Observable<ExternalOAuth> call(Boolean didLogin) {
                return externalOAuthService.getAccessToken(form, code, "authorization_code", callback);
              }
            })
            .flatMap(new Func1<ExternalOAuth, Observable<BookingForm>>() {
              @Override public Observable<BookingForm> call(ExternalOAuth externalOAuth) {
                return Observable.just(form.setAuthData(externalOAuth));
              }
            })
            .flatMap(new Func1<BookingForm, Observable<BookingForm>>() {
              @Override public Observable<BookingForm> call(BookingForm bookingForm) {
                return bookingService.postFormAsync(form.getAction().getUrl(),
                                                    InputForm.from(form.getForm()));
              }
            });

      } else if (uri.getQueryParameter("error") != null) {
        return Observable.error(new Error(uri.getQueryParameter("error")));
      }
    }
    return Observable.error(new Error("No booking!"));
  }

  @Override public Observable<BookingForm> handleRetryURL(final BookingForm bookingForm) {
    return bookingService.postFormAsync(bookingForm.getAction().getUrl(), InputForm.from(bookingForm.getForm()));
  }

  // TODO: remove when google log in is implemented
  private Observable<Boolean> silentLogIn() {

    // Silent login
    if (userTokenProvider != null && userTokenProvider.call() == null) {

      Account[] accounts = accountManager.getAccountsByType("com.google");

      if (accounts.length > 0) {
        // Let's use the first account.
        Account account = accounts[0];

        return accountService.logInSilentAsync(account.type.hashCode() + "" + account.name.hashCode())
            .map(new Func1<LogInResponse, Boolean>() {
              @Override public Boolean call(LogInResponse logInResponse) {
                return true;
              }
            })
            .onErrorReturn(new Func1<Throwable, Boolean>() {
              @Override public Boolean call(Throwable throwable) {
                return false;
              }
            })
            ;
      }
    }

    return Observable.just(false);
  }
}
