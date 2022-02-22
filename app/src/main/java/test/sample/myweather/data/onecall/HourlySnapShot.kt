package test.sample.myweather.data.onecall

import java.text.SimpleDateFormat
import java.util.*


data class HourlySnapShot(
    val clouds: Int,
    val dewPoint: Double,
    val dt: Int,
    val feelsLike: Double,
    val humidity: Int,
    val pop: Double,
    val pressure: Int,
    val temp: Double,
    val uvi: Double,
    val visibility: Int,
    val weather: List<Weather>,
    val windDeg: Int,
    val windGust: Double,
    val windSpeed: Double,
    val timezone: Long
) {
    fun getHourlyTime(): String {
        val dateFormat = SimpleDateFormat("d MMM, h:mm a", Locale.getDefault())
        val localOffset = Calendar.getInstance().get(Calendar.ZONE_OFFSET)
        return dateFormat.format((dt + timezone)*1000 - localOffset).uppercase(Locale.getDefault())
    }
}
