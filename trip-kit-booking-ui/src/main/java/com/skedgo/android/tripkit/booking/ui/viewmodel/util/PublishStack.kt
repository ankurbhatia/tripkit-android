package com.skedgo.android.tripkit.booking.ui.viewmodel.util

import com.jakewharton.rxrelay.BehaviorRelay
import com.jakewharton.rxrelay.PublishRelay
import rx.Observable

private enum class Action { None, Push, Pop }

class PublishStack<T> {

  private val subjectStack = PublishRelay.create<Pair<Action, List<T>>>()
  private val subjectT = BehaviorRelay.create<T?>()

  init {
    subjectStack.scan { (_, list): Pair<Action, List<T>>, t2: Pair<Action, List<T>>? ->
      when {
        t2?.first == Action.Push -> Pair(Action.None, list + t2.second)
        t2?.first == Action.Pop && list.isNotEmpty() -> Pair(Action.None, list - listOf(list.last()))
        else -> Pair(Action.None, list)
      }
    }
        .subscribe { (_, list) ->
          when {
            list.isEmpty() -> subjectT.call(null)
            else -> subjectT.call(list.last())
          }
        }
  }

  fun asObservable(): Observable<T> =
      subjectT.asObservable()
          .filter { it != null }
          .map { it!! }

  fun push(element: T) {
    subjectStack.call(Pair(Action.Push, listOf(element)))
  }

  fun pop() {
    subjectStack.call(Pair(Action.Pop, listOf()))
  }

  fun peek(): T? = subjectT.value
}