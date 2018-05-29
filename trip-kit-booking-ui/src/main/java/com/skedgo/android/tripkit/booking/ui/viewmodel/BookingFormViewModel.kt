package com.skedgo.android.tripkit.booking.ui.viewmodel

import android.content.res.Resources
import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableList
import android.os.Bundle
import android.util.Log
import com.skedgo.android.tripkit.booking.*
import com.skedgo.android.tripkit.booking.ui.activity.KEY_URL
import com.skedgo.android.tripkit.booking.ui.usecase.GetBookingFormFromAction
import com.skedgo.android.tripkit.booking.ui.usecase.GetBookingFormFromUrl
import com.skedgo.android.tripkit.booking.ui.usecase.IsCancelAction
import com.skedgo.android.tripkit.booking.ui.usecase.IsDoneAction
import com.skedgo.android.tripkit.booking.ui.viewmodel.util.PublishStack
import rx.Observable
import rx.android.schedulers.AndroidSchedulers.mainThread
import rx.subjects.PublishSubject
import javax.inject.Inject

private const val TAG = "BookingFormViewModel"

class BookingFormViewModel
@Inject constructor(
    private val resources: Resources,
    private val getBookingFormFromUrl: GetBookingFormFromUrl,
    private val getBookingFormFromAction: GetBookingFormFromAction,
    private val isCancelAction: IsCancelAction,
    private val isDoneAction: IsDoneAction
) : DisposableViewModel() {

  val hasError = ObservableBoolean(false)
  val isLoading = ObservableBoolean(false)
  val items: ObservableList<Any> = ObservableArrayList()
  val title: ObservableField<String> = ObservableField()
  val actionTitle: ObservableField<String> = ObservableField()
  val errorTitle: ObservableField<String> = ObservableField()
  val errorMessage: ObservableField<String> = ObservableField()
  val retryText: ObservableField<String> = ObservableField()
  val showAction = ObservableBoolean(false)
  val showRetry = ObservableBoolean(false)

  val onUpdateFormTitle: PublishSubject<String> = PublishSubject.create()
  val onNextBookingForm: PublishSubject<BookingForm> = PublishSubject.create()
  val onExternalForm: PublishSubject<ExternalFormField> = PublishSubject.create()
  val onDone: PublishSubject<BookingForm> = PublishSubject.create()
  val onCancel: PublishSubject<Boolean> = PublishSubject.create()
  val onErrorRetry: PublishSubject<String> = PublishSubject.create()

  var bookingError: BookingError? = null

  private val bookingFormStack = PublishStack<BookingForm>()

  init {
    bookingFormStack
        .asObservable()
        .observeOn(mainThread())
        .subscribe({ updateBookingForm() }, { Log.e(TAG, "Error  $it") })

    onNextBookingForm
        .asObservable()
        .observeOn(mainThread())
        .doOnError { bookingError ->
          isLoading.set(false)
          if (bookingError is BookingError) {
            showError(bookingError)
          }
        }
        .doOnSubscribe { isLoading.set(true) }
        .doOnCompleted { isLoading.set(false) }
        .subscribe({
          bookingFormStack.push(it)
        }, { Log.e(TAG, "Error  $it") })

  }

  fun consumeBack(): Boolean {
    bookingFormStack.pop()
    return bookingFormStack.peek() != null
  }

  fun onAction() {

    bookingFormStack.peek()?.let {
      when {
        isCancelAction.execute(it) -> onCancel.onNext(true)
        isDoneAction.execute(it) -> onDone.onNext(it)
        else -> getBookingFormFromAction.execute(it)
            .observeOn(mainThread())
            .doOnError { bookingError ->
              isLoading.set(false)
              if (bookingError is BookingError) {
                showError(bookingError)
              }
            }
            .doOnSubscribe { isLoading.set(true) }
            .doOnCompleted { isLoading.set(false) }
            .subscribe({
              bookingFormStack.push(it)
            }, { Log.e(TAG, "Error  $it") })
      }
    }
  }

  fun onRetry() {
    onErrorRetry.onNext(bookingError?.url)
  }

  fun onCancel() {
    onCancel.onNext(true)
  }

  fun handleExtras(bundle: Bundle): Observable<Unit> = if (bundle.containsKey(KEY_URL)) {
    getBookingFormFromUrl.execute(bundle.getString(KEY_URL))
        .observeOn(mainThread())
        .doOnNext {
          bookingFormStack.push(it)
        }
        .doOnError { bookingError ->
          isLoading.set(false)
          if (bookingError is BookingError) {
            showError(bookingError)
          }
        }
        .map { Unit }
        .doOnSubscribe { isLoading.set(true) }
        .doOnCompleted { isLoading.set(false) }

  } else {
    Observable.empty<Unit>()
  }

  private fun updateBookingForm() {
    val nextBookingForm = bookingFormStack.peek()

    if (nextBookingForm == null) {
      onDone.onNext(nextBookingForm)
    } else {
      updateBookingFormInfo(nextBookingForm)
      items.clear()
      updateFieldList(nextBookingForm)
    }
  }

  fun showError(error: BookingError) {
    bookingError = error

    hasError.set(true)
    errorTitle.set(error.title)
    errorMessage.set(error.error)
    showRetry.set(error.recoveryTitle != null && error.url != null)
    retryText.set(
        if (error.recoveryTitle != null && error.url != null) {
          error.recoveryTitle
        } else {
          resources.getString(R.string.retry)
        })
  }

  fun updateBookingFormInfo(nextBookingForm: BookingForm) {
    onUpdateFormTitle.onNext(nextBookingForm.title ?: "")
    actionTitle.set(nextBookingForm.action?.title)
    showAction.set(nextBookingForm.action != null)
  }

  fun updateFieldList(nextBookingForm: BookingForm) {
    nextBookingForm.form
        ?.forEach {
          if (it.title != null) {
            items.add(it.title)
          }
          it.fields.forEach {
            when (it) {
              is StringFormField -> items.add(FieldStringViewModel(it))
              is PasswordFormField -> items.add(FieldPasswordViewModel(it))
              is ExternalFormField -> items.add(FieldExternalViewModel(it, onExternalForm))
              is BookingForm -> items.add(FieldBookingFormViewModel(it, onNextBookingForm))
            }
          }
          if (it.footer != null) {
            items.add(it.footer)
          }
        }
  }
}