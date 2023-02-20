package com.irfan.dansjob.vm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.irfan.dansjob.data.Job
import com.irfan.dansjob.network.ApiResponse
import com.irfan.dansjob.network.RetrofitInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class JobViewModel(private val retrofitInterface: RetrofitInterface) : ViewModel() {

    fun getJobs(
        page: Int,
        description: String,
        location: String,
        fulltime: Boolean
    ): LiveData<ApiResponse<ArrayList<Job>>> {
        return flow {
            val jobs = retrofitInterface.getPositionsScroll(
                page = page,
                description = description,
                location = location,
                full_time = fulltime,
            )
            try {
                if (jobs.isNotEmpty()) {
                    emit(ApiResponse.Success(jobs))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO).asLiveData()
    }
}