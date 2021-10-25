package test.sample.myweather.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import test.sample.myweather.data.current.CurrentWeather
import test.sample.myweather.data.onecall.OneCall

// https://openweathermap.org/current
interface WeatherApi : Api {

    @GET("weather?units=metric&appid=APIKEY_HERE")
    suspend fun getCurrentWeatherData(
        @Query("lat") lat: String,
        @Query("lon") long: String
    ): Response<CurrentWeather>

    @GET("onecall?units=metric&appid=APIKEY_HERE")
    suspend fun getWeatherForecast(
        @Query("lat") lat: String,
        @Query("lon") long: String
    ): Response<OneCall>
}