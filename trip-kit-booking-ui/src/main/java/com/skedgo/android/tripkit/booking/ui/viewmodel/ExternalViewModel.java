package com.skedgo.android.tripkit.booking.ui.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.webkit.WebViewClient;

import com.skedgo.android.tripkit.booking.ExternalFormField;
import com.skedgo.android.tripkit.booking.ui.usecase.LoadExternalWeb;
import com.skedgo.android.tripkit.booking.ui.usecase.LoadHttpURLConnection;

import javax.inject.Inject;

import rx.Observable;

public class ExternalViewModel extends DisposableViewModel {

  private final LoadExternalWeb loadExternalWeb;
  private final LoadHttpURLConnection loadHttpURLConnection;

  @Inject
  ExternalViewModel(LoadExternalWeb loadExternalWeb, LoadHttpURLConnection loadHttpURLConnection) {
    this.loadExternalWeb = loadExternalWeb;
    this.loadHttpURLConnection = loadHttpURLConnection;
  }

  public ObservableBoolean showWebView() {
    return loadExternalWeb.showWebView;
  }

  public ObservableField<String> url() {
    return loadExternalWeb.url;
  }

  public Observable<String> nextUrlObservable() {
    return Observable.merge(loadExternalWeb.nextUrlObservable(),
                            loadHttpURLConnection.nextUrlObservable())
        .first();
  }

  public WebViewClient webViewClient() {
    return loadExternalWeb.webViewClient();
  }

  public void handleArgs(Bundle args) {
    ExternalFormField externalFormField = args.getParcelable("externalFormField");
    if (externalFormField != null && externalFormField.getValue() != null) {

      String url = externalFormField.getValue();
      String disregardURL = externalFormField.getDisregardURL();

      if (url.startsWith(disregardURL)) {
        loadHttpURLConnection.loadHttp(externalFormField);
      } else {
        loadExternalWeb.setExternalFormFieldAndLoadWeb(externalFormField);
      }

    }
  }

}