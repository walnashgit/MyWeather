package test.sample.myweather.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import test.sample.myweather.data.onecall.OneCall
import test.sample.myweather.network.ApiResult
import test.sample.myweather.base.BaseWeatherRepository
import test.sample.myweather.network.WeatherRepository

@Module
@InstallIn(ViewModelComponent::class)
abstract class WeatherRepositoryModule {

    @Binds
    abstract fun bindWeatherRepository(weatherRepository: WeatherRepository): BaseWeatherRepository<ApiResult<OneCall>>
}