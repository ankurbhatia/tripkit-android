package com.skedgo.routepersistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RouteDatabaseHelper extends SQLiteOpenHelper {
  private static int DATABASE_VERSION = 2;

  public RouteDatabaseHelper(Context context, String name) {
    super(context, name, null, DATABASE_VERSION);
  }

  @Override public void onCreate(SQLiteDatabase database) {
    RouteContract.createTables(database);
  }

  @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    switch (oldVersion) {
      case 1:
        RoutingStatusContract.INSTANCE.create(db);
    }
  }
}
