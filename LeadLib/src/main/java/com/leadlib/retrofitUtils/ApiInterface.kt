package com.leadlib.retrofitUtils

import android.graphics.ColorSpace.Model
import retrofit2.Call
import retrofit2.http.*


interface ApiInterface {

    @GET("android_pb.php")
    fun createInstance(
        @Query("clickid") referrer: String?,
        @Query("deviceid") device_id: String?,
        @Query("ip") ip: String?
    ): Call<Unit>?

    @GET("android_pb_event.php")
    fun recordEvent(
        @Query("clickid") referrer: String?,
        @Query("deviceid") device_id: String?,
        @Query("eventname") eventname: String?
    ): Call<Unit>?

}