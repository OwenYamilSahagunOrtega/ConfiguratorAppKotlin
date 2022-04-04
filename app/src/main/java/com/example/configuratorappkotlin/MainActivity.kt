package com.example.configuratorappkotlin

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.Switch
import androidx.annotation.RequiresApi
import org.json.JSONObject
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    lateinit var contentPayload: ContentPayload
    private lateinit var contentValues: ContentValues
    private val uri: Uri = Uri.parse("content://com.example.configuratorappkotlin/flagName")

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        contentPayload = ContentPayload()
        contentValues = ContentValues()

        val swController = findViewById<View>(R.id.SwitchControllerEnvironment) as Switch
        readDatabase(swController)

        //Switch controller
        swController.setOnCheckedChangeListener { _, b ->
            contentPayload.switchStatus1 = b
            updateDatabase()
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private fun readDatabase(swController: Switch) {
        //We insert a data in our database if is empty (the record that we are going to read)
        val cursor = contentResolver.query(uri, null, null, null)
        if (cursor!!.count == 0) {
            contentValues.put("flagSwitch", contentPayload.switchStatus1)
            contentResolver.insert(uri, contentValues)
        } else {
            val rowObject = readDataFromCursor(cursor)
            if (rowObject.has("_id") && rowObject.has("flagSwitch")) {
                val isActive = rowObject.getString("flagSwitch").toInt() == 1
                swController.isChecked = isActive
            }
        }
        cursor.close()
    }

    /*Update the values from the 1 record that we have */
    private fun updateDatabase() {
        try {
            contentValues.put("flagSwitch", contentPayload.switchStatus1)
            contentResolver.update(uri, contentValues, null, null)
        } catch (e: Exception) {
            Log.e("Error: $e", e.toString())
        }
    }

    private fun readDataFromCursor(cursor: Cursor) : JSONObject {
        cursor.moveToLast()
        val rowObject = JSONObject()

        for (i in 0 until cursor.columnCount) {
            if (cursor.getColumnName(i) != null) {
                try {
                    rowObject.put(
                        cursor.getColumnName(i),
                        cursor.getString(i)
                    )
                } catch (e: Exception) {
                    Log.d("AppEnvironmentConfig", e.message!!)
                }
            }
        }

        return rowObject
    }
}