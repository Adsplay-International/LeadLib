package com.adsplaylib

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.leadlib.LeadLib

class MainActivity : AppCompatActivity() {

    var referralData = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        LeadLib.getReferData(this)

        Handler(Looper.getMainLooper()).postDelayed({
            referralData = LeadLib.getRefererr()
            Log.d("LEADLIB", "createInstance: ${referralData}")
        }, 2000)


        Handler(Looper.getMainLooper()).postDelayed({

            LeadLib.createInstance(
                applicationContext,
                referralData!!,
                "Your_Device_ID",
                "Your_IP_Address"
            )

            LeadLib.recordEvent(referralData, "Your_Device_ID", "Your_Event_Name")

        }, 3000)

    }
}