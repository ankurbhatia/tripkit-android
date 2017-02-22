package com.skedgo.android.tripkit.booking.ui.usecase;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.skedgo.android.tripkit.booking.ExternalFormField;

import javax.inject.Inject;

import rx.Observable;
import rx.subjects.PublishSubject;

public class LoadExternalWeb {

  public final ObservableBoolean showWebView = new ObservableBoolean(false);
  public final ObservableField<String> url = new ObservableField<>();
  private final PublishSubject<String> publishSubjectNextUrl = PublishSubject.create();
  private ExternalFormField externalFormField;

  @Inject LoadExternalWeb() { }

  public Observable<String> nextUrlObservable() {
    return publishSubjectNextUrl.asObservable();
  }

  public void setExternalFormFieldAndLoadWeb(@NonNull ExternalFormField externalFormField) {
    this.externalFormField = externalFormField;
    url.set(externalFormField.getValue());
  }

  public WebViewClient webViewClient() {
    return new WebViewClient() {
      @Override
      public boolean shouldOverrideUrlLoading(WebView webView, String url) {
        return handleCallback(url);
      }

      @Override
      public void onPageFinished(WebView view, final String url) {
        showWebView.set(true);
      }
    };
  }

  @VisibleForTesting
  boolean handleCallback(String webUrl) {
    if (webUrl.startsWith(externalFormField.getDisregardURL())) {
      publishSubjectNextUrl.onNext(externalFormField.getNextURL());
      return false;
    } else {
      showWebView.set(true);
      this.url.set(webUrl);
    }
    return true;
  }

}
