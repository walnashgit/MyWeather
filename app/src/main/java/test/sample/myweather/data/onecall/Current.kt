package test.sample.myweather.data.onecall


import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

data class Current(
    @SerializedName("clouds")
    val clouds: Int,
    @SerializedName("dew_point")
    val dewPoint: Double,
    @SerializedName("dt")
    val dt: Int,
    @SerializedName("feels_like")
    val feelsLike: Double,
    @SerializedName("humidity")
    val humidity: Int,
    @SerializedName("pressure")
    val pressure: Int,
    @SerializedName("rain")
    val rain: Rain,
    @SerializedName("sunrise")
    val sunrise: Int,
    @SerializedName("sunset")
    val sunset: Int,
    @SerializedName("temp")
    val temp: Double,
    @SerializedName("uvi")
    val uvi: Double,
    @SerializedName("visibility")
    val visibility: Int,
    @SerializedName("weather")
    val weather: List<Weather>,
    @SerializedName("wind_deg")
    val windDeg: Int,
    @SerializedName("wind_speed")
    val windSpeed: Double,

    // This value is not serialized
    var timezone: Long
) {
    fun getCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat("d MMM, h:mm a", Locale.getDefault())
        val localOffset = Calendar.getInstance().get(Calendar.ZONE_OFFSET)
        return dateFormat.format((dt + timezone)*1000 - localOffset).uppercase(Locale.getDefault())
    }

    fun getSunriseTime(): String {
        val dateFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
        val localOffset = Calendar.getInstance().get(Calendar.ZONE_OFFSET)
        return dateFormat.format((sunrise + timezone)*1000 - localOffset).uppercase(Locale.getDefault())
    }

    fun getSunsetTime(): String {
        val dateFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
        val localOffset = Calendar.getInstance().get(Calendar.ZONE_OFFSET)
        return dateFormat.format((sunset + timezone)*1000 - localOffset).uppercase(Locale.getDefault())
    }
}