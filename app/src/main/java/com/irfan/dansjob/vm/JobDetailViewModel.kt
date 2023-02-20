package com.irfan.dansjob.vm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.irfan.dansjob.data.JobDetail
import com.irfan.dansjob.network.ApiResponse
import com.irfan.dansjob.network.RetrofitInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class JobDetailViewModel(private val retrofitInterface: RetrofitInterface) : ViewModel() {

    fun getJobDetail(id: String): LiveData<ApiResponse<JobDetail>> {
        return flow {
            val job = retrofitInterface.getPositionsDetail(id)
            try {
                if (!job.company.isNullOrBlank()) {
                    emit(ApiResponse.Success(job))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e : Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO).asLiveData()
    }
}