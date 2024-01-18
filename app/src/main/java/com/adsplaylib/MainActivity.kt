package com.adsplaylib

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.leadlib.LeadLib

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

      /*  val referralData = LeadLib.getReferData(this)

        //get Values
        val installReferrer = referralData.get("installReferrer")
        val referrerClickTime = referralData.get("referrerClickTime")
        val appInstallTime = referralData.get("appInstallTime")
        val instantExperienceLaunched = referralData.get("instantExperienceLaunched")


        LeadLib.createInstance(
            applicationContext,
            installReferrer!!,
            "Your_Device_ID",
            "Your_IP_Address"
        )

        LeadLib.recordEvent(installReferrer, "Your_Device_ID", "Your_Event_Name")*/

    }
}