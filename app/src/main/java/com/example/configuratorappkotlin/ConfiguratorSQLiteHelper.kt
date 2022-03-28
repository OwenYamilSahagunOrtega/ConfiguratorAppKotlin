package com.example.configuratorappkotlin

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/* It create the structure of our database*/
class ConfiguratorSQLiteHelper(context: Context?) :
    SQLiteOpenHelper(context, "flags", null, 1) {
    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        sqLiteDatabase.execSQL(
            "CREATE TABLE flags ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "flagSwitch BOOLEAN)"
        )
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {}
}
