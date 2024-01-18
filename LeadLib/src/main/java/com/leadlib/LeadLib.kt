package com.leadlib

import android.app.Activity
import android.content.Context
import android.os.RemoteException
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import android.widget.Toast
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.android.installreferrer.api.ReferrerDetails
import com.google.gson.Gson
import com.leadlib.retrofitUtils.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection
import java.net.URL

object LeadLib {

    init {
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
    }

    /*public fun createInstance(referrer: String, deviceID: String, ip: String) {
    var url =
        "https://affiliates.adsplay.in/android_pb.php?clickid=$referrer&deviceid=$deviceID&ip=$ip"

    val thread = Thread {
        try {
            val u = URL(url)
            val http: HttpURLConnection = u.openConnection() as HttpURLConnection
            http.connect()
            Log.d("LeadLib", "createInstanceUrl: ${url}")
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
    thread.start()
}*/

    /* public fun recordEvent(referrer: String, deviceID: String, event: String) {
        var url =
            "https://affiliates.adsplay.in/android_pb_events.php?clickid=$referrer&deviceid=$deviceID&eventname=$event"

        val thread = Thread {
            try {
                val u = URL(url)
                val http: HttpURLConnection = u.openConnection() as HttpURLConnection
                http.connect()
                Log.d("LeadLib", "hitUrl: ${url}")
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
        thread.start()
    }*/

    fun getReferData(activity: Activity): HashMap<String, String> {

        var response: ReferrerDetails? = null
        val referrerClient = InstallReferrerClient.newBuilder(activity).build()

        referrerClient.startConnection(object : InstallReferrerStateListener {
            override fun onInstallReferrerSetupFinished(responseCode: Int) {

                when (responseCode) {
                    InstallReferrerClient.InstallReferrerResponse.OK -> {
                        try {

                            response = referrerClient.getInstallReferrer()
                            val referrerUrl = response!!.installReferrer
                            val referrerClickTime = response!!.referrerClickTimestampSeconds
                            val appInstallTime = response!!.installBeginTimestampSeconds
                            val instantExperienceLaunched = response!!.googlePlayInstantParam
                            var referrer = response!!.installReferrer

                            Log.d(
                                "REFFFERTAGG",
                                "onInstallReferrerSetupFinished: " + "Referrer is : \n$referrerUrl\nReferrer Click Time is : $referrerClickTime\nApp Install Time : $appInstallTime"
                            )

                        } catch (e: RemoteException) {
                            e.printStackTrace()
                        }
                    }
                    InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED ->
                        Log.d(
                            "LeadLib",
                            "onInstallReferrerSetupFinished: Refer Feature Not Available, Please upload app to Play Store"
                        )
                    InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE ->
                        Log.d(
                            "LeadLib",
                            "onInstallReferrerSetupFinished: Connection couldn't be estabished, Please add Internet Permissions"
                        )
                }
            }

            override fun onInstallReferrerServiceDisconnected() {
                Log.d(
                    "LeadLib",
                    "onInstallReferrerSetupFinished: Connection couldn't be estabished, Please try to restart the Connection"
                )
            }
        })

        val map = HashMap<String, String>()
        if (response != null) {
            map.put("installReferrer", response!!.installReferrer ?: " ")
            map.put("referrerClickTime", response!!.referrerClickTimestampSeconds.toString() ?: "")
            map.put("appInstallTime", response!!.installBeginTimestampSeconds.toString() ?: "")
            map.put("instantExperienceLaunched", response!!.googlePlayInstantParam.toString() ?: "")

            println(map)
        } else {
            map.put("installReferrer", "NoReferReturned")
            map.put("referrerClickTime", "NoReferReturned")
            map.put("appInstallTime", "NoReferReturned")
            map.put("instantExperienceLaunched", "NoReferReturned")

            println(map)
        }

        return map
    }

    fun createInstance(context: Context, referrer: String, deviceID: String, ip: String) {

        val prefers = Prefers(context)

        if (prefers.getString(Prefers.INSTANCE_CREATED) != "true") {

            val call: Call<Unit>? = ApiClient.getClient.createInstance(
                referrer, deviceID, ip
            )
            call!!.enqueue(object : Callback<Unit> {
                override fun onResponse(
                    call: Call<Unit>, response: Response<Unit>
                ) {
                    Log.d("LeadLib - Create Instance", "onResponse: Instance Created")
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    Log.d("LeadLib - Create Instance Failed", t.toString())
                }
            })
        } else {
            Log.d("LeadLib - Create Instance", "onResponse: Instance Already Created")
        }

        prefers.setString(Prefers.INSTANCE_CREATED, "true")
    }

    fun recordEvent(referrer: String, deviceID: String, event: String) {

        val call: Call<Unit>? = ApiClient.getClient.recordEvent(
            referrer, deviceID, event
        )
        call!!.enqueue(object : Callback<Unit> {
            override fun onResponse(
                call: Call<Unit>, response: Response<Unit>
            ) {
                Log.d("LeadLib - Record Event", "onResponse: Event Recorded")
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Log.d("LeadLib - Record Event Failed", t.toString())
            }
        })

    }
}