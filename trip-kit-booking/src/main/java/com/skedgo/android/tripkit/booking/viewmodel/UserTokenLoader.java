package com.skedgo.android.tripkit.booking.viewmodel;

import android.accounts.Account;
import android.accounts.AccountManager;

import com.skedgo.android.tripkit.account.AccountService;
import com.skedgo.android.tripkit.account.LogInResponse;

import rx.Observable;
import rx.functions.Func0;
import rx.functions.Func1;

public class UserTokenLoader {

  private final AccountService service;
  private final AccountManager accountManager;
  private Func0<String> userTokenProvider;

  public UserTokenLoader(AccountService service, AccountManager accountManager, Func0<String> userTokenProvider) {
    this.service = service;
    this.accountManager = accountManager;
    this.userTokenProvider = userTokenProvider;
  }

  public Observable<String> getUserToken() {
    return Observable.concatDelayError(Observable.just(userTokenProvider.call()), login())
        .first(new Func1<String, Boolean>() {
          @Override public Boolean call(String token) {
            return token != null;
          }
        });
  }

  private Observable<String> login() {
    return Observable.just(accountManager)
        .map(new Func1<AccountManager, Account>() {
          @Override public Account call(AccountManager accountManager) {
            return accountManager.getAccountsByType("com.google")[0];
          }
        })
        .map(new Func1<Account, String>() {
          @Override public String call(Account acc) {
            return acc.type.hashCode() + "" + acc.name.hashCode();
          }
        })
        .flatMap(new Func1<String, Observable<LogInResponse>>() {
          @Override public Observable<LogInResponse> call(String token) {
            return service.logInSilentAsync(token);
          }
        })
        .map(new Func1<LogInResponse, String>() {
          @Override public String call(LogInResponse logInResponse) {
            return logInResponse.userToken();
          }
        });
  }
}
