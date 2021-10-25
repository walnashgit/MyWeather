package test.sample.myweather.data.current


import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

data class CurrentWeather(
    @SerializedName("base")
    val base: String,
    @SerializedName("clouds")
    val clouds: Clouds,
    @SerializedName("cod")
    val cod: Int,
    @SerializedName("coord")
    val coord: Coord,
    @SerializedName("dt")
    val dt: Long,
    @SerializedName("id")
    val id: Int,
    @SerializedName("main")
    val main: Main,
    @SerializedName("name")
    val name: String,
    @SerializedName("sys")
    val sys: Sys,
    @SerializedName("timezone")
    val timezone: Long,
    @SerializedName("visibility")
    val visibility: Int,
    @SerializedName("weather")
    val weather: List<Weather>,
    @SerializedName("wind")
    val wind: Wind
) {
    fun getCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat("d MMM, h:mm a", Locale.getDefault())
        val localOffset = Calendar.getInstance().get(Calendar.ZONE_OFFSET)
        return dateFormat.format((dt + timezone)*1000 - localOffset).uppercase(Locale.getDefault())
    }

    fun getSunriseTime(): String {
        val dateFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
        val localOffset = Calendar.getInstance().get(Calendar.ZONE_OFFSET)
        return dateFormat.format((sys.sunrise + timezone)*1000 - localOffset).uppercase(Locale.getDefault())
    }

    fun getSunsetTime(): String {
        val dateFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
        val localOffset = Calendar.getInstance().get(Calendar.ZONE_OFFSET)
        return dateFormat.format((sys.sunset + timezone)*1000 - localOffset).uppercase(Locale.getDefault())
    }
}