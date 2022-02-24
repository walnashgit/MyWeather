package test.sample.myweather.network

import test.sample.myweather.data.onecall.OneCall

const val ERROR_MSG = "Returning error."
class FakeWeatherRepository: WeatherRepositoryOneCallBase {

    var returnError = false

    override suspend fun getWeatherData(lat: String, long: String): ApiResult<OneCall> {
        if (returnError) {
            return ApiResult.Error(null, ERROR_MSG)
        }
        return ApiResult.Success(null)
    }
}