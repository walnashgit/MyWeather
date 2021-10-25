package test.sample.myweather.data.current


import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

data class Sys(
    @SerializedName("country")
    val country: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("message")
    val message: Double,
    @SerializedName("sunrise")
    val sunrise: Long,
    @SerializedName("sunset")
    val sunset: Long,
    @SerializedName("type")
    val type: Int,
)