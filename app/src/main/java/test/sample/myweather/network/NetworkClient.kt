package test.sample.myweather.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkClient {

    private val retrofitClient = Retrofit.Builder()
        .baseUrl("https://api.darksky.net/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T: Api>createNetworkService(clazz: Class<T>): T {
        return retrofitClient.create(clazz)
    }
}