package com.udacity.asteroidradar.util

import android.content.Context
import androidx.lifecycle.viewModelScope
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.udacity.asteroidradar.api.NeoController
import com.udacity.asteroidradar.api.NetworkUtils
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.Neo
import com.udacity.asteroidradar.model.NeoWrapper
import com.udacity.asteroidradar.repository.NeoRepository
import com.udacity.asteroidradar.viewmodel.Status
import kotlinx.coroutines.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class AsteroidWorker(appContext: Context, params: WorkerParameters)
    : CoroutineWorker(appContext, params){

    companion object {
        const val WORKER_NAME = "ASTEROID_LOADER_DATA"
    }
    override suspend fun doWork(): Result {
        //val dataBase = AppDatabase.getDatabase(applicationContext).getNeoDao()
        val repository = NeoRepository(applicationContext)

        return try{
            val dates = NetworkUtils.getNextSevenDaysFormattedDates()

            val call = NeoController.getNeoFeedCall(dates[0], dates[7])

            call?.enqueue(object : Callback<Neo> {
                override fun onFailure(call: Call<Neo>?, t: Throwable?) {

                }

                override fun onResponse(
                    call: Call<Neo>?,
                    response: Response<Neo>?
                ) {
                    if(response?.code() == 200){
                        val neo = response.body()

                        val jsonInString = Gson().toJson(neo)
                        val mJSONObject = JSONObject(jsonInString)
                        val asteroidList = NetworkUtils.parseAsteroidsJsonResult(mJSONObject)

                        GlobalScope.launch  {
                            repository.insertAll(asteroidList)
                        }
                    }
                }
            })

            Result.success()
        }catch (e: HttpException){
            Result.retry()
        }
    }
}