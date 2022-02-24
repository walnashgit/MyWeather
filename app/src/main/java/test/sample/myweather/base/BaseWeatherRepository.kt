package test.sample.myweather.base

/**
 * Base repository class for fetching weather data.
 */
interface BaseWeatherRepository<out T> {
    /**
     * Get the weather data of the location passed in latitude [lat] and longitude [long] of the
     * current location.
     */
    suspend fun getWeatherData(lat: String, long: String): T
}