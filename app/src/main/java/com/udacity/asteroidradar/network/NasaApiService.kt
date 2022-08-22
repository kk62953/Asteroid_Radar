package com.udacity.asteroidradar.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.util.Constants
import com.udacity.asteroidradar.domain.PictureOfDay
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


//create Moshi object with KotlinJSONAdapterFactory
private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

//create the Retrofit object for asteroids add an instance of MoshiConverterFactory and the BASE_URL
private val retrofit = Retrofit.Builder().
                        addConverterFactory(ScalarsConverterFactory.create()).
                        baseUrl(Constants.BASE_URL).build()

//create retrofit object for Picture og the day and instance of MoshiConverterFactory and the BASE_URL
private val retrofitPictureOfDay = Retrofit.Builder().
                        addConverterFactory(MoshiConverterFactory.create(moshi)).
                        baseUrl(Constants.BASE_URL).build()


// Define interface on how retrofit communicate with webserver using HTTP request to get JSON string
interface NasaApiService {
    //Specify the endpoint and path to use
    @GET("neo/rest/v1/feed")
   suspend fun getAsteroidProperties(
        @Query("start_date") start_date: String,
        @Query("end_date") end_date: String,
        @Query("api_key") api_key: String
    ):String
}

//Define interface for Picture of the day from webserver
interface PictureOfDayApiService{
    //Specify the endpoint and path to use
    @GET("planetary/apod")
    suspend fun getImageOftheDay(
        @Query("api_key") apiKey: String
    ): PictureOfDay

}
// create a public object called MarsApi to expose the Retrofit service to the rest of the app
object NasaAPI {
    val retrofitService: NasaApiService by lazy {
        retrofit.create(NasaApiService::class.java)
    }
}

//Create a public object called PictureOfDayAPI to expose retrofit service to the rest of the app
object PictureOfDayAPI{
    val retrofitService : PictureOfDayApiService by lazy {
        retrofitPictureOfDay.create(PictureOfDayApiService::class.java)
    }




}




