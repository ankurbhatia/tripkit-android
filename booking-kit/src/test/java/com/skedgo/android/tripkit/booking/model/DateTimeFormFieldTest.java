package com.skedgo.android.tripkit.booking.model;

import android.os.Parcel;

import com.skedgo.android.tripkit.booking.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class DateTimeFormFieldTest {

  @Test public void Parcelable() {
    DateTimeFormField expected = new DateTimeFormField();
    expected.setValue(100);
    Parcel parcel = Parcel.obtain();
    expected.writeToParcel(parcel, 0);
    parcel.setDataPosition(0);
    DateTimeFormField actual = DateTimeFormField.CREATOR.createFromParcel(parcel);
    assertThat(actual.getValue()).isEqualTo(expected.getValue());
  }
}