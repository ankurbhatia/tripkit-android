package skedgo.tripkit.account.data

import android.accounts.AccountManager
import android.annotation.SuppressLint
import rx.Observable
import rx.schedulers.Schedulers
import skedgo.tripkit.account.domain.User
import skedgo.tripkit.account.domain.UserRepository

internal class DeviceUserRepository constructor(
    private val accountManager: AccountManager
) : UserRepository {
  @SuppressLint("MissingPermission")
  override fun getUser(): Observable<User> {
    return Observable
        .fromCallable {
          accountManager.accounts.first()
        }
        .map {
          User(it.name, it.type)
        }
        .subscribeOn(Schedulers.io())
  }
}
