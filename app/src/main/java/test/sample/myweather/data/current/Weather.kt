package test.sample.myweather.data.current


import com.google.gson.annotations.SerializedName
import java.lang.StringBuilder

data class Weather(
    @SerializedName("description")
    val desc: String,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("main")
    val main: String
) {
    fun getDescription(): String {
        val sb = StringBuilder(desc)
        sb.setCharAt(0, sb[0].uppercaseChar())
        return sb.toString()
    }
}