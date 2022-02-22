package test.sample.myweather.data.onecall


import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

data class OneCall(
    @SerializedName("alerts")
    val alerts: List<Alert>,
    @SerializedName("current")
    val current: Current,
    @SerializedName("daily")
    val daily: List<Daily>,
    @SerializedName("hourly")
    val hourly: List<Hourly>,
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lon")
    val lon: Double,
    @SerializedName("minutely")
    val minutely: List<Minutely>,
    @SerializedName("timezone")
    val timezone: String,
    @SerializedName("timezone_offset")
    val timezoneOffset: Long
) {
    fun getHourlyData(): List<Hourly> {
        return hourly.onEach {
            it.time = getTime(it.dt)
        }
    }

    private fun getTime(dt: Int): String {
        val dateFormat = SimpleDateFormat("h a", Locale.getDefault())
        val localOffset = Calendar.getInstance().get(Calendar.ZONE_OFFSET)
        return dateFormat.format((dt + timezoneOffset)*1000 - localOffset).uppercase(Locale.getDefault())
    }
}