package com.skedgo.routepersistence

import android.database.sqlite.SQLiteDatabase
import skedgo.sqlite.DatabaseField
import skedgo.sqlite.DatabaseTable
import skedgo.sqlite.UniqueIndices

object RoutingStatusContract {
  const val REQUEST_ID = "requestId"
  const val STATUS = "status"
  const val ROUTING_STATUS = "routingStatus"

  fun create(database: SQLiteDatabase) {
    val requestId = DatabaseField(REQUEST_ID, "TEXT")
    val status = DatabaseField(STATUS, "TEXT")
    DatabaseTable(
        ROUTING_STATUS,
        arrayOf(requestId, status),
        UniqueIndices.of(ROUTING_STATUS, requestId)
    ).create(database)
  }
}
