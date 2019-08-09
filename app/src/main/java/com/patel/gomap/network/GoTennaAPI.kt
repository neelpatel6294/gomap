package com.patel.gomap.network

import com.patel.gomap.model.PinData
import io.reactivex.Flowable
import retrofit2.http.GET

interface GoTennaAPI {
    @GET("/development/scripts/get_map_pins.php")
    fun getPinData() : Flowable<List<PinData>>
}
