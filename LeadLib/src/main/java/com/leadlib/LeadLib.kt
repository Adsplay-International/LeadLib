package com.leadlib

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
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

    var referrerUrl = "Empty"

    init {
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
    }

    fun getReferData(activity: Activity) {

        val referrerClient = InstallReferrerClient.newBuilder(activity).build()
        referrerClient.startConnection(object : InstallReferrerStateListener {

            override fun onInstallReferrerSetupFinished(responseCode: Int) {
                when (responseCode) {

                    InstallReferrerClient.InstallReferrerResponse.OK -> {
                        try {
                            referrerUrl = referrerClient.getInstallReferrer()!!.installReferrer

                            Log.d(
                                "REFFFERTAGG",
                                "onInstallReferrerSetupFinished: " + "Referrer is : \n$referrerUrl"
                            )

                        } catch (e: RemoteException) {
                            e.printStackTrace()

                            Log.d(
                                "LeadLib",
                                "onInstallReferrerSetupFinished: Something Went Wrong, Refer Couldn't Be captured"
                            )
                        }
                    }

                    InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {
                        Log.d(
                            "LeadLib",
                            "onInstallReferrerSetupFinished: Refer Feature Not Available, Please upload app to Play Store"
                        )
                    }

                    InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> {
                        Log.d(
                            "LeadLib",
                            "onInstallReferrerSetupFinished: Connection couldn't be estabished, Please add Internet Permissions"
                        )
                    }

                }
            }

            override fun onInstallReferrerServiceDisconnected() {
                Log.d(
                    "LeadLib",
                    "onInstallReferrerSetupFinished: Connection couldn't be estabished, Please try to restart the Connection"
                )
            }
        })
    }

    fun getRefererr(): String {
        return referrerUrl
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