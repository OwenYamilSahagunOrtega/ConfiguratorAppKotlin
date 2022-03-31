package com.example.configuratorappkotlin

import android.annotation.SuppressLint
import android.content.ContentValues
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.Switch
import androidx.annotation.RequiresApi
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    lateinit var contentPayload: ContentPayload
    private lateinit var contentValues: ContentValues
    lateinit var uri: Uri

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("Range", "UseSwitchCompatOrMaterialCode", "Recycle")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        contentPayload = ContentPayload()
        contentValues = ContentValues()
        uri = Uri.parse("content://com.example.configuratorappkotlin/flagName")

        val swController = findViewById<View>(R.id.SwitchControllerEnvironment) as Switch
        //We insert a data in our database if is empty (the record that we are going to read)
        val cursor = contentResolver.query(uri, null, null, null)
        if (cursor!!.count == 0) {
            contentValues.put("flagSwitch", contentPayload.switchStatus1)
            contentResolver.insert(uri, contentValues)
        }

        //Switch controller
        swController.setOnCheckedChangeListener { _, b ->
            contentPayload.switchStatus1 = b
            updateDatabase()
        }
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
}