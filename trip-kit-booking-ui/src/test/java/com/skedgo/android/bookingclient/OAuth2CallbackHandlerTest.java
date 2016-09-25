package com.skedgo.android.bookingclient;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.net.Uri;

import com.skedgo.android.tripkit.account.AccountService;
import com.skedgo.android.tripkit.account.LogInResponse;
import com.skedgo.android.tripkit.booking.BookingAction;
import com.skedgo.android.tripkit.booking.BookingForm;
import com.skedgo.android.tripkit.booking.BookingService;
import com.skedgo.android.tripkit.booking.ExternalOAuth;
import com.skedgo.android.tripkit.booking.ExternalOAuthService;
import com.skedgo.android.tripkit.booking.InputForm;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import rx.Observable;
import rx.functions.Func0;
import rx.observers.TestSubscriber;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(TestRunner.class)
@Config(constants = BuildConfig.class)
public class OAuth2CallbackHandlerTest {

  @Mock private BookingService bookingService;
  @Mock private AccountService accountService;
  @Mock private ExternalOAuthService externalOAuthService;
  private OAuth2CallbackHandlerImpl oAuth2CallbackHandler;

  @Before public void before() {
    MockitoAnnotations.initMocks(this);

    oAuth2CallbackHandler = new OAuth2CallbackHandlerImpl(RuntimeEnvironment.application, externalOAuthService,
                                                          bookingService, accountService,
                                                          new Func0<String>() {
                                                            @Override public String call() {
                                                              return null;
                                                            }
                                                          });
  }

  @Test public void handleOAuthURLNoBookingError() {
    final TestSubscriber<BookingForm> subscriber = new TestSubscriber<>();

    oAuth2CallbackHandler.handleOAuthURL(null, mock(Uri.class), "callback").subscribe(subscriber);
    subscriber.awaitTerminalEvent();
    subscriber.assertError(Error.class);
  }

  @Test public void handleOAuthURLNoCodeError() {
    final TestSubscriber<BookingForm> subscriber = new TestSubscriber<>();

    oAuth2CallbackHandler.handleOAuthURL(mock(BookingForm.class), mock(Uri.class), "callback").subscribe(subscriber);
    subscriber.awaitTerminalEvent();
    subscriber.assertError(Error.class);
  }

  @Test public void handleOAuthURL() {

    final BookingForm bookingForm = mock(BookingForm.class);
    final BookingAction bookingAction = mock(BookingAction.class);
    when(bookingForm.getAction()).thenReturn(bookingAction);
    when(bookingForm.getForm()).thenReturn(null);
    when(bookingAction.getUrl()).thenReturn("url");

    final ExternalOAuth externalOAuth = mock(ExternalOAuth.class);
    when(externalOAuth.token()).thenReturn("token");

    final Uri uri = mock(Uri.class);
    when(uri.getQueryParameter("code")).thenReturn("a code");

    when(externalOAuthService.getAccessToken(bookingForm, "a code", "authorization_code", "callback"))
        .thenReturn(Observable.just(externalOAuth));

    when(accountService.logInSilentAsync(any(String.class)))
        .thenReturn(Observable.just(mock(LogInResponse.class)));

    AccountManager am = mock(AccountManager.class);
    Account account = new Account("google", "mail");

    Account[] accounts = new Account[1];
    accounts[0] = account;

    when(am.getAccountsByType(any(String.class)))
        .thenReturn(accounts);

    oAuth2CallbackHandler.accountManager = am;

    when(bookingService.postFormAsync(bookingForm.getAction().getUrl(),
                                      InputForm.from(bookingForm.getForm())))
        .thenReturn(Observable.just(bookingForm));

    final TestSubscriber<BookingForm> subscriber = new TestSubscriber<>();

    oAuth2CallbackHandler.handleOAuthURL(bookingForm, uri, "callback").subscribe(subscriber);
    subscriber.awaitTerminalEvent();
    subscriber.assertNoErrors();

    BookingForm result = subscriber.getOnNextEvents().get(0);

    // Result is same booking. TODO: token value added should be test
    assertThat(result).isEqualTo(bookingForm);
  }

  @Test public void handleRetryURL() {

    final BookingForm bookingForm = mock(BookingForm.class);
    final BookingAction bookingAction = mock(BookingAction.class);
    when(bookingForm.getAction()).thenReturn(bookingAction);
    when(bookingForm.getForm()).thenReturn(null);
    when(bookingAction.getUrl()).thenReturn("url");

    final BookingForm bookingFormResult = mock(BookingForm.class);

    when(bookingService.postFormAsync("url", null))
        .thenReturn(Observable.just(bookingFormResult));

    final TestSubscriber<BookingForm> subscriber = new TestSubscriber<>();

    oAuth2CallbackHandler.handleRetryURL(bookingForm).subscribe(subscriber);
    subscriber.awaitTerminalEvent();
    subscriber.assertNoErrors();

    BookingForm result = subscriber.getOnNextEvents().get(0);

    assertThat(result).isEqualTo(bookingFormResult);

  }

}
