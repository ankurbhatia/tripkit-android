package com.skedgo.android.tripkit.booking.ui.usecase;

import com.skedgo.android.tripkit.booking.ExternalFormField;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

public class LoadHttpURLConnection {

  private final PublishSubject<String> publishSubjectNextUrl = PublishSubject.create();

  @Inject LoadHttpURLConnection() { }

  public Observable<String> nextUrlObservable() {
    return publishSubjectNextUrl.asObservable();
  }

  public void loadHttp(final ExternalFormField externalFormField) {

    doHttpUrlConnectionAction(externalFormField.getValue())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<String>() {
          @Override public void call(String result) {
            publishSubjectNextUrl.onNext(externalFormField.getNextURL());
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable e) {
            publishSubjectNextUrl.onError(e);
          }
        });

  }

  private Observable<String> doHttpUrlConnectionAction(final String desiredUrl) {

    return Observable.create(new Observable.OnSubscribe<String>() {
      @Override public void call(Subscriber<? super String> subscriber) {
        URL url;
        BufferedReader reader = null;
        StringBuilder stringBuilder;

        try {
          url = new URL(desiredUrl);
          HttpURLConnection connection = (HttpURLConnection) url.openConnection();

          connection.setRequestMethod("GET");
          connection.setReadTimeout(15 * 1000);
          connection.connect();

          // read the output from the server
          reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
          stringBuilder = new StringBuilder();

          String line;
          while ((line = reader.readLine()) != null) {
            stringBuilder.append(line + "\n");
          }
          subscriber.onNext(stringBuilder.toString());
        } catch (Exception e) {
          subscriber.onError(e);
        } finally {
          if (reader != null) {
            try {
              reader.close();
            } catch (IOException ioe) {
              ioe.printStackTrace();
            }
          }
        }
      }
    });
  }
}
