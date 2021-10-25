package test.sample.myweather.network

sealed class ApiResult<T> {

    data class Success<T>(val data: T) : ApiResult<T>()

    data class Error<T>(val data: T? = null, val message: String) : ApiResult<T>()

    class Loading<T> : ApiResult<T>()
}