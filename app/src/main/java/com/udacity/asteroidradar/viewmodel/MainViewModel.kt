package com.udacity.asteroidradar.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.udacity.asteroidradar.api.NeoController
import com.udacity.asteroidradar.api.NetworkUtils
import com.udacity.asteroidradar.model.*
import com.udacity.asteroidradar.repository.NeoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(
    private val repository: NeoRepository,
    application: Application
) : ViewModel() {
    private val _apod = MutableLiveData<ApodWrapper>()
    val apod: LiveData<ApodWrapper> = _apod

    private val _neo = MutableLiveData<NeoWrapper>()
    val neo: LiveData<NeoWrapper> = _neo

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    fun getApod(date: String?) {
        viewModelScope.launch {
            val call = NeoController.getApodCall(date)

            call?.enqueue(object : Callback<Apod> {
                override fun onFailure(call: Call<Apod>?, t: Throwable?) {
                    _apod.postValue(ApodWrapper("error", null))
                }

                override fun onResponse(
                    call: Call<Apod>?,
                    response: Response<Apod>?
                ) {
                    if(response?.code() == 200){
                        val apod = response.body()
                        apod?.let {
                            saveApod(it)
                        }
                        _apod.postValue(ApodWrapper("success", apod))
                        _status.postValue(Status.DONE)
                    }else{
                        _apod.postValue(ApodWrapper("error", null))
                        _status.postValue(Status.ERROR)
                    }
                }
            })
        }
    }

    fun getNeoFeed(startDate: String?, endDate: String?) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val l = NeoController.getNeoFeed(startDate, endDate)
                val call = NeoController.getNeoFeedCall(startDate, endDate)

                call?.enqueue(object : Callback<Neo> {
                    override fun onFailure(call: Call<Neo>?, t: Throwable?) {
                        _neo.value = NeoWrapper("error", null)
                        _status.value = Status.ERROR
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

                            insertAll(asteroidList)
                            _neo.value = NeoWrapper("success", asteroidList)
                            _status.value = Status.DONE
                        }else{
                            _neo.value = NeoWrapper("error", null)
                            _status.value = Status.ERROR
                        }
                    }
                })
            }
        }
    }

    private fun saveApod(apod: Apod) {
        viewModelScope.launch{
            //withContext(Dispatchers.IO){
                repository.deleteAllApod()
                repository.insertApod(apod)
            //}
        }
    }

    private fun insertAll(list: ArrayList<Asteroid>) {
        viewModelScope.launch{
            withContext(Dispatchers.IO){
                repository.deleteAllNeo()
                repository.insertAll(list)
            }
        }
    }

    fun getApod(){
        viewModelScope.launch{
            //withContext(Dispatchers.IO){
                try{
                    val apod = repository.getApod()
                    _apod.postValue(ApodWrapper("success", apod))
                    _status.postValue(Status.DONE)
                }catch (e: Exception){
                    _apod.postValue(ApodWrapper("error", null))
                    _status.postValue(Status.ERROR)
                }

            //}
        }
    }

    fun getAllNeos(){
        viewModelScope.launch{
            //withContext(Dispatchers.IO){
                try{
                    val neos = repository.getAllNeos()
                    _neo.postValue(NeoWrapper("success", neos))
                    _status.postValue(Status.DONE)
                }catch (e: Exception){
                    _neo.postValue(NeoWrapper("error", null))
                    _status.postValue(Status.ERROR)
                }
            //}
        }
    }
}

enum class Status { LOADING, ERROR, DONE }