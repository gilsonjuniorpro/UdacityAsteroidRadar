package com.udacity.asteroidradar.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import com.google.gson.Gson
import com.udacity.asteroidradar.model.NearEarthObjects
import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class Utils {
    companion object {

        val sdf = SimpleDateFormat("yyyy-MM-dd")

        fun isConnected(context: Context): Boolean {
            return try {
                val connectivity =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                if (connectivity != null) {
                    val info = connectivity.allNetworkInfo
                    if (info != null) {
                        for (i in info.indices) {
                            if (info[i].state == NetworkInfo.State.CONNECTED) {
                                return true
                            }
                        }
                    }
                }
                false
            } catch (e: Exception) {
                e.message?.let { Log.e("Util", it) }
                false
            }
        }

        fun mappingMeteors(jsonResult: JSONObject?, range: List<String>?): ArrayList<NearEarthObjects> {
            val asteroidList = ArrayList<NearEarthObjects>()
            for (i in range!!.indices) {
                try {
                    val dateAsteroidJsonArray = jsonResult!!.getJSONArray(range[i])
                    for (i in 0 until dateAsteroidJsonArray.length()) {
                        val asteroidJson = dateAsteroidJsonArray.getJSONObject(i).toString()
                        var nearEarthObjects = Gson().fromJson(
                            asteroidJson,
                            NearEarthObjects::class.java
                        )
                        asteroidList.add(nearEarthObjects)
                    }
                }catch (e: Exception){

                }
            }
            return asteroidList
        }

        fun getRangeBetweenDates(startDate: String?, endDate: String?): List<String>? {
            var list = mutableListOf<String>()
            try {
                val initialDate = getDateFromString(startDate)
                val finalDate = getDateFromString(endDate)
                list = getDaysBetweenDates(initialDate)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return list
        }

        private fun getDaysBetweenDates(startDate: Date): MutableList<String> {
            val dates = ArrayList<String>()
            val calendar = GregorianCalendar()
            calendar.time = startDate

            (0..7).forEach { _ ->
                val result = calendar.time
                val formatter = SimpleDateFormat("yyyy-MM-dd")
                val today = formatter.format(result)
                today.split("|")
                dates.add(today)
                calendar.add(Calendar.DATE, 1)
            }

            return dates
        }

        private fun getDateFromString(strDate: String?): Date {
            return sdf.parse(strDate)
        }
    }
}