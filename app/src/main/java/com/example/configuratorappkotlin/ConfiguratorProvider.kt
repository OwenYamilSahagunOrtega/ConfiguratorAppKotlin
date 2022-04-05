package com.example.configuratorappkotlin

import android.content.ContentProvider
import android.content.UriMatcher
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteQueryBuilder
import android.content.ContentValues
import android.content.ContentUris
import android.database.Cursor
import android.database.SQLException
import android.net.Uri
import android.text.TextUtils
import java.lang.IllegalArgumentException

class ConfiguratorProvider : ContentProvider() {
    companion object {
        /*Information that we are going to use to communicate that is also included in manifest*/
        private const val AUTHORITY = "com.example.configuratorappkotlin"
        private val CONTENT_URI = Uri.parse("content://com.example.configuratorappkotlin/flagName")
        private const val All_ELEMENTS = 1
        private const val ONE_ELEMENT = 2
        private var URI_MATCHER: UriMatcher? = null
        //Name of the table that we are going to use/create
        private const val TABLE = "flags"

        init {
            URI_MATCHER = UriMatcher(UriMatcher.NO_MATCH)
            URI_MATCHER!!.addURI(AUTHORITY, "flagName", All_ELEMENTS)
            URI_MATCHER!!.addURI(AUTHORITY, "flagName/#", ONE_ELEMENT)
        }
    }

    private var database: SQLiteDatabase? = null
    override fun onCreate(): Boolean {
        val dbHelper = ConfiguratorSQLiteHelper(context)
        database = dbHelper.writableDatabase
        return database != null && database!!.isOpen
    }

    override fun getType(uri: Uri): String {
        return when (URI_MATCHER!!.match(uri)) {
            All_ELEMENTS -> "vnd.android.cursor.dir/com.example.configuratorappkotlin"
            ONE_ELEMENT -> "vnd.android.cursor.item/com.example.configuratorappkotlin"
            else -> throw IllegalArgumentException("Incorrect URI: $uri")
        }
    }

    override fun query(
        uri: Uri, proyection: Array<String>?, selection: String?, argSelection: Array<String>?,
        order: String?
    ): Cursor? {
        val queryBuilder = SQLiteQueryBuilder()
        queryBuilder.tables = TABLE
        when (URI_MATCHER!!.match(uri)) {
            All_ELEMENTS -> {
            }
            ONE_ELEMENT -> {
                val id = uri.pathSegments[1]
                queryBuilder.appendWhere("_id = $id")
            }
            else -> throw IllegalArgumentException("Incorrect Uri: $uri")
        }
        return queryBuilder.query(database, proyection, selection, argSelection, null, null, null)
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri {
        val idRow = database!!.insert(TABLE, null, values)
        return if (idRow > 0) {
            ContentUris.withAppendedId(CONTENT_URI, idRow)
        } else {
            throw SQLException("Error adding $uri")
        }
    }

    override fun delete(uri: Uri, s: String?, strings: Array<String>?): Int {
        return 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        select: String?,
        ArgumentsSelection: Array<String>?
    ): Int {
        var selection = select
        when (URI_MATCHER!!.match(uri)) {
            All_ELEMENTS -> {
            }
            ONE_ELEMENT -> {
                val id = uri.pathSegments[1]
                selection = if (TextUtils.isEmpty(selection)) {
                    "_id = $id"
                } else {
                    "_id + $id AND ($selection)"
                }
            }
            else -> throw IllegalArgumentException("Incorrect URI: $uri")
        }
        return database!!.update(TABLE, values, selection, ArgumentsSelection)
    }
}