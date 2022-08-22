package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.network.*
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidRepository (private val database: AsteroidDatabase) {

    //Define LiveVideo for list of Asteroids converting  LiveData database objects to domain  objects.
    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroid()) {
            it.asDomainModel()
        }

    //A function to refresh the offline cache: which will be called from a coroutine.
    suspend fun refreshAsteroid(startDate: String, endDate: String, apiKey: String) {
        //Kotlin Coroutine will run in IO dispatcher
        withContext(Dispatchers.IO) {
            try {
                //Get the data from the network and then put it in the database:
                val resultList =
                    NasaAPI.retrofitService.getAsteroidProperties(startDate, endDate, apiKey)

                //Parse JSON
                val asteroidList = parseAsteroidsJsonResult(JSONObject(resultList))
                database.asteroidDao.insertAll(* asteroidList.asDatabaseModel())
            } catch (e: Exception) {
                Log.i("Failure", e.message!!)
            }
        }
    }

    //A function to get Picture of the day
    suspend fun getPictureOfTheDay(apiKey: String) =
        PictureOfDayAPI.retrofitService.getImageOftheDay(apiKey)

    fun getWeekAsteroids(startDate: String, endDate: String) =
        database.asteroidDao.getWeekAsteroids(startDate, endDate)

    fun getTodayAsteroids(todayDate: String) =  database.asteroidDao.getTodayAsteroids(todayDate)

    fun deletePreviousDayAsteroids(date: String) = database.asteroidDao.deletePreviousDayAsteroids(date)
}