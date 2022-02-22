package test.sample.myweather.ui.main

import test.sample.myweather.data.current.CurrentWeather
import test.sample.myweather.data.onecall.Hourly

data class CurrentData(val data: CurrentWeather?)

data class HourlyData(val data: List<Hourly>?)

data class ShowError(val errorMessage: String, val showError: Boolean)