package com.irfan.dansjob.network

import com.irfan.dansjob.data.Job
import com.irfan.dansjob.data.JobDetail
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitInterface {

    @GET("positions.json")
    suspend fun getPositionsScroll(
        @Query("page") page: Int?,
        @Query("description") description: String?,
        @Query("location") location: String?,
        @Query("full_time") full_time: Boolean?,
    ): ArrayList<Job>

    @GET("positions/{ID}")
    suspend fun getPositionsDetail(
        @Path("ID") ID: String?
    ): JobDetail
}
