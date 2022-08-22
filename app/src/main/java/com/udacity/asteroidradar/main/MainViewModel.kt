package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.util.Constants
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch
import java.lang.Exception
import com.udacity.asteroidradar.network.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.database.asDomainModel

class MainViewModel(application: Application) : AndroidViewModel(application) {


    //Create three NASA API status
    enum class NasaApiStatus {LOADING,DONE,ERROR}

    //Variable to get start date of Asteroid
    private val startDate = getNextSevenDaysFormattedDates()[0]

    //Variable to get  end date of Asteroid
    private val endDate = getNextSevenDaysFormattedDates()[5]

    // Variable to get weekend date of Asteroid
    private val weekEnd = getNextSevenDaysFormattedDates()[5]

    // Create database Singleton
    private val database = AsteroidDatabase.getInstance(application)

    //Create repository
    private val asteroidRepository = AsteroidRepository(database)

    //Create varaible to get asteroids list from repository
    var asteroids: LiveData<List<Asteroid>> = asteroidRepository.asteroids

    //Refresh asteroid data using repository
    init{
        getRefreshAsteroid()
    }

    private fun getRefreshAsteroid(){
        viewModelScope.launch {
            try{
                asteroidRepository.refreshAsteroid(startDate,endDate, Constants.API_KEY)
                getPictureOftheDay()
            }catch(e: Exception){

            }
        }
    }

    // The internal MutableLiveData String that stores the status of the most recent request
    private val _status = MutableLiveData<NasaApiStatus>()

    // The external immutable LiveData for the request status String
    val status: LiveData<NasaApiStatus>
        get() = _status

    //Add an encapsulated LiveData<PictureOfDay> property: To expose property image
    //URL be loading
    private val _pictureOfTheDay = MutableLiveData<PictureOfDay>()

    val pictureOfTheDay: LiveData<PictureOfDay>
     get() = _pictureOfTheDay


    // Get Picture of the day from Retrofit service API
    private fun getPictureOftheDay(){
        //launch the coroutine
        viewModelScope.launch{
            try{
            _status.value = NasaApiStatus.LOADING
            val picture = asteroidRepository.getPictureOfTheDay(Constants.API_KEY)
            picture.let{
                _pictureOfTheDay.value= picture
                _status.value = NasaApiStatus.DONE
            }

        } catch(e: Exception){
            _status.value = NasaApiStatus.ERROR

            }        }

        }

    fun getWeekAsteroids(): LiveData<List<Asteroid>>{
        val weekAsteroid = Transformations.map(asteroidRepository.getWeekAsteroids(startDate,weekEnd)){
            it.asDomainModel()
        }
        return weekAsteroid
    }

    fun getTodayAsteroids(): LiveData<List<Asteroid>>{
        val todayAsteroids = Transformations.map(asteroidRepository.getTodayAsteroids(startDate)){
            it.asDomainModel()
        }
        return todayAsteroids
    }
    }





