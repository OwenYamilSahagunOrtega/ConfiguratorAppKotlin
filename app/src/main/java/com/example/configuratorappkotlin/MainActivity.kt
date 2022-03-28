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
        var Cp: ContentPayload = ContentPayload()
        var values = ContentValues()
        var uri: Uri? = null

        @RequiresApi(api = Build.VERSION_CODES.O)
        @SuppressLint("Range")
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            val swController = findViewById<View>(R.id.SwitchControllerEnvironment) as Switch
            uri = Uri.parse("content://com.example.configuratorappkotlin/flagName")

            val c = contentResolver.query(uri!!, null, null, null)
            if (c!!.count == 0) {
                values.put("flagSwitch", Cp.switchStatus1)
                contentResolver.insert(uri!!, values)
            }
            swController.setOnCheckedChangeListener { compoundButton, b ->
                Cp.switchStatus1 = b
                UpdateDatabase()
                Log.e("Aqui", b.toString());
            }

        }

        fun UpdateDatabase() {
            values.put("flagSwitch", Cp.switchStatus1)
            try {
                contentResolver.update(uri!!, values, null, null)
            } catch (e: Exception) {
                Log.e("Error: $e", e.toString())
            }
        }
    }